package com.example.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Đảm bảo rằng khi vào ProfileActivity, menu profile được tô đậm
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }

        // Thiết lập sự kiện chọn mục trong BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Chuyển hướng tới MainActivity khi chọn "Home"
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            // Chuyển hướng tới CartActivity khi chọn "Cart"
            else if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            }
            // Đảm bảo vẫn giữ ProfileActivity khi chọn "Profile"
            else if (id == R.id.nav_profile) {
                // Không cần thực hiện gì nếu đã ở trong ProfileActivity
                return true;
            }

            return false;
        });
    }
}
