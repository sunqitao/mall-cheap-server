package cn.hiio.mall.cheap.banner.controller

import cn.hiio.mall.cheap.banner.service.BannerService
import cn.hiio.mall.cheap.common.model.ResultVo
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

@Controller
@RequestMapping("/banner")
@CrossOrigin(origins = ["*"])
class BannerController: Logging {

    @Resource
    lateinit var bannerService: BannerService

    @GetMapping("/list" )
    @ResponseBody
    fun list(request: HttpServletRequest, response: HttpServletResponse):String{
        var returnBannerList = ArrayList<String>()
        bannerService.list().forEach{
            returnBannerList.add(it.picUrl!!)
        }
        var resultVo = ResultVo<List<String>>(data = returnBannerList)
        return JSON.toJSONString(resultVo)
    }
}