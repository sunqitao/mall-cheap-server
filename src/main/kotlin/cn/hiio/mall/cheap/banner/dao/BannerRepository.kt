package cn.hiio.mall.cheap.banner.dao

import cn.hiio.mall.cheap.banner.vo.BannerVo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BannerRepository : MongoRepository<BannerVo, String>