package cn.hiio.mall.cheap.third.dataoke

import java.net.URISyntaxException
import java.util.*

class ApiClient {
    companion object{
        fun sendReqNew(url: String, secret: String, paraMap: TreeMap<String, String>): String {
            if (null == url || "" == url) {
                return "请求地址不能为空"
            }
            if (null == secret || "" == secret) {
                return "secret不能为空"
            }
            if (null == paraMap || paraMap.size < 1) {
                return "参数不能为空"
            }
            val timer = System.currentTimeMillis().toString()
            paraMap["timer"] = timer
            paraMap["nonce"] = "110505"
            paraMap["signRan"] = SignMD5Util.getSignStr(paraMap, secret)
            var data: String = ""
            try {
                data = HttpUtil.httpGetRequest(url, paraMap)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return data
        }

        fun sendReq(url: String, secret: String, paraMap: TreeMap<String, String>): String {
            if (null == url || "" == url) {
                return "请求地址不能为空"
            }
            if (null == secret || "" == secret) {
                return "secret不能为空"
            }
            if (null == paraMap || paraMap.size < 1) {
                return "参数不能为空"
            }
            paraMap["sign"] = SignMD5Util.getSignStr(paraMap, secret)
            var data: String = ""
            try {
                data = HttpUtil.httpGetRequest(url, paraMap)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return data
        }
    }

}