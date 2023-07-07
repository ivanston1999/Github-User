package com.submission.githubfinal.core.util

import android.app.Application
import androidx.lifecycle.LiveData

import com.submission.githubfinal.core.db.RoomDb
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.core.db.UserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repo (application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = RoomDb.getDatabase(application)
        mUserDao = db.UserDao()
    }
    fun getAllFavorites(): LiveData<List<User>> = mUserDao.getAllUser()

    fun insert(user: User) {
        executorService.execute { mUserDao.insertFavorite(user) }
    }

    fun delete(id: Int) {
        executorService.execute { mUserDao.removeFavorite(id) }
    }
}