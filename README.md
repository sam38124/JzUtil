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
fun View.listView():List<View>
//將String轉換成HexString
fun String.toHex():String
//將HexString轉換成Byte
fun String.hexToByte():ByteArray
//將Byte轉換成HexString
fun ByteArray.toHex():String
//取得網頁原始碼
fun String.getWebResource(timeout:Int):String?
//下載檔案並儲存至Sqlite資料庫
fun String.storeFile(name:String,timeout:Int):Boolean
//取得檔案
fun String.getFile():ByteArray?
//將Byte帶入ImageView
fun ImageView.setImage(data:ByteArray)
```

<a name="About"></a>
### 關於我
橙的電子android and ios developer

*line:sam38124

*gmail:sam38124@gmail.com

