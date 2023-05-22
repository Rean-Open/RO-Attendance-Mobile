package com.attendance.qrcode.model;

import com.attendance.qrcode.R;

public enum EMenu {

    HOME(R.drawable.ic_home, R.string.menu_home),
    TIME_IN_OUT(R.drawable.ic_time, R.string.menu_time_in_out),
    ATTENDANCE_LIST(R.drawable.ic_list, R.string.menu_attendance_list),
    MY_INFO(R.drawable.ic_user, R.string.menu_my_info),
    SIGN_OUT(R.drawable.ic_sign_out, R.string.menu_sign_out);

    private int iconId;
    private int titleId;

    EMenu(int iconId, int titleId) {
        this.iconId = iconId;
        this.titleId = titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getTitleId() {
        return titleId;
    }
}
