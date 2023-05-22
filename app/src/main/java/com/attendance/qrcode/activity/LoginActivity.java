package com.attendance.qrcode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.attendance.qrcode.AppDatabase;
import com.attendance.qrcode.AuthHelper;
import com.attendance.qrcode.R;
import com.attendance.qrcode.databinding.ActivityLoginBinding;
import com.attendance.qrcode.model.User;
import com.attendance.qrcode.service.APIClient;
import com.attendance.qrcode.service.UserService;
import com.attendance.qrcode.service.response.AuthResponse;
import com.attendance.qrcode.service.response.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private TextView emailEditText;
    private TextView passwordEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AuthHelper.auth(this)) {
            goToMainActivity();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEditText = binding.getRoot().findViewById(R.id.emailEditText);
        passwordEditText = binding.getRoot().findViewById(R.id.passwordEditText);

        signInButton = binding.getRoot().findViewById(R.id.signInButton);
        signInButton.setOnClickListener(view -> {
            try {
                onLogin();
            } catch (IOException e) {
                Log.e("LoginActivity", e.getMessage(), e);
            }
        });
    }

    private void onLogin() throws IOException {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.equals("")) {
            showMessage("E-mail is required!");
        } else if (password.equals("")) {
            showMessage("Password is required!");
        } else {
            signInButton.setEnabled(false);

            UserService service = APIClient.getInstance().getUserService(this);
            Call<AuthResponse> call = service.auth(email, password);
            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.getStatusCode() == 200) {
                        if (AuthHelper.saveToken(LoginActivity.this, authResponse.getToken())) {
                            UserResponse userResponse = authResponse.getData();

                            User user = new User();
                            user.setId(userResponse.getId());
                            user.setName(userResponse.getName());
                            user.setEmail(userResponse.getEmail());
                            user.setRole(userResponse.getRole());
                            user.setVerified(userResponse.isVerified());
                            AppDatabase.getInstance(LoginActivity.this).userDao().insert(user);

                            goToMainActivity();
                        }
                    } else {
                        signInButton.setEnabled(true);
                        showMessage(authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    signInButton.setEnabled(true);
                    showMessage("Connection error, please try again!");
                }
            });
        }

    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setNegativeButton("OK", null).show();
    }
}
