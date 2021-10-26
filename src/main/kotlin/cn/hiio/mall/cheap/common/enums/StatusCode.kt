package cn.hiio.mall.cheap.common.enums

enum class StatusCode :BaseEnum{
    success(Int.MAX_VALUE,"成功"),
    failed(Int.MIN_VALUE,"请求失败"),
    forbidden(-403,"非法"),
    timeout(-408,"请求超时");

    var code:Int
    var message:String
    constructor(code:Int,message:String){
        this.code = code
        this.message = message
    }

    override fun code(): Int {
        return this.code
    }

    override fun message(): String {
        return this.message
    }


}