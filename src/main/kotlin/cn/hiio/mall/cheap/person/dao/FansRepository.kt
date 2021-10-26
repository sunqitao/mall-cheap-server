package cn.hiio.mall.cheap.person.dao

import cn.hiio.mall.cheap.person.vo.FansVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FansRepository : MongoRepository<FansVo, String> {
    fun queryByUserIdAndFansId(userId:String,fansId:String):List<FansVo>
    fun queryByUserId(userId:String):List<FansVo>
}