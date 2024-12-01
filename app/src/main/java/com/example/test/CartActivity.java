package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.PaymentActivity;
import com.example.test.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView lvCartItems;
    private TextView tvTotalPrice;
    private Button btnPayNow;
    private BottomNavigationView bottomNavigationView;

    private ArrayList<String> cartItems;
    private ArrayAdapter<String> cartAdapter;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ các View
        lvCartItems = findViewById(R.id.lv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnPayNow = findViewById(R.id.btn_pay_now);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Khởi tạo giỏ hàng với một số sản phẩm ví dụ
        cartItems = new ArrayList<>();
        cartItems.add("Sản phẩm 1 - 100000 VND");
        cartItems.add("Sản phẩm 2 - 200000 VND");
        cartItems.add("Sản phẩm 3 - 300000 VND");

        // Tính tổng tiền từ danh sách giỏ hàng
        for (String item : cartItems) {
            String[] parts = item.split(" - ");
            int price = Integer.parseInt(parts[1].replace(" VND", "").trim());
            totalPrice += price;
        }

        // Cập nhật tổng tiền trên màn hình
        tvTotalPrice.setText("Tổng tiền: " + totalPrice + " VND");

        // Thiết lập Adapter cho ListView
        cartAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        lvCartItems.setAdapter(cartAdapter);

        // Xử lý sự kiện click vào sản phẩm trong giỏ hàng
        lvCartItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = cartItems.get(position);
                Toast.makeText(CartActivity.this, "Chọn: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện click nút thanh toán
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình thanh toán (PaymentActivity)
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện chọn mục trong BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Đảm bảo rằng khi vào ProfileActivity, menu profile được tô đậm
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
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
