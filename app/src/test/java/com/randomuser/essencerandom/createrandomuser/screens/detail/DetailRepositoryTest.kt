package com.randomuser.essencerandom.createrandomuser.screens.detail

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.randomuser.essencerandom.createrandomuser.database.UserDao
import com.randomuser.essencerandom.createrandomuser.model.User
import com.randomuser.essencerandom.createrandomuser.screens.MockModel
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.collections.ArrayList

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DetailRepositoryTest: AutoCloseKoinTest() {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val firstName = "John"
    private val lastName = "Doe"
    private val email = "john.doe@gmail.com"
    private lateinit var detailRepository: DetailRepository

    @Before
    fun setUp() {
        val userDao: UserDao by inject()
        runBlocking(Dispatchers.IO) {
            userDao.insert(getUsers())
        }
        detailRepository = DetailRepository()
    }

    private fun getUsers(): List<User> {
        val users = ArrayList<User>()
        users.add(MockModel.buildUser(firstName, lastName, email))
        return users
    }

    @Test
    fun testRetrieveUser() {
        runBlocking {
            val user: User = detailRepository.retrieveUser(email)
            Assert.assertNotNull(user)
            Assert.assertEquals(firstName, user.name.first)
            Assert.assertEquals(lastName, user.name.last)
        }
    }
}