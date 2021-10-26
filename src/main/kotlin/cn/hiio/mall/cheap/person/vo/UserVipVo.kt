package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "person_user_vip")
class UserVipVo() : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211592974071075
    }
    var userId: String = ""
    //0:试用状态，1：试用到期，2：付费到期状态 6：付费状态，7：好友帮付费
    var userStatus:Int = 0

    //截止日期
    var endDate:Long = 0


}