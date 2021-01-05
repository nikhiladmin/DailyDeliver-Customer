package com.daytoday.customer.dailydelivery.Network;

import com.daytoday.customer.dailydelivery.Network.Response.*;
import com.daytoday.customer.dailydelivery.searchui.SearchResponseModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    String BASE_URL = "https://dailydeliver.000webhostapp.com/v1/";

    @GET("insert-buss-user-details")
    Call<YesNoResponse> addBussUserDetails(@Query("userid") String userId, @Query("username") String userName
            , @Query("userphone") String userPhone, @Query("useradd") String userAdd);

    @GET("insert-cust-user-details")
    Call<YesNoResponse> addCustUserDetails(@Query("userid") String userId,@Query("username") String userName
            ,@Query("userphone") String userPhone,@Query("useradd") String userAdd);

    @GET("insert-buss-details")
    Call<YesNoResponse> addBussDetails(@Query("bussname") String bussName,@Query("monordaily") String monOrDaily
            ,@Query("paymode") String payMode,@Query("price") String price,@Query("userId") String userId);

    @GET("insert-buss-cust-details")
    Call<YesNoResponse> addBussCustDetails(@Query("bussid") String bussId,@Query("custid") String custId);

    @GET("insert-accepted")
    Call<YesNoResponse> addAcceptedRequest(@Query("bussUserId") String bussUserId,@Query("quantity") String quantity,@Query("date")String date);

    @GET("insert-rejected")
    Call<YesNoResponse> addRejectedRequest(@Query("bussUserId") String bussUserId,@Query("quantity") String quantity,@Query("date")String date);

    @GET("fetch-buss-list-cust")
    Call<BussDetailsResponse> getBussList(@Query("bussid") String bussId,@Query("custid") String custid);

    @GET("fetch-rel-buss")
    Call<CustRelBussResponse> getRelBuss(@Query("custid") String custId);

    @GET("update-buss-details")
    Call<YesNoResponse> updateBussDetails(@Query("name") String name, @Query("phone") String phone
            , @Query("address") String address, @Query("price") String price, @Query("paymode") String payMode);

    @GET("update-buss-user-details")
    Call<YesNoResponse> updateBussUserDetails(@Query("username") String userName
            ,@Query("userphone") String userPhone,@Query("useraddress") String userAdd);

    @GET("update-cust-user-details")
    Call<YesNoResponse> updateCutUserDetails(@Query("name") String name
            ,@Query("phone") String phone,@Query("address") String address,@Query("custid") String custid);

   /* @GET("fetch-emp")
    Call<> getEmp(@Query("") String );*/

    @GET("fetch-daywise")
    Call<DayWiseResponse> getDayWise(@Query("busscustid") String bussCustId);
    @GET("/v1/explore-business")
    Call<SearchResponseModel> getSearch(@QueryMap Map<String,String> map);
}
