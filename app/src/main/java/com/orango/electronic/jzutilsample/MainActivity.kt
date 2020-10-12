package com.orango.electronic.jzutilsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.orango.electronic.jzutil.getObject
import com.orango.electronic.jzutil.getWebResource

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread{
            Log.e("timeout","data->"+"https://www.gotit.tw/WS/Mobile/UserValidate".getWebResource(5000))
        }.start()
    }
}
