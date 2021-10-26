package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.UserVipVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserVipRepository : MongoRepository<UserVipVo, String> {

}