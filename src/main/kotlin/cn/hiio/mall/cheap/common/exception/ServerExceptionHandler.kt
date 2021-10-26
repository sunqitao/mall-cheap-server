package cn.hiio.mall.cheap.common.exception

import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.common.utils.out
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 统一处理所有的异常
 */
@ControllerAdvice
@CrossOrigin //允许跨域
class ServerExceptionHandler :Logging{

    @ExceptionHandler(RuntimeExceptionLifeGoods::class)
    fun defaultRuntimeErrorHandler(request: HttpServletRequest, response: HttpServletResponse?, e: RuntimeExceptionLifeGoods) {
        logger.error("request normal exception ", e)
        // 业务逻辑异常
        out(response!!, ResultVo(e.statusCode.code(), if(e.message!=null){e.message}else{e.statusCode.message()},null))
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @Throws(Exception::class)
    fun defaultErrorHandler(request: HttpServletRequest, response: HttpServletResponse?, e: Exception) {
        logger.error("request  error ", e)
        // 业务逻辑之外的异常，都需要将异常保存起来
        out(response!!,ResultVo(StatusCode.failed.code, e.message!!,null))

    }

    @ExceptionHandler(Throwable::class)
    @ResponseBody
    @Throws(Exception::class)
    fun defaultThrowableHandler(request: HttpServletRequest, response: HttpServletResponse?, e: Exception) {
        logger.error("request  error ", e)
        out(response!!,ResultVo(StatusCode.failed.code, e.message!!,""))
    }
}