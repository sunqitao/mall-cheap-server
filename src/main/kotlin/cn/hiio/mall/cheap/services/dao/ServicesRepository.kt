package cn.hiio.mall.cheap.services.dao

import cn.hiio.mall.cheap.services.vo.ServicesVo
import org.springframework.data.mongodb.repository.MongoRepository

interface ServicesRepository : MongoRepository<ServicesVo, String> {

    fun queryByUserId(userId:String):List<ServicesVo>
}