package com.randomuser.essencerandom.createrandomuser.screens.api

import android.os.Build
import android.os.Build.VERSION_CODES.KITKAT
import com.randomuser.essencerandom.createrandomuser.api.OkHttpBuilder
import com.randomuser.essencerandom.createrandomuser.api.RandomUserAPI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.net.ssl.SSLPeerUnverifiedException

@Config(sdk = [Build.VERSION_CODES.P], manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class OkHttpBuilderTest: AutoCloseKoinTest() {
    private val urlToTest = "https://developer.android.com/"

    @Config(sdk = [KITKAT])
    @Test
    fun testGetClient() {
        val response: Response = launchRequest(RandomUserAPI.BASE_URL)
        Assert.assertTrue(response.isSuccessful)
    }

    @Config(sdk = [KITKAT])
    @Test(expected = SSLPeerUnverifiedException::class)
    fun testGetClientHostnameNotSupported() {
        launchRequest(urlToTest)
    }

    private fun launchRequest(url: String): Response {
        val okHttpClient: OkHttpClient = OkHttpBuilder.getClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        return okHttpClient.newCall(request).execute()
    }

    @Test
    fun testGetClientForAndroidP() {
        val response: Response = launchRequest(RandomUserAPI.BASE_URL)
        Assert.assertTrue(response.isSuccessful)
    }

    @Test
    fun testGetClientDifferentHostnameSupportedForAndroidP() {
        val response: Response = launchRequest(urlToTest)
        Assert.assertTrue(response.isSuccessful)
    }
}