package com.example.test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPaymentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvPaymentInfo = findViewById(R.id.tv_payment_info);

        // Lấy giá trị tổng tiền từ Intent
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);

        // Hiển thị thông tin thanh toán
        tvPaymentInfo.setText("Tổng tiền cần thanh toán: " + totalPrice + " VND");
    }
}
