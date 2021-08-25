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
                    val questions = response.body()?.questions ?: emptyList()
                    onSuccess(questions)
                } else {
                    onError(response.errorBody()?.string() ?: "unknown exception")
                }
            }

            override fun onFailure(call: Call<StackOverflowQuestions>, t: Throwable) {
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

}