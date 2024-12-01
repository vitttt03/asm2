package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Edit extends AppCompatActivity {
    List<CarModel> carModelList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dialog_edit_car);
        EditText edtTen = (EditText) findViewById(R.id.edtName);
        EditText edtNamSX = (EditText) findViewById(R.id.edtNamSX);
        EditText edtHang = (EditText) findViewById(R.id.edtHang);
        EditText edtGia = (EditText) findViewById(R.id.edtGia);


    }
}