package cn.hiio.mall.cheap.admin.controller

import cn.hiio.mall.cheap.admin.service.AdminService
import cn.hiio.mall.cheap.admin.vo.AdminUserVo
import cn.hiio.mall.cheap.common.component.SecurityAdmin
import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.utils.*
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@Controller
@RequestMapping("/admin/user")
@CrossOrigin(origins = ["*"])
class AdminUserController: Logging {
    @Resource
    lateinit var adminService: AdminService
    @Resource
    lateinit var passwordEncoder: PasswordEncoder
    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

// 用户登录
    @PostMapping("/login")
    @ResponseBody
    private fun login(request: HttpServletRequest, response: HttpServletResponse, @RequestBody adminUserCondition: AdminUserVo):String {
        logger.debug("adminUserCondition login "+JSON.toJSONString(adminUserCondition))
        AssertUtil.isNotNull(adminUserCondition.name,StatusCode.failed,"账号不能为空")
        AssertUtil.isNotNull(adminUserCondition.password,StatusCode.failed,"密码不能为空")
        var adminUserVo:AdminUserVo? = null
        if(adminUserCondition.name !=null){
            adminUserVo = adminService.findAdminUserByName(adminUserCondition.name!!)
            if(adminUserVo == null){
                adminUserVo = adminService.findAdminUserByPhone(adminUserCondition.name!!)
            }
        }
        var pa = passwordEncoder.encode(adminUserCondition.password)
        logger.debug(pa)
        AssertUtil.isNotNull(adminUserVo,StatusCode.failed,"账号不存在，请核实")
        AssertUtil.isTrue(passwordEncoder.matches(adminUserCondition.password,adminUserVo!!.password),StatusCode.failed,"密码不对，请重新登录")

        val token: String = MD5Util.MD5Encoding(UUID.randomUUID().getUUID())
        stringRedisTemplate.opsForValue().set(token,adminUserVo.id!! , 30L, TimeUnit.DAYS)
        var data = mapOf<String,String>("adminToken" to token)
        return out(data)
    }

    // 用户登出
    @SecurityAdmin
    @PostMapping("/logout")
    @ResponseBody
    private fun logout(request: HttpServletRequest, response: HttpServletResponse):String {
        var token = request.getHeader("adminToken")
        stringRedisTemplate.delete(token)
        return out(HashMap<String,String>())
    }
    @SecurityAdmin
    @GetMapping("/info")
    @ResponseBody
    private fun info(request: HttpServletRequest, response: HttpServletResponse):String {
        val adminUser = getAdminAuthUser(request)
        val roles = ArrayList<String>()
        roles.add("TEST")
        var reMap = mapOf<String,Any>("icon" to adminUser.avatar!!,"userName" to adminUser.name!!,"roles" to roles)
        return out(reMap)
    }
}