package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "person_user_vip_detail")
class UserVipDetailVo() : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211592974071175
    }
    var userId: String = ""
    //0:支付中，1支付失败，2支付成功
    var payStatus:Int = 0

    var payUserId:String = ""

}