package com.example.lobbyapp.data

import com.example.lobbyapp.util.readConfigFromFile
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.ByteString
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

object ApiClient {
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        if (retrofit != null) {
            return retrofit as Retrofit
        }
        val properties = readConfigFromFile()
        val baseUrl = properties.getProperty("IDP_URL")
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("api-key", properties.getProperty("IDP_KEY"))
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(object : Converter.Factory() {
                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *>? {
                    return if (type == ByteString::class.java) {
                        StringConverter()
                    } else {
                        null
                    }
                }
            })
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

        return retrofit as Retrofit
    }

    class StringConverter : Converter<ResponseBody, ByteString> {
        override fun convert(value: ResponseBody): ByteString {
            return value.byteString()
        }
    }
}