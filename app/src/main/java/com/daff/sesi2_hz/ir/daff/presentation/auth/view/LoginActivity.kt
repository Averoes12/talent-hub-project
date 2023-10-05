package com.daff.sesi2_hz.ir.daff.presentation.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daff.sesi2_hz.ir.daff.MainActivity
import com.daff.sesi2_hz.ir.daff.databinding.ActivityLoginBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.auth.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityLoginBinding
	var isValid = false

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var loginViewModel: LoginViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

		DaggerDIComponent.builder()
			.dIModule(DIModule(this))
			.build().inject(this)

		loginViewModel = ViewModelProvider(
			this,
			viewModelFactory
		)[LoginViewModel::class.java]

		binding.username.editText?.doOnTextChanged { text, _, _, _ ->
			val username = text.toString()
			if(username.isBlank()){
				binding.username.editText?.error = "Username can not be blank"
				binding.btnSubmit.visibility = View.GONE
			} else {
				val email = binding.email.editText?.text.toString()
				val password = binding.password.editText?.text.toString()
				
				validate(username, email, password)
			}
		}
		
		binding.email.editText?.doOnTextChanged { text, _, _, _ ->
			val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
			val email = text.toString()
			if (email.isBlank()){
				binding.email.editText?.error = "Email can not be blank"
				binding.btnSubmit.visibility = View.GONE
			} else if(!email.matches(emailRegex.toRegex())){
				binding.email.editText?.error = "Enter an valid email"
				binding.btnSubmit.visibility = View.GONE
			}else {
				val username = binding.username.editText?.text.toString()
				val password = binding.password.editText?.text.toString()
				
				validate(username, email, password, )
			}
		}
		
		binding.password.editText?.doOnTextChanged { text, _, _, _ ->
			val password = text.toString()
			if(password.isBlank()){
				binding.password.editText?.error = "Password can not be blank"
				binding.btnSubmit.visibility = View.GONE
			}else {
				val username = binding.username.editText?.text.toString()
				val email = binding.email.editText?.text.toString()
				
				validate(username, email, password)
			}
		}
		
		binding.btnSubmit.setOnClickListener {
			val username = binding.username.editText?.text.toString()
			val email = binding.email.editText?.text.toString()
			loginViewModel.saveUserLogin(email, username)

		}

		loginViewModel.saveUserLogin.observe(this, Observer {
			if(it === "success"){
				Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
				val intent = Intent(this@LoginActivity, MainActivity::class.java)
				startActivity(intent)
				finish()
			}
		})
	}
	
	private fun validate(username: String, email: String, password: String){
		val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
		if(username.isNotBlank() && email.matches(regex.toRegex()) && password.isNotBlank()){
			binding.btnSubmit.visibility = View.VISIBLE
		}
	}
}