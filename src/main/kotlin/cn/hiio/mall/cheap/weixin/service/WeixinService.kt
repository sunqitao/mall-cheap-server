package cn.hiio.mall.cheap.weixin.service

import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.common.utils.MD5Util
import cn.hiio.mall.cheap.common.utils.get
import cn.hiio.mall.cheap.weixin.enums.WeixinCacheKeys
import com.alibaba.fastjson.JSON
import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.model.PutObjectRequest
import com.qcloud.cos.region.Region
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*
import javax.annotation.Resource


@Service
class WeixinService : Logging {
    //微信小程序相关参数
    var appId = ""
    var secret = ""

    @Resource
    lateinit var redisTemplate: RedisTemplate<String, String>
    @Resource
    lateinit var env: Environment

    fun codeToParam(code:String):ResultVo<String>{
        var jsCode = code
        var response = get("https://api.weixin.qq.com/sns/jscode2session?appid=$appId&secret=$secret&js_code=$jsCode&grant_type=authorization_code")
        logger.info("response is $response")
        var openId = JSON.parseObject(response).getString("openid")
        var resultVo = ResultVo<String>(data = openId)
        if(openId == null || openId.length<1){
            resultVo = ResultVo<String>(code = StatusCode.failed.code,message = "code过期，重新获取code",data = openId)
        }
        return resultVo
    }
    fun codeToOpenId(code:String):String{
        var jsCode = code
        var response = get("https://api.weixin.qq.com/sns/jscode2session?appid=$appId&secret=$secret&js_code=$jsCode&grant_type=authorization_code")
        logger.info("response is $response")
        var openId = JSON.parseObject(response).getString("openid")
        return openId
    }

    fun ticket(): String {
        val url = "http://"
        val client = HttpClientBuilder.create().build()
        val get = HttpGet(url)
        val res = client.execute(get!!)
        val entity = res.entity
        var responseContent = EntityUtils.toString(entity, "UTF-8")
        return responseContent
    }

    private fun getAccessToken():String{
        var redisKey = WeixinCacheKeys.WEIXIN_ACCESS_TOKEN.key+"_$appId"

        var accessToken = redisTemplate.opsForValue().get(redisKey)
        if(accessToken != null ){
        }else{
            var response = get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$appId&secret=$secret")
            logger.info("response is $response")
            accessToken = JSON.parseObject(response).getString("access_token")
            var expiresIn = JSON.parseObject(response).getLong("expires_in")
            redisTemplate.opsForValue().set(redisKey,accessToken, Duration.ofSeconds(expiresIn))
        }
        return accessToken!!
    }

//    fun getQRCode(communityId:String):String{
//        logger.info("communityId is $communityId")
//        var accessToken = "" //getAccessToken()
//        var response = get("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=$accessToken")
//        logger.info("response is $response")
//        var openId = JSON.parseObject(response).getString("openid")
//        var resultVo = ResultVo<String>(data = openId)
//        if(openId == null || openId.length<1){
//            resultVo = ResultVo<String>(code = StatusCode.failed.code,message = "code过期，重新获取code",data = openId)
//        }
//        return JSON.toJSONString(resultVo)
//    }

//    fun getminiQr(sceneStr:String):String {
//        var accessToken = getAccessToken()
//        var paramJson =   JSONObject();
//        paramJson.put("scene", sceneStr);
//        var response = post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=$accessToken",null,null,null,null,paramJson.toJSONString())
//        var imageInputStream = response.entity.content
//        var tempFile = File(FileUtils.getTempDirectoryPath() + UUID.randomUUID().getUUID()+".jpeg")
//        FileUtils.copyInputStreamToFile(imageInputStream,tempFile)
//        return putFile(tempFile)
//    }

    //有需要上传文件的需要在这配置
    fun putFile(file : MultipartFile):String{
        // 指定要上传到的存储桶
        val bucketName = "mallcheap-12121231"
        //拿到后缀
        val extensionT = file.originalFilename
        val extension = extensionT?.substring(extensionT.lastIndexOf("."))
        val fileBytes =  file.inputStream.readBytes()
        var fileKey = MD5Util.MD5Encoding(Base64.getEncoder().encodeToString(fileBytes))
        //组装要返回的url
        var returnUrl = "https://mallcheap-12121231.cos.ap-beijing.myqcloud.com/$fileKey.$extension"
        // 指定要上传到 COS 上对象键
        val putObjectRequest = PutObjectRequest(bucketName, "$fileKey.$extension", file.inputStream,null)
        val putObjectResult = CosClient.cosClient.putObject(putObjectRequest)
        return returnUrl
    }
}


object CosClient {
    //腾讯云上传文件相关配置参数
    // 1 初始化用户身份信息（secretId, secretKey）。
    val appid = ""
    val secretId =  ""
    val secretKey =  ""
    var cred = BasicCOSCredentials(secretId, secretKey)
    // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    var region = Region("ap-beijing")
    var clientConfig = ClientConfig(region)
    // 3 生成 cos 客户端。
    var cosClient = COSClient(cred, clientConfig)

}