package cn.hiio.mall.cheap.services.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document("services_info")
class ServicesVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799221592974070075
    }
    //
    var title:String? = null

    var subtitle:String? = null
    //
    var detail: String = ""
    //
    var userId: String = ""

    var imgList:String = ""

}