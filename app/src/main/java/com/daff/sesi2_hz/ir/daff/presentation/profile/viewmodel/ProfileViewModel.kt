package com.daff.sesi2_hz.ir.daff.presentation.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daff.sesi2_hz.ir.daff.data.repo.Repository
import javax.inject.Inject

class ProfileViewModel @Inject
constructor(): ViewModel() {

	@Inject
	lateinit var repo: Repository

	private var _userEmail = MutableLiveData<String>()
	val userEmail: LiveData<String>
		get() = _userEmail

	private var _username = MutableLiveData<String>()
	val username: LiveData<String>
		get() = _username

	private var _logoutState = MutableLiveData<String>()
	val logoutState: LiveData<String>
		get() = _logoutState

	fun getUserEmail() {
		_userEmail.value = repo.getUserLogin()
	}

	fun getUsername() {
		_username.value = repo.getUsername()
	}

	fun logout(){
		_logoutState.value = repo.logout()
	}

}
