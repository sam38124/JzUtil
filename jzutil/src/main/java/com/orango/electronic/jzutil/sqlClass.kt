package com.orango.electronic.jzutil

import com.jzsql.lib.mmySql.JzSqlHelper
import com.orange.jzchi.jzframework.JzActivity

class sqlClass {
    var item_File: JzSqlHelper = JzSqlHelper(JzActivity.getControlInstance().getRootActivity(), "JzutilFiles.db")

    init {
        item_File.exsql(
            "CREATE TABLE IF NOT EXISTS `file` (\n" +
                    "    name VARCHAR PRIMARY KEY,\n" +
                    "    data      VARCHAR\n" +
                    ");\n"
        )
    }

    fun createRout(rout:String){
        item_File.exsql("CREATE TABLE IF NOT EXISTS `$rout` (\n" +
                "    name VARCHAR PRIMARY KEY,\n" +
                "    data      VARCHAR\n" +
                ");\n")
    }

    companion object {
        var instanc: sqlClass? = null
        fun getControlInstance(): sqlClass {
            if (instanc == null) {
                instanc = sqlClass()
            }
            return instanc!!
        }
    }
}