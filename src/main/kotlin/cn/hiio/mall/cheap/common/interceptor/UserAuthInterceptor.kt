package cn.hiio.mall.cheap.common.interceptor

import cn.hiio.mall.cheap.admin.service.AdminService
import cn.hiio.mall.cheap.admin.vo.AdminUserVo
import cn.hiio.mall.cheap.common.component.Security
import cn.hiio.mall.cheap.common.component.SecurityAdmin
import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.exception.RuntimeExceptionLifeGoods
import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.common.utils.*
import cn.hiio.mall.cheap.person.service.UserService
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class UserAuthInterceptor : HandlerInterceptor, Logging {
    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String,String>

    @Resource
    lateinit var userService: UserService
    @Resource
    lateinit var adminService: AdminService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //调用url
        var requestUrl = request.requestURI
        logger.debug("this request url is [$requestUrl]")
        //如果请求有跨域问题在此处解决
        setResponseHeaders(response)
        if(request.method.equals("OPTIONS")){
            logger.debug("options request")
            return true
        }
        try {
            var handlerMethod = handler as HandlerMethod
            var m = handlerMethod.method
            if (m?.isAnnotationPresent(Security::class.java)){
                var token = request.getHeader("ACCESSTOKEN")
                AssertUtil.isNotNull(token, StatusCode.forbidden)
                //在缓存中获取正确的用户信息
                var userVoId = stringRedisTemplate.opsForValue().get(token)
                AssertUtil.isNotNull(userVoId,StatusCode.forbidden)
                var userVo = userService.getUserFromCache(userVoId!!)
                AssertUtil.isNotNull(userVo,StatusCode.forbidden)
                setAuthUser(request,userVo!!)
                logger.debug("@@@### user is ${JSON.toJSONString(userVo)}")
            }else if (m?.isAnnotationPresent(SecurityAdmin::class.java)){
                var token = request.getHeader("admintoken")
                AssertUtil.isNotNull(token, StatusCode.forbidden)
                //在缓存中获取正确的用户信息
                var userVoId = stringRedisTemplate.opsForValue().get(token)
                AssertUtil.isNotNull(userVoId,StatusCode.forbidden)
                var userVo:AdminUserVo = adminService.getUserFromCache(userVoId!!)
                AssertUtil.isNotNull(userVo,StatusCode.forbidden)
                setAdminAuthUser(request,userVo!!)
                logger.debug("@@@### adminuser is ${JSON.toJSONString(userVo)}")
            }
        }catch (e: RuntimeExceptionLifeGoods){
            out(response, ResultVo(e.statusCode.code(),e.statusCode.message(),null))
            logger.info("interceptor is exception ")
            logger.info(e.localizedMessage)
            return false
        }catch (e:ClassCastException){
            out(response, ResultVo(StatusCode.failed.code, StatusCode.failed.message(),null))
            logger.info("this request url not HandlerMethod")
            logger.info(e.localizedMessage)
            return false
        }catch (e:Exception){
            out(response, ResultVo(StatusCode.failed.code, StatusCode.failed.message(),null))
            logger.error(e.localizedMessage)
            return false
        }
        return true
    }
}