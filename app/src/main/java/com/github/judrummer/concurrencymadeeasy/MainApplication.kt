package com.github.judrummer.concurrencymadeeasy

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.judrummer.concurrencymadeeasy.data.GithubApi
import com.github.judrummer.concurrencymadeeasy.domain.GetRepoItemsUsecase
import com.github.judrummer.concurrencymadeeasy.presentation.user.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        //Data
        bind<OkHttpClient>() with provider {
            OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()
        }

        bind("API_URL") from provider { "https://api.github.com/" }

        bind<Retrofit>() with provider {
            Retrofit.Builder().apply {
                baseUrl(instance<String>("API_URL"))
                client(instance())
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            }.build()
        }

        bind<GithubApi>() with provider { instance<Retrofit>().create(GithubApi::class.java) }
        bind() from provider { GetRepoItemsUsecase(instance()) }
        //Presentation
        bind() from provider { UserViewModel(instance(), instance()) }

    }
}


class ExampleWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}