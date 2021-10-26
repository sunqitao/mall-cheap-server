package cn.hiio.mall.cheap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
//@EnableTransactionManagement
//@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}