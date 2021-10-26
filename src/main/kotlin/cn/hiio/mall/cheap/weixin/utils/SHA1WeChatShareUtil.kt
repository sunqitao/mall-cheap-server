package cn.hiio.mall.cheap.weixin.utils

import java.security.DigestException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class SHA1WeChatShareUtil {
    companion object {

        @Throws(DigestException::class)
        fun SHA1(maps: Map<String, String>): String {
            val decrypt = getOrderByLexicographic(maps)

            try {
                val digest = MessageDigest.getInstance("SHA-1")
                digest.update(decrypt.toByteArray())
                val messageDigest = digest.digest()
                val hexString = StringBuffer()

                for (i in messageDigest.indices) {
                    val shaHex = Integer.toHexString(messageDigest[i].toInt() and 255)
                    if (shaHex.length < 2) {
                        hexString.append(0)
                    }

                    hexString.append(shaHex)
                }

                return hexString.toString()
            } catch (var7: NoSuchAlgorithmException) {
                var7.printStackTrace()
                throw DigestException("签名错误！")
            }

        }

        private fun getOrderByLexicographic(maps: Map<String, String>): String {
            return splitParams(lexicographicOrder(getParamsName(maps)), maps)
        }

        private fun getParamsName(maps: Map<String, String>): List<String> {
            val paramNames = ArrayList<String>()
            val var2 = maps.entries.iterator()

            while (var2.hasNext()) {
                val entry = var2.next() as Map.Entry<String, String>
                paramNames.add(entry.key)
            }

            return paramNames
        }

        private fun lexicographicOrder(paramNames: List<String>): List<String> {
            Collections.sort(paramNames)
            return paramNames
        }

        private fun splitParams(paramNames: List<String>, maps: Map<String, Any>): String {
            val paramStr = StringBuilder()
            val var3 = paramNames.iterator()

            while (var3.hasNext()) {
                val paramName = var3.next()
                paramStr.append(paramName)
                val var5 = maps.entries.iterator()

                while (var5.hasNext()) {
                    val entry = var5.next() as Map.Entry<*, *>
                    if (paramName == entry.key) {
                        paramStr.append("=" + entry.value.toString() + "&")
                    }
                }
            }

            paramStr.deleteCharAt(paramStr.length - 1)
            return paramStr.toString()
        }
    }
}
