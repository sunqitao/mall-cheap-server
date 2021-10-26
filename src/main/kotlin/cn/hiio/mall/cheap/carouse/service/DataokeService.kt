package cn.hiio.mall.cheap.carouse.service

import cn.hiio.mall.cheap.carouse.properties.DaTaoKeProperties
import cn.hiio.mall.cheap.common.utils.DateFormatType
import cn.hiio.mall.cheap.common.utils.DateUtil
import cn.hiio.mall.cheap.common.utils.stringFormat
import cn.hiio.mall.cheap.third.dataoke.ApiClient
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.google.gson.JsonObject
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service
class DataokeService : Logging {
    val goodsListurl = "https://openapi.dataoke.com/api/goods/explosive-goods-list"
    val categoryurl = "https://openapi.dataoke.com/api/category/get-super-category";
    val hotsearchurl = "https://openapi.dataoke.com/api/category/get-top100";
    val searchurl = "https://openapi.dataoke.com/api/tb-service/get-tb-service";
    val dataokesearchurl = "https://openapi.dataoke.com/api/goods/get-dtk-search-goods";
    val supersearchurl = "https://openapi.dataoke.com/api/goods/list-super-goods";
    val privilegechurl = "https://openapi.dataoke.com/api/tb-service/get-privilege-link";
    val goodsDetailurl = "https://openapi.dataoke.com/api/goods/get-goods-details";
    val similergoodsurl = "https://openapi.dataoke.com/api/goods/list-similer-goods-by-open";
    val getgoodsListurl = "https://openapi.dataoke.com/api/goods/get-goods-list";
    val rankinglisturl = "https://openapi.dataoke.com/api/goods/get-ranking-list";
    val nineyuanurl = "https://openapi.dataoke.com/api/goods/nine/op-goods-list";

    //活动商品列表
    val activegoodslisturl = "https://openapi.dataoke.com/api/goods/topic/goods-list";
    val carouselisturl = "https://openapi.dataoke.com/api/goods/topic/carouse-list";

    //banner 活动获取 具体页面
    val carouseActiveurl = "https://openapi.dataoke.com/api/tb-service/activity-link";

    //每日半价
    val halfGoodsListurl = "https://openapi.dataoke.com/api/goods/get-half-price-day";

    //咚咚抢
    val dongdongListurl = "https://openapi.dataoke.com/api/category/ddq-goods-list";

    @Autowired
    lateinit var daTaoKeProperties: DaTaoKeProperties

    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

    private fun createParams(): TreeMap<String, String> {
        var params = mapOf<String, String>("appKey" to daTaoKeProperties.appKey!!, "version" to "v2.1.2")
        return TreeMap(params)
    }

