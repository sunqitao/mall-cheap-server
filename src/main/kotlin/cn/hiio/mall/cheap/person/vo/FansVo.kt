package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "person_user_fans")
class FansVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211592974070175
    }
    var userId: String = ""
    //
    var fansId:String = ""
    //身份0：无 1：游客，2顾客，3成员
    var typeIndex:Int = 0
    //状态 0未够，1已购，2复购，3持续
    var statusIndex:Int = 0
    //本次购买数量
    var bCount:Int = 0
    //累计购买量
    var totalCount:Int = 0
    //下次购买时间
    var nexDate:String = ""
    //0初次关注，1取消关注，2再次关注
    var fansStatus:Int = 0
    //备注
    var remark:String = ""
    //    view param
    //昵称
    var nickName: String = ""
    //头像
    var avatarUrl: String = ""
}