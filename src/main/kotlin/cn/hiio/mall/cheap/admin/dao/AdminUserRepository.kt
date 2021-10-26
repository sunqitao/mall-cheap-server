package cn.hiio.mall.cheap.admin.dao

import cn.hiio.mall.cheap.admin.vo.AdminUserVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminUserRepository :MongoRepository<AdminUserVo,String>{
    fun findAdminUserVoByName(name:String):AdminUserVo
    fun findAdminUserVoByPhone(phone:String):AdminUserVo
    fun findAdminUserVoByGroupId(groupId:String):AdminUserVo
}