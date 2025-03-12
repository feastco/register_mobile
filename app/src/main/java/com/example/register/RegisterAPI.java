package com.example.register;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("post_register.php")
    Call<ResponseBody> register(@Field("email") String email,
                                @Field("nama") String nama,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("get_login.php")
    Call<ResponseBody> login(
            @Field("nama") String nama,
            @Field("password") String password
    );

    @GET("get_products.php")
    Call<List<Product>> getProducts();

    @GET("get_profile.php")
    Call<ResponseBody> getProfile(
            @Query("email") String email
    );

    @FormUrlEncoded
    @POST("post_profile.php") // Sesuaikan dengan endpoint di server
    Call<ResponseBody> updateProfile(
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("kota") String kota,
            @Field("provinsi") String provinsi,
            @Field("telp") String telp,
            @Field("kodepos") String kodepos,
            @Field("email") String email
    );
}
