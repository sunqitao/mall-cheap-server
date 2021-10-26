package cn.hiio.mall.cheap.common.constants

open class CacheKeys{
    var key:String
    var db:Int
    constructor(key: String,db: Int){
        this.key = key
        this.db = db
    }
}
fun keyLink(cacheKeys: CacheKeys, linkStr: String): CacheKeys {
    return CacheKeys(cacheKeys.key+linkStr,cacheKeys.db)
}