package cn.hiio.mall.cheap.article.dao

import cn.hiio.mall.cheap.services.vo.ArticleFanVo
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleFanRepository : MongoRepository<ArticleFanVo, String> {

    fun queryByUserId(userId:String):List<ArticleFanVo>

    fun queryByUserIdAndArticleId(userId: String,articleId:String):List<ArticleFanVo>
}