package com.submission.githubfinal.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.githubfinal.core.api.ApiConfig
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Boolean>()
    private val _userFollow = MutableLiveData<List<DetailUserResponse>>()

    val loading : LiveData<Boolean> = _isLoading
    val error : LiveData<Boolean> = _error
    val userFollow : LiveData<List<DetailUserResponse>> = _userFollow

    fun getUserFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<DetailUserResponse>> {
            override fun onResponse(
                call: Call<List<DetailUserResponse>>,
                response: Response<List<DetailUserResponse>>,
            ) {
                _isLoading.value = false
                _userFollow.value = response.body()
            }

            override fun onFailure(call: Call<List<DetailUserResponse>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }

        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<DetailUserResponse>> {
            override fun onResponse(
                call: Call<List<DetailUserResponse>>,
                response: Response<List<DetailUserResponse>>,
            ) {
                _isLoading.value = false
                _userFollow.value = response.body()
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