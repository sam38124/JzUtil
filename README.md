[![](https://jitpack.io/v/sam38124/JzUtil.svg)](https://jitpack.io/#sam38124/JzUtil)
[![Platform](https://img.shields.io/badge/平台-%20Android%20-brightgreen.svg)](https://github.com/sam38124)
[![characteristic](https://img.shields.io/badge/特點-%20輕量級%20%7C%20簡單易用%20%20%7C%20穩定%20-brightgreen.svg)](https://github.com/sam38124)
# JzUtil
Android開發常用小工具
## 目錄
* [如何導入到專案](#Import)
* [快速使用](#Use)
* [關於我](#About)

<a name="Import"></a>
## 如何導入到項目
> 支持jcenter。 <br/>

### jcenter導入方式
在app專案包的build.gradle中添加
```kotlin
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

在需要用到這個庫的module中的build.gradle中的dependencies中加入
```kotlin
dependencies {
implementation 'com.github.sam38124:JzUtil:1.0'
}
```
<a name="Use"></a>
## 所有extension
```kotlin

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
fun String.getWebResource(timeout: Int): String? {
    return util.getText(this, timeout)
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
fun Any.storeObject(name:String):Boolean{
    try {
        val out = ByteArrayOutputStream()
        val oos = ObjectOutputStream(out)
        oos.writeObject(this);
        sqlClass.getControlInstance()
            .item_File.exsql("insert or replace into file (name,data) values ('$name','${out.toByteArray().toHex()}')")
        return true
    }catch (e:Exception){
        e.printStackTrace()
        return false
    }
}
//取得序列化物件
fun String.getObject():Any?{
    try {
        val out = ByteArrayInputStream(this.getFile())
        val oos = ObjectInputStream(out)
        return oos.readObject()
    }catch (e:Exception){
        e.printStackTrace()
        return null
    }
}

```

<a name="About"></a>
### 關於我
橙的電子android and ios developer

*line:sam38124

*gmail:sam38124@gmail.com

