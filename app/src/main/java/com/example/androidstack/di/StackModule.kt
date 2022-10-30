package com.example.androidstack.di

import android.content.Context
import androidx.room.Room
import com.example.androidstack.api.StackService
import com.example.androidstack.db.StackCache
import com.example.androidstack.db.StackDao
import com.example.androidstack.db.StackDatabase
import com.example.androidstack.repository.StackRepository
import com.example.androidstack.viewmodel.StackViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
class ContextModule(val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context.applicationContext
    }
}

@Module(includes = [ContextModule::class])
class CacheModule{

    @Singleton
    @Provides
    fun provideDatabase(context: Context): StackDatabase {
        return Room.databaseBuilder(context, StackDatabase::class.java, "questions-db")
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideDAO(stackDatabase: StackDatabase): StackDao {
        return stackDatabase.stackDao()
    }

    @Singleton
    @Provides
    fun provideCache(stackDao: StackDao): StackCache {
        return StackCache(stackDao, Executors.newSingleThreadExecutor())
    }

}


@Module(includes = [NetworkModule::class])
class ContentModule {

    @Singleton
    @Provides
    fun provideRepository(service: StackService, cache: StackCache ): StackRepository {
        return StackRepository(service, cache)
    }

    @Provides
    fun provideStackViewModelFactory(stackRepository: StackRepository): StackViewModelFactory {
        return StackViewModelFactory(stackRepository)
    }

}


@Module
class NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    @Provides
    fun provideOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/")
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideStackService(retrofit: Retrofit): StackService {
        return retrofit.create(StackService::class.java)
    }

}


