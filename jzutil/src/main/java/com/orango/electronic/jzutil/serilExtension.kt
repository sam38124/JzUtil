package com.orango.electronic.jzutil

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jzsql.lib.mmySql.Sql_Result
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


//儲存序列化物件
fun <T> T.storeObject(name: String, rout: String = "file"): Boolean {
    try {
        sqlClass.getControlInstance().createRout(rout)

        sqlClass.getControlInstance()
            .item_File.exsql(
                "insert or replace into $rout (name,data) values ('$name','${  Base64.encodeToString(  Gson().toJson(this,
                    object : TypeToken<T>() {}.type
                ).toByteArray(), DateFormat.DEFAULT)}')"
            )
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}
//取得序列化物件
inline fun <reified T> String.getObject(rout: String = "file"): T? {
    try {
        var data = ""
        sqlClass.getControlInstance().item_File.query(
            "select data from $rout where name='$this'",
            Sql_Result {
                data = String(Base64.decode(it.getString(0),0))
            })
        return Gson().fromJson(data, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
//刪除序列化物件
inline fun <reified T> String.delete(rout: String = "file"): Boolean? {
    try {
        sqlClass.getControlInstance().item_File.exsql("delete from $rout where name='$this'")
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

//列出此路徑序列化物件
 fun String.listObject(): ArrayList<JsonObject> {
    try {
        val data :ArrayList<JsonObject> = ArrayList<JsonObject>()
        sqlClass.getControlInstance().item_File.query(
            "select * from $this ",
            Sql_Result {
                data.add(JsonObject(it.getString(0),String(Base64.decode(it.getString(1),0))))
            })
        return data
    } catch (e: Exception) {
        e.printStackTrace()
        return ArrayList<JsonObject>()
    }

}

//清空此路徑序列化物件
fun String.clearObject(rout: String = "file"): Boolean {
    try {
        sqlClass.getControlInstance().item_File.exsql("DROP TABLE $this;\n")
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

class JsonObject(var name:String,var json:String){
    inline fun <reified T> getObject(): T? {
        try {
            return Gson().fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}