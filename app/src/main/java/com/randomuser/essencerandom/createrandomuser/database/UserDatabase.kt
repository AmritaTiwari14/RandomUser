package com.randomuser.essencerandom.createrandomuser.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.randomuser.essencerandom.createrandomuser.model.User


@Database(entities = [User::class], version = 1)
@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase: RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "random_user_database"
    }

    abstract fun userDao(): UserDao
}