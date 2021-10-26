package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.UserQrCodeVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserQrCodeRepository : MongoRepository<UserQrCodeVo, String> {
    fun queryByUserId(userId:String):List<UserQrCodeVo>
}