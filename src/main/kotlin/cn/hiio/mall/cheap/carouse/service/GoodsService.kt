package cn.hiio.mall.cheap.carouse.service

import cn.hiio.mall.cheap.carouse.vo.ProductVo
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.annotation.Resource

@Service
class GoodsService : Logging {
    @Resource
    lateinit var stringRedisTemplate: RedisTemplate<String, String>

    @Resource
    lateinit var dataokeService: DataokeService

    val RECOMMEND_GOODS_REDIS_KEY = "recommend_goods"
    val GOODS_PRIVILEGE_LINK_KEY = "goods_privilege_link_key"
    val GOODS_DETAIL_KEY = "goods_detail_key"

    fun recommendGoods(page: Int, size: Int): JSONObject {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val cachedRecommendGoods = operations[getRecommendGoodsKey(page)]
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val params: MutableMap<String, String> = HashMap()
            params["pageId"] = page.toString()
            params["pageSize"] = size.toString()
            params["PriceCid"] = "3"
            val data: JSONObject = dataokeService.getGoodsList(params)
            operations.set(getRecommendGoodsKey(page), data.toJSONString(), 2, TimeUnit.HOURS)
            return data
        }
        return JSON.parseObject(cachedRecommendGoods)
    }

    private fun getRecommendGoodsKey(page: Int): String {
        return RECOMMEND_GOODS_REDIS_KEY + "_page_" + page
    }


    fun loadCate(): JSONArray {
        return dataokeService.loadCate()
    }

    fun getHotSearch(): List<Any> {
        return dataokeService.loadHotSearch()
    }

    fun search2(keyword: String, page: Int, sort: String, hasCoupon: String): List<ProductVo> {
        val array: JSONArray = dataokeService.search(keyword, page, sort, hasCoupon)
        return if (CollectionUtils.isEmpty(array)) {
            ArrayList<ProductVo>()
        } else {
            array.stream().map<ProductVo> { o: Any ->
                val jb = o as JSONObject
                val productVo = ProductVo()
                productVo.goodsId = (jb.getString("item_id"))
                productVo.mainPic = (jb.getString("pict_url"))
                productVo.shopType = (jb.getIntValue("user_type"))
                productVo.title = (jb.getString("title"))
                productVo.shopName = (jb.getString("shop_title"))
                if (jb.getDoubleValue("coupon_amount") > 0) {
                    productVo.actualPrice = (
                            jb.getBigDecimal("zk_final_price")
                                    .subtract(jb.getBigDecimal("coupon_amount"))
                                    .toDouble())
                } else {
                    productVo.actualPrice = (jb.getDoubleValue("zk_final_price"))
                }
                productVo.couponPrice = (jb.getDoubleValue("coupon_amount"))
                productVo.monthSales = (jb.getIntValue("volume"))
                productVo.couponReceiveNum = (jb.getIntValue("coupon_total_count") - jb.getIntValue("coupon_remain_count"))
                productVo
            }.collect(Collectors.toList())
        }
    }


    fun getPrivilegeLink(goodsId: String): Map<String, String> {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val redisKey = getPrivilegeLinkKey(goodsId)
        val cachedRecommendGoods = operations[redisKey]
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val result: JSONObject = dataokeService.getPrivilegeLink(goodsId)
            val map: MutableMap<String, String> = java.util.HashMap()
            map["tpwd"] = result.getString("tpwd")
            map["couponClickUrl"] = result.getString("couponClickUrl")
            map["itemUrl"] = result.getString("itemUrl")

            //todo: 审核代码
//        map.put("audit", "1");
//        map.put("tpwdTxt", "点击复制，在浏览器中打开链接，即可购买");
//        map.put("tpwdValue", result.getString("couponClickUrl"));
//        map.put("tpwdTip", "复制成功");
            //正式代码
            map["audit"] = "0"
            map["tpwdTxt"] = result.getString("longTpwd")
            map["tpwdValue"] = result.getString("longTpwd")
            map["tpwdTip"] = "口令复制成功，请打开手机淘宝"
            operations.set(redisKey, JSON.toJSONString(map), 2, TimeUnit.HOURS)
            return map
        }
        return JSON.parseObject(cachedRecommendGoods, HashMap<String, String>().javaClass!!)


    }

    private fun getPrivilegeLinkKey(goodsId: String): String {
        return GOODS_PRIVILEGE_LINK_KEY + "_" + goodsId
    }

    fun goodsDetail(goodsId: String, checkFavorite: Boolean): ProductVo {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val redisKey = GOODS_DETAIL_KEY + "_" + goodsId
        val cachedRecommendGoods = operations[redisKey]
        if (StringUtils.isEmpty(cachedRecommendGoods)) {
            val goodsDetail: JSONObject = dataokeService.loadGoodsDetail(goodsId)
            val productVo: ProductVo = buildProductVo(goodsDetail)
            operations.set(redisKey, JSON.toJSONString(productVo), 2, TimeUnit.HOURS)
            return productVo
        }
        return JSON.parseObject(cachedRecommendGoods, ProductVo().javaClass!!)
//        val currentUserId: Long = SecurityUtils.getCurrentUserId()
//        if (currentUserId != null) {
//            //是否已收藏
//            val collection: Collection = collectionRepository.findByUserIdAndDataIdAndDataType(currentUserId, goodsId, DataType.Product.name())
//            productVo.setFavorite(collection != null)
//        }
//        return productVo
    }

    private fun buildProductVo(goodsDetail: JSONObject): ProductVo {
        var productVo = ProductVo()

        productVo.id = (goodsDetail.getLong("id"))
        productVo.title = (goodsDetail.getString("title"))
        productVo.actualPrice = (goodsDetail.getDouble("actualPrice"))
        productVo.originalPrice = (goodsDetail.getDouble("originalPrice"))
        productVo.monthSales = (goodsDetail.getInteger("monthSales"))
        productVo.dailySales = (goodsDetail.getInteger("dailySales"))
        productVo.couponReceiveNum = (goodsDetail.getInteger("couponReceiveNum"))
        productVo.couponPrice = (goodsDetail.getDouble("couponPrice"))
        productVo.couponStartTime = (goodsDetail.getString("couponStartTime"))
        productVo.couponEndTime = (goodsDetail.getString("couponEndTime"))
        productVo.desc = (goodsDetail.getString("desc"))
        productVo.shopName = (goodsDetail.getString("shopName"))
        productVo.descScore = (goodsDetail.getDouble("descScore"))
        productVo.serviceScore = (goodsDetail.getDouble("serviceScore"))
        productVo.shipScore = (goodsDetail.getDouble("shipScore"))
        productVo.mainPic = (goodsDetail.getString("mainPic"))
        productVo.goodsId = (goodsDetail.getString("goodsId"))
        productVo.imgs = (goodsDetail.getString("imgs"))
        productVo.marketingMainPic = (goodsDetail.getString("marketingMainPic"))

        return productVo
    }


    fun similarGoods(daTaoKeGoodsId: String): JSONArray {
        return dataokeService.loadSimilarGoods(daTaoKeGoodsId, 20)
    }


    fun loadGoodsByCate(subcid: String, page: Int, size: Int, sort: Int): JSONObject {
        val operations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val key = "goods_cate_subcid_page_sort_"+subcid+"_"+page+"_"+sort
        val categoryGoodsListStr = operations.get(key)
        if (StringUtils.isEmpty(categoryGoodsListStr)) {
            val params: MutableMap<String, String> = java.util.HashMap()
            params["subcid"] = subcid
            params["pageId"] = page.toString()
            params["pageSize"] = size.toString()
            params["sort"] = sort.toString()
            val data = dataokeService.loadGoods(params)
            operations.set(key, data.toJSONString(), 6, TimeUnit.HOURS)
            return data
        }else{
            return JSON.parseObject(categoryGoodsListStr)
        }

    }

    fun rankGoods(cid: String): JSONArray {
        return dataokeService.loadRankGoods(cid)
    }

    fun nineGoods(page: Int, size: Int, nineCid: String): JSONObject {
        return dataokeService.loadNineGoods(page, size, nineCid)
    }


    fun getActiveGoodsList(page: Int, size: Int, activeId: String): JSONArray {
        return dataokeService.loadActiveGoods(page, size, activeId);
    }

    fun getHalfGoodsList(): JSONObject {
        return dataokeService.getHalfGoodsList();
    }
    fun getDongdongGoodsList(): JSONArray {
        return dataokeService.getDongdongGoodsList();
    }
}