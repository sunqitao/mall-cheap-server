package cn.hiio.mall.cheap.common.utils

import cn.hiio.mall.cheap.admin.vo.AdminUserVo
import cn.hiio.mall.cheap.common.model.ResultVo
import cn.hiio.mall.cheap.person.vo.UserVo
import com.alibaba.fastjson.JSON
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.crypto.Data

//详情request
fun <T> out(response: HttpServletResponse, data : ResultVo<T>){
    setResponseHeaders(response)
    val out = response.writer
    out.print(JSON.toJSONString(data))
    out.flush()
}
fun<T> out(data:T):String{
    return JSON.toJSONString(ResultVo(data = data))
}
fun<T> out(data:T,attribute:Map<String,Any>):String{
    return JSON.toJSONString(ResultVo(data = data,attribute =attribute))
}
fun<T> out(data:T,total:Long):String{
    var attribute = mapOf<String,Any>("total" to total)
    return JSON.toJSONString(ResultVo(data = data,attribute =attribute))
}

val AUTHED_USER_KEY = "__AUTHED_USER__"
fun setAuthUser(request: HttpServletRequest, userVo: UserVo){
    request.setAttribute(AUTHED_USER_KEY,userVo)
}
fun getAuthUser(request: HttpServletRequest):UserVo{
    return request.getAttribute(AUTHED_USER_KEY) as UserVo
}


val ADMIN_AUTHED_USER_KEY = "__ADMIN_AUTHED_USER__"
fun setAdminAuthUser(request: HttpServletRequest, adminUserVo: AdminUserVo){
    request.setAttribute(ADMIN_AUTHED_USER_KEY,adminUserVo)
}
fun getAdminAuthUser(request: HttpServletRequest):AdminUserVo{
    return request.getAttribute(ADMIN_AUTHED_USER_KEY) as AdminUserVo
}

//暂时无用
fun setResponseHeaders(response: HttpServletResponse){
    response.setCharacterEncoding("utf-8")
    if (response.getContentType()!=null && response.getContentType().length>0){
        response.setContentType("application/json")
    }
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
    response.setHeader("Pragma", "no-cache")
    response.setDateHeader("Expires", 3600L)
    response.setHeader("connection", "close")
    response.setHeader("Access-Control-Max-Age", "3600")
    response.setHeader("Access-Control-Allow-Origin", "*")
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
    response.addHeader("Access-Control-Allow-Headers", "access-control-allow-origin, x-requested-with, content-type, Accept, x-xsrf-token, Authorization,ACCESSTOKEN, admintoken,X-Auth-Token")
    response.setStatus(200)
}

fun get(url:String):String{
    val client = HttpClientBuilder.create().build()
    val get = HttpGet(url)
    val res = client.execute(get!!)
    val entity = res.entity
    var responseContent = EntityUtils.toString(entity, "UTF-8")
    return responseContent
}

/**
 * Post String
 *
 * @param host
 * @param path
 * @param method
 * @param headers
 * @param querys
 * @param body
 * @return
 * @throws Exception
 */
@Throws(Exception::class)
fun post(host: String, path: String?, method: String?,
           headers: Map<String, String>?,
           querys: Map<String, String>?,
           body: String?): HttpResponse {
    val httpClient: HttpClient = wrapClient(host)
    val request = HttpPost(buildUrl(host, path, querys))
    if (null != headers) for ((key, value) in headers) {
        request.addHeader(key, value)
    }
    if (StringUtil.hasText(body)) {
        request.entity = StringEntity(body, "utf-8")
    }
    return httpClient.execute(request)
}

private fun wrapClient(host: String): HttpClient {
    val httpClient: HttpClient = DefaultHttpClient()
    if (host.startsWith("https://")) {
        sslClient(httpClient)
    }
    return httpClient
}


private  fun sslClient(httpClient: HttpClient) {
    try {
        val ctx = SSLContext.getInstance("TLS")
        val tm: X509TrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(xcs: Array<X509Certificate>, str: String) {}
            override fun checkServerTrusted(xcs: Array<X509Certificate>, str: String) {}
        }
        ctx.init(null, arrayOf<TrustManager>(tm), null)
        val ssf = SSLSocketFactory(ctx)
        ssf.hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        val ccm = httpClient.connectionManager
        val registry = ccm.schemeRegistry
        registry.register(Scheme("https", 443, ssf))
    } catch (ex: KeyManagementException) {
        throw RuntimeException(ex)
    } catch (ex: NoSuchAlgorithmException) {
        throw RuntimeException(ex)
    }
}


@Throws(UnsupportedEncodingException::class)
private  fun buildUrl(host: String, path: String?, querys: Map<String, String>?): String? {
    val sbUrl = StringBuilder()
    sbUrl.append(host)
    if (!StringUtil.isBlank(path)) {
        sbUrl.append(path)
    }
    if (null != querys) {
        val sbQuery = StringBuilder()
        for ((key, value) in querys) {
            if (0 < sbQuery.length) {
                sbQuery.append("&")
            }
            if (StringUtil.isBlank(key) && !StringUtil.isBlank(value)) {
                sbQuery.append(value)
            }
            if (!StringUtil.isBlank(key)) {
                sbQuery.append(key)
                if (!StringUtil.isBlank(value)) {
                    sbQuery.append("=")
                    sbQuery.append(URLEncoder.encode(value, "utf-8"))
                }
            }
        }
        if (0 < sbQuery.length) {
            sbUrl.append("?").append(sbQuery)
        }
    }
    return sbUrl.toString()
}
//return string
fun Any.toResJsonString():String{
    return JSON.toJSONString(ResultVo<Any>(data = this))
}
fun toSuccessJsonString():String{
    return JSON.toJSONString(ResultVo<Any>(data = null))
}