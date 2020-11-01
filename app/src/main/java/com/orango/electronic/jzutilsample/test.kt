package com.orango.electronic.jzutilsample

import com.google.gson.Gson
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.jzutil.postRequest

fun main(){
    val map = mutableMapOf<String, Any>()
    map["request"] = "login"
    map["account"] = "sam38124"
    map["password"] = "sam28520"
    println( "http://192.168.0.13:80/Api".postRequest(1000*10, Gson().toJson(map),{},{}))


}