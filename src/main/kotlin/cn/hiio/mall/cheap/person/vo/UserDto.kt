package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel

class UserDto : BaseModel(){
    companion object{
        private const val serialVersionUID = -7992112592974070075
    }

    //昵称
    var nickName: String = ""
    var slogan: String? = ""
    //头像
    var avatarUrl: String = ""
    var title: String? = ""
    var detail: String? = ""
    var tag1:String = ""
    var tag2:String = ""
    var tag3:String = ""
    var serviceTypeStr: String=""
}