package com.hugo.myrxjava.retrofit;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/9.
 * 版本：v1.0
 * 描述：
 */
public interface Api {

    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse>  register(@Body RegisterRequest request);
}
