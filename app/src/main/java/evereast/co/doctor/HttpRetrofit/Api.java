package evereast.co.doctor.HttpRetrofit;

import java.util.List;

import evereast.co.doctor.Constants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Belal on 10/2/2017.
 */

public interface Api {

    String BASE_URL = Constants.BASE_URL;

    @GET("medicine.php")
    Call<List<MedicinesModel>> GetMedicines();

    @GET("generics.php")
    Call<List<GenericsModel>> GetGenerics();

    @FormUrlEncoded
    @POST("send_otp.php")
    Call<String> SendOtp(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("send_forgot_pass_otp.php")
    Call<String> SendForgotPasswordOtp(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("reset_password.php")
    Call<String> ResetPassword(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("verify_otp.php")
    Call<String> VerifyOtp(@Field("phone")String phone,@Field("otp")String otp);

}
