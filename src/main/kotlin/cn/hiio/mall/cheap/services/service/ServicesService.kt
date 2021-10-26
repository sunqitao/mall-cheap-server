package cn.hiio.mall.cheap.services.service

import cn.hiio.mall.cheap.services.dao.ServicesRepository
import cn.hiio.mall.cheap.services.vo.ServicesVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServicesService : Logging {
    @Autowired
    lateinit var servicesRepository: ServicesRepository

    fun save(servicesVo: ServicesVo){
        servicesRepository.save(servicesVo)
    }
    fun deleteIt(id: String){
        servicesRepository.deleteById(id)
    }

    fun queryByUserid(userId:String):List<ServicesVo>{
        return servicesRepository.queryByUserId(userId)
    }
    fun queryById(id:String):ServicesVo{
        return servicesRepository.findById(id).get()
    }
}