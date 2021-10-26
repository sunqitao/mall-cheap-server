package cn.hiio.mall.cheap.third.dataoke

import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URISyntaxException
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

class HttpUtil {
    companion object{
        private var cm: PoolingHttpClientConnectionManager? = null
        private val EMPTY_STR = ""
        private val UTF_8 = "UTF-8"
        private fun init() {
            if (cm == null) {
                cm = PoolingHttpClientConnectionManager()
                // 整个连接池最大连接数
                cm!!.maxTotal = 1000
                // 每路由最大连接数，默认值是2
                cm!!.defaultMaxPerRoute = cm!!.maxTotal
            }
        }

        /**
         * 通过连接池获取HttpClient
         *
         * @return
         */
        private fun getHttpClient(): CloseableHttpClient? {
            init()
            return HttpClients.custom()
                    .setRetryHandler { exception, executionCount, context ->
                        if (executionCount >= 1) {
                            // Do not retry if over max retry count
                            false
                        } else true
                    }
                    .setConnectionManager(cm).setConnectionManagerShared(true).build()
        }

        /**
         * @param url
         * @return
         */
        fun httpGetRequest(url: String?): String? {
            val httpGet = HttpGet(url)
            val config = RequestConfig.custom()
                    .setConnectTimeout(5000) //设置连接超时时间
                    .setConnectionRequestTimeout(5000) // 设置请求超时时间
                    .setSocketTimeout(5000)
                    .setRedirectsEnabled(false) //默认允许自动重定向
                    .build()
            httpGet.config = config
            httpGet.setHeader("Accept", "application/json")
            httpGet.addHeader("Accept-Encoding", "gzip") //请求使用数据压缩
            return getResult(httpGet)
        }

        @Throws(URISyntaxException::class)
        fun httpGetRequest(url: String, params: Map<String, String>): String {
            var url = url
            val paramsStr = covertParamsForStr(params)
            url = if (url.contains("?")) url else "$url?"
            val httpGet = HttpGet(url + paramsStr)
            val config = RequestConfig.custom()
                    .setConnectTimeout(5000) //设置连接超时时间
                    .setConnectionRequestTimeout(5000) // 设置请求超时时间
                    .setSocketTimeout(5000)
                    .setRedirectsEnabled(false) //默认允许自动重定向
                    .build()
            httpGet.config = config
            httpGet.setHeader("Accept", "application/json")
            httpGet.addHeader("Accept-Encoding", "gzip") //请求使用数据压缩
            return getResult(httpGet)
        }

        @Throws(URISyntaxException::class)
        fun httpGetRequest(url: String?, headers: Map<String?, Any>, params: Map<String, Any>): String? {
            val ub = URIBuilder()
            ub.path = url
            val pairs = covertParams2NVPS(params)
            ub.setParameters(pairs)
            val httpGet = HttpGet(ub.build())
            for ((key, value) in headers) {
                httpGet.addHeader(key, value.toString())
            }
            return getResult(httpGet)
        }

        fun httpPostRequest(url: String?): String? {
            val httpPost = HttpPost(url)
            return getResult(httpPost)
        }

        fun httpPostRequest(url: String?, json: String?): String? {
            val httpPost = HttpPost(url)
            val entity = StringEntity(json, "utf-8") //解决中文乱码问题
            entity.setContentEncoding("UTF-8")
            entity.setContentType("application/json")
            httpPost.entity = entity
            return getResult(httpPost)
        }

        @Throws(UnsupportedEncodingException::class)
        fun httpPostRequest(url: String?, params: Map<String, Any>): String? {
            val httpPost = HttpPost(url)
            val pairs = covertParams2NVPS(params)
            httpPost.entity = UrlEncodedFormEntity(pairs, UTF_8)
            return getResult(httpPost)
        }

        @Throws(UnsupportedEncodingException::class)
        fun httpPostRequest(url: String?, headers: Map<String?, Any>, params: Map<String, Any>): String? {
            val httpPost = HttpPost(url)
            for ((key, value) in headers) {
                httpPost.addHeader(key, value.toString())
            }
            val pairs = covertParams2NVPS(params)
            httpPost.entity = UrlEncodedFormEntity(pairs, UTF_8)
            return getResult(httpPost)
        }

        private fun covertParams2NVPS(params: Map<String, Any>): ArrayList<NameValuePair> {
            val pairs = ArrayList<NameValuePair>()
            for ((key, value) in params) {
                pairs.add(BasicNameValuePair(key, value.toString()))
            }
            return pairs
        }

        private fun covertParams2NVPSForStr(params: Map<String, String>): ArrayList<NameValuePair>? {
            val pairs = ArrayList<NameValuePair>()
            for ((key, value) in params) {
                try {
                    pairs.add(BasicNameValuePair(key, URLEncoder.encode(value, "utf-8")))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
            return pairs
        }

        private fun covertParamsForStr(paraMap: Map<String, String>): String {
            var paraMap: Map<String, String>? = paraMap
            if (paraMap == null) {
                paraMap = HashMap()
            }
            paraMap = TreeMap(paraMap)
            val sb = StringBuilder()
            paraMap.entries.stream().forEach { entry: Map.Entry<String, String> ->
                sb.append(entry.key)
                sb.append("=")
                try {
                    sb.append(URLEncoder.encode(entry.value, "utf-8"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                sb.append("&")
            }
            return sb.toString()
        }

        /**
         * 处理Http请求
         */
        private fun getResult(request: HttpRequestBase): String {
            val httpClient = getHttpClient()
            var result: String = ""
            try {
                val response = httpClient!!.execute(request)
                val entity = response.entity
                if (entity != null) {
                    result = EntityUtils.toString(entity)
                }
                response.close()
            } catch (e: IOException) {
                //e.printStackTrace();
            } finally {
                try {
                    httpClient?.close()
                } catch (e: Exception) {
                }
            }
            return result
        }
    }

}