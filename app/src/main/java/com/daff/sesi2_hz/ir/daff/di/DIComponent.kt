package com.daff.sesi2_hz.ir.daff.di

import com.daff.sesi2_hz.ir.daff.presentation.auth.view.LoginActivity
import com.daff.sesi2_hz.ir.daff.presentation.favorite.view.FavoriteFragment
import com.daff.sesi2_hz.ir.daff.presentation.home.detail.view.DetailNewsActivity
import com.daff.sesi2_hz.ir.daff.presentation.home.view.HomeFragment
import com.daff.sesi2_hz.ir.daff.presentation.profile.view.ProfileFragment
import com.daff.sesi2_hz.ir.daff.presentation.splash.view.SplashActivity
import com.daff.sesi2_hz.ir.daff.utils.factory.ViewModelModule
import dagger.Component

@Component(modules = [DIModule::class, ViewModelModule::class])
interface DIComponent {

  fun inject(loginActivity: LoginActivity)
  fun inject(homeFragment: HomeFragment)
  fun inject(splashActivity: SplashActivity)
  fun inject(detailNewsActivity: DetailNewsActivity)
  fun inject(favoriteFragment: FavoriteFragment)
  fun inject(profileFragment: ProfileFragment)
}