    fun getGoodsList(params: Map<String, String>): JSONObject {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val recommendGoods = operations["goods_recommend_list"]
        if (StringUtils.isEmpty(recommendGoods)) {
            val params2: TreeMap<String, String> = createParams()
            params2.putAll(params)
            val body: String = ApiClient.sendReq(goodsListurl, daTaoKeProperties.appSecret!!, params2)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询推荐商品失败,params:$params, body:$body")
                return JSONObject()
            }
            val data = result.getJSONObject("data")
            operations.set("goods_recommend_list",data.toJSONString())
            return data
        }else{
            return JSON.parseObject(recommendGoods)
        }

    }


    fun loadCate(): JSONArray {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations["goods_cate_list"]

        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params = createParams()
            val body = ApiClient.sendReq(categoryurl, daTaoKeProperties.appSecret!!, params)
            val result = JSON.parseObject(body)

            if (result.getInteger("code") != 0) {
                logger.error("查询分类失败, body:$body")
                return JSONArray()
            }
            val data = result.getJSONArray("data")
            operations.set("goods_cate_list", data.toJSONString(), 24, TimeUnit.HOURS)
            return data
        } else {
            return JSON.parseArray(cachedRecommendGoods)
        }

    }

    fun loadHotSearch(): List<Any> {
        val params = createParams()
        val body = ApiClient.sendReq(hotsearchurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("热搜, body:$body")
            return ArrayList()
        }
        val array = result.getJSONObject("data").getJSONArray("hotWords")
        return array.subList(0, 10)
    }


    fun search(keyword: String, page: Int, sort: String, hasCoupon: String): JSONArray {
        val params = createParams()
        params["pageNo"] = page.toString()
        params["pageSize"] = "100"
        params["keyWords"] = keyword
        if(sort==null||sort.length<1){
            params["sort"] = "tk_total_sales_des"
        }else{
            params["sort"] = sort
        }

        params["hasCoupon"] = hasCoupon

        //为了应对 苹果审核 ，目前苹果审核规定，平台商品不能是 充值第三方的闪屏
//        if ((keyword.contains("充", ignoreCase = true) || keyword.contains("交", ignoreCase = true)
//                    || keyword.contains("缴", ignoreCase = true) || keyword.contains("机", ignoreCase = true)
//                    || keyword.contains("会员", ignoreCase = true)
//                    || keyword.contains("腾讯", ignoreCase = true)
//                    || keyword.contains("优酷", ignoreCase = true)
//                    || keyword.contains("爱奇艺", ignoreCase = true)
//                    || keyword.contains("芒果", ignoreCase = true)
//                    || keyword.contains("tv", ignoreCase = true)
//                    )
//            ||
//            (keyword.contains("值", ignoreCase = true) || keyword.contains("费", ignoreCase = true)
//                    || keyword.contains("卡", ignoreCase = true)
//                    )
//        ) {
//            var newkeyword = keyword
//                .replace("充", "", ignoreCase = true)
//                .replace("值", "", ignoreCase = true)
//                .replace("费", "", ignoreCase = true)
//                .replace("缴", "", ignoreCase = true)
//                .replace("机", "", ignoreCase = true)
//                .replace("卡", "", ignoreCase = true)
//                .replace("会员", "", ignoreCase = true)
//                .replace("腾讯", "", ignoreCase = true)
//                .replace("优酷", "", ignoreCase = true)
//                .replace("爱奇艺", "", ignoreCase = true)
//                .replace("芒果", "", ignoreCase = true)
//                .replace("tv", "", ignoreCase = true)
//            params["keyWords"] = newkeyword
//        }



        if (params["keyWords"].equals("")) {
            params["keyWords"] = "热销"
        }



        val body = ApiClient.sendReq(searchurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)

        if (result.getInteger("code") != 0) {
            logger.error("TaobaoSearchApi, body:$body,params:$params")
            return JSONArray()
        }
        val nowJsonArray = result.getJSONArray("data")
        var rejsonArray = JSONArray();
        for (i in 0..(nowJsonArray.size - 1)) {
            var obj: JSONObject = nowJsonArray.get(i) as JSONObject
            var obj_name = obj.getString("title")
//            此处也是为了应对苹果上架审核 临时使用
//            if (obj_name.contains("充值") || obj_name.contains("续费") || obj_name.contains("会员") || obj_name.contains(
//                    "vip",
//                    ignoreCase = true
//                )
//                || obj_name.contains("月卡") || obj_name.contains("年卡") || obj_name.contains("网盘") || obj_name.contains("云盘")
//                || obj_name.contains("流量") || obj_name.contains("话费") || obj_name.contains("校园卡") || obj_name.contains("全国")
//                || obj_name.contains("联通") || obj_name.contains("电信") || obj_name.contains("移动")|| obj_name.contains("卡")
//            ) {
//            } else
//            {
                rejsonArray.add(obj)
//            }
        }
        return rejsonArray
    }



    fun superSearch(keyword: String, page: Int, sort: String, hasCoupon: String): JSONArray {
        val params = createParams()
        params["type"] = "0"
        params["pageId"] = page.toString()
        params["pageSize"] = "10"
        params["keyWords"] = keyword
        if(sort==null||sort.length<1){
            params["sort"] = "tk_total_sales_des"
        }else{
            params["sort"] = sort
        }
        if(hasCoupon!=null && hasCoupon!="null" && hasCoupon.length>0){
            if(hasCoupon.toBoolean()){
                params["hasCoupon"] = "1"
            }else{
            }
        }


        //为了应对 苹果审核 ，目前苹果审核规定，平台商品不能是 充值第三方的闪屏
//        if ((keyword.contains("充", ignoreCase = true) || keyword.contains("交", ignoreCase = true)
//                    || keyword.contains("缴", ignoreCase = true) || keyword.contains("机", ignoreCase = true)
//                    || keyword.contains("会员", ignoreCase = true)
//                    || keyword.contains("腾讯", ignoreCase = true)
//                    || keyword.contains("优酷", ignoreCase = true)
//                    || keyword.contains("爱奇艺", ignoreCase = true)
//                    || keyword.contains("芒果", ignoreCase = true)
//                    || keyword.contains("tv", ignoreCase = true)
//                    )
//            ||
//            (keyword.contains("值", ignoreCase = true) || keyword.contains("费", ignoreCase = true)
//                    || keyword.contains("卡", ignoreCase = true)
//                    )
//        ) {
//            var newkeyword = keyword
//                .replace("充", "", ignoreCase = true)
//                .replace("值", "", ignoreCase = true)
//                .replace("费", "", ignoreCase = true)
//                .replace("缴", "", ignoreCase = true)
//                .replace("机", "", ignoreCase = true)
//                .replace("卡", "", ignoreCase = true)
//                .replace("会员", "", ignoreCase = true)
//                .replace("腾讯", "", ignoreCase = true)
//                .replace("优酷", "", ignoreCase = true)
//                .replace("爱奇艺", "", ignoreCase = true)
//                .replace("芒果", "", ignoreCase = true)
//                .replace("tv", "", ignoreCase = true)
//            params["keyWords"] = newkeyword
//        }



        if (params["keyWords"].equals("")) {
            params["keyWords"] = "热销"
        }



        val body = ApiClient.sendReq(supersearchurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)

        if (result.getInteger("code") != 0) {
            logger.error("TaobaoSearchApi, body:$body,params:$params")
            return JSONArray()
        }
        val nowJsonArray = result.getJSONObject("data").getJSONArray("list")
        var rejsonArray = JSONArray();
        for (i in 0..(nowJsonArray.size - 1)) {
            var obj: JSONObject = nowJsonArray.get(i) as JSONObject
            var obj_name = obj.getString("title")
            //此处也是为了应对苹果上架审核 临时使用
//            if (obj_name.contains("充值") || obj_name.contains("续费") || obj_name.contains("会员") || obj_name.contains(
//                    "vip",
//                    ignoreCase = true
//                )
//                || obj_name.contains("月卡") || obj_name.contains("年卡") || obj_name.contains("网盘") || obj_name.contains("云盘")
//                || obj_name.contains("流量") || obj_name.contains("话费") || obj_name.contains("校园卡") || obj_name.contains("全国")
//                || obj_name.contains("联通") || obj_name.contains("电信") || obj_name.contains("移动")
//            ) {
//            } else
//            {
                rejsonArray.add(obj)
//            }
        }
        return rejsonArray
    }



    fun searchPDD(keyword: String, page: Int, sort: String, hasCoupon: String): JSONArray {
        val params = createParams()
        params["type"] = "0"
        params["pageId"] = page.toString()
        params["pageSize"] = "100"
        params["keyWords"] = keyword
        if(sort==null||sort.length<1){
            params["sort"] = "tk_total_sales_des"
        }else{
            params["sort"] = sort
        }

        params["hasCoupon"] = hasCoupon

        //为了应对 苹果审核 ，目前苹果审核规定，平台商品不能是 充值第三方的闪屏
//        if ((keyword.contains("充", ignoreCase = true) || keyword.contains("交", ignoreCase = true)
//                    || keyword.contains("缴", ignoreCase = true) || keyword.contains("机", ignoreCase = true)
//                    || keyword.contains("会员", ignoreCase = true)
//                    || keyword.contains("腾讯", ignoreCase = true)
//                    || keyword.contains("优酷", ignoreCase = true)
//                    || keyword.contains("爱奇艺", ignoreCase = true)
//                    || keyword.contains("芒果", ignoreCase = true)
//                    || keyword.contains("tv", ignoreCase = true)
//                    )
//            ||
//            (keyword.contains("值", ignoreCase = true) || keyword.contains("费", ignoreCase = true)
//                    || keyword.contains("卡", ignoreCase = true)
//                    )
//        ) {
//            var newkeyword = keyword
//                .replace("充", "", ignoreCase = true)
//                .replace("值", "", ignoreCase = true)
//                .replace("费", "", ignoreCase = true)
//                .replace("缴", "", ignoreCase = true)
//                .replace("机", "", ignoreCase = true)
//                .replace("卡", "", ignoreCase = true)
//                .replace("会员", "", ignoreCase = true)
//                .replace("腾讯", "", ignoreCase = true)
//                .replace("优酷", "", ignoreCase = true)
//                .replace("爱奇艺", "", ignoreCase = true)
//                .replace("芒果", "", ignoreCase = true)
//                .replace("tv", "", ignoreCase = true)
//            params["keyWords"] = newkeyword
//        }



        if (params["keyWords"].equals("")) {
            params["keyWords"] = "热销"
        }



        val body = ApiClient.sendReq(searchurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)

        if (result.getInteger("code") != 0) {
            logger.error("TaobaoSearchApi, body:$body,params:$params")
            return JSONArray()
        }
        val nowJsonArray = result.getJSONArray("data")
        var rejsonArray = JSONArray();
        for (i in 0..(nowJsonArray.size - 1)) {
            var obj: JSONObject = nowJsonArray.get(i) as JSONObject
            var obj_name = obj.getString("title")
            //此处也是为了应对苹果上架审核 临时使用
//            if (obj_name.contains("充值") || obj_name.contains("续费") || obj_name.contains("会员") || obj_name.contains(
//                    "vip",
//                    ignoreCase = true
//                )
//                || obj_name.contains("月卡") || obj_name.contains("年卡") || obj_name.contains("网盘") || obj_name.contains("云盘")
//                || obj_name.contains("流量") || obj_name.contains("话费") || obj_name.contains("校园卡") || obj_name.contains("全国")
//                || obj_name.contains("联通") || obj_name.contains("电信") || obj_name.contains("移动")
//            ) {
//            } else
//            {
                rejsonArray.add(obj)
//            }
        }
        return rejsonArray
    }


    fun getPrivilegeLink(goodsId: String): JSONObject {
        val params = createParams()
        params["pid"] = "mm_25673488_1498550237_110221050495"
        params["goodsId"] = goodsId
        val body = ApiClient.sendReq(privilegechurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("转换链接失败, body:$body")
            return JSONObject()
        }
        return result.getJSONObject("data")
    }

    fun loadGoodsDetail(tbGoodsId: String): JSONObject {
        val params = createParams()
//        if (StringUtils.isNotEmpty(id)) {
//            params["id"] = id
//        }
        params["goodsId"] = tbGoodsId

        val body = ApiClient.sendReq(goodsDetailurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("查询商品详情失败, body:$body")
            return JSONObject()
        }
        return result.getJSONObject("data")
    }


    fun loadSimilarGoods(daTaoKeGoodsId: String, size: Int): JSONArray {
        val params = createParams()
        params["id"] = daTaoKeGoodsId
        params["size"] = size.toString()
        val body = ApiClient.sendReq(similergoodsurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("查询相似商品失败, body:$body")
            return JSONArray()
        }
        return result.getJSONArray("data")
    }

    fun loadGoods(params: Map<String, String>?): JSONObject {
        val params2 = createParams()
        params2.putAll(params!!)
        val body = ApiClient.sendReq(getgoodsListurl, daTaoKeProperties.appSecret!!, params2)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("查询分类失败, body:$body")
            return JSONObject()
        }
        return result.getJSONObject("data")
    }

    fun loadRankGoods(cid: String): JSONArray {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations.get("rank_goods_list")
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params = createParams()
            params["rankType"] = "1"
            params["cid"] = cid
            val body = ApiClient.sendReq(rankinglisturl, daTaoKeProperties.appSecret!!, params)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询榜单商品失败, body:$body")
                return JSONArray()
            }
            val  data = result.getJSONArray("data")
            operations.set("rank_goods_list", data.toJSONString(), 5, TimeUnit.HOURS)
            return data
        }else{
            return JSON.parseArray(cachedRecommendGoods)
        }

    }

    fun loadNineGoods(page: Int, size: Int, nineCid: String): JSONObject {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations.get("nine_goods_list")
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params = createParams()
            params["pageId"] = page.toString()
            params["pageSize"] = size.toString()
            params["nineCid"] = nineCid
            val body = ApiClient.sendReq(nineyuanurl, daTaoKeProperties.appSecret!!, params)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询9.9商品失败, body:$body")
                return JSONObject()
            }
            val data = result.getJSONObject("data")
            operations.set("nine_goods_list", data.toJSONString(), 5, TimeUnit.HOURS)
            return data
        }else{
            return JSON.parseObject(cachedRecommendGoods)
        }

    }

    fun loadBanners(): JSONArray {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations.get("home_banners_list")
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params = createParams()
            val body = ApiClient.sendReq(carouselisturl, daTaoKeProperties.appSecret!!, params)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询轮播图失败, body:$body")
                return JSONArray()
            }
            val data = result.getJSONArray("data")
            operations.set("home_banners_list", data.toJSONString(), 5, TimeUnit.HOURS)
            return data
        } else {
            return JSON.parseArray(cachedRecommendGoods)
        }
    }

    fun loadBannersActivePageUrl(activityId: String): String {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val key = "home_banners_list_page_url_" + activityId
        var pageUrl: String? = operations[key]
        if (pageUrl == null) {

        } else {
            return pageUrl!!;
        }
        val params = createParams()
        params["promotionSceneId"] = activityId
        val body = ApiClient.sendReq(carouseActiveurl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("轮播图查询页面失败, body:$body")
            operations.set(key, "", 5, TimeUnit.HOURS)
            return ""
        }
        val data = result.getJSONObject("data")
        pageUrl = data.getString("click_url")
        operations.set(key, pageUrl, 5, TimeUnit.HOURS)
        return pageUrl
    }

    fun loadActiveGoods(page: Int, size: Int, activeId: String): JSONArray {
        val params = createParams()
        params["pageId"] = page.toString()
        params["pageSize"] = size.toString()
        params["topicId"] = activeId
        val body = ApiClient.sendReq(activegoodslisturl, daTaoKeProperties.appSecret!!, params)
        val result = JSON.parseObject(body)
        if (result.getInteger("code") != 0) {
            logger.error("查询活动商品失败, body:$body")
            return JSONArray()
        }
        return result.getJSONObject("data").getJSONArray("list")
    }


    fun getHalfGoodsList(): JSONObject {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations["home_half_goods_list"]

        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params2: TreeMap<String, String> = createParams()
            val body: String = ApiClient.sendReq(halfGoodsListurl, daTaoKeProperties.appSecret!!, params2)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询推荐商品失败, body:$body")
                return JSONObject()
            }
            val data = result.getJSONObject("data").getJSONObject("halfPriceInfo")
            operations.set("home_half_goods_list", data.toJSONString(), 3, TimeUnit.HOURS)
            return data
        } else {
            return JSON.parseObject(cachedRecommendGoods)
        }


    }

    fun getDongdongGoodsList(): JSONArray {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations["dongdong_goods_list"]
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params2: TreeMap<String, String> = createParams()
            val body: String = ApiClient.sendReq(dongdongListurl, daTaoKeProperties.appSecret!!, params2)
            val result = JSON.parseObject(body)
            if (result.getInteger("code") != 0) {
                logger.error("查询推荐商品失败, body:$body")
                return JSONArray()
            }
            val dataNow = result.getJSONObject("data")
            val roundsList: JSONArray = dataNow.getJSONArray("roundsList")
//        var reJsonObject = JSONObject()
            var reTimeList = JSONArray()
            for (i in 0..(roundsList.size - 1)) {
                val ddqTime = roundsList.getJSONObject(i).getString("ddqTime")
                val oneTime = HashMap<String, Any>()
                val dateHHString = DateFormatType.parse(ddqTime, DateFormatType.PATTERN_YYYY_MM_DD_HH_MM_SS)
                    ?.stringFormat(DateFormatType.PATTERN_HH_MM)
                oneTime["text"] = dateHHString!! + "点"
                oneTime["value"] = dateHHString!! + "点"
                val params3: TreeMap<String, String> = createParams()
                params3["roundTime"] = ddqTime
                val body11: String = ApiClient.sendReq(dongdongListurl, daTaoKeProperties.appSecret!!, params3)
                val result11 = JSON.parseObject(body11)
                oneTime["goodslist"] = result11.getJSONObject("data").getJSONArray("goodsList")
                reTimeList.add(oneTime)
            }
            val data = reTimeList
            if (data.size > 0)
                operations.set("dongdong_goods_list", data.toJSONString(), 8, TimeUnit.HOURS)
            return data
        } else {
            return JSON.parseArray(cachedRecommendGoods)
        }

    }
}


