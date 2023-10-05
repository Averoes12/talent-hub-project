package com.daff.sesi2_hz.ir.daff.di

import android.content.Context
import android.content.SharedPreferences
import com.daff.sesi2_hz.ir.daff.data.db.AppDatabase
import com.daff.sesi2_hz.ir.daff.data.db.LocalData
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import com.daff.sesi2_hz.ir.daff.network.APIClient
import com.daff.sesi2_hz.ir.daff.network.APIService
import com.daff.sesi2_hz.ir.daff.utils.SharedPreferencesFactory
import dagger.Module
import dagger.Provides

@Module
class DIModule(val context: Context) {

  @Provides
  fun provideContext(): Context {
    return context
  }

  @Provides
  fun provideService(): APIService {
    return APIService.createService(APIService::class.java)
  }

  @Provides
  fun providePref(context: Context): SharedPreferences {
    return SharedPreferencesFactory.initPreferences(context)
  }

  @Provides
  fun provideDB(context: Context): AppDatabase {
    return AppDatabase.getDatabase(context)
  }

  @Provides
  fun provideRemote(apiService: APIService): APIClient {
    return APIClient(apiService)
  }

  @Provides
  fun provideLocal(pref: SharedPreferences, db: AppDatabase): LocalData {
    return LocalData(pref, db)
  }

  @Provides
  fun provideRepository(
    localDataSource: LocalData,
    remoteDataSource: APIClient,
  ): Repository {
    return Repository(localDataSource, remoteDataSource)
  }

}