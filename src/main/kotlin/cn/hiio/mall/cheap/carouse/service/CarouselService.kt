package cn.hiio.mall.cheap.carouse.service

import cn.hiio.mall.cheap.carouse.dao.CarouselRepository
import cn.hiio.mall.cheap.carouse.vo.CarouselVo
import cn.hiio.mall.cheap.carouse.vo.ProductVo
import com.alibaba.fastjson.JSONObject
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.util.ArrayList
import java.util.stream.Collectors
import javax.annotation.Resource

@Service
class CarouselService : Logging {
    @Autowired
    lateinit var carouselRepository: CarouselRepository
    @Resource
    lateinit var dataokeService: DataokeService
    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

    fun list(): List<CarouselVo> {
        var relist = ArrayList<CarouselVo>()

        var bannerJsonArray = dataokeService.loadBanners()
         if (CollectionUtils.isEmpty(bannerJsonArray)) {
             return relist
        } else {
            for (i in 0..(bannerJsonArray.size - 1)){
                val jb = bannerJsonArray.getJSONObject(i)
                val carouselVo = CarouselVo()
                carouselVo.backgroundColor="#c37b85"
                carouselVo.image=jb.getString("topicImage")
                carouselVo.urlType="Native"
                carouselVo.title=jb.getString("topicName")
                carouselVo.topicId=jb.getString("topicId")
                if( carouselVo.topicId==null || carouselVo.topicId!!.toInt() < 1){
                    val pageUrl = dataokeService.loadBannersActivePageUrl(jb.getString("activityId"))
                    if(pageUrl.length>1){
                        carouselVo.urlType="H5"
                        carouselVo.url=pageUrl
                        relist.add(carouselVo)
                    }
                }else{
                    carouselVo.url="/pages/active/index?activeId="+jb.getString("topicId")
                    relist.add(carouselVo)
                }

            }
             return relist
        }
//        return carouselRepository.queryByStatus(1)
    }
}