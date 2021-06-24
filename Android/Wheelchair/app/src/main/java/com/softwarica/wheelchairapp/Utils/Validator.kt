package com.softwarica.wheelchairapp.Utils

class Validator {

    companion object{
        fun validateFields(query : String) : Boolean{
            if(query.isEmpty()){
                return false
            }
            return true
        }

        fun validatePassord(query : String) : Int{
            if(query.length < 6 ){
                return 1
            }else if(query.isEmpty()){
                return 2
            }

            return 0
        }
    }
}