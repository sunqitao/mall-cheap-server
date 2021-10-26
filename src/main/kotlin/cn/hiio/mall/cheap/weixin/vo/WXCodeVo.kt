package cn.hiio.mall.cheap.weixin.vo

import cn.hiio.mall.cheap.common.model.BaseModel

class WXCodeVo(val code:String,val avatarUrl:String,val city:String,val country:String,val gender:Int,val nickName:String,val province:String) : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211259294070075
    }
}