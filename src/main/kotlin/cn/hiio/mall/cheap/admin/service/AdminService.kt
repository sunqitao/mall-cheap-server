package cn.hiio.mall.cheap.admin.service

import cn.hiio.mall.cheap.admin.dao.AdminUserRepository
import cn.hiio.mall.cheap.admin.vo.AdminUserVo
import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.utils.AssertUtil
import cn.hiio.mall.cheap.common.utils.MD5Util
import cn.hiio.mall.cheap.group.vo.GroupVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Service
class AdminService : Logging {
    @Autowired
    lateinit var adminUserRepository: AdminUserRepository
    @Resource
    lateinit var redisTemplate : RedisTemplate<String, AdminUserVo>
    @Resource
    lateinit var passwordEncoder: PasswordEncoder

    fun findAdminUserByName(name: String): AdminUserVo {

        return adminUserRepository.findAdminUserVoByName(name)
    }

    fun findAdminUserByPhone(phone: String): AdminUserVo? {
        return findAdminUserByPhone(phone)
    }
    fun getUserFromCache(adminUserId: String): AdminUserVo {
        var userVo = redisTemplate.opsForValue().get(adminUserId)
        if(userVo==null){
            userVo = adminUserRepository.findById(adminUserId).get()
            redisTemplate.opsForValue().set(adminUserId,userVo,30L, TimeUnit.DAYS)
        }
        return userVo
    }
    fun updateAdminUserPassword(adminUserVo: AdminUserVo,password:String){
        adminUserVo.password = passwordEncoder.encode(password)
        adminUserRepository.save(adminUserVo)
    }
    fun updateAdminUserPassword(adminUserVo: AdminUserVo, groupVo: GroupVo) {
        AssertUtil.isNotNull(groupVo.ids, StatusCode.failed,"数据为空，无法初始化")
        groupVo.ids!!.split(",").forEach {
           var adminUserVo = adminUserRepository.findAdminUserVoByGroupId(it)
            adminUserVo.password = passwordEncoder.encode(MD5Util.MD5Encoding("000000"))
            adminUserRepository.save(adminUserVo)
        }
    }

}