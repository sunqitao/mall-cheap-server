package cn.hiio.mall.cheap.carouse.task

import cn.hiio.mall.cheap.carouse.service.DataokeService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class InitDataTask:Logging {
    @Autowired
    lateinit var dataokeService: DataokeService

    //每四小时执行一次
    @Scheduled(cron = "0 0 */4 * * ?")
    private fun initAllCache(){
        val begTime = System.currentTimeMillis()
        logger.info("task-initCache beg")
        dataokeService.loadCate()
        dataokeService.loadBanners()
        dataokeService.getDongdongGoodsList()
        logger.info("task-initCache end")
        logger.info("cost time is ${System.currentTimeMillis() - begTime}ms")
    }
}