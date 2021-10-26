package cn.hiio.mall.cheap.banner.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "banner_info")
class BannerVo : BaseModel(){
    companion object{
        private const val serialVersionUID = -799211215929747075
    }
    //图片地址
    var picUrl:String? = null
    var sortNo:Int? = null


}
