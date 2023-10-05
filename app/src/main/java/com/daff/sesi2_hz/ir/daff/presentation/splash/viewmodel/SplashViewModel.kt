package com.daff.sesi2_hz.ir.daff.presentation.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import javax.inject.Inject


class SplashViewModel @Inject
constructor(): ViewModel() {

  @Inject
  lateinit var repo: Repository

  private var _userEmail = MutableLiveData<String>()
  val userEmail: LiveData<String>
    get() = _userEmail

  fun getUserEmail() {
    _userEmail.value = repo.getUserLogin()
  }

}
