package com.attendance.qrcode;

import com.attendance.qrcode.model.EMenu;

public interface Constants {

    EMenu[] MENUS = new EMenu[]{
            EMenu.HOME,
            EMenu.TIME_IN_OUT,
            EMenu.ATTENDANCE_LIST,
            EMenu.MY_INFO,
            EMenu.SIGN_OUT
    };
}
