package com.attendance.qrcode.activity.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.attendance.qrcode.R;
import com.attendance.qrcode.activity.MainActivity;
import com.attendance.qrcode.databinding.FragmentScanQrBinding;
import com.attendance.qrcode.service.APIClient;
import com.attendance.qrcode.service.AttendanceService;
import com.attendance.qrcode.service.response.LogAttendanceResponse;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRFragment extends Fragment  {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private FragmentScanQrBinding binding;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    String intentData = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanQrBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        surfaceView = view.findViewById(R.id.surfaceView);
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    Log.e("ScanQRCode", e.getMessage(), e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.d("ScanQRCode", "To prevent memory leaks barcode scanner has been stopped");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    String rawValue = barcodes.get(0).rawValue;
                    Log.d("Scan Value", rawValue);

                    if (rawValue.startsWith("app.qr") && rawValue.endsWith("=>>")) {
                        cameraSource.release();

                        onLogAttendance(rawValue);
                    }
                }
            }
        });
    }

    private void onLogAttendance(String rawValue) {
        double latitude = 11.563584; // sample
        double longtitude = 104.911654; // sample

        int clockType = 1;
        String data = rawValue.replace("app.qr", "");
        data = data.replace("=>>", "");
        if (data.equals("x01")) { // Time In
            clockType = 1;
        } else { // Time Out
            clockType = 2;
        }

        int type = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("a", Locale.getDefault());
        String am_pm = dateFormat.format(new Date());
        if (am_pm.toLowerCase(Locale.getDefault()).equals("pm")) {
            type = 2;
        }

        AttendanceService service = APIClient.getInstance().getAttendanceService(getContext());
        Call<LogAttendanceResponse> call = service.logAttendance(type, clockType, latitude, longtitude);
        call.enqueue(new Callback<LogAttendanceResponse>() {
            @Override
            public void onResponse(Call<LogAttendanceResponse> call, Response<LogAttendanceResponse> response) {
                LogAttendanceResponse logAttendanceResponse = response.body();

                if (logAttendanceResponse.getStatusCode() == 200) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.selectMenu(2, true);
                } else {
                    showMessage(logAttendanceResponse.getMessage());
                    initialiseDetectorsAndSources();
                }
            }

            @Override
            public void onFailure(Call<LogAttendanceResponse> call, Throwable t) {
                Log.e(ScanQRFragment.class.getSimpleName(), t.getMessage(), t);
                showMessage("Something wrong, please try again!");
                initialiseDetectorsAndSources();
            }
        });
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setNegativeButton("OK", null).show();
    }
}
