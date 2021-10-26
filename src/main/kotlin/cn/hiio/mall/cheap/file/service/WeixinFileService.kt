package cn.hiio.mall.cheap.file.service

import cn.hiio.mall.cheap.weixin.service.WeixinService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource


@Service
class WeixinFileService {
    @Resource
    lateinit var weixinService: WeixinService

    fun putFile(file : MultipartFile):String{
        return weixinService.putFile(file)
    }
}