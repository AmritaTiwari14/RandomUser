package com.randomuser.essencerandom.createrandomuser.api

import com.randomuser.essencerandom.createrandomuser.model.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Represent the access point for the Random User API.
 * We fetch the data by batch of ten items.
 * @see: https://randomuser.me/api/1.0/?seed=lydia&results=10&page=1
 */
interface RandomUserAPI {
    companion object {
        const val HOSTNAME = "randomuser.me"
        const val BASE_URL = "https://randomuser.me/api/1.0/"
        const val NUMBER_OF_ITEM_PER_REQUEST = 10
        const val SEED = "lydia"
    }

    @GET(" ")
    suspend fun getUsersBatch(@Query("page") page: Int,
                              @Query("results") results: Int = NUMBER_OF_ITEM_PER_REQUEST,
                              @Query("seed") seed: String = SEED): Response<RandomUserResponse>
}