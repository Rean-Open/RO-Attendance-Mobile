package com.attendance.qrcode.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.attendance.qrcode.AppDatabase;
import com.attendance.qrcode.R;
import com.attendance.qrcode.model.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends BaseAdapter {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
    List<Attendance> items = new ArrayList<>();

    public AttendanceAdapter(Context context) {
        items = AppDatabase.getInstance(context).attendanceDao().getAll();
    }

    public void reload() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cell_attendance, viewGroup, false);
        }

        Holder holder = (Holder)view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }

        Attendance attendance = items.get(i);
        holder.statusTextView.setText(attendance.getType() == 1? "Morning": "Afternoon");
        holder.timeStatusTextView.setText(attendance.getClockType() == 1? "Time In": "Time Out");
        holder.dateTimeTextView.setText(dateFormat.format(attendance.getLogDateTime()));
        return view;
    }

    static class Holder {
        TextView statusTextView;
        TextView timeStatusTextView;
        TextView dateTimeTextView;

        Holder(View view) {
            statusTextView = view.findViewById(R.id.statusTextView);
            timeStatusTextView = view.findViewById(R.id.timeStatusTextView);
            dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
        }
    }
}
