package cn.hiio.mall.cheap.common.utils

import cn.hiio.mall.cheap.common.enums.BaseEnum
import cn.hiio.mall.cheap.common.exception.RuntimeExceptionLifeGoods


class AssertUtil {
    companion object{
        fun fail(statusCode: BaseEnum, defaultMessage:String?){
            throw RuntimeExceptionLifeGoods(statusCode,defaultMessage)
        }
        fun hasLengthByTrim(text:String,statusCode: BaseEnum){
            if(text.trim().length == 0){
                fail(statusCode,null)
            }
        }
        fun hasLengthByTrim(text:String,statusCode: BaseEnum,defaultMessage: String){
            if(text.trim().length == 0){
                fail(statusCode,defaultMessage)
            }
        }
        fun isNotNull(any: Any?,statusCode: BaseEnum){
            if(any == null){
                fail(statusCode,null)
            }
        }
        fun isNotNull(any: Any?,statusCode: BaseEnum,defaultMessage: String){
            if(any == null){
                fail(statusCode,defaultMessage)
            }
        }
        fun hasLength(text:String?,statusCode: BaseEnum){
            if(text==null || text.length < 1){
                fail(statusCode,null)
            }
        }
        fun hasLength(text:String?,statusCode: BaseEnum,defaultMessage: String){
            if(text==null || text.length < 1){
                fail(statusCode,defaultMessage)
            }
        }


        fun isTrue(expression:Boolean,statusCode: BaseEnum){
            if(!expression){
                fail(statusCode,null)
            }
        }
        fun isTrue(expression:Boolean,statusCode: BaseEnum,defaultMessage: String){
            if(!expression){
                fail(statusCode,defaultMessage)
            }
        }

        fun isFalse(expression:Boolean,statusCode: BaseEnum){
            Companion.isTrue(!expression,statusCode)
        }
        fun isFalse(expression:Boolean,statusCode: BaseEnum,defaultMessage: String){
            Companion.isTrue(!expression,statusCode,defaultMessage)
        }

        fun isNull(any: Any,statusCode: BaseEnum){
            if(any != null){
                fail(statusCode,null)
            }
        }
        fun isNull(any: Any,statusCode: BaseEnum,defaultMessage: String){
            if(any != null){
                fail(statusCode,defaultMessage)
            }
        }
    }
}