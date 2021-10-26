package cn.hiio.mall.cheap.carouse.dao

import cn.hiio.mall.cheap.carouse.vo.MenuVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository: MongoRepository<MenuVo, String> {

    fun queryByStatusOrderBySortAsc(status:Int):List<MenuVo>

}