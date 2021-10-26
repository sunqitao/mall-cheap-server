package cn.hiio.mall.cheap.services.dao

import cn.hiio.mall.cheap.services.vo.ArticleVo
import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository : MongoRepository<ArticleVo, String> {

    fun queryByUserId(userId:String):List<ArticleVo>
}