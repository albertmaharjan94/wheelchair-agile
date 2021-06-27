package com.softwarica.wheelchairapp.Utils

import java.lang.Exception

class SerialStringMaker {
    fun makeString(str:String, size: Int = 6, lineSplit: String = "#", listSplit:String = ","): List<String>? {
        try{
            val split = str.split(lineSplit)
            return if (split.isNotEmpty()) {
                val commaSplit = split[split.size - 1].split(listSplit)
                return if (commaSplit.size >= size) {
                    commaSplit.slice(1 until size-1)
                }else{
                    null
                }
            } else{
                null
            }
        }catch(e: Exception){
            e.printStackTrace()
            return null
        }
    }
}
