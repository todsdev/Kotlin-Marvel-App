package com.tods.project_marvel.dependency_injection

import android.content.Context
import androidx.room.Room
import com.tods.project_marvel.data.local.MarvelDatabase
import com.tods.project_marvel.data.remote.ServiceApi
import com.tods.project_marvel.util.Constants
import com.tods.project_marvel.util.Constants.BASE_URL
import com.tods.project_marvel.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideMarvelDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MarvelDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideMarvelDao(database: MarvelDatabase) = database.marvelDao()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder().addInterceptor { chain ->
            val currentTimeStamp = System.currentTimeMillis()
            val newUrl = chain.request().url.newBuilder()
                .addQueryParameter(Constants.TS, currentTimeStamp.toString())
                .addQueryParameter(Constants.APIKEY, Constants.PUBLIC_KEY)
                .addQueryParameter(Constants.HASH, providesToMd5Hash(
                        currentTimeStamp.toString() + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY)
            ).build()

            val newRequest = chain.request().newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }.addInterceptor(logging).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }

    @Singleton
    @Provides
    fun providesToMd5Hash(encrypted: String): String {
        var pass = encrypted
        var encryptedString: String? = null
        val md5: MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
            md5.update(pass.toByteArray(), 0, pass.length)
            pass = BigInteger(1, md5.digest()).toString(16)
            while (pass.length < 32) {
                pass = "0$pass"
            }
            encryptedString = pass
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        Timber.d("hash -> $encryptedString")
        return encryptedString?: ""
    }
}