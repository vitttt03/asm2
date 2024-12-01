package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends BaseAdapter {
    private List<CarModel> carModelList;
    private Context context;
    private APIService apiService;

    public CarAdapter(Context context, List<CarModel> carModelList, APIService apiService) {
        this.context = context;
        this.carModelList = carModelList;
        this.apiService = apiService;
    }

    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return carModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarModel car = carModelList.get(position);
        holder.bindData(car);

        // Xử lý sự kiện nhấn nút chỉnh sửa
        holder.btnEdit.setOnClickListener(v -> showEditDialog(car, position));

        // Xử lý sự kiện nhấn nút xóa
        holder.btnDelete.setOnClickListener(v -> deleteCar(position));
        convertView.setOnClickListener(v -> showCarDetailsDialog(car));
        holder.btnMua.setOnClickListener(v -> {
            // Chuyển dữ liệu của item sang CartActivity
            Intent intent = new Intent(context, CartActivity.class);
            intent.putExtra("car_id", car.get_id());
            intent.putExtra("car_name", car.getTen());
            intent.putExtra("car_brand", car.getHang());
            intent.putExtra("car_price", car.getGia());
            intent.putExtra("car_image", car.getAnh());
            context.startActivity(intent);
        });
        return convertView;
    }

    private void showEditDialog(CarModel car, int position) {
        // Tạo dialog chỉnh sửa
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_car, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(dialogView);

        // Ánh xạ các trường nhập liệu
        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtNamSX = dialogView.findViewById(R.id.edtNamSX);
        EditText edtHang = dialogView.findViewById(R.id.edtHang);
        EditText edtGia = dialogView.findViewById(R.id.edtGia);

        // Hiển thị thông tin hiện tại của xe
        edtName.setText(car.getTen());
        edtNamSX.setText(String.valueOf(car.getNamSX()));
        edtHang.setText(car.getHang());
        edtGia.setText(String.valueOf(car.getGia()));

        // Xử lý sự kiện khi nhấn nút Lưu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Kiểm tra dữ liệu nhập vào
            String name = edtName.getText().toString().trim();
            String year = edtNamSX.getText().toString().trim();
            String brand = edtHang.getText().toString().trim();
            String price = edtGia.getText().toString().trim();

            if (name.isEmpty() || year.isEmpty() || brand.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật thông tin xe
            car.setTen(name);
            car.setNamSX(Integer.parseInt(year));
            car.setHang(brand);
            car.setGia(Double.parseDouble(price));

            // Gọi phương thức cập nhật xe
            updateCar(car, position);
        });

        // Xử lý sự kiện khi nhấn nút Hủy
        builder.setNegativeButton("Hủy", null);

        builder.create().show();
    }
    private void showCarDetailsDialog(CarModel car) {
        // Tạo dialog hiển thị chi tiết
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_car_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(dialogView);

        // Ánh xạ các TextView
        TextView tvDetailName = dialogView.findViewById(R.id.tvDetailName);
        TextView tvDetailNamSX = dialogView.findViewById(R.id.tvDetailNamSX);
        TextView tvDetailHang = dialogView.findViewById(R.id.tvDetailHang);
        TextView tvDetailGia = dialogView.findViewById(R.id.tvDetailGia);
        ImageView imgDetailAvatar = dialogView.findViewById(R.id.imgDetailAvatar);

        // Gán dữ liệu cho TextView
        tvDetailName.setText("Tên: " + car.getTen());
        tvDetailNamSX.setText("Năm sản xuất: " + car.getNamSX());
        tvDetailHang.setText("Hãng: " + car.getHang());
        tvDetailGia.setText("Giá: " + car.getGia());

        Glide.with(imgDetailAvatar.getContext())
                .load(car.getAnh())
                .placeholder(R.drawable.load)
                .error(R.drawable.erro)
                .into(imgDetailAvatar);

        // Nút đóng dialog
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
    private void updateCar(CarModel car, int position) {
        apiService.updateCar(car.get_id(), car).enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                // Kiểm tra phản hồi từ API
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isEmpty()) {
                        // Cập nhật dữ liệu tại vị trí trong danh sách
                        carModelList.set(position, car);
                        notifyDataSetChanged();  // Cập nhật lại UI

                        // Thông báo thành công
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Nếu không có dữ liệu trong phản hồi
                        Toast.makeText(context, "Không có dữ liệu trong phản hồi!", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "No data in response body.");
                    }
                } else {
                    // Thông báo lỗi khi không thành công (lỗi từ server)
                    Toast.makeText(context, "Cập nhật thất bại: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Error code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                // Thông báo lỗi khi kết nối bị lỗi
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });
    }


    private void deleteCar(int position) {
        CarModel car = carModelList.get(position);

        // Hiển thị dialog xác nhận xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa xe \"" + car.getTen() + "\" không?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi API xóa xe
            apiService.deleteCar(car.get_id()).enqueue(new Callback<List<CarModel>>() {
                @Override
                public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Cập nhật danh sách xe sau khi xóa
                        carModelList.remove(position); // Xóa đối tượng khỏi danh sách
                        notifyDataSetChanged();
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CarModel>> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private static class ViewHolder {
        TextView tvName, tvNamSX, tvHang, tvGia;
        ImageView imgAvatar;
        Button btnEdit, btnDelete,btnMua;

        ViewHolder(View view) {
            tvName = view.findViewById(R.id.tvName);
            tvNamSX = view.findViewById(R.id.tvNamSX);
            tvHang = view.findViewById(R.id.tvHang);
            tvGia = view.findViewById(R.id.tvGia);
            imgAvatar = view.findViewById(R.id.imgAvatatr);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnDelete = view.findViewById(R.id.btn_delete);
            btnMua = view.findViewById(R.id.btn_buy);
        }

        void bindData(CarModel car) {
            tvName.setText("Tên: " + car.getTen());
            tvNamSX.setText("Năm: " + car.getNamSX());
            tvHang.setText("Hãng: " + car.getHang());
            tvGia.setText("Giá: " + car.getGia());

            Glide.with(imgAvatar.getContext())
                    .load(car.getAnh())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.erro)
                    .into(imgAvatar);
        }
    }
}
