package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.FansBuyVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FansBuyRepository : MongoRepository<FansBuyVo, String> {
    fun queryByUserIdAndFansId(userId:String,fansId:String):List<FansBuyVo>
    fun queryByUserId(userId:String):List<FansBuyVo>
}