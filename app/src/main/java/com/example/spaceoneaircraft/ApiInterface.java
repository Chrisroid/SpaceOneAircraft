package com.example.spaceoneaircraft;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @GET("/api/scope/chris/items/aircraft")
    Call<List<Aircrafts>> getAircraft();

    @FormUrlEncoded
    @POST("/api/scope/chris/items/aircraft")
    Call<Aircrafts> insertAircraft(
            @Field("key") String key,
            @Field("name") String name,
            @Field("manufacturer") String manufacturer,
            @Field("manufacturingYear") String manufacturingYear;

    @FormUrlEncoded
    @PUT("/api/scope/chris/item/{id}")
    Call<Aircrafts> updateAircraft(
            @Field("key") String key,
            @Field("id") int id,
            @Field("name") String name,
            @Field("manufacturer") String manufacturer,
            @Field("manufacturingYear") String manufacturingYear;

    @FormUrlEncoded
    @DELETE("/api/scope/chris/item/{id}")
    Call<Aircrafts> deleteAircraft(
            @Field("key") String key,
            @Field("id") int id;

    void insertAircraft();
}
