package cn.hiio.mall.cheap.carouse.dao

import cn.hiio.mall.cheap.carouse.vo.CarouselVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CarouselRepository: MongoRepository<CarouselVo, String> {
    fun queryByStatus(status:Int):List<CarouselVo>
}