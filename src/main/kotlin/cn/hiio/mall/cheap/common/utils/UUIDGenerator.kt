package cn.hiio.mall.cheap.common.utils

import java.util.*

fun UUID.getUUID():String{
    var uuid = this.toString()
    return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24)
}