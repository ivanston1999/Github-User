package com.submission.githubfinal.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.githubfinal.core.api.ApiConfig
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.core.util.Repo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class DetailViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _listUser = MutableLiveData<DetailUserResponse>()
    private val _error = MutableLiveData<Boolean>()

    val loading : LiveData<Boolean> = _isLoading
    val listUser : LiveData<DetailUserResponse> = _listUser
    val error : LiveData<Boolean> = _error

    private val mUserRepository: Repo =
        Repo(application)

    fun insert(user: User){
        mUserRepository.insert(user)
    }

    fun delete(id: Int){
        mUserRepository.delete(id)
    }

    fun getFavorite():LiveData<List<User>> =
        mUserRepository.getAllFavorites()

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _listUser.value = response.body()
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
            }
        })
    }

    fun doneToastError(){
        _error.value = false
    }
}
