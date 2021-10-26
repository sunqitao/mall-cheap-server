package cn.hiio.mall.cheap.admin.controller

import cn.hiio.mall.cheap.admin.service.AdminService
import cn.hiio.mall.cheap.common.component.SecurityAdmin
import cn.hiio.mall.cheap.common.utils.getAdminAuthUser
import cn.hiio.mall.cheap.common.utils.out
import cn.hiio.mall.cheap.group.service.GroupService
import cn.hiio.mall.cheap.group.vo.GroupVo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/admin/group")
class AdminGroupController{
    @Resource
    lateinit var groupService: GroupService

    @Resource
    lateinit var adminService: AdminService

    @GetMapping("/list")
    @ResponseBody
    @SecurityAdmin
    fun list(pageNum:Int,pageSize:Int,request: HttpServletRequest, response: HttpServletResponse):String{
        var adminUserVo = getAdminAuthUser(request)
        return out(groupService.queryList(adminUserVo.groupId!!,pageNum,pageSize))
    }
    @PostMapping("/updateAdminUserPassword")
    @ResponseBody
    @SecurityAdmin
    fun updateAdminUserPassword(request: HttpServletRequest, response: HttpServletResponse,groupVo: GroupVo):String{
        var adminUserVo = getAdminAuthUser(request)
        adminService.updateAdminUserPassword(adminUserVo,groupVo)
        return out(HashMap<String,String>())
    }
}