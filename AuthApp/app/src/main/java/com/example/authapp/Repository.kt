package com.example.authapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.authapp.Data.Token
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://easypay.world/api-test/"
private const val PREFS_CREDENTIALS = "PREFS_TOKENS"
private const val ACCESS_TOKEN = "ACCESS_TOKEN"
private const val LOGIN = "LOGIN"
private const val PASSWORD = "PASSWORD"
class Repository {

     fun saveAccessToken(context: Context, token: String){
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          prefs.edit().putString(ACCESS_TOKEN, token).apply()
     }

     fun getAccessToken(context:Context): String?{
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          return prefs.getString(ACCESS_TOKEN, "")
     }

     fun saveLogin(context: Context, login: String){
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          prefs.edit().putString(LOGIN, login).apply()
     }

     fun getLogin(context:Context): String?{
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          return prefs.getString(LOGIN, "")
     }

     fun savePassword(context: Context, password: String){
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          prefs.edit().putString(PASSWORD, password).apply()
     }

     fun getPassword(context:Context): String?{
          val prefs = context.getSharedPreferences(PREFS_CREDENTIALS, MODE_PRIVATE)
          return prefs.getString(PASSWORD, "")
     }


     object RetrofitInstance{
          private val request = Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build()

          val payments = request.create(SearchPayments::class.java)
     }
}

interface SearchPayments{
     @Headers(
          "app-key: 12345",
          "v: 1"
     )
     @GET("payments")
     fun getPaymentsJson(@Header("token") token: String): Call<ResponseBody>

     @Headers(
          "app-key: 12345",
          "v: 1"
     )
     @POST("login")
     @FormUrlEncoded
     suspend fun getToken(@Field("login") login: String, @Field("password") password: String): Token
}
