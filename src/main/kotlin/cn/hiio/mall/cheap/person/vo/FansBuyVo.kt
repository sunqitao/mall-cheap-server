package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "person_user_fans_buy")
class FansBuyVo : BaseModel() {
    companion object {
        private const val serialVersionUID = -799211592974071175
    }
    var userId: String = ""
    var fansId:String = ""
    var bCount:Int = 0
    var remark:String = ""
    var bDate:String? = null
}