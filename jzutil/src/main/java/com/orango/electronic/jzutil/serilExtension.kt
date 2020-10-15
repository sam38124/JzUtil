package com.orango.electronic.jzutil

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jzsql.lib.mmySql.Sql_Result


//儲存序列化物件
fun <T> T.storeObject(name: String, rout: String = "file"): Boolean {
    try {
        sqlClass.getControlInstance().createRout(rout)
        sqlClass.getControlInstance()
            .item_File.exsql(
                "insert or replace into $rout (name,data) values ('$name','${sqliteEscape(
                    Gson().toJson(
                        this,
                        object : TypeToken<T>() {}.type
                    )
                )}')"
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
                data = it.getString(0)
            })
        return Gson().fromJson(data, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

//清空序列化物件
fun String.clearObject(rout: String = "file"): Boolean {
    try {
        sqlClass.getControlInstance().item_File.exsql("DROP TABLE $this;\n")
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

//過濾特殊字元
fun sqliteEscape(keyWord: String): String? {
    var keyWord = keyWord
    keyWord = keyWord.replace("/", "//")
    keyWord = keyWord.replace("'", "''")
    keyWord = keyWord.replace("[", "/[")
    keyWord = keyWord.replace("]", "/]")
    keyWord = keyWord.replace("%", "/%")
    keyWord = keyWord.replace("&", "/&")
    keyWord = keyWord.replace("_", "/_")
    keyWord = keyWord.replace("(", "/(")
    keyWord = keyWord.replace(")", "/)")
    return keyWord
}