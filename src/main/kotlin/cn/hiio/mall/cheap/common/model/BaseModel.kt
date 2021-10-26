package cn.hiio.mall.cheap.common.model

import org.springframework.data.annotation.Id
import java.io.Serializable
import java.util.*

abstract class BaseModel() : Serializable {
    @Id
    var id: String? = null
    var update: Date = Date();
    var created: Date = Date();
    var deleted: Boolean = false;

}