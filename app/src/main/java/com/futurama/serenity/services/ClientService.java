package com.futurama.serenity.services;


import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.models.User;
import com.futurama.serenity.utils.GenericObjectResult;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by wilfried on 04/01/2016.
 */
public interface ClientService {


    @FormUrlEncoded
    @POST("/collarpartners/public/application/client/update")
    Call<GenericObjectResult<User>> subscribe(@Header("token") String token);

    @FormUrlEncoded
    @POST("/collarpartners/public/application/client/update")
    Call<GenericObjectResult<User>> updateRegistration(@Header("token") String token, @Field("uid") String uid, @Field("androidId") String androidId);


    @FormUrlEncoded
    @POST("/collarpartners/public/application/client/add")
    Call<GenericObjectResult<SmsSuspect>> transfererMessage(@Header("token") String token, @Field("uid") String uid, @Field("latitude") float latitude, @Field("longitude") float longitude,
                                                @Field("datereception") String datereception, @Field("numerosuspect") String numerosuspect, @Field("numeroexpediteur") String numeroexpediteur,
                                                @Field("msg") String msg, @Field("envoyerposition") boolean envoyerposition);

}
