# RO-Attendance-Mobile
Rean-Open - QR Attendance - Mobile

The project is learning project from various group study, it's not yet a final product so far until now.

## Attendance-QR-code
The mobile project is taking from the private project submitted.

Please change baseUrl in Class [com.attendance.qrcode.service.APIClient]

    Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.1.6:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


Sample QR Code in Screenshots directory

## Revision
### Version 1.0 - 2022
- API with https://github.com/Rean-Open/RO-Attendance-PHP

## Team
### Initial Project Team - 2022
- Lecturer: mt@osify.com
- Ms. Thida TEK
- Mr. Pengleng HUOT
- Mr. Keomorakort MAN
- Mr. Raingsey KHUN
- Mr. Sovanarith SREY
