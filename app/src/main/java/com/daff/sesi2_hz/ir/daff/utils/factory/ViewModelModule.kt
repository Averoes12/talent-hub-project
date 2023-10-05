package com.daff.sesi2_hz.ir.daff.utils.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daff.sesi2_hz.ir.daff.presentation.auth.viewmodel.LoginViewModel
import com.daff.sesi2_hz.ir.daff.presentation.favorite.viewmodel.FavoriteViewModel
import com.daff.sesi2_hz.ir.daff.presentation.home.detail.viewmodel.DetailViewModel
import com.daff.sesi2_hz.ir.daff.presentation.home.viewmodel.HomeViewModel
import com.daff.sesi2_hz.ir.daff.presentation.profile.viewmodel.ProfileViewModel
import com.daff.sesi2_hz.ir.daff.presentation.splash.viewmodel.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

  @Binds
  internal abstract fun bindViewModelFactory(factory: ViewModelFactory) : ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(LoginViewModel::class)
  internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  internal abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SplashViewModel::class)
  internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(DetailViewModel::class)
  internal abstract fun detailViewModel(viewModel: DetailViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(FavoriteViewModel::class)
  internal abstract fun favoriteViewModel(viewModel: FavoriteViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

}