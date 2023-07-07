package com.submission.githubfinal.core.api


import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.core.modelresponse.GitHubUserResponse

import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsersList(): Call<List<DetailUserResponse>>

    @GET("search/users")
    fun getGitHubUser(
        @Query("q") query: String
    ): Call<GitHubUserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String
    ): Call<List<DetailUserResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String
    ): Call<List<DetailUserResponse>>


}