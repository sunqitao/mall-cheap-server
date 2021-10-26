package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "person_user")
class UserVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -7992112592974070075
    }



    //手机号
    var mobile:String? = null
    //微信id
    var openId: String = ""
    //昵称
    var nickName: String = ""
    //性别
    var gender: Int = 1
    //头像
    var avatarUrl: String = ""
    //登录名
    var userName: String?=null
    //预留
    var password: String?=null
    //姓名
    var name:String? = null
    var status: Int = 0
    //地址
    var addressName :String? = null
    //身份证号
    var idCard :String? = null
    //车牌号
    var cardNo :String? = null
    //工作地点/公司名称
    var workName :String? = null
    //    户籍地
    var userFrom :String? = null
    //备注
    var remark :String? = null
    //来源
    var source :Int = 0

    //邀请码
    var invitationCode:String? = "0"

    var city:String? = null
    var country:String? = null
    var province:String? = null
    var fansCount :Int = 0

    //view
    var hasFans:Boolean = false
    var title: String? = ""
    var detail: String? = ""
    var tag1:String = ""
    var tag2:String = ""
    var tag3:String = ""

}
