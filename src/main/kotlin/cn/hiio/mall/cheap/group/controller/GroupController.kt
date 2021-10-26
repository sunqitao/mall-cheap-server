package cn.hiio.mall.cheap.group.controller

import cn.hiio.mall.cheap.group.service.GroupService
import cn.hiio.mall.cheap.group.vo.GroupVo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource

@Controller
@RequestMapping("/group")
class GroupController{
    @Resource
    lateinit var groupService: GroupService

    @GetMapping("/list")
    fun list(rootId:String):List<GroupVo>{
        return groupService.queryList(rootId,1,1)
    }
    @GetMapping("/updateAdminUserPassword")
    fun updateAdminUserPassword(rootId:String):List<GroupVo>{
        return groupService.queryList(rootId,1,1)
    }
}