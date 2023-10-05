package com.daff.sesi2_hz.ir.daff.utils.extension

fun <T> List<T>?.toArrayList(): ArrayList<T> {
  val list: ArrayList<T> = arrayListOf()
  this?.forEach { item ->
    list.add(item)
  }
  return list
}
