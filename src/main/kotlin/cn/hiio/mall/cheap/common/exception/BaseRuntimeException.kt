package cn.hiio.mall.cheap.common.exception

import java.lang.RuntimeException

abstract class BaseRuntimeException(message: String?) : RuntimeException(message) {
    companion object{
        fun buildMessage(message:String,cause:Throwable):String{
            if(message ==null ){
                return "$message;base exception is $cause"
            }else{
                return message
            }
        }
    }
}