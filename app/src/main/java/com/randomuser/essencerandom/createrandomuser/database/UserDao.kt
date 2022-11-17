package com.randomuser.essencerandom.createrandomuser.database

import androidx.room.*
import com.randomuser.essencerandom.createrandomuser.model.User

/**
 * Responsible of all the access made to the user table.
 */
@Dao
interface UserDao {
    @Query("SELECT count(email) FROM user")
    suspend fun count(): Int

    @Query("SELECT * FROM user WHERE email=:email ")
    suspend fun get(email: String): User

    @Query("SELECT * FROM user LIMIT :limit OFFSET :offset")
    suspend fun get(offset: Int, limit: Int): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<User>)
}