package cn.hiio.mall.cheap.carouse.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "dataoke")
class DaTaoKeProperties {
    var appKey: String? = null
    var appSecret: String? = null
}