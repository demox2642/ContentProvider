package com.skilbox.usersandcourses.network

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object NetworkRetrofit {

  //  val networkFlipperPlugin = NetworkFlipperPlugin()

    private val okHttpClient = OkHttpClient.Builder()
          .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
       // .addInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        .build()

    private val moshiBuilder = Moshi.Builder()

    private val retrofit = Retrofit.Builder()
        .baseUrl("content//:com.skilbox.contentprovider.provider/")
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .client(okHttpClient)
        .build()


//    val userAndCoursesAPI: UserAndCoursesAPI
//        get() = retrofit.create()
}
