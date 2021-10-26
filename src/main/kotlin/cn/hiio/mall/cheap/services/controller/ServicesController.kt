package cn.hiio.mall.cheap.services.controller

import cn.hiio.mall.cheap.common.component.Security
import cn.hiio.mall.cheap.common.utils.getAuthUser
import cn.hiio.mall.cheap.common.utils.toResJsonString
import cn.hiio.mall.cheap.common.utils.toSuccessJsonString
import cn.hiio.mall.cheap.services.service.ServicesService
import cn.hiio.mall.cheap.services.vo.ServicesVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/services")
class ServicesController:Logging{
    @Autowired
    lateinit var servicesService: ServicesService

    @PostMapping("/save" )
    @ResponseBody
    @Security
    fun save(request: HttpServletRequest, response: HttpServletResponse, servicesVo: ServicesVo):String{
        var dataUser = getAuthUser(request)
        servicesVo.userId = dataUser.id!!

        servicesService.save(servicesVo)
        return toSuccessJsonString()
    }
    @PostMapping("/deleteIt" )
    @ResponseBody
    @Security
    fun deleteIt(request: HttpServletRequest, response: HttpServletResponse, servicesVo: ServicesVo):String{
        var dataUser = getAuthUser(request)
        if(servicesVo.id!=null){
            servicesService.deleteIt(servicesVo.id!!)
        }
        return toSuccessJsonString()
    }
    @PostMapping("/list" )
    @ResponseBody
    @Security
    fun list(request: HttpServletRequest, response: HttpServletResponse, servicesVo: ServicesVo):String{
        var dataUser = getAuthUser(request)
        var selefVoList = servicesService.queryByUserid(dataUser.id!!);
        return selefVoList.toResJsonString()
    }
    @PostMapping("/info" )
    @ResponseBody
    @Security
    fun info(request: HttpServletRequest, response: HttpServletResponse, servicesVo: ServicesVo):String{
        var dataUser = getAuthUser(request)
        var selefVo = servicesService.queryById(servicesVo.id!!)
        return selefVo.toResJsonString()
    }
}