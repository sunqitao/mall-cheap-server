package cn.hiio.mall.cheap.services.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document("article_fan")
class ArticleFanVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799221592974070175
    }
    //
    var userId: String = ""
    var articleId:String = ""
    var toUserId:String = ""
}