package xyz.mperminov.tfscoursework.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.mperminov.tfscoursework.activities.LoginRepository
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {
    @Provides
    @Singleton
    fun context(application: Application): Context = application

    @Provides
    @Singleton
    fun database(context: Context, databaseName: String): HomeworkDatabase {
        return Room.databaseBuilder(
            context,
            HomeworkDatabase::class.java, databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun databaseName(): String = "lectures.db"

    @Provides
    @Singleton
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val timeout: Long = 2
        return OkHttpClient.Builder()
            .addInterceptor(
                loggingInterceptor
            )
            .addInterceptor { chain ->
                TimeUnit.SECONDS.sleep(timeout)
                chain.proceed(chain.request())
            }.build()
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { msg -> Log.d("OkHttp", msg) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun retrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(Api.API_URL).client(httpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun api(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun sharedPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun authHolder(sharedPreferences: SharedPreferences): AuthHolder = AuthHolder(sharedPreferences)

    @Provides
    @Singleton
    fun loginRepository(authHolder: AuthHolder): LoginRepository = LoginRepository(authHolder)

    @Provides
    @Singleton
    fun userRepository(context: Context): UserRepository = SharedPrefUserRepository(context)
}