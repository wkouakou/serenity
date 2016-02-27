package com.futurama.serenity.services;


import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.models.User;
import com.futurama.serenity.models.WhiteList;
import com.futurama.serenity.utils.GenericObjectResult;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by wilfried on 04/01/2016.
 */
public interface ClientService {


    /**
     * @param token
     * @return
     */
    @POST("/serenity/public/app/user/add")
    Call<GenericObjectResult<User>> subscribe(@Header("token") String token);

    /**
     * @param token
     * @return
     */
    @GET("/serenity/public/app/user/upd")
    Call<List<GenericObjectResult<WhiteList>>> getWhiteList(@Header("token") String token);

    /**
     * @param token
     * @param uid
     * @param androidId
     * @return
     */
    @FormUrlEncoded
    @POST("/serenity/public/app/user/upd")
    Call<GenericObjectResult<User>> updateRegistration(@Header("token") String token, @Field("uid") String uid, @Field("androidId") String androidId);

    /**
     * @param token
     * @param uid
     * @param numero
     * @return
     */
    @FormUrlEncoded
    @POST("/serenity/public/app/user/upd")
    Call<GenericObjectResult<User>> updateNumero(@Header("token") String token, @Field("uid") String uid, @Field("numero") String numero);

    /**
     * @param token
     * @param uid
     * @param email
     * @return
     */
    @FormUrlEncoded
    @POST("/serenity/public/app/user/upd")
    Call<GenericObjectResult<User>> updateEmail(@Header("token") String token, @Field("uid") String uid, @Field("email") String email);

    /**
     * @param token
     * @param uid
     * @param latitude
     * @param longitude
     * @param datereception
     * @param numerosuspect
     * @param numeroexpediteur
     * @param msg
     * @param envoyerposition
     * @return
     */
    @FormUrlEncoded
    @POST("/collarpartners/public/application/client/add")
    Call<GenericObjectResult<SmsSuspect>> transfererMessage(@Header("token") String token, @Field("uid") String uid, @Field("latitude") float latitude, @Field("longitude") float longitude,
                                                @Field("datereception") String datereception, @Field("numerosuspect") String numerosuspect, @Field("numeroexpediteur") String numeroexpediteur,
                                                @Field("msg") String msg, @Field("envoyerposition") boolean envoyerposition);

}
