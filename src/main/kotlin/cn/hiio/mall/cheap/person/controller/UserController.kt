package cn.hiio.mall.cheap.person.controller

import cn.hiio.mall.cheap.common.component.Security
import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.common.utils.*
import cn.hiio.mall.cheap.person.service.UserService
import cn.hiio.mall.cheap.person.vo.*
import cn.hiio.mall.cheap.services.service.ArticleService
import cn.hiio.mall.cheap.services.service.ServicesService
import cn.hiio.mall.cheap.services.vo.ArticleFanVo
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Controller
@RequestMapping("/user")
//@CrossOrigin(origins = ["*"])
class UserController: Logging {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var articleService: ArticleService
    @Autowired
    lateinit var servicesService: ServicesService
    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>


//    @PostMapping("/login" )
//    @ResponseBody
//    fun login(request: HttpServletRequest, response: HttpServletResponse, @RequestBody userVo: UserVo):String{
//        var resultVo = userService.login(userVo)
//        return JSON.toJSONString(resultVo)
//    }
//    @PostMapping("/loginByWx")
//    @ResponseBody
//    fun loginByWx(request: HttpServletRequest, response: HttpServletResponse, userwxVo: UserWxVo):String{
//        AssertUtil.hasLengthByTrim(userwxVo.code, StatusCode.failed,"该访问有问题")
//        var resultVo = userService.loginWx(userwxVo)
//        return JSON.toJSONString(resultVo)
//    }
//    @PostMapping("/register" )
//    @ResponseBody
//    fun register(request: HttpServletRequest, response: HttpServletResponse, @RequestBody userVo: UserVo):String{
//        var resultVo = userService.register(userVo)
//        return JSON.toJSONString(resultVo)
//    }
//    @PostMapping("/info" )
//    @ResponseBody
//    @Security
//    fun info(request: HttpServletRequest, response: HttpServletResponse):String{
//        var userVo = getAuthUser(request)
//        return userVo.toResJsonString()
//    }
//    @PostMapping("/list" )
//    @ResponseBody
//    fun list(request: HttpServletRequest, response: HttpServletResponse):String{
//        var userList = userService.queryUserList()
//        var reUserList = ArrayList<UserDto>()
//        userList.forEach {
//            var oneD = UserDto()
//            oneD.id = it.id
//            oneD.nickName = it.nickName
//            oneD.avatarUrl = it.avatarUrl
//            var selfInfoList = userService.queryUserSelfInfoByUserId(it.id!!)
//            if(selfInfoList.isNotEmpty()){
//                var tSelfInfoVo = selfInfoList[0]
//                oneD.title = tSelfInfoVo.title
//                oneD.detail = tSelfInfoVo.detail
//                oneD.tag1 = tSelfInfoVo.tag1
//                oneD.tag2 = tSelfInfoVo.tag2
//                oneD.tag3 = tSelfInfoVo.tag3
//                oneD.avatarUrl = tSelfInfoVo.imgList
//                oneD.slogan = tSelfInfoVo.slogan
//                var serviceType = tSelfInfoVo.serviceType
//                oneD.serviceTypeStr = getServiceTypeStr(serviceType)
//                reUserList.add(oneD)
//            }
//
//        }
//        return reUserList.toResJsonString()
//    }
//    var serviceTypeList = listOf<String>("健康", "居家", "出行")
//    private fun getServiceTypeStr(index:Int):String{
//        return serviceTypeList[index]
//    }
//
//    /**
//     */
//    @PostMapping("/update" )
//    @ResponseBody
//    @Security
//    fun update(request: HttpServletRequest,response: HttpServletResponse,userVo: UserVo):String{
//        var dataUser = getAuthUser(request)
//        userVo.id=dataUser.id
//        userService.update(userVo)
//        return JSON.toJSONString(ResultVo<Map<String,String>>(data = HashMap<String,String>()))
//    }
////    个人介绍
//    @PostMapping("/selfinfo" )
//    @ResponseBody
//    @Security
//    fun selfinfo(request: HttpServletRequest,response: HttpServletResponse):String{
//        var dataUser = getAuthUser(request)
//        var selefVoList = userService.queryUserSelfInfoByUserId(dataUser.id!!);
//        if(selefVoList.size>0){
//            return selefVoList[0].toResJsonString()
//        }else{
//            return UserSelfInfoVo().toResJsonString()
//        }
//    }
//    @PostMapping("/saveselfinfo" )
//    @ResponseBody
//    @Security
//    fun saveSelfinfo(request: HttpServletRequest,response: HttpServletResponse,userSelfInfoVo: UserSelfInfoVo):String{
//        var dataUser = getAuthUser(request)
//        userSelfInfoVo.userId = dataUser.id!!
//        var selefVoList = userService.queryUserSelfInfoByUserId(dataUser.id!!);
//        if(selefVoList.isNotEmpty()){
//            userSelfInfoVo.id = selefVoList[0].id
//        }
//        userService.saveUserSelfInfoByUserId(userSelfInfoVo)
//        return HashMap<String,String>().toResJsonString()
//    }
//
//    //个人介绍预览 selfvo， articlevo， 如果当前用户登录则展示 是否一句关注
//    @PostMapping("/selfview" )
//    @ResponseBody
//    fun selfview(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var userId = id
//        var userInfo = userService.getUserFromCache(userId)
//        userInfo.openId = ""
//        var selefVoList = userService.queryUserSelfInfoByUserId(userId);
//        var articleVoList = articleService.queryByUserid(userId)
//        var servicesList = servicesService.queryByUserid(userId)
//
//        AssertUtil.isTrue(selefVoList.isNotEmpty() && articleVoList.isNotEmpty() && servicesList.isNotEmpty(),StatusCode.failed,"请返回先完善信息")
//        //如果当前用户是登录状态，则需要拿到改用户是否一句点赞
//        var token = request.getHeader("ACCESSTOKEN")
//        if(token!=null && token.length>0){
//            var userVoId = stringRedisTemplate.opsForValue().get(token)
//            if(userVoId!=null && userVoId.length > 0){
//                var tuserVo = userService.getUserFromCache(userVoId!!)
//                var userFanList = userService.queryUserFans(tuserVo.id!!,userId)
//                userInfo.hasFans = userFanList.isNotEmpty()
//            }
//        }
//
//
//        return mapOf("userinfo" to userInfo, "selfs" to selefVoList[0],"articles" to articleVoList,
//            "services" to servicesList).toResJsonString()
//    }
//
//    //个人介绍预览 selfvo， articlevo， 如果当前用户登录则展示 是否一句关注
//    @PostMapping("/selfservices" )
//    @ResponseBody
//    fun selfservices(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var servicesVo = servicesService.queryById(id)
//        var userInfo = userService.getUserFromCache(servicesVo.userId)
//        //如果当前用户是登录状态，则需要拿到改用户是否一句点赞
////        var token = request.getHeader("ACCESSTOKEN")
////        if(token!=null && token.length>0){
////            var userVoId = stringRedisTemplate.opsForValue().get(token)
////            if(userVoId!=null && userVoId.length > 0){
////                var tuserVo = userService.getUserFromCache(userVoId!!)
////                var userFanList = userService.queryUserFans(tuserVo.id!!,userId)
////                userInfo.hasFans = userFanList.isNotEmpty()
////            }
////        }
//        return mapOf("userinfo" to userInfo, "servicesinfo" to servicesVo).toResJsonString()
//    }
//
//    //预览 个人的 内容列表
//    @PostMapping("/selfarticle" )
//    @ResponseBody
//    fun selfarticle(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var userId = id
//        var userInfo = userService.getUserFromCache(userId)
//        userInfo.openId = ""
//        var articleVoList = articleService.queryByUserid(userId)
//        return mapOf("userinfo" to userInfo,"articles" to articleVoList
//                ).toResJsonString()
//    }
//    //预览 个人的 内容详情
//    @PostMapping("/selfarticleinfo" )
//    @ResponseBody
//    fun selfarticleinfo(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var articleInfo = articleService.queryById(id)
//        if(articleInfo.createdStr == null){
//            articleInfo.createdStr = articleInfo.created.stringFormat(DateFormatType.PATTERN_UNIT_YYYY_MM_DD)
//        }
//        //保存浏览记录数
//        articleInfo.viewCount +=1
//        articleService.save(articleInfo)
//
//        var userInfo = userService.getUserFromCache(articleInfo.userId)
//
//        //如果当前用户是登录状态，则需要拿到改用户是否一句点赞
//        var token = request.getHeader("ACCESSTOKEN")
//        if(token!=null && token.length>0){
//            var userVoId = stringRedisTemplate.opsForValue().get(token)
//            if(userVoId!=null && userVoId.length > 0){
//                var tuserVo = userService.getUserFromCache(userVoId!!)
//                var articleFanList :List<ArticleFanVo> = articleService.queryByUserIdAndArticleId(tuserVo.id!!,id);
//                articleInfo.hasLike = articleFanList.isNotEmpty()
//            }
//        }
//        //在缓存中获取正确的用户信息
//        return mapOf("userinfo" to userInfo,"articleInfo" to articleInfo
//        ).toResJsonString()
//    }
//    //点赞 个人的 内容详情
//    @PostMapping("/likeselfarticle" )
//    @ResponseBody
//    @Security
//    fun likeselfarticle(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var user = getAuthUser(request)
//        var articleInfo = articleService.queryById(id)
//        //查询当前文章 改用户有没有点过赞
//        var articleFanList :List<ArticleFanVo> = articleService.queryByUserIdAndArticleId(user.id!!,articleInfo.id!!);
//        if(articleFanList.isEmpty()){
//            var articleFanVo = ArticleFanVo()
//            articleFanVo.articleId = articleInfo.id!!
//            articleFanVo.toUserId = articleInfo.userId
//            articleFanVo.userId = user.id!!
//            articleInfo.likeCount = articleInfo.likeCount+1
//            articleService.save(articleInfo)
//            articleService.saveArticleFanVo(articleFanVo)
//        }else{
//            if(articleInfo.likeCount>0){
//                articleInfo.likeCount = articleInfo.likeCount-1
//                articleService.save(articleInfo)
//            }
//            articleService.deleteArticleFanVo(articleFanList[0].id!!)
//        }
//        return toSuccessJsonString()
//    }
//    //点赞 个人的 内容详情
//    @PostMapping("/likeuser" )
//    @ResponseBody
//    @Security
//    fun likeuser(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var user = getAuthUser(request)
//        //查询当前文章 改用户有没有点过赞
//        var fansVoList :List<FansVo> = userService.queryUserFans(user.id!!,id);
//        if(fansVoList.isEmpty()){
//            var fansVo = FansVo()
//            fansVo.userId = id
//            fansVo.fansId = user.id!!
//            userService.saveUserFans(fansVo)
//        }else{
//            fansVoList[0].fansStatus = 1
//            userService.saveUserFans(fansVoList[0])
//        }
//        return toSuccessJsonString()
//    }
//
//    @PostMapping("/qrcode" )
//    @ResponseBody
//    @Security
//    fun qrcode(request: HttpServletRequest,response: HttpServletResponse):String{
//        var user = getAuthUser(request)
//        var userQrCodeVoList = userService.queryUserQrCode(user.id!!)
//
//        if(userQrCodeVoList.isNotEmpty()){
//            return userQrCodeVoList[0].toResJsonString()
//        }
//        return toSuccessJsonString()
//    }
//    @PostMapping("/saveqrcode" )
//    @ResponseBody
//    @Security
//    fun saveQrcode(request: HttpServletRequest,response: HttpServletResponse,userQrCodeVo: UserQrCodeVo):String{
//        var user = getAuthUser(request)
//        var userQrCodeVoList = userService.queryUserQrCode(user.id!!)
//        if(userQrCodeVoList.isNotEmpty()){
//            var tempVo = userQrCodeVoList[0]
//            if(userQrCodeVo.aliPay!=null){
//                if(userQrCodeVo.aliPay.equals("undefined")){
//                    tempVo.aliPay = null
//                }else{
//                    tempVo.aliPay = userQrCodeVo.aliPay
//                }
//            }
//            if(userQrCodeVo.wechatBar!=null){
//                if(userQrCodeVo.wechatBar.equals("undefined")){
//                    tempVo.wechatBar = null
//                }else{
//                    tempVo.wechatBar = userQrCodeVo.wechatBar
//                }
//            }
//            if(userQrCodeVo.wechatPay!=null){
//                if(userQrCodeVo.wechatPay.equals("undefined")){
//                    tempVo.wechatPay = null
//                }else{
//                    tempVo.wechatPay = userQrCodeVo.wechatPay
//                }
//            }
//            userService.saveUserQrCode(tempVo)
//        }else{
//            var userQrCodeData = UserQrCodeVo()
//            userQrCodeData.userId = user.id!!
//            userQrCodeData.aliPay = userQrCodeVo.aliPay
//            userQrCodeData.wechatBar = userQrCodeVo.wechatBar
//            userQrCodeData.wechatPay = userQrCodeVo.wechatPay
//            userService.saveUserQrCode(userQrCodeData)
//        }
//        return toSuccessJsonString()
//    }
//
//    @PostMapping("/myfans" )
//    @ResponseBody
//    @Security
//    fun myFans(request: HttpServletRequest,response: HttpServletResponse):String{
//        var user = getAuthUser(request)
//        var userQrCodeVoList = userService.queryMyFans(user.id!!)
//        return userQrCodeVoList.toResJsonString()
//    }
//    @PostMapping("/myfansinfo" )
//    @ResponseBody
//    @Security
//    fun myFansInfo(request: HttpServletRequest,response: HttpServletResponse,fansId:String):String{
//        var user = getAuthUser(request)
//        var fansInfoVo = userService.queryMyFans(user.id!!,fansId)
//
//        return fansInfoVo.toResJsonString()
//    }
//    @PostMapping("/updateMyFans" )
//    @ResponseBody
//    @Security
//    fun updateMyFans(request: HttpServletRequest,response: HttpServletResponse,fansVo: FansVo):String{
//        var user = getAuthUser(request)
//        fansVo.userId = user.id!!
//        var fansInfoVo = userService.updateMyFans(fansVo)
//        return fansInfoVo.toResJsonString()
//    }
//
//    @PostMapping("/myFansBuy" )
//    @ResponseBody
//    @Security
//    fun myFansBuy(request: HttpServletRequest,response: HttpServletResponse,fansId: String):String{
//        var user = getAuthUser(request)
//        var fansBuyVoList = userService.myFansBuyVoList(user.id!!,fansId)
//        return fansBuyVoList.toResJsonString()
//    }
//    @PostMapping("/saveMyFansBuy" )
//    @ResponseBody
//    @Security
//    fun saveMyFansBuy(request: HttpServletRequest,response: HttpServletResponse,fansBuyVo: FansBuyVo):String{
//        var user = getAuthUser(request)
//        fansBuyVo.userId = user.id!!
////        if(fansBuyVo.bDate!=null){
////            fansBuyVo.bDate = Date().minus(fansBuyVo.bDate!!.toInt()).stringFormat(DateFormatType.PATTERN_YYYYMMDD_X)
////        }
//        userService.saveMyFansBuyVo(fansBuyVo)
//        return fansBuyVo.toResJsonString()
//    }
//    @PostMapping("/delMyFansBuy" )
//    @ResponseBody
//    @Security
//    fun delMyFansBuy(request: HttpServletRequest,response: HttpServletResponse,id:String):String{
//        var user = getAuthUser(request)
//        userService.delMyFansBuy(id)
//        return toSuccessJsonString()
//    }

    @GetMapping("/currentUser")
    @ResponseBody
//    @Security
    fun getCurrentUser(request: HttpServletRequest): String {
//        var user = getAuthUser(request)
        return toSuccessJsonString() //user.toResJsonString()
    }
    @GetMapping("/logout")
    @ResponseBody
    @Security
    fun logout(request: HttpServletRequest): String {
//        var user = getAuthUser(request)
        return toSuccessJsonString()
    }


}