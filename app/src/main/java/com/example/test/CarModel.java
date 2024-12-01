package com.example.test;

import com.google.gson.annotations.SerializedName;

public class CarModel {
    @SerializedName("_id") // Trường "_id" trong JSON sẽ ánh xạ vào biến này
    private String _id;

    @SerializedName("ten") // Trường "ten" trong JSON sẽ ánh xạ vào biến này
    private String ten;

    @SerializedName("namSX") // Trường "namSX" trong JSON sẽ ánh xạ vào biến này
    private int namSX;

    @SerializedName("hang") // Trường "hang" trong JSON sẽ ánh xạ vào biến này
    private String hang;

    @SerializedName("gia") // Trường "gia" trong JSON sẽ ánh xạ vào biến này
    private double gia;

    @SerializedName("anh") // Trường "anh" trong JSON sẽ ánh xạ vào biến này
    private String anh;

    // Constructor với _id
    public CarModel(String _id, String ten, int namSX, String hang, double gia, String anh) {
        this._id = _id;
        this.ten = ten;
        this.namSX = namSX;
        this.hang = hang;
        this.gia = gia;
        this.anh = anh;
    }

    // Constructor không có _id (Dùng khi tạo xe mới)
    public CarModel(String ten, int namSX, String hang, double gia, String anh) {
        this.ten = ten;
        this.namSX = namSX;
        this.hang = hang;
        this.gia = gia;
        this.anh = anh;
    }

    // Getter và Setter cho _id
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    // Getter và Setter cho ten
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    // Getter và Setter cho namSX
    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    // Getter và Setter cho hang
    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    // Getter và Setter cho gia
    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    // Getter và Setter cho anh
    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
