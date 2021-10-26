package cn.hiio.mall.cheap.group.vo

import cn.hiio.mall.cheap.common.model.BaseModel
import org.springframework.data.mongodb.core.mapping.Document

@Document("group_info")
class GroupVo :BaseModel(){
    companion object{
        private const val serialVersionUID = -7992112592974070075
    }
    //机构名称
    var name:String? = null
    //组织结构编码
    var code:String? = null
    //联系方式
    var tel:String? = null
    //联系人
    var linkName:String? = null
    //地址
    var addressName :String? = null
    //上级
    var parentId:String?=null

    var ids:String?=null

}