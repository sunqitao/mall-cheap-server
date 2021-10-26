package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.UserSelfInfoVo
import org.springframework.data.mongodb.repository.MongoRepository

interface UserSelfInfoRepository  : MongoRepository<UserSelfInfoVo, String> {
    fun queryByUserId(userId:String):List<UserSelfInfoVo>
}