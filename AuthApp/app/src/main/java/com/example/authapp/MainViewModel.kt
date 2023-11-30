package com.example.authapp

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.Data.KotlinxGenericMapSerializer
import com.example.authapp.Data.LoadingState
import com.example.authapp.Data.Payment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _loading = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loading = _loading.asStateFlow()

    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    private val _payments = MutableStateFlow<List<Payment>>(listOf())
    val payments = _payments.asStateFlow()

    fun getToken(login: String, password: String) {
        _loading.value = LoadingState.Loading
        viewModelScope.launch {
            kotlin.runCatching {
                Repository.RetrofitInstance.payments.getToken(login, password)
            }.fold(
                onSuccess = {
                    _token.value = it.response.token
                    _loading.value = LoadingState.Success
                },
                onFailure = {
                    _loading.value = LoadingState.Failure
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.failure_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    fun emptyTokenValue(){
        _token.value = ""
    }

    fun setTokenValue(value: String){
        _token.value = value
        _loading.value = LoadingState.Success
    }

    fun loadPayments(token: String){
        _loading.value = LoadingState.Loading
        Repository.RetrofitInstance.payments.getPaymentsJson(token)
            .enqueue(object : Callback, retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val bytes = (response.body()!!.bytes())
                    val paymentsJson = String(bytes)
                    _payments.value = parsePaymentsJson(paymentsJson)
                    _loading.value = LoadingState.Success
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, context.resources.getString(R.string.failure_message), Toast.LENGTH_LONG).show()
                    _loading.value = LoadingState.Failure
                }
            })
    }

    private fun parsePaymentsJson(paymentsJson: String): List<Payment>{
        val strResponse = paymentsJson.split("\"response\":[")[1]
        val strList = strResponse.split("{").toMutableList()
        val paymentsStrList = mutableListOf<String>()
        strList.forEach {
            if (it.startsWith("\"id\""))
                paymentsStrList.add(it)
        }
        val serializer = KotlinxGenericMapSerializer()
        val paymentsMaps = mutableListOf<Map<String, Any?>>()

        for (i in paymentsStrList.indices){
            repeat(2){
                paymentsStrList[i] = paymentsStrList[i].replace(".$".toRegex(), "")
            }
            if (i < paymentsStrList.lastIndex)
                paymentsStrList[i] = "{${paymentsStrList[i]}}"
            else paymentsStrList[i] = "{${paymentsStrList[i]}"
        }

        paymentsStrList.forEach {
            paymentsMaps.add(Json.decodeFromString(serializer, it))
        }

        val payments = mutableListOf<Payment>()
        paymentsMaps.forEach {
            payments.add(
                Payment(
                it["id"].toString(),
                it["title"].toString(),
                it["amount"].toString(),
                it["created"].toString()
            )
            )
        }
        return payments
    }
}