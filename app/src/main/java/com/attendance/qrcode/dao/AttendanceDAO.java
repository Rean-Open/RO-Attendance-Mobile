package com.attendance.qrcode.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.attendance.qrcode.model.Attendance;
import com.attendance.qrcode.model.User;

import java.util.List;

@Dao
public interface AttendanceDAO {
    @Insert
    void insertAll(Attendance... Attendances);

    @Delete
    void delete(Attendance Attendance);

    @Query("DELETE FROM attendance")
    void deleteAll();

    @Query("SELECT * FROM attendance ORDER BY logDateTime")
    List<Attendance> getAll();

}
