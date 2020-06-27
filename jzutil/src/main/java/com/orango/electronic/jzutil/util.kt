package com.orango.electronic.jzutil

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object util {
    fun getHex(url: String, timeout: Int): String? {
        try {
            var data = ""
            val conn = URL(url).openConnection()
            conn.connectTimeout = timeout * 1000
            val inp = conn.inputStream
            val bufferSize = 8192
            val buf = ByteArray(bufferSize)
            while (true) {
                val read = inp.read(buf)
                if (read == -1) {
                    break
                }
                val d = buf.copyOfRange(0, read).toHex()
                data += d
            }
            return data
        } catch (e: Exception) {
            Log.e("getHex", e.message)
            e.printStackTrace()
            return null
        }
    }

    fun getText(tempurl: String, timeout: Int, method: String,data:String=""): String? {
        try {
            val url =
                if (method.toUpperCase() == "POST" && tempurl.contains("?") && data.isNotEmpty()) tempurl.substring(0,
                    tempurl.indexOf("?")) else tempurl
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout
            conn.requestMethod = method
            if (method.toUpperCase() == "POST" && (tempurl.contains("?") || data.isNotEmpty())) {
                conn.doOutput = true;
            }
            conn.doInput = true;
            if (method.toUpperCase() == "POST" && (tempurl.contains("?") || data.isNotEmpty())) {
                val wr = DataOutputStream(conn.outputStream)
                wr.writeBytes(if(data.isNotEmpty()) data else tempurl.substring(tempurl.indexOf("?")+1))
                wr.flush()
                wr.close()
            }
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            var line: String? = null
            val strBuf = StringBuffer()
            line = reader.readLine()
            while (line != null) {
                strBuf.append(line)
                line = reader.readLine()
            }
            return strBuf.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
