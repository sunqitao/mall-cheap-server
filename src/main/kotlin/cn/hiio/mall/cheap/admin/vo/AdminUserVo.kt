package cn.hiio.mall.cheap.admin.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "admin_user")
class AdminUserVo : BaseModel(){
    var phone: String? = null
    var name: String? = null
    var nickname: String? = null
    var password: String? = null
    var avatar: String? = null
    var groupId: String? = null
}