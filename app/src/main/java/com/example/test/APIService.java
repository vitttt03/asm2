package com.example.test;

import java.util.List;

// Import Retrofit annotations
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    // Base URL của API (đảm bảo đúng địa chỉ server)
    String DOMAIN = "http://10.24.45.255:3000/"; // Đổi 192.168.1.5 thành IP server nếu cần

    // Lấy danh sách xe
    @GET("api/list")
    Call<List<CarModel>> getCars();

    // Thêm xe mới
    @POST("api/add_xe")
    Call<List<CarModel>> addCar(@Body CarModel xe);

    // Cập nhật thông tin xe (nếu sử dụng PUT hoặc PATCH)
    @PUT("api/update_xe/{id}")
    Call<List<CarModel>> updateCar(@Path("id") String id, @Body CarModel xe);

    // Xóa xe theo ID
    @DELETE("api/xoa/{id}")
    Call<List<CarModel>> deleteCar(@Path("id") String id);
}
