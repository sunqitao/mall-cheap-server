package cn.hiio.mall.cheap.carouse.controller

import cn.hiio.mall.cheap.carouse.service.CarouselService
import cn.hiio.mall.cheap.carouse.service.GoodsService
import cn.hiio.mall.cheap.carouse.service.MenuService
import cn.hiio.mall.cheap.carouse.vo.CarouselVo
import cn.hiio.mall.cheap.common.utils.toResJsonString
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Controller
@RequestMapping("/carousel")
@CrossOrigin(origins = ["*"])
class CarouselController: Logging {

    @Autowired
    lateinit var carouselService: CarouselService
    @Autowired
    lateinit var menuService: MenuService
    @Autowired
    lateinit var goodsService: GoodsService

    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

    @RequestMapping("/listCarousel")
    @ResponseBody
    fun listCarousel(): String{
        return carouselService.list().toResJsonString()
    }

    @RequestMapping("/listMenus")
    @ResponseBody
    fun listMenus(): String {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations["home_list_menus_list"]
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val data = menuService.list().toResJsonString()
            operations.set("home_list_menus_list", data, 6, TimeUnit.HOURS)
            return data
        }else{
            return cachedRecommendGoods!!
        }


    }

    @GetMapping("/loadRecommendGoods")
    @ResponseBody
    fun loadRecommendGoods(@RequestParam(defaultValue = "1") page: Int): String {
        return goodsService.recommendGoods(page, 50).toResJsonString()
    }
    @GetMapping("/loadCate")
    @ResponseBody
    fun loadCate(): String {
        return goodsService.loadCate().toResJsonString()
    }

    @GetMapping("/getHotSearch")
    @ResponseBody
    fun getHotSearch(): String {
        return goodsService.getHotSearch().toResJsonString()
    }

    @GetMapping("/search2")
    @ResponseBody
    fun search2(keyword: String, page: Int, sort: String, hasCoupon: String): String {
        return goodsService.search2(keyword, page, sort, hasCoupon).toResJsonString()
    }

    @GetMapping("/getPrivilegeLink")
    @ResponseBody
    fun getPrivilegeLink(goodsId: String): String {
        return goodsService.getPrivilegeLink(goodsId).toResJsonString()
    }


    @GetMapping("/goodsDetail")
    @ResponseBody
    fun goodsDetail( goodsId: String): String {
        return goodsService.goodsDetail( goodsId, true).toResJsonString()
    }


    @GetMapping("/getSimilarGoods")
    @ResponseBody
    fun getSimilarGoods(daTaoKeGoodsId: String): String {
        return goodsService.similarGoods(daTaoKeGoodsId).toResJsonString()
    }

    @GetMapping("/loadGoodsByCate")
    @ResponseBody
    fun loadGoodsByCate(subcid: String, page: Int, sort: Int): String {
        return goodsService.loadGoodsByCate(subcid, page, 50, sort).toResJsonString()
    }


    @GetMapping("/loadRankGoods")
    @ResponseBody
    fun loadRankGoods(cid: String): String {
        return goodsService.rankGoods(cid).toResJsonString()
    }

    @GetMapping("/loadNineGoods")
    @ResponseBody
    fun loadNineGoods(@RequestParam(defaultValue = "1")page: Int, @RequestParam(defaultValue = "20")size: Int, nineCid: String): String {
        return goodsService.nineGoods(page, size, nineCid).toResJsonString()
    }


    @GetMapping("/getActiveGoodsList")
    @ResponseBody
    fun getActiveGoodsList(activeId: String, page: Int): String {
        return goodsService.getActiveGoodsList(page, 50, activeId).toResJsonString()
    }
//    @GetMapping("/getActiveGoodsListV2")
//    @ResponseBody
//    fun getActiveGoodsListV2(activeId2: String,activeId: String, page: Int): String {
//        return goodsService.getActiveGoodsList(page, 50, activeId,activeId2).toResJsonString()
//    }
    @RequestMapping("/getHalfGoodsList")
    @ResponseBody
    fun getHalfGoodsList(): String {
        return goodsService.getHalfGoodsList().toResJsonString()
    }
    @GetMapping("/getDongList")
    @ResponseBody
    fun getDongdongGoodsList(): String {
        return goodsService.getDongdongGoodsList().toResJsonString()
    }
}


