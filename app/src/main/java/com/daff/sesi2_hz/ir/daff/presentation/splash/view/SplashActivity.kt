package com.daff.sesi2_hz.ir.daff.presentation.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daff.sesi2_hz.ir.daff.MainActivity
import com.daff.sesi2_hz.ir.daff.databinding.ActivitySplashBinding
import com.daff.sesi2_hz.ir.daff.di.DIModule
import com.daff.sesi2_hz.ir.daff.di.DaggerDIComponent
import com.daff.sesi2_hz.ir.daff.presentation.auth.view.LoginActivity
import com.daff.sesi2_hz.ir.daff.presentation.splash.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySplashBinding

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var splashViewModel: SplashViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashBinding.inflate(layoutInflater)
    setContentView(binding.root)

    DaggerDIComponent.builder()
      .dIModule(DIModule(this))
      .build().inject(this)

    splashViewModel = ViewModelProvider(
      this,
      viewModelFactory
    )[SplashViewModel::class.java]


    Handler(Looper.getMainLooper()).postDelayed({
      splashViewModel.getUserEmail()

    }, 2000)

    splashViewModel.userEmail.observe(this, Observer {
      if (it.isNotBlank()) {
        val intent = Intent(this, MainActivity::class.java).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
          addFlags(flags)
        }
        startActivity(intent)
        finish()
      } else {
        val intent = Intent(this, LoginActivity::class.java).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
          addFlags(flags)
        }
        startActivity(intent)
        finish()
      }
    })
  }
}