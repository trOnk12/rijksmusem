package com.example.maxdoroassigment.data.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


// i know unsafe to store it here
const val AUTH_KEY = "0fiuZFh4"

class KeyAuthenticationInterceptor : AuthenticationInterceptor() {

    override fun interceptUrl(newUrlBuilder: HttpUrl.Builder): HttpUrl {
       return newUrlBuilder
            .addQueryParameter("key", AUTH_KEY)
            .build()
    }
}

abstract class AuthenticationInterceptor : Interceptor {

    abstract fun interceptUrl(newUrlBuilder: HttpUrl.Builder): HttpUrl

    override fun intercept(chain: Interceptor.Chain): Response {
        val interceptUrl = interceptUrl(chain.request().url.newBuilder())

        val request = chain.request().newBuilder().url(interceptUrl).build()

        return chain.proceed(request)
    }

}