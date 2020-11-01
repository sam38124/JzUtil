package com.orango.electronic.jzutilsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.jzsql.lib.mmySql.Sql_Result
import com.orango.electronic.jzutil.*
import java.text.DateFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SerialExtension.initial(applicationContext)
        Thread {
            for (i in 0 until 1000) {
                sqlClass.getControlInstance().item_File.exsql("insert or replace into file (name,data) values ('$i','dskdsods')")
            }
            sqlClass.getControlInstance().item_File.exsql("delete from file where rowid <= (select MAX(rowid-50) from `file`)")
            sqlClass.getControlInstance().item_File.query("select * from file order by rowid desc",
                Sql_Result {
                    Log.e("data", "name->${it.getString(0)}")
                })
        }.start()

    }
}
