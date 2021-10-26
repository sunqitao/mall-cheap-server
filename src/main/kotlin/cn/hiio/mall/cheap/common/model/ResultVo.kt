package cn.hiio.mall.cheap.common.model

data class ResultVo<T>(var code:Int = Int.MAX_VALUE, var message:String = "成功", var data: T?,var attribute:Map<String,Any>?=null ){

}