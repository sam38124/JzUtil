package com.orango.electronic.jzutil

import android.content.Context
import android.util.Log
import com.jzsql.lib.mmySql.JzSqlHelper
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


object JzUtil {
    fun setUp(context: Context) {
        SqlClass.getControlInstance().item_File = JzSqlHelper(context, "JzutilFiles.db")
    }

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
            e.printStackTrace()
            return null
        }
    }

    fun postRequest(
        url: String,
        timeout: Int,
        dataArray: ByteArray,
        uploadProgress: (a: Int) -> Unit = {},
        downloadProgress: (a: Int) -> Unit = {}
    ): String? {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout
            conn.readTimeout = timeout
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.doInput = true;
            val inputStream = dataArray.inputStream()
            val wr = DataOutputStream(conn.outputStream)
            val buffer = ByteArray(1024)
            val length = inputStream.available()
            var uploaded = 0L
            inputStream.use {
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    uploaded += read
                    wr.write(buffer, 0, read)
                    uploadProgress((uploaded * 100 / length).toInt())
                    wr.flush()
                }
            }
            wr.close()
            val reader = DataInputStream(conn.inputStream)
            var strBuf = ""
            var downLoad = 0L
            reader.use {
                var read: Int
                while (reader.read(buffer).also { read = it } != -1) {
                    downLoad += read
                    strBuf += String(buffer.copyOfRange(0, read))
                    if (reader.available() > 0) {
                        downloadProgress((downLoad * 100 / reader.available()).toInt())
                    }

                }
            }
            reader.close()
            return strBuf.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getRequest(
        url: String, timeout: Int, downloadProgress: (a: Int) -> Unit = {}
    ): String? {
        try {
            val conn: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = timeout
            conn.readTimeout = timeout
            conn.requestMethod = "GET"
            conn.doInput = true;
            val buffer = ByteArray(1024)
            val reader = DataInputStream(conn.inputStream)
            var strBuf = ""
            var downLoad = 0L
            reader.use {
                var read: Int
                while (reader.read(buffer).also { read = it } != -1) {
                    downLoad += read
                    strBuf += String(buffer.copyOfRange(0, read))
                    if (reader.available() > 0) {
                        downloadProgress((downLoad * 100 / reader.available()).toInt())
                    }
                }
            }
            reader.close()
            return strBuf.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}
