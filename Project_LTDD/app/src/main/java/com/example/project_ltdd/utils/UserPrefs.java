package com.example.project_ltdd.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefs {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_FULLNAME = "usLoginFullname";
    private static final String KEY_USER_ID = "usLoginId";

    private SharedPreferences prefs;

    public UserPrefs(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu thông tin người dùng
    public void saveUser(String fullName, int userId) {
        prefs.edit()
                .putString(KEY_FULLNAME, fullName)
                .putInt(KEY_USER_ID, userId)
                .apply();
    }

    // Lấy tên người dùng
    public String getFullName() {
        return prefs.getString(KEY_FULLNAME, "Guest");
    }

    // Lấy ID người dùng
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Xoá dữ liệu khi logout (nếu cần)
    public void clear() {
        prefs.edit().clear().apply();
    }

    // Kiểm tra xem đã đăng nhập hay chưa
    public boolean isLoggedIn() {
        return prefs.contains(KEY_USER_ID) && prefs.getInt(KEY_USER_ID, -1) != -1;
    }
}
