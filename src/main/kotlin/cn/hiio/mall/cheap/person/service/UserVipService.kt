package cn.hiio.mall.cheap.person.service

import cn.hiio.mall.cheap.person.dao.UserVipDetailRepository
import cn.hiio.mall.cheap.person.dao.UserVipRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserVipService : Logging {
    @Autowired
    lateinit var userVipRepository: UserVipRepository
    @Autowired
    lateinit var userVipDetailRepository: UserVipDetailRepository



}