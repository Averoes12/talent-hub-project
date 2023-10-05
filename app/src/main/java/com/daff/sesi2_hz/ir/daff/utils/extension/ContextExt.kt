package com.daff.sesi2_hz.ir.daff.utils.extension

import android.content.Context
import android.widget.Toast


fun Context.showToast(
  message: String
) {
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
