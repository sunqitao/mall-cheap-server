package cn.hiio.mall.cheap.weixin.enums

import cn.hiio.mall.cheap.common.constants.CacheKeys


class WeixinCacheKeys(key: String,db: Int) : CacheKeys(key,db) {
    companion object{
        val WEIXIN_ACCESS_TOKEN = WeixinCacheKeys("weixin_access_token", 1)
        val WEIXIN_ACCESS_TOKEN_TICKET = WeixinCacheKeys("weixin_access_token_ticket_", 1)
    }
}