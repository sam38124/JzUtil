package com.orango.electronic.jzutil

import com.jzsql.lib.mmySql.JzSqlHelper

class SqlClass {

    lateinit var item_File: JzSqlHelper

    fun createRout(rout:String){
        item_File.exsql("CREATE TABLE IF NOT EXISTS `$rout` (\n" +
                "    name VARCHAR PRIMARY KEY,\n" +
                "    data      VARCHAR\n" +
                ");\n")
    }

    companion object {
        var instanc: SqlClass? = null
        fun getControlInstance(): SqlClass {
            if (instanc == null) {
                instanc = SqlClass()
            }
            return instanc!!
        }
    }
}