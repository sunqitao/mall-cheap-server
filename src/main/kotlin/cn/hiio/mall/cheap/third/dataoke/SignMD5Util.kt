package cn.hiio.mall.cheap.third.dataoke

import cn.hiio.mall.cheap.common.utils.MD5Util
import java.util.*

class SignMD5Util {

    companion object{
        /**
         * 获取签名的util
         * @param map 请求参数
         * @param secretKey 密钥
         * @return
         */
        fun getSignStr(map: TreeMap<String, String>, secretKey: String): String {
            if (map.size == 0) {
                return ""
            }
            val sb = StringBuffer("")
            val keySet: Set<String> = map.keys
            val iter = keySet.iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                sb.append("&" + key + "=" + map[key])
            }
            sb.deleteCharAt(0)
            return sign(sb.toString(), secretKey)
        }

        /**
         * 获取签名的util
         * @param map 请求参数
         * @param secretKey 密钥
         * @return
         */
        fun getSignStrNew(map: TreeMap<String, String>, secretKey: String): String {
            if (map.size == 0) {
                return ""
            }
            val sb = StringBuilder()
            sb.append("appKey=")
            sb.append(map["appKey"])
            sb.append("&timer=")
            sb.append(map["timer"])
            sb.append("&nonce=")
            sb.append(map["nonce"])
            println(sb.toString())
            return sign(sb.toString(), secretKey)
        }

        fun sign(content: String, key: String): String {
            var signStr = ""
            signStr = "$content&key=$key"
            //MD5加密后，字符串所有字符转换为大写
            return MD5Util.MD5Encoding(signStr)!!.toUpperCase()
        }
    }

}