package cn.hiio.mall.cheap.carouse.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "shop_menu")
class MenuVo :BaseModel(){
     var label: String? = null
     var icon: String? = null
     var url: String? = null
     var sort = 0
     var status = 0 //0 禁用 1启用

     var title: String? = null
     var top = 0
     var urlType: String = "H5"
}