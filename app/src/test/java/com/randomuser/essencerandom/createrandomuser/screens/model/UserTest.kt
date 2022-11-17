package com.randomuser.essencerandom.createrandomuser.screens.model

import android.os.Build
import com.randomuser.essencerandom.createrandomuser.model.User
import com.randomuser.essencerandom.createrandomuser.screens.MockModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class UserTest: AutoCloseKoinTest() {
    private val firstName= "John"
    private val lastName = "Snow"
    private lateinit var user: User

    @Before
    fun setUp() {
        user = MockModel.buildUser(firstName, lastName, "john.snow@gmail.com")
    }

    @Test
    fun testGetCountryName() {
        Assert.assertEquals("France", user.getCountryName())
        user = MockModel.buildUser("John", "Snow", "john.snow@gmail.com", "AU")
        Assert.assertEquals("Australia", user.getCountryName())
    }

    @Test
    fun testGetUserCompleteName() {
        val completeName: String = user.getUserCompleteName()
        Assert.assertTrue(completeName.contains(firstName))
        Assert.assertTrue(completeName.contains(lastName))
    }
}