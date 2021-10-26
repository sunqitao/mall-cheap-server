package cn.hiio.mall.cheap.person.service

import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.person.dao.*
import cn.hiio.mall.cheap.person.vo.*
import cn.hiio.mall.cheap.weixin.service.WeixinService
import com.alibaba.fastjson.JSON
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.DigestUtils
import java.util.concurrent.TimeUnit
import javax.annotation.Resource


@Service
class UserService : Logging {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userQrCodeRepository: UserQrCodeRepository

    @Autowired
    lateinit var userSelfInfoRepository: UserSelfInfoRepository

    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

    @Resource
    lateinit var redisTemplate: RedisTemplate<String, UserVo>
    @Resource
    lateinit var weixinService: WeixinService
    @Resource
    lateinit var fansRepository: FansRepository
    @Resource
    lateinit var fansBuyRepository: FansBuyRepository

    @Transactional(propagation = Propagation.REQUIRED)
    fun login(userVo: UserVo): ResultVo<HashMap<String, String>> {
        var resultVo = ResultVo<HashMap<String, String>>(data = HashMap())
        var userList = userRepository.queryByUserName(userVo.userName!!)
        if (userList.isEmpty()) {
            resultVo.code = Integer.MIN_VALUE;
            resultVo.message = "该用户不存在"
        } else {
            var dabUservo = userList.get(0);
            if (dabUservo.password.equals(userVo.password)) {
                var token = DigestUtils.md5DigestAsHex(dabUservo.id!!.toByteArray())
                stringRedisTemplate.opsForValue().set(token, dabUservo.id!!, 30L, TimeUnit.DAYS)
                redisTemplate.opsForValue().set(dabUservo.id!!, dabUservo, 30L, TimeUnit.DAYS)
                resultVo.data!!.put("token", token);
                println(JSON.toJSONString(redisTemplate.opsForValue().get(token)));
            } else {
                resultVo.code = Integer.MIN_VALUE;
                resultVo.message = "密码不对"
            }
        }
        return resultVo
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun loginWx(userWxVo: UserWxVo): ResultVo<HashMap<String, String>> {
        logger.debug("userWxvo is ${JSON.toJSONString(userWxVo)}")
        var openId = weixinService.codeToOpenId(userWxVo.code)

        var resultVo = ResultVo<HashMap<String, String>>(data = HashMap())
        var userVoList = userRepository.queryByOpenId(openId)
        var userVo = UserVo()
        if (userVoList.isNotEmpty()) {
            userVo = userVoList.get(0)
        } else {
            userVo.openId = openId
        }
        userVo.nickName = userWxVo.nickName
        userVo.gender = userWxVo.gender
        userVo.avatarUrl = userWxVo.avatarUrl
        userVo.city = userWxVo.city
        userVo.province = userWxVo.province
        userVo.country = userWxVo.country
        userRepository.save(userVo)

        var token = DigestUtils.md5DigestAsHex(userVo.id!!.toByteArray())
        stringRedisTemplate.opsForValue().set(token, userVo.id!!, 30L, TimeUnit.DAYS)
        redisTemplate.opsForValue().set(userVo.id!!, userVo, 30L, TimeUnit.DAYS)
        resultVo.data!!["token"] = token;
        resultVo.data!!["id"] = userVo.id!!;
        if(userVo.mobile!=null){
            resultVo.data!!["mobile"] = userVo.mobile!!;
        }
        return resultVo
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun register(userVo: UserVo): ResultVo<HashMap<String, String>> {
        var resultVo = ResultVo<HashMap<String, String>>(data = HashMap())
        var userList = userRepository.queryByUserName(userVo.userName!!)
        var userList2 = userList//userRepository.queryByTel(userVo.mobile!!)
        if (userList.isEmpty() && userList2.isEmpty()) {
            userRepository.insert(userVo)
            var token = DigestUtils.md5DigestAsHex(userVo.id!!.toByteArray())
            stringRedisTemplate.opsForValue().set(token, userVo.id!!, 30L, TimeUnit.DAYS)
            redisTemplate.opsForValue().set(userVo.id!!, userVo, 30L, TimeUnit.DAYS)
            resultVo.data!!.put("token", token);
            println(JSON.toJSONString(redisTemplate.opsForValue().get(token)));
        } else {
            resultVo.code = Integer.MIN_VALUE;
            resultVo.message = "您录入的手机号或者用户名重复"
        }
        return resultVo
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun update(userVo: UserVo) {
        var oldUser = getUserFromCache(userVo.id!!)
        oldUser.mobile = userVo.mobile
        oldUser.name = userVo.name
        oldUser.addressName = userVo.addressName
        userRepository.save(oldUser)
        delCache(oldUser.id!!)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateUserVo(userVo: UserVo) {
        userRepository.save(userVo)
        delCache(userVo.id!!)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateIdCard(idCard: String, flag: Boolean, userId: String) {
        var oldUser = getUserFromCache(userId)
        if (idCard == oldUser.idCard) {
            return
        }
        oldUser.idCard = idCard
        userRepository.save(oldUser)
        delCache(oldUser.id!!)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateIdCardMore(idCard: String, workName: String?, userFrom: String?, cardNo: String?,
                         houseType: String?,
                         houseUserName: String?,
                         houseUserTel: String?,
                         houseUserIdCard: String?,
                         flag: Boolean, userId: String) {
        var oldUser = getUserFromCache(userId)
        oldUser.idCard = idCard
        oldUser.workName = workName
        oldUser.userFrom = userFrom
        oldUser.cardNo = cardNo
        userRepository.save(oldUser)
        delCache(oldUser.id!!)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateAddress(userVo: UserVo) {
        var oldUser = userRepository.findById(userVo.id!!).get()
        oldUser.addressName = userVo.addressName
        userRepository.save(oldUser)
        delCache(oldUser.id!!)
    }

    fun getUserFromCache(userId: String): UserVo {
//        delCache(userId)
        var userVo = redisTemplate.opsForValue().get(userId)
        if (userVo == null) {
            userVo = userRepository.findById(userId).get()
            redisTemplate.opsForValue().set(userId, userVo, 30L, TimeUnit.DAYS)
        }
        return userVo
    }
    fun queryUserList(): List<UserVo> {
        return userRepository.findAll()
    }

    fun queryByIdCardAndTelAndName(idcard: String, tel: String, name: String): List<UserVo> {
        return userRepository.queryByIdCardAndName(idcard, name)
    }

    private fun delCache(key: String) {
        redisTemplate.delete(key)
    }
//    private fun setCache(key:String){
//        redisTemplate.delete(key)
//    }

    fun queryUserSelfInfoByUserId(userId: String): List<UserSelfInfoVo> {
        return userSelfInfoRepository.queryByUserId(userId)
    }
    fun saveUserSelfInfoByUserId(userSelfInfoVo: UserSelfInfoVo){
        userSelfInfoRepository.save(userSelfInfoVo)
    }

    fun queryUserFans(userId: String,toUserId:String):List<FansVo>{
        return fansRepository.queryByUserIdAndFansId(toUserId,userId)
    }
    fun saveUserFans(fansVo: FansVo){
        fansRepository.save(fansVo)
    }
    //所有二维码
    fun saveUserQrCode(userQrCodeVo: UserQrCodeVo){
        userQrCodeRepository.save(userQrCodeVo)
    }

    fun queryUserQrCode(userId:String):List<UserQrCodeVo>{
        return userQrCodeRepository.queryByUserId(userId)
    }

    //leader 查看自己的粉丝
    fun queryMyFans(userId: String):List<FansVo>{
        var fansList = fansRepository.queryByUserId(userId)
        fansList.forEach {
            var userInfo = getUserFromCache(it.fansId)
            it.avatarUrl = userInfo.avatarUrl
            it.nickName = userInfo.nickName
        }
        return fansList
    }
    //leader 查看自己的粉丝详情
    fun queryMyFans(userId: String,fansId:String):FansVo{
        var fansList = fansRepository.queryByUserIdAndFansId(userId,fansId)
        var fansInfo = fansList[0]
        var userInfo = getUserFromCache(fansInfo.fansId)
        fansInfo.avatarUrl = userInfo.avatarUrl
        fansInfo.nickName = userInfo.nickName

        return fansInfo
    }
    //leader 保存自己的粉丝详情
    fun updateMyFans(fansVo: FansVo){
        var fansList = fansRepository.queryByUserIdAndFansId(fansVo.userId,fansVo.fansId)
        var fansInfo = fansList[0]
        fansInfo.typeIndex = fansVo.typeIndex
        fansInfo.statusIndex = fansVo.statusIndex
        fansInfo.bCount = fansVo.bCount
        fansInfo.totalCount = fansVo.totalCount
        fansInfo.nexDate = fansVo.nexDate
        fansInfo.remark = fansVo.remark
        fansRepository.save(fansInfo)
    }
    fun myFansBuyVoList(userId:String,fansId: String):List<FansBuyVo>{
        return fansBuyRepository.queryByUserIdAndFansId(userId,fansId)
    }
    fun saveMyFansBuyVo(fansBuyVo: FansBuyVo){
        fansBuyRepository.save(fansBuyVo)
    }
    fun delMyFansBuy(id:String){
        fansBuyRepository.deleteById(id)
    }
}