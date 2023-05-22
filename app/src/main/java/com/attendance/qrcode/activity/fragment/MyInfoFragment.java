package com.attendance.qrcode.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.attendance.qrcode.AppDatabase;
import com.attendance.qrcode.R;
import com.attendance.qrcode.dao.UserDAO;
import com.attendance.qrcode.databinding.FragmentHomeBinding;
import com.attendance.qrcode.databinding.FragmentMyInfoBinding;
import com.attendance.qrcode.model.User;

import java.util.List;

public class MyInfoFragment extends Fragment  {

    private FragmentMyInfoBinding binding;

    private TextView nameTextView;
    private TextView roleTextView;
    private TextView emailTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.nameTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        UserDAO userDAO = AppDatabase.getInstance(getContext()).userDao();
        List<User> users = userDAO.getAll();
        User user = null;
        if (users.size() > 0) {
            user = users.get(0);
        }
        if (user != null) {
            nameTextView.setText(user.getName());
            roleTextView.setText("Role: " + user.getRole());
            emailTextView.setText(user.getEmail());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
