package com.submission.githubfinal.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.githubfinal.core.api.ApiConfig
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.core.modelresponse.GitHubUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _user = MutableLiveData<List<DetailUserResponse>>()
    private val _error = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean> = _isLoading
    val listUser: LiveData<List<DetailUserResponse>> = _user
    val error: LiveData<Boolean> = _error

    init {
        detailUser()
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGitHubUser(query)
        client.enqueue(object : Callback<GitHubUserResponse> {
            override fun onResponse(
                call: Call<GitHubUserResponse>,
                response: Response<GitHubUserResponse>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _user.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<GitHubUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("ERROR", "Error : ${t.message.toString()}")
            }
        })
    }


    fun detailUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersList()
        client.enqueue(object : Callback<List<DetailUserResponse>> {
            override fun onResponse(
                call: Call<List<DetailUserResponse>>,
                response: Response<List<DetailUserResponse>>,
            ) {

                if (response.isSuccessful){
                    _isLoading.value = false
                    _user.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<DetailUserResponse>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }
        })
    }


    fun doneToastError(){
        _error.value = false
    }
}