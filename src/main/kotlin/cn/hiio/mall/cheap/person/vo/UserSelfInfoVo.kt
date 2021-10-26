package cn.hiio.mall.cheap.person.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "person_user_self_info")
class UserSelfInfoVo() : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211592974070075
    }
    //
    var title:String? = null
    var slogan:String? = null
    //
    var detail: String = ""
    //
    var userId: String = ""

    var imgList:String = ""

    var tag1:String = ""
    var tag2:String = ""
    var tag3:String = ""
    var serviceType:Int = 0
}