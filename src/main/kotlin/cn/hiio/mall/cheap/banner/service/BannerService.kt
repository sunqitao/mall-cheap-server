package cn.hiio.mall.cheap.banner.service

import cn.hiio.mall.cheap.banner.dao.BannerRepository
import cn.hiio.mall.cheap.banner.vo.BannerVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import javax.annotation.Resource


@Service
class BannerService {
    @Autowired
    lateinit var bannerRepository: BannerRepository

    @Resource
    lateinit var redisTemplate : RedisTemplate<String, BannerVo>


    fun list(): List<BannerVo> {
        var list = bannerRepository.findAll(Sort.by(Sort.Direction.ASC,"sortNo"))
        return list
    }

}