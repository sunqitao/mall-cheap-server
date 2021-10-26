package cn.hiio.mall.cheap.weixin.utils

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * AES加密
 */
class AES {


    @Throws(InvalidAlgorithmParameterException::class)
    fun decrypt(content: ByteArray, keyByte: ByteArray, ivByte: ByteArray): ByteArray? {
        initialize()
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val sKeySpec = SecretKeySpec(keyByte, "AES")
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte))// 初始化
            return cipher.doFinal(content)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {

        var initialized = false

        fun initialize() {
            if (initialized)
                return
            Security.addProvider(BouncyCastleProvider())
            initialized = true
        }

        // 生成iv
        @Throws(Exception::class)
        fun generateIV(iv: ByteArray): AlgorithmParameters {
            val params = AlgorithmParameters.getInstance("AES")
            params.init(IvParameterSpec(iv))
            return params
        }
    }
}