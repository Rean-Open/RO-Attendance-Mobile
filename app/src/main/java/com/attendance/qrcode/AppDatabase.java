package com.attendance.qrcode;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.attendance.qrcode.dao.AttendanceDAO;
import com.attendance.qrcode.dao.UserDAO;
import com.attendance.qrcode.model.Attendance;
import com.attendance.qrcode.model.User;

@Database(entities = {User.class, Attendance.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDAO userDao();

    public abstract AttendanceDAO attendanceDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "qr_code_attendance.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
