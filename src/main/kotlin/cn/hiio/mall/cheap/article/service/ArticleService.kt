package cn.hiio.mall.cheap.services.service

import cn.hiio.mall.cheap.article.dao.ArticleFanRepository
import cn.hiio.mall.cheap.common.utils.DateFormatType
import cn.hiio.mall.cheap.common.utils.stringFormat
import cn.hiio.mall.cheap.services.dao.ArticleRepository
import cn.hiio.mall.cheap.services.vo.ArticleFanVo
import cn.hiio.mall.cheap.services.vo.ArticleVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService : Logging {
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var articleFanRepository: ArticleFanRepository

    fun save(articleVo: ArticleVo){
        articleVo.createdStr = articleVo.created.stringFormat(DateFormatType.PATTERN_UNIT_YYYY_MM_DD)
        articleRepository.save(articleVo)
    }
    fun deleteIt(id: String){
        articleRepository.deleteById(id)
    }

    fun queryByUserid(userId:String):List<ArticleVo>{
        return articleRepository.queryByUserId(userId)
    }
    fun queryById(id:String):ArticleVo{
        return articleRepository.findById(id).get()
    }

    fun queryByUserIdAndArticleId(userId: String,articleId:String):List<ArticleFanVo>{
        return articleFanRepository.queryByUserIdAndArticleId(userId,articleId)
    }
    fun saveArticleFanVo(articleFanVo: ArticleFanVo){
        articleFanRepository.save(articleFanVo)
    }
    fun deleteArticleFanVo(id:String){
        articleFanRepository.deleteById(id)
    }
}