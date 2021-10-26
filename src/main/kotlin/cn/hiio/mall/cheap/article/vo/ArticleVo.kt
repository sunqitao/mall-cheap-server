package cn.hiio.mall.cheap.services.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document("article_info")
class ArticleVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799221592974070075
    }
    var title:String? = null

    var subtitle:String? = null
    //
    var detail: String = ""
    //
    var userId: String = ""

    var imgList:String = ""
    //0 健康，1居家，2出行
    var tagIndex:Int = 0
    //浏览数量
    var viewCount:Int = 0
    //点赞
    var likeCount:Int = 0
    //留言
    var noteCount:Int = 0
    //view param
    var createdStr:String? = null
    var hasLike:Boolean = false
}