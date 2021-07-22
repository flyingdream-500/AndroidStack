package com.example.androidstack.api

import android.util.Log
import com.example.androidstack.model.Question
import com.example.androidstack.model.StackOverflowQuestions
import com.example.androidstack.model.StackRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


fun search(
    service: StackService,
    request: StackRequest,
    page: Int,
    pageSize: Int,
    onSuccess: (List<Question>) -> Unit,
    onError: (String) -> Unit
) {
    Log.d("TAGG", "loadQuestions $request")
    service.loadQuestions(request.query, request.sort, request.order, page, pageSize).enqueue(
        object : Callback<StackOverflowQuestions> {
            override fun onResponse(
                call: Call<StackOverflowQuestions>,
                response: Response<StackOverflowQuestions>
            ) {
                if (response.isSuccessful) {
                    Log.d("TAGG", "loadQuestions onSuccess")
                    val questions = response.body()?.questions ?: emptyList()
                    onSuccess(questions)
                } else {
                    Log.d("TAGG", "loadQuestions onError ${response.errorBody()?.string()}")
                    onError(response.errorBody()?.string() ?: "unknown exception")
                }
            }

            override fun onFailure(call: Call<StackOverflowQuestions>, t: Throwable) {
                Log.d("TAGG", "fail")
                onError(t.message ?: "unknown exception")
            }

        }
    )
}

interface StackService {

    @GET("2.2/search?site=stackoverflow")
    fun loadQuestions(@Query("intitle") request: String,
                      @Query("sort") sort: String,
                      @Query("order") order: String,
                      @Query("page") page: Int,
                      @Query("pagesize") pageSize: Int): Call<StackOverflowQuestions>

    companion object {

        fun create(): StackService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client =OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/")
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StackService::class.java)
        }

    }

}