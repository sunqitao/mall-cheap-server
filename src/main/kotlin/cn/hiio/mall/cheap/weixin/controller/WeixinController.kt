package cn.hiio.mall.cheap.weixin.controller

import cn.hiio.mall.cheap.weixin.service.WeixinService
import cn.hiio.mall.cheap.weixin.vo.WXCodeVo
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.annotation.Resource

/**
 * 此文件由于涉及具体的小程序参数，故未经脱敏不得提交代码
 */
@Controller
@RequestMapping("/weixin")
class WeixinController  : Logging {

    @Resource
    lateinit var weixinService: WeixinService

    @Resource
    lateinit var redisTemplate : RedisTemplate<String, String>

//    @GetMapping("/initWeixinParam")
//    @ResponseBody
//    fun initWeixinParam(url:String):String{
//        var ononcestr = UUID.randomUUID().getUUID()
//        var jsapi_ticket = weixinService.ticket()
//        var timestamp = (System.currentTimeMillis()/1000) .toString()
//        var signMap = hashMapOf("noncestr" to ononcestr,"jsapi_ticket" to jsapi_ticket,"timestamp" to timestamp,"url" to url)
//        var signature = SHA1WeChatShareUtil.SHA1(signMap)
//        signMap.remove("jsapi_ticket")
//        signMap.put("signature",signature)
//        signMap.put("appId",appId)
//        return JSON.toJSONString(ResultVo<HashMap<String,String>>(data = signMap))
//    }
    @PostMapping("/codeToParam")
    @ResponseBody
    fun codeToParam(wxCodeVo: WXCodeVo):String{
        logger.info("cod is ${JSON.toJSONString(wxCodeVo)}")
        return JSON.toJSONString(weixinService.codeToParam(wxCodeVo.code))
    }

}