package com.daff.sesi2_hz.ir.daff.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferencesFactory {

  fun SharedPreferences.clearPref() {
    edit().clear().apply()
  }

  fun initPreferences(context: Context, name: String = "PREF_GENERAL"): SharedPreferences =
    with(context) {
      getSharedPreferences(name, MODE_PRIVATE)
    }

}
