package com.attendance.qrcode.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.attendance.qrcode.R;
import com.attendance.qrcode.activity.MainActivity;
import com.attendance.qrcode.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton buton = view.findViewById(R.id.timeInOutButton);
        buton.setOnClickListener(this);

        buton = view.findViewById(R.id.attendanceListButton);
        buton.setOnClickListener(this);

        buton = view.findViewById(R.id.myInfoButton);
        buton.setOnClickListener(this);

        buton = view.findViewById(R.id.signOutButton);
        buton.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            if (view.getId() == R.id.timeInOutButton) {
                mainActivity.selectMenu(1, true);
            } else if (view.getId() == R.id.attendanceListButton) {
                mainActivity.selectMenu(2, true);
            } else if (view.getId() == R.id.myInfoButton) {
                mainActivity.selectMenu(3, true);
            } else if (view.getId() == R.id.signOutButton) {
                mainActivity.selectMenu(4, true);
            }
        }
    }
}
