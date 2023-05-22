package com.attendance.qrcode.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.attendance.qrcode.AppDatabase;
import com.attendance.qrcode.AuthHelper;
import com.attendance.qrcode.Constants;
import com.attendance.qrcode.R;
import com.attendance.qrcode.dao.UserDAO;
import com.attendance.qrcode.databinding.ActivityMainBinding;
import com.attendance.qrcode.model.EMenu;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private TextView headerTitleView;

    protected View mainView;
    protected NavigationView mNavView;
    protected DrawerLayout mDrawer;

    private Integer selectedIndex = -1;
    private EMenu[] menus = Constants.MENUS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mNavView = findViewById(R.id.nav_view);
        mNavView.setItemIconTintList(null);
        mNavView.setNavigationItemSelectedListener(menuItem -> {
            selectMenu(menuItem.getOrder());
            return true;
        });

        mDrawer = findViewById(R.id.drawer_layout);
        buildMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mDrawer.openDrawer(GravityCompat.START);
        return true;
    }

    public void selectMenu(int position) {
        selectMenu(position, false);
    }

    public void selectMenu(int position, boolean force) {
        if (force || selectedIndex != position) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            if (selectedIndex > 0) {
                navController.navigateUp();
            }
            if (position == 0) {
                navController.navigateUp();
            } else if (position == 1) {
                navController.navigate(R.id.action_HomeFragment_to_ScanQRFragment);
            } else if (position == 2) {
                navController.navigate(R.id.action_HomeFragment_to_AttendanceListFragment);
            } else if (position == 3) {
                navController.navigate(R.id.action_HomeFragment_to_MyInfoFragment);
            } else {
                onSignOut();
            }
            if (!force)
                selectedIndex = position;
        } else if (position == 4) {
            onSignOut();
        }
//        NavController navController = NavHostFragment.findNavController(HomeFragment.this);
//        navController.navigate(R.id.action_SecondFragment_to_ThirdFragment);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void onSignOut() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (AuthHelper.clearToken(MainActivity.this)) {
                            UserDAO userDAO = AppDatabase.getInstance(MainActivity.this).userDao();
                            userDAO.deleteAll();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to sign out now?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void buildMenu() {
        Menu menu = mNavView.getMenu();
        menu.clear();

        int position = 0;
        for (EMenu _menu: menus) {
            MenuItem menuItem = menu.add(Menu.NONE, Menu.NONE, position, getString(_menu.getTitleId()));
            menuItem.setVisible(true);
            menuItem.setIcon(_menu.getIconId());

            position += 1;
        }
    }

    public void setHeaderTitle(String title) {
        headerTitleView.setText(title);
    }

    @Override
    public void onClick(View view) {

    }
}
