package cn.hiio.mall.cheap.common.exception

import cn.hiio.mall.cheap.common.enums.BaseEnum


class RuntimeExceptionLifeGoods: BaseRuntimeException {
    lateinit var statusCode: BaseEnum

    private constructor(message: String?) : super(message)

    constructor(statusCode:BaseEnum,defaultMessage:String?):this(if (defaultMessage!=null&&defaultMessage.length>0) defaultMessage else statusCode.message()){
        this.statusCode = statusCode
    }
}