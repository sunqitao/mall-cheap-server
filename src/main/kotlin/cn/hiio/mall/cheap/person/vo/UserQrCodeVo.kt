package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "person_user_qr")
class UserQrCodeVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211292974070175
    }
    var userId: String = ""
    var wechatBar: String? = null
    var wechatPay: String? = null
    var aliPay: String? = null

}