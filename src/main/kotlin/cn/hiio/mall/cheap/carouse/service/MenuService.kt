package cn.hiio.mall.cheap.carouse.service

import cn.hiio.mall.cheap.carouse.dao.CarouselRepository
import cn.hiio.mall.cheap.carouse.dao.MenuRepository
import cn.hiio.mall.cheap.carouse.vo.CarouselVo
import cn.hiio.mall.cheap.carouse.vo.MenuVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class MenuService : Logging {
    @Autowired
    lateinit var menuRepository: MenuRepository

    fun list(): List<MenuVo> {
        val menus: List<MenuVo> = menuRepository.queryByStatusOrderBySortAsc(1)
        return menus
    }
}