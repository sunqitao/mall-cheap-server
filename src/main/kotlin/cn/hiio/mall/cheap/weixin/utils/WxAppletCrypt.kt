package cn.hiio.mall.cheap.weixin.utils

import com.alibaba.fastjson.JSONObject
import org.apache.commons.codec.binary.Base64

object WxAppletCrypt {

    private val WATERMARK = "watermark"
    private val APPID = "appid"
    /**
     * 解密数据
     * @return
     * @throws Exception
     */
    fun decrypt(appId: String, encryptedData: String, sessionKey: String, iv: String): String {
        var result = ""
        try {
            val aes = AES()
            val resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv))
            if (null != resultByte && resultByte.size > 0) {
                result = String(WxPKCS7Encoder.decode(resultByte))
                val jsonObject = JSONObject.parseObject(result)
                val decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID)
                if (appId != decryptAppid) {
                    result = ""
                }
            }
        } catch (e: Exception) {
            result = ""
            e.printStackTrace()
        }

        return result
    }
}