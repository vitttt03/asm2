package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ListView lvMain;
    private List<CarModel> listCarModel;
    private CarAdapter carAdapter;
    private APIService apiService;
    private static final String DEFAULT_IMAGE_URL = "https://via.placeholder.com/150";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = findViewById(R.id.listviewMain);
        searchView = findViewById(R.id.searchView);

        // Khởi tạo Retrofit và APIService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIService.class);

        // Tải danh sách xe từ server
        loadCars();

        // Thiết lập Floating Action Button để mở dialog thêm xe mới
        findViewById(R.id.fab_add).setOnClickListener(v -> showAddCarDialog());

        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        // Thiết lập SearchView để tìm kiếm xe
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Không cần xử lý khi nhấn submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Nếu không có gì tìm kiếm, hiển thị lại toàn bộ danh sách
                if (newText.isEmpty()) {
                    loadData(listCarModel); // Hiển thị danh sách gốc
                } else {
                    // Tạo danh sách mới và lọc theo tên xe
                    ArrayList<CarModel> filteredList = new ArrayList<>();
                    for (CarModel car : listCarModel) {
                        if (car.getTen().toLowerCase().contains(newText.toLowerCase())) {
                            filteredList.add(car);
                        }
                    }
                    loadData(filteredList); // Cập nhật danh sách tìm kiếm
                }
                return false;
            }
        });
    }

    // Hàm để cập nhật dữ liệu vào Adapter
    private void loadData(List<CarModel> updatedList) {
        // Cập nhật dữ liệu mới cho adapter và thông báo cho UI thay đổi
        carAdapter = new CarAdapter(MainActivity.this, updatedList, apiService);
        lvMain.setAdapter(carAdapter);
    }

    // Hàm tải danh sách xe từ API
    private void loadCars() {
        apiService.getCars().enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listCarModel = response.body();
                    assignDefaultImageUrls(listCarModel);
                    loadData(listCarModel); // Hiển thị danh sách xe sau khi tải
                } else {
                    Log.e("MainActivity", "API error: " + response.code());
                    Toast.makeText(MainActivity.this, "Lỗi khi tải danh sách xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("MainActivity", "API failure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Không thể kết nối với server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị Dialog để thêm xe mới
    private void showAddCarDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_car, null);

        new AlertDialog.Builder(this)
                .setTitle("Thêm xe mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    try {
                        // Lấy dữ liệu từ dialog và kiểm tra
                        String name = ((EditText) dialogView.findViewById(R.id.edtCarName)).getText().toString().trim();
                        String yearStr = ((EditText) dialogView.findViewById(R.id.edtCarYear)).getText().toString().trim();
                        String brand = ((EditText) dialogView.findViewById(R.id.edtCarBrand)).getText().toString().trim();
                        String priceStr = ((EditText) dialogView.findViewById(R.id.edtCarPrice)).getText().toString().trim();
                        String imageUrl = ((EditText) dialogView.findViewById(R.id.edtCarImage)).getText().toString().trim();

                        if (name.isEmpty() || yearStr.isEmpty() || brand.isEmpty() || priceStr.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int year = Integer.parseInt(yearStr);
                        int price = Integer.parseInt(priceStr);

                        if (imageUrl.isEmpty()) {
                            imageUrl = DEFAULT_IMAGE_URL;
                        }

                        CarModel newCar = new CarModel(name, year, brand, price, imageUrl);
                        addNewCar(newCar);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Hàm thêm xe mới vào danh sách
    private void addNewCar(CarModel newCar) {
        apiService.addCar(newCar).enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listCarModel.clear();
                    listCarModel.addAll(response.body());
                    assignDefaultImageUrls(listCarModel);
                    loadData(listCarModel); // Cập nhật danh sách sau khi thêm xe mới
                    Toast.makeText(MainActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi khi thêm xe mới", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm gán ảnh mặc định cho xe nếu không có ảnh
    private void assignDefaultImageUrls(List<CarModel> cars) {
        for (CarModel car : cars) {
            if (car.getAnh() == null || car.getAnh().isEmpty()) {
                car.setAnh(DEFAULT_IMAGE_URL);
            }
        }
    }
}
