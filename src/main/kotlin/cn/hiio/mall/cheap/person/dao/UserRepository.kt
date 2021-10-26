package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.UserVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserVo, String> {
    fun queryByUserName(name:String):List<UserVo>
    fun queryByOpenId(openId:String):List<UserVo>
    fun queryByIdCardAndName(idcard:String,name:String):List<UserVo>
}