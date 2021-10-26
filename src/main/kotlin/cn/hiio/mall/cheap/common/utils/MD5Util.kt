package cn.hiio.mall.cheap.common.utils

import java.security.MessageDigest

class MD5Util {

    companion object{
        private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
        private var md: MessageDigest? = null

        init {
            try {
                md = MessageDigest.getInstance("MD5")
            } catch (e: Exception) {
            }

        }

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

        fun MD5Encoding(origin: String): String {
            var  resultString = byteArrayToHexString(md!!.digest(origin.toByteArray()))
            return resultString
        }
    }

}
