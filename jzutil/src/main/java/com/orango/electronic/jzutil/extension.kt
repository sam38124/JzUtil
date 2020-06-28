package com.orango.electronic.jzutil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.jzsql.lib.mmySql.Sql_Result
import com.orange.jzchi.jzframework.JzActivity
import com.orango.electronic.jzutil.util.getBytes
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//取得父類別中的所有子View
fun View.listView(): List<View> {
    val allchildren = ArrayList<View>()
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val viewchild = this.getChildAt(i)
            allchildren.add(viewchild)
            Log.d("ChildView", "$viewchild")
            allchildren.addAll(viewchild.listView())
        }
    }
    return allchildren
}

//將String轉換成HexString
fun String.toHex(): String {
    val sb = StringBuilder()
    for (b in this.toByteArray()) {
        sb.append(String.format("%02X", b))
    }
    return sb.toString()
}

//將HexString轉換成Byte
fun String.hexToByte(): ByteArray {
    val bytes = ByteArray(this.length / 2)
    for (i in 0 until this.length / 2)
        bytes[i] = Integer.parseInt(this.toString().substring(2 * i, 2 * i + 2), 16).toByte()
    return bytes
}

//將Byte轉換成HexString
fun ByteArray.toHex(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02X", b))
    }
    return sb.toString()
}

//取得網頁原始碼
fun String.getWebResource(timeout: Int, method: String = "GET", postData: String = ""): String? {
    return util.getText(this, timeout, method, postData)
}

//添加請求內容
fun String.addParameters(item: Array<String>, result: Array<String>): String {
    var re = this
    for (i in item.indices) {
        re += if (!re.contains("?")) {
            "?${item[i]}=${result[i]}"
        } else {
            "&${item[i]}=${result[i]}"
        }
    }
    Log.e("addParameters", re)
    return re
}

//下載檔案並儲存至Sqlite資料庫
fun String.storeFile(name: String, timeout: Int): Boolean {
    val data = util.getHex(this, timeout)
    if (data != null) {
        try {
            Log.e("dataHex", data)
            sqlClass.getControlInstance()
                .item_File.exsql("insert or replace into file (name,data) values ('$name','$data')")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    } else {
        return false
    }
}

//取得檔案
fun String.getFile(): ByteArray? {
    var data: String? = null
    sqlClass.getControlInstance().item_File.query(
        "select data from file where name='$this'",
        Sql_Result {
            data = it.getString(0)
        })
    if (data != null) {
        return data!!.hexToByte()
    } else {
        return null
    }
}

//將Byte帶入ImageView
fun ImageView.setImage(data: ByteArray) {
    val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
    this.setImageBitmap(
        Bitmap.createScaledBitmap(
            bmp, this.width,
            this.height, false
        )
    )
}

//將HexString檔案存入資料庫
fun String.storeFile(name: String): Boolean {
    try {
        Log.e("dataHex", this)
        sqlClass.getControlInstance()
            .item_File.exsql("insert or replace into file (name,data) values ('$name','$this')")
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

//儲存序列化物件
fun Any.storeObject(name: String): Boolean {
    try {
        val out = ByteArrayOutputStream()
        val oos = ObjectOutputStream(out)
        oos.writeObject(this);
        sqlClass.getControlInstance()
            .item_File.exsql("insert or replace into file (name,data) values ('$name','${out.toByteArray().toHex()}')")
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

//取得序列化物件
fun String.getObject(): Any? {
    try {
        val out = ByteArrayInputStream(this.getFile())
        val oos = ObjectInputStream(out)
        return oos.readObject()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

//将utf-8的汉字转换成unicode格式汉字码
fun String.stringToUnicode(): String? {
    var str = this
    str = str ?: ""
    var tmp: String
    val sb = StringBuffer(1000)
    var c: Char
    var i: Int
    var j: Int
    sb.setLength(0)
    i = 0
    while (i < str.length) {
        c = str[i]
        sb.append("\\\\u")
        j = c.toInt() ushr 8 //取出高8位
        tmp = Integer.toHexString(j)
        if (tmp.length == 1) sb.append("0")
        sb.append(tmp)
        j = c.toInt() and 0xFF //取出低8位
        tmp = Integer.toHexString(j)
        if (tmp.length == 1) sb.append("0")
        sb.append(tmp)
        i++
    }
    return String(sb)
}

//将unicode的汉字码转换成utf-8格式的汉字
fun String.unicodeToString(): String? {
    val string = StringBuffer()
    val hex = this.replace("\\\\u", "\\u").split("\\u").toTypedArray()
    for (i in 1 until hex.size) { //        System.out.println(hex[i].length());
        if (hex[i].length > 4) {
            string.append(hex[i].substring(4))
        }
        val data = hex[i].substring(0, 4).toInt(16)
        // 追加成string
        string.append(data.toChar())
    }
    Log.e("hex", "" + hex.size)
    return if (hex.size <= 1) this else string.toString()
}

//時間計算"yyyy-MM-dd HH:mm:ss"格式
fun String.CalculateTime(): String {
    if (JzActivity.getControlInstance().getLanguage() != null) {
        JzActivity.getControlInstance().setLanguage(JzActivity.getControlInstance().getLanguage()!!)
    }
    var resororce = JzActivity.getControlInstance().getRootActivity().resources
    val nowTime = System.currentTimeMillis() // 获取当前时间的毫秒数
    var msg: String = "剛剛"
    val sdf =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // 指定时间格式
    var setTime: Date? = null // 指定时间
    try {
        setTime = sdf.parse(this) // 将字符串转换为指定的时间格式
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val reset = setTime!!.time // 获取指定时间的毫秒数
    val dateDiff = nowTime - reset
    if (dateDiff < 0) {
        msg = "剛剛"
    } else {
        val dateTemp1 = dateDiff / 1000 // 秒
        val dateTemp2 = dateTemp1 / 60 // 分钟
        val dateTemp3 = dateTemp2 / 60 // 小时
        val dateTemp4 = dateTemp3 / 24 // 天数
        val dateTemp5 = dateTemp4 / 30 // 月数
        val dateTemp6 = dateTemp5 / 12 // 年数
        if (dateTemp6 > 0) {
            msg = "1年前".replace("1", dateTemp6.toString())
        } else if (dateTemp5 > 0) {
            msg = "1個月前".replace("1", dateTemp5.toString())
        } else if (dateTemp4 > 0) {
            msg = "1天前".replace("1", dateTemp4.toString())
        } else if (dateTemp3 > 0) {
            msg = "1小時前".replace("1", dateTemp3.toString())
        } else if (dateTemp2 > 0) {
            msg = "1分鐘前".replace("1", dateTemp2.toString())
        } else if (dateTemp1 > 0) {
            msg = "剛剛"
        }
    }
    return msg
}

//InputStream換array
fun InputStream.streamToArray(): ByteArray {
    return getBytes(this)
}
