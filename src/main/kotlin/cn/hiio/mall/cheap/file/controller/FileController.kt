package cn.hiio.mall.cheap.file.controller

import cn.hiio.mall.cheap.common.component.Security
import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.utils.*
import cn.hiio.mall.cheap.file.service.WeixinFileService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/file")
@CrossOrigin(origins = ["*"])
class FileController: Logging {
    @Resource
    lateinit var weixinFileService:WeixinFileService

    @PostMapping("/upload")
    @ResponseBody
    @Security
    fun upload(request: HttpServletRequest, response: HttpServletResponse,file: MultipartFile):String{
        var user = getAuthUser(request)
        AssertUtil.isNotNull(file, StatusCode.failed,"文件为空")
        logger.info("upload  file from ${user.nickName} id is ${user.id}")
        return weixinFileService.putFile(file)
    }
}