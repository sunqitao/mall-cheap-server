package cn.hiio.mall.cheap.carouse.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "shop_carousel")
class CarouselVo :BaseModel(){
    companion object {
        private const val serialVersionUID = -799111592974071175
    }
    var title: String? = null
    var image: String? = null
    var backgroundColor: String? = null
    var url: String? = null
    var status:Int = 0 //0 禁用 1启用

    var top = 0
    var sort = 0
    var urlType: String = "H5"
    var topicId: String? = null
    var activityId: String? = null
}