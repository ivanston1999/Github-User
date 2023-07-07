package com.submission.githubfinal.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.core.util.Repo

class FavoriteViewModel(application: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()


    val loading : LiveData<Boolean> = _isLoading

    private val userRepo : Repo = Repo(application)
    fun getFavorite() : LiveData<List<User>> = userRepo.getAllFavorites()
}