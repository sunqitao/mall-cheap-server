package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel


class UserWxVo(val code:String,val avatarUrl:String,val city:String,val country:String,val gender:Int,val nickName:String,val province:String) : BaseModel(){
    companion object{
        private const val serialVersionUID = -7992112592974070075
    }
    var mobile:String?=null
}