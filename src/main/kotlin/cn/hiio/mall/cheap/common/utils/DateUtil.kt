package cn.hiio.mall.cheap.common.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


enum class DateUtil {
    YEAR,MONTH,DATE;
    fun parseType():Int{
        var value = Calendar.DATE
        when(this){
            YEAR -> value = Calendar.DATE
            MONTH -> value = Calendar.MONTH
            DATE ->  value = Calendar.DATE
        }
        return value
    }
}

class DateFormatType{
    companion object{
        val PATTERN_YYYY_MM_DD = "yyyy-MM-dd"
        val PATTERN_YYYY_MM_DD_P = "yyyy.MM.dd"
        val PATTERN_YYYYMMDD = "yyyyMMdd"
        val PATTERN_YYYYMMDD_X = "yyyy/MM/dd"
        val PATTERN_MM_DD = "MM-dd"
        val PATTERN_MM_DD_HH_MM = "MM-dd HH:mm"
        val PATTERN_UNIT_YYYY_MM_DD = "yyyy年-MM月-dd日"
        val PATTERN_NEW_YYYY_MM_DD = "yyyy年MM月dd日"
        val PATTERN_UNIT_MM_DD = "MM月-dd日"
        val PATTERN_UNIT_MMDD = "MM月dd日"
        val PATTERN_MM_DD_HH_DD_ZH = "MM月dd日HH时mm分"
        val PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        val PATTERN_YYYY_MM_DD_HH_MM_SS_P = "yyyy.MM.dd HH:mm:ss"
        val PATTERN_HH_MM_SS = "HH:mm:ss"
        val PATTERN_HH_MM = "HH:mm"
        val PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
        val PATTERN_YYYY_MM_DD_HH = "yyyy-MM-dd HH"
        val PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS"
        val UNIT_SECOND_TIME = 1000L
        val UNIT_MINUS_TIME = 60000L
        val UNIT_HOUR_TIME = 3600000L
        val UNIT_DAY_TIME = 86400000L
        val UNIT_SECOND_NAME = "秒"
        val UNIT_MINUS_NAME = "分钟"
        val UNIT_HOUR_NAME = "小时"
        val UNIT_DAY_NAME = "天"
        val PATTERN_MM_DD_HH = "MM月dd日HH点"

        /**
         * 根据特定模式，将字符串型日期对象解析成Date对象
         * source 要解析的字符串, pattern 解析模式，默认为{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}
         * 返回 解析结果
         * ParseException 如果要解析的字符串格式不匹配，则抛出此异常
         */
        fun parse(source: String, pattern: String?): Date? { // 检查value是否为空
            var pattern = pattern
            if (source == null || StringUtil.isEmpty(source)) {
                return null
            }
            // 如果pattern为空
            if (pattern == null) { // 设置pattern为PATTERN_YYY_MM_DD_HH_MM_SS
                pattern = PATTERN_YYYY_MM_DD
            }
            // 初始化一个format类
            val format = SimpleDateFormat(pattern)
            // 开始解析
            return format.parse(source)
        }
    }
}

data class DateOperator(val unit :DateUtil,val value: Int)

fun Any.year(value:Int):DateOperator {
    return DateOperator(DateUtil.YEAR,value)
}

fun Any.month(value:Int):DateOperator {
    return DateOperator(DateUtil.MONTH,value)
}

fun Any.day(value:Int):DateOperator {
    return DateOperator(DateUtil.DATE,value)
}



/**
 * date+1
 * 往后的几天
 */
operator fun Date.plus(nextVal:Int): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.DATE, nextVal)
    return calendar.time
}

/**
 * date-1
 */
operator fun Date.minus(nextVal:Int): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.DATE, nextVal*-1)
    return calendar.time
}

/**
 * date+year(3)
 * 往后的几天
 */
operator fun Date.plus(nextVal:DateOperator): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(nextVal.unit.parseType(), nextVal.value)
    return calendar.time
}

/**
 * date-month(4)
 */
operator fun Date.minus(nextVal:DateOperator): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(nextVal.unit.parseType(), nextVal.value*-1)
    return calendar.time
}

/**
 * 得到月末
 */
operator fun Date.inc(): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, 0);
    return calendar.time
}

/**
 * 得到月初
 */
operator fun Date.dec(): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

/**
 * 取 年月日时分秒 0 - 5
 * 例如 2020-12-21 22:15:56
 * date[0]:2020  date[1]:12 date[2]:21
 */
operator fun Date.get(position:Int):Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    var value = 0
    when(position) {
        0 -> value = calendar.get(Calendar.YEAR)
        1 -> value = calendar.get(Calendar.MONTH)+1
        2 -> value = calendar.get(Calendar.DAY_OF_MONTH)
        3 -> value = calendar.get(Calendar.HOUR)
        4 -> value = calendar.get(Calendar.MINUTE)
        5 -> value = calendar.get(Calendar.SECOND)
    }
    return value
}

/**
 * 比较2个日期
 * if(date1 > date2) {
 * }
 */

operator fun Date.compareTo(compareDate : Date):Int {
    return (time - compareDate.time).toInt()
}

/**
 * 日期转化为字符串
 */
fun Date.stringFormat(formatType:String):String{
    return SimpleDateFormat(formatType).format(this)
}
