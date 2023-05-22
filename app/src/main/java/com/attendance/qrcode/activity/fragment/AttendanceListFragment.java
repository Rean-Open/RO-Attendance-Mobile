package com.attendance.qrcode.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.attendance.qrcode.AppDatabase;
import com.attendance.qrcode.R;
import com.attendance.qrcode.activity.adapter.AttendanceAdapter;
import com.attendance.qrcode.dao.AttendanceDAO;
import com.attendance.qrcode.databinding.FragmentAttendanceListBinding;
import com.attendance.qrcode.model.Attendance;
import com.attendance.qrcode.service.APIClient;
import com.attendance.qrcode.service.AttendanceService;
import com.attendance.qrcode.service.response.AttendanceListResponse;
import com.attendance.qrcode.service.response.AttendanceResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceListFragment extends Fragment  {

    private FragmentAttendanceListBinding binding;

    private ListView attendanceListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAttendanceListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attendanceListView = view.findViewById(R.id.attendanceListView);

        AttendanceAdapter adapter = new AttendanceAdapter(getContext());
        attendanceListView.setAdapter(adapter);

        AttendanceService service = APIClient.getInstance().getAttendanceService(getContext());
        Call<AttendanceListResponse> call = service.list();
        call.enqueue(new Callback<AttendanceListResponse>() {
            @Override
            public void onResponse(Call<AttendanceListResponse> call, Response<AttendanceListResponse> response) {
                if (response.isSuccessful()) {
                    AttendanceListResponse listResponse = response.body();
                    if (listResponse.getData().size() > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        List<Attendance> attendances = new ArrayList<>();

                        for (AttendanceResponse attendanceResponse: listResponse.getData()) {
                            try {
                                Attendance attendance = new Attendance();
                                attendance.setId(attendanceResponse.getId());
                                attendance.setLogDateTime(dateFormat.parse(attendanceResponse.getLogDate()));
                                attendance.setType(attendanceResponse.getType());
                                attendance.setClockType(attendanceResponse.getClockType());
                                attendance.setLatitude(attendanceResponse.getLatitude());
                                attendance.setLongtitude(attendanceResponse.getLongtitude());
                                attendances.add(attendance);
                            } catch (ParseException ex) {
                                Log.e("AttendanceListFragment", ex.getMessage(), ex);
                            }
                        }

                        if (attendances.size() > 0) {
                            AttendanceDAO dao = AppDatabase.getInstance(getContext()).attendanceDao();
                            dao.deleteAll();
                            dao.insertAll(attendances.toArray(new Attendance[]{}));
                            adapter.reload();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AttendanceListResponse> call, Throwable t) {
                showMessage("Can listing attendance from server now, please try again!");
            }
        });
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setNegativeButton("OK", null).show();
    }
}
