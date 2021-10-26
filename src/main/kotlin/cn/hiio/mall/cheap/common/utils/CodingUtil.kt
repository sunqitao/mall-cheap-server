package cn.hiio.mall.cheap.common.utils

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

class CodingUtil {
    companion object{
        val GBK = "GBK"
        val UTF8 = "UTF-8"
        val UTF16 = "UTF-16"
        val UTF16BE = "UTF-16BE"
        val UTF16LE = "UTF-16LE"
        val US_ASCII = "US-ASCII"
        val ISO8859_1 = "ISO-8859-1"
        val CHARSET_GBK = Charset.forName(GBK)
        val CHARSET_UTF8 = Charset.forName(UTF8)
        val CHARSET_ISO8859_1 = Charset.forName(ISO8859_1)

        /**
         * 默认编码
         */
        fun getDefaultEncoding(): String? {
            return Charset.defaultCharset().name()
        }

        /**
         * MD5加密
         * 加密后的结果
         */
        fun MD5Encoding(origin: String?): String? {
            return MD5.MD5Encoding(origin)
        }

        /**
         * Base64编码
         */
        fun base64Encode(input: ByteArray?): String {
            return Base64.getEncoder().encodeToString(input)
        }

        fun base64EncodeToBytes(input: ByteArray?): ByteArray? {
            return Base64.getEncoder().encode(input)
        }

        /**
         * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
         */
//	public static String base64UrlSafeEncode(byte[] input) {
//	  return StringUtils.newStringUtf8(Base64.getEncoder().encodeToString(input, false, true));
//	}

        /**
         * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
         */
        //	public static String base64UrlSafeEncode(byte[] input) {
        //	  return StringUtils.newStringUtf8(Base64.getEncoder().encodeToString(input, false, true));
        //	}
        /**
         * Base64解码.
         */
        fun base64Decode(input: String?): ByteArray? {
            return Base64.getDecoder().decode(input)
        }

        /**
         * URL 编码, Encode默认为UTF-8.
         */
        @Throws(UnsupportedEncodingException::class)
        fun urlEncode(input: String?): String? {
            return URLEncoder.encode(input, UTF8)
        }

        /**
         * URL 解码, Encode默认为UTF-8.
         */
        @Throws(UnsupportedEncodingException::class)
        fun urlDecode(input: String?): String? {
            return if (input == null) null else URLDecoder.decode(input, UTF8)
        }


        /**
         * MD5加密算法
         */
        internal object MD5 {
            private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
            private var md: MessageDigest? = null
            fun byteArrayToHexString(b: ByteArray): String {
                val resultSb = StringBuffer()
                for (i in b.indices) {
                    resultSb.append(byteToHexString(b[i]))
                }
                return resultSb.toString()
            }

            private fun byteToHexString(b: Byte): String {
                var n = b.toInt()
                if (n < 0) {
                    n += 256
                }
                val d1 = n / 16
                val d2 = n % 16
                return hexDigits[d1] + hexDigits[d2]
            }

            fun MD5Encoding(origin: String?): String? {
                var resultString: String? = null
                try {
                    resultString = (origin)
                    resultString = byteArrayToHexString(md!!.digest(resultString!!.toByteArray()))
                } catch (localException: Exception) {
                }
                return resultString
            }

            init {
                try {
                    md = MessageDigest.getInstance("MD5")
                } catch (e: Exception) {
                }
            }
        }
    }


}