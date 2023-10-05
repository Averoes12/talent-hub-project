package com.daff.sesi2_hz.ir.daff.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import javax.inject.Inject

class LoginViewModel@Inject
constructor(): ViewModel(){

  @Inject
  lateinit var repo: Repository

  private var _saveUserLogin = MutableLiveData<String>()
  val saveUserLogin: LiveData<String>
    get() = _saveUserLogin

  fun saveUserLogin(email: String, username: String){
    _saveUserLogin.value = repo.saveUserLogin(email, username)
  }
}