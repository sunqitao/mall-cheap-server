package cn.hiio.mall.cheap.services.controller

import cn.hiio.mall.cheap.common.component.Security
import cn.hiio.mall.cheap.common.utils.getAuthUser
import cn.hiio.mall.cheap.common.utils.toResJsonString
import cn.hiio.mall.cheap.common.utils.toSuccessJsonString
import cn.hiio.mall.cheap.services.service.ArticleService
import cn.hiio.mall.cheap.services.vo.ArticleVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/article")
class ArticleController:Logging{
    @Autowired
    lateinit var articleService: ArticleService

    @PostMapping("/save" )
    @ResponseBody
    @Security
    fun save(request: HttpServletRequest, response: HttpServletResponse, articleVo: ArticleVo):String{
        var dataUser = getAuthUser(request)
        articleVo.userId = dataUser.id!!
        if(articleVo.id!=null && articleVo.id!!.isNotEmpty()){
            var dbData = articleService.queryById(articleVo.id!!)
            articleVo.viewCount = dbData.viewCount
            articleVo.likeCount = dbData.likeCount
            articleVo.noteCount = dbData.noteCount
        }
        articleService.save(articleVo)
        return toSuccessJsonString()
    }
    @PostMapping("/deleteIt" )
    @ResponseBody
    @Security
    fun deleteIt(request: HttpServletRequest, response: HttpServletResponse, articleVo: ArticleVo):String{
        var dataUser = getAuthUser(request)
        if(articleVo.id!=null){
            articleService.deleteIt(articleVo.id!!)
        }
        return toSuccessJsonString()
    }
    @PostMapping("/list" )
    @ResponseBody
    @Security
    fun list(request: HttpServletRequest, response: HttpServletResponse, articleVo: ArticleVo):String{
        var dataUser = getAuthUser(request)
        var selefVoList = articleService.queryByUserid(dataUser.id!!);
        return selefVoList.toResJsonString()
    }
    @PostMapping("/info" )
    @ResponseBody
    @Security
    fun info(request: HttpServletRequest, response: HttpServletResponse, articleVo: ArticleVo):String{
        var dataUser = getAuthUser(request)
        var selefVo = articleService.queryById(articleVo.id!!)
        return selefVo.toResJsonString()
    }
}