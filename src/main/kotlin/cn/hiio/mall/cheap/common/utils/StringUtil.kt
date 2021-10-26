package cn.hiio.mall.cheap.common.utils

import java.io.UnsupportedEncodingException
import java.lang.Character.UnicodeBlock
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

object StringUtil {
    //(http|ftp|https)://[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:/~+#-]*[\w@?^=%&amp;/~+#-])?
//var urlPattern = new Regexp("(http|ftp|https)://[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:/~+#-]*[\w@?^=%&amp;/~+#-])?")
//var urlPattern = /(http|ftp|https):\/\/[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:\/~+#-]*[\w@?^=%&amp;\/~+#-])?/
    const val FOLDER_SEPARATOR = "/"
    const val WINDOWS_FOLDER_SEPARATOR = "\\"
    const val TOP_PATH = ".."
    const val CURRENT_PATH = "."
    const val EXTENSION_SEPARATOR = '.'
    //private static final char CHAR_SPACE = '\u0020';
//private static final char CHAR_CHINESE_SPACE = '\u3000';
    const val PATTERN_PHONE = "0?(13|14|15|16|17|18|19|26)[0-9]{9}"

    fun matchesPhone(phone: String?): Boolean {
        return Pattern.matches(PATTERN_PHONE, phone)
    }

    /**
     * 检查包含空白字符在内的字符系列长度
     *
     * <pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
    </pre> *
     * 参数 str the CharSequence to check (may be `null`)
     * 返回 `true` if the CharSequence is not null and has length
     *
     * @see .hasText
     */
    fun hasLength(str: CharSequence?): Boolean {
        return str != null && str.length > 0
    }

    /**
     * 检查包含空白字符在内的字符系列长度
     * 参数 str the String to check (may be `null`)
     * 返回 `true` if the String is not null and has length
     *
     * @see .hasLength
     */
    fun hasLength(str: String?): Boolean {
        return hasLength(str as CharSequence?)
    }

    /**
     * 检查字符串过滤前后空白后的长度
     * 参数 str
     * 返回 boolean
     */
    fun hasLengthAfterTrimWhiteSpace(str: String?): Boolean {
        return str != null && str.trim { it <= ' ' }.length > 0
    }

    fun hasLengthBytrim(str: String?): Boolean {
        return hasLengthAfterTrimWhiteSpace(str)
    }

    /**
     * Check whether the given CharSequence has actual text.
     * More specifically, returns `true` if the string not `null`,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     *
     * <pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
    </pre> *
     * 参数 str the CharSequence to check (may be `null`)
     * 返回 `true` if the CharSequence is not `null`,
     * its length is greater than 0, and it does not contain whitespace only
     *
     * @see Character.isWhitespace
     */
    fun hasText(str: CharSequence): Boolean {
        if (!hasLength(str)) {
            return false
        }
        val strLen = str.length
        for (i in 0 until strLen) {
            if (!Character.isWhitespace(str[i])) {
                return true
            }
        }
        return false
    }

    /**
     * Check whether the given String has actual text.
     * More specifically, returns `true` if the string not `null`,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * 参数 str the String to check (may be `null`)
     * 返回 `true` if the String is not `null`, its length is
     * greater than 0, and it does not contain whitespace only
     *
     * @see .hasText
     */
    fun hasText(str: String?): Boolean {
        return hasText(str as CharSequence)
    }

    fun length(str: String): Int {
        return if (hasLength(str)) str.length else 0
    }

    /**
     * 根据匹配串是提取占位符中的值
     *
     *
     * 参数 url
     * 原字符串
     * 参数 ref
     * 匹配串
     * 参数 placeholder
     * 占位符
     * 返回 占位符中的值
     *
     *
     * 示例: String ref = "list_*.html"; String url = "xxx/list_2578.html?a=1"; System.out.println(getParamValueInPlaceholder(url, ref, "\\*"));
     */
    fun getParamValueInPlaceholder(url: String?, ref: String, placeholder: String): String? {
        var result: String? = null
        try {
            val refs = ref.split(placeholder.toRegex()).toTypedArray()
            var reg = "^.*" + refs[0] + "(.+?)"
            if (refs.size > 1) {
                reg = "^.*" + refs[0] + "(.+?)" + refs[1] + ".*"
            }
            val p = Pattern.compile(reg)
            val m = p.matcher(url)
            if (m.matches()) {
                result = m.group(1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun getPointBefore(str: String): String {
        return try {
            str.split("\\.".toRegex()).toTypedArray()[0]
        } catch (e: Exception) {
            str
        }
    }

    /**
     * 截串
     *
     *
     * 参数 source
     * 参数 length
     * 参数 fill
     * 返回
     */
    fun getStringByLength(source: String?, length: Int, fill: String): String {
        if (source == null) {
            return ""
        }
        return if (source.length < length + 1) {
            source
        } else source.substring(0, length) + fill
    }

    /**
     * getter方法名
     * 参数 fieldName
     * 返回
     */
    fun getterName(fieldName: String?): String? {
        var fieldName = fieldName
        if (fieldName != null && "" != fieldName) {
            fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
        }
        return fieldName
    }

    /**
     * setter方法名
     * 参数 fieldName
     * 返回
     */
    fun setterName(fieldName: String?): String? {
        var fieldName = fieldName
        if (fieldName != null && "" != fieldName) {
            fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
        }
        return fieldName
    }

    /**
     * 通过getter setter方法名取得字段名
     */
    fun fieldName(getterMethodName: String): String {
        return if (hasLengthAfterTrimWhiteSpace(getterMethodName) && getterMethodName.startsWith("get")) {
            getterMethodName.substring(3, 4).toLowerCase() + getterMethodName.substring(4)
        } else getterMethodName
    }


    /**
     * 把null转化为空字符串
     */
    fun toString(str: String?): String {
        if (str == null) {
            return ""
        }
        if ("null" == str) {
            return ""
        }
        return if (str.length == 0) {
            ""
        } else str.trim { it <= ' ' }
    }

    /**
     * 复制字符串
     *
     *
     * 参数 cs 字符串
     * 参数 num 数量
     * 返回 新字符串
     */
    fun dup(cs: CharSequence, num: Int): String {
        if (isEmpty(cs) || num <= 0) {
            return ""
        }
        val sb = StringBuilder(cs.length * num)
        for (i in 0 until num) {
            sb.append(cs)
        }
        return sb.toString()
    }

    /**
     * 复制字符
     *
     *
     * 参数 c
     * 字符
     * 参数 num
     * 数量
     * 返回 新字符串
     */
    fun dup(c: Char, num: Int): String {
        if (c.toInt() == 0 || num < 1) {
            return ""
        }
        val sb = StringBuilder(num)
        for (i in 0 until num) {
            sb.append(c)
        }
        return sb.toString()
    }

    /**
     * 将字符串首字母大写
     * 参数 s 字符串
     * 返回 首字母大写后的新字符串
     */
    fun capitalize(s: CharSequence?): String? {
        if (null == s) {
            return null
        }
        val len = s.length
        if (len == 0) {
            return ""
        }
        val char0 = s[0]
        return if (Character.isUpperCase(char0)) {
            s.toString()
        } else StringBuilder(len).append(Character.toUpperCase(char0))
                .append(s.subSequence(1, len))
                .toString()
    }

    /**
     * 将字符串首字母小写
     * 参数 s 字符串
     * 返回 首字母小写后的新字符串
     */
    fun lowerFirst(s: CharSequence?): String? {
        if (null == s) {
            return null
        }
        val len = s.length
        if (len == 0) {
            return ""
        }
        val c = s[0]
        return if (Character.isLowerCase(c)) {
            s.toString()
        } else StringBuilder(len).append(Character.toLowerCase(c))
                .append(s.subSequence(1, len))
                .toString()
    }

    /**
     * 检查两个字符串的忽略大小写后是否相等.
     *
     *
     * 参数 s1 字符串A
     * 参数 s2 字符串B
     * 返回 true 如果两个字符串忽略大小写后相等,且两个字符串均不为null
     */
    fun equalsIgnoreCase(s1: String?, s2: String?): Boolean {
        return s1?.equals(s2, ignoreCase = true) ?: (s2 == null)
    }

    /**
     * 检查两个字符串是否相等.
     *
     *
     * 参数 s1 字符串A
     * 参数 s2 字符串B
     * 返回 true 如果两个字符串相等,且两个字符串均不为null
     */
    fun equals(s1: String?, s2: String?): Boolean {
        return if (s1 == null) s2 == null else s1 == s2
    }

    /**
     * 判断字符串是否以特殊字符开头
     *
     *
     * 参数 s 字符串
     * 参数 c 特殊字符
     * 返回 是否以特殊字符开头
     */
    fun startsWithChar(s: String?, c: Char): Boolean {
        return null != s && s.length != 0 && s[0] == c
    }

    /**
     * 判断字符串是否以特殊字符结尾
     *
     *
     * 参数 s 字符串
     * 参数 c 特殊字符
     * 返回 是否以特殊字符结尾
     */
    fun endsWithChar(s: String?, c: Char): Boolean {
        return null != s && s.length != 0 && s[s.length - 1] == c
    }

    /**
     * 参数 cs 字符串
     * 返回 是不是为空字符串
     */
    fun isEmpty(cs: CharSequence?): Boolean {
        return null == cs || cs.length == 0
    }

    fun isEmpty(str: String?): Boolean {
        return str == null || str.length == 0
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 参数 cs 字符串
     * 返回 是不是为空白字符串
     */
    fun isBlank(cs: CharSequence?): Boolean {
        if (null == cs) {
            return true
        }
        val length = cs.length
        for (i in 0 until length) {
            if (!Character.isWhitespace(cs[i])) {
                return false
            }
        }
        return true
    }

    /**
     * 去掉字符串前后空白
     *
     *
     * 参数 cs 字符串
     * 返回 新字符串
     */
    fun trim(cs: CharSequence?): String? {
        if (null == cs) {
            return null
        }
        if (cs is String) {
            return cs.trim { it <= ' ' }
        }
        val length = cs.length
        if (length == 0) {
            return cs.toString()
        }
        var l = 0
        val last = length - 1
        var r = last
        while (l < length) {
            if (!Character.isWhitespace(cs[l])) {
                break
            }
            l++
        }
        while (r > l) {
            if (!Character.isWhitespace(cs[r])) {
                break
            }
            r--
        }
        if (l > r) {
            return ""
        } else if (l == 0 && r == last) {
            return cs.toString()
        }
        return cs.subSequence(l, r + 1).toString()
    }

    /**
     * 将字符串按半角逗号，拆分成数组，空元素将被忽略
     *
     *
     * 参数 s
     * 字符串
     * 返回 字符串数组
     */
    fun splitIgnoreBlank(s: String?): Array<String?>? {
        return splitIgnoreBlank(s, ",")
    }

    /**
     * 根据一个正则式，将字符串拆分成数组，空元素将被忽略
     * 参数 s 字符串
     * 参数 regex 正则式
     * 返回 字符串数组
     */
    fun splitIgnoreBlank(s: String?, regex: String): Array<String?>? {
        if (null == s) {
            return null
        }
        val ss = s.split(regex.toRegex()).toTypedArray()
        val list: MutableList<String?> = LinkedList()
        for (st in ss) {
            if (isBlank(st)) {
                continue
            }
            list.add(trim(st))
        }
        return list.toTypedArray()
    }

    /**
     * 将一个整数转换成最小长度为某一固定数值的十进制形式字符串
     *
     *
     * 参数 d 整数
     * 参数 width 宽度
     * 返回 新字符串
     */
    fun fillDigit(d: Int, width: Int): String? {
        return alignRight(d.toString(), width, '0')
    }

    /**
     * 将一个整数转换成最小长度为某一固定数值的十六进制形式字符串
     *
     *
     * 参数 d
     * 整数
     * 参数 width
     * 宽度
     * 返回 新字符串
     */
    fun fillHex(d: Int, width: Int): String? {
        return alignRight(Integer.toHexString(d), width, '0')
    }

    /**
     * 将一个整数转换成最小长度为某一固定数值的二进制形式字符串
     *
     *
     * 参数 d 整数
     * 参数 width 宽度
     * 返回 新字符串
     */
    fun fillBinary(d: Int, width: Int): String? {
        return alignRight(Integer.toBinaryString(d), width, '0')
    }

    /**
     * 将一个整数转换成固定长度的十进制形式字符串
     *
     *
     * 参数 d 整数
     * 参数 width 宽度
     * 返回 新字符串
     */
    fun toDigit(d: Int, width: Int): String? {
        return cutRight(d.toString(), width, '0')
    }

    /**
     * 将一个整数转换成固定长度的十六进制形式字符串
     *
     *
     * 参数 d 整数
     * 参数 width 宽度
     * 返回 新字符串
     */
    fun toHex(d: Int, width: Int): String? {
        return cutRight(Integer.toHexString(d), width, '0')
    }

    /**
     * 将一个整数转换成固定长度的二进制形式字符串
     *
     *
     * 参数 d 整数
     * 参数 width 宽度
     * 返回 新字符串
     */
    fun toBinary(d: Int, width: Int): String? {
        return cutRight(Integer.toBinaryString(d), width, '0')
    }

    /**
     * 保证字符串为一固定长度。超过长度，切除，否则补字符。
     *
     *
     * 参数 s 字符串
     * 参数 width 长度
     * 参数 c 补字符
     * 返回 修饰后的字符串
     */
    fun cutRight(s: String?, width: Int, c: Char): String? {
        if (null == s) {
            return null
        }
        val len = s.length
        if (len == width) {
            return s
        }
        return if (len < width) {
            dup(c, width - len) + s
        } else s.substring(len - width, len)
    }

    /**
     * 在字符串左侧填充一定数量的特殊字符
     *
     *
     * 参数 cs 字符串
     * 参数 width 字符数量
     * 参数 c 字符
     * 返回 新字符串
     */
    fun alignRight(cs: CharSequence?, width: Int, c: Char): String? {
        if (null == cs) {
            return null
        }
        val len = cs.length
        return if (len >= width) {
            cs.toString()
        } else StringBuilder().append(dup(c, width - len)).append(cs).toString()
    }

    /**
     * 在字符串右侧填充一定数量的特殊字符
     *
     *
     * 参数 cs 字符串
     * 参数 width 字符数量
     * 参数 c 字符
     * 返回 新字符串
     */
    fun alignLeft(cs: CharSequence?, width: Int, c: Char): String? {
        if (null == cs) {
            return null
        }
        val length = cs.length
        return if (length >= width) {
            cs.toString()
        } else StringBuilder().append(cs).append(dup(c, width - length)).toString()
    }

    /**
     * 参数 cs
     * 字符串
     * 参数 lc
     * 左字符
     * 参数 rc
     * 右字符
     * 返回 字符串是被左字符和右字符包裹 -- 忽略空白
     */
    fun isQuoteByIgnoreBlank(cs: CharSequence?, lc: Char, rc: Char): Boolean {
        if (null == cs) {
            return false
        }
        val len = cs.length
        if (len < 2) {
            return false
        }
        var l = 0
        val last = len - 1
        var r = last
        while (l < len) {
            if (!Character.isWhitespace(cs[l])) {
                break
            }
            l++
        }
        if (cs[l] != lc) {
            return false
        }
        while (r > l) {
            if (!Character.isWhitespace(cs[r])) {
                break
            }
            r--
        }
        return l < r && cs[r] == rc
    }

    /**
     * 参数 cs
     * 字符串
     * 参数 lc
     * 左字符
     * 参数 rc
     * 右字符
     * 返回 字符串是被左字符和右字符包裹
     */
    fun isQuoteBy(cs: CharSequence?, lc: Char, rc: Char): Boolean {
        if (null == cs) {
            return false
        }
        val length = cs.length
        return length > 1 && cs[0] == lc && cs[length - 1] == rc
    }

    /**
     * 获得一个字符串集合中，最长串的长度
     *
     *
     * 参数 coll 字符串集合
     * 返回 最大长度
     */
    fun maxLength(coll: Collection<CharSequence?>?): Int {
        var re = 0
        if (null != coll) {
            for (s in coll) {
                if (null != s) {
                    re = Math.max(re, s.length)
                }
            }
        }
        return re
    }

    /**
     * 获得一个字符串数组中，最长串的长度
     *
     *
     * 参数 array
     * 字符串数组
     * 返回 最大长度
     */
    fun <T : CharSequence?> maxLength(array: Array<T>?): Int {
        var re = 0
        if (null != array) {
            for (s in array) {
                if (null != s) {
                    re = Math.max(re, s.length)
                }
            }
        }
        return re
    }
    /**
     * 对obj进行toString()操作,如果为null返回def中定义的值
     *
     *
     * 参数 obj
     * 参数 defaultValue
     * 如果obj==null返回的内容
     * 返回 obj的toString()操作
     */
    /**
     * 对obj进行toString()操作,如果为null返回""
     *
     *
     * 参数 obj
     * 返回 obj.toString()
     */
    @JvmOverloads
    fun sNull(obj: Any?, defaultValue: String? = ""): String {
        return obj?.toString() ?: defaultValue!!
    }
    /**
     * 对obj进行toString()操作,如果为空串返回def中定义的值
     *
     *
     * 参数 obj
     * 参数 def
     * 如果obj==null返回的内容
     * 返回 obj的toString()操作
     */
    /**
     * 对obj进行toString()操作,如果为空串返回""
     *
     *
     * 参数 obj
     * 返回 obj.toString()
     */
    @JvmOverloads
    fun sBlank(obj: Any?, def: String = ""): String {
        if (null == obj) {
            return def
        }
        val s = obj.toString()
        return if (isBlank(s)) def else s
    }

    /**
     * 截去第一个字符
     *
     *
     * 比如:
     *
     *  * removeFirst("12345") ＝》 2345
     *  * removeFirst("A") ＝》 ""
     *
     *
     *
     * 参数 str
     * 字符串
     * 返回 新字符串
     */
    fun removeFirst(str: CharSequence?): String? {
        if (str == null) {
            return null
        }
        return if (str.length > 1) {
            str.subSequence(1, str.length).toString()
        } else ""
    }

    /**
     * 如果str中第一个字符和 c一致,则删除,否则返回 str
     *
     *
     * 比如:
     *
     *  * removeFirst("12345",1) ＝》 "2345"
     *  * removeFirst("ABC",'B') ＝》 "ABC"
     *  * removeFirst("A",'B') ＝》 "A"
     *  * removeFirst("A",'A') ＝》 ""
     *
     *
     *
     * 参数 str
     * 字符串
     * 参数 c
     * 第一个个要被截取的字符
     * 返回 新字符串
     */
    fun removeFirst(str: String, c: Char): String {
        return if (isEmpty(str) || c != str[0]) str else str.substring(1)
    }

    /**
     * 判断一个字符串数组是否包括某一字符串
     *
     *
     * 参数 ss
     * 字符串数组
     * 参数 s
     * 字符串
     * 返回 是否包含
     */
    fun isin(ss: Array<String>?, s: String): Boolean {
        if (null == ss || ss.size == 0 || isBlank(s)) {
            return false
        }
        for (w in ss) {
            if (s == w) {
                return true
            }
        }
        return false
    }

    /**
     * 将一个字符串某一个字符后面的字母变成大写，比如
     *
     *
     * <pre>
     * upperWord("hello-world", '-') 》 "helloWorld"
    </pre> *
     *
     *
     * 参数 s
     * 字符串
     * 参数 c
     * 字符
     *
     *
     * 返回 转换后字符串
     */
    fun upperWord(s: CharSequence, c: Char): String {
        val sb = StringBuilder()
        val len = s.length
        var i = 0
        while (i < len) {
            var ch = s[i]
            if (ch == c) {
                do {
                    i++
                    if (i >= len) {
                        return sb.toString()
                    }
                    ch = s[i]
                } while (ch == c)
                sb.append(Character.toUpperCase(ch))
            } else {
                sb.append(ch)
            }
            i++
        }
        return sb.toString()
    }

    fun indexOf(referer: String, string: String?): Int {
        return if (hasLengthBytrim(referer)) {
            referer.indexOf(string!!)
        } else -1
    }

    /**
     * 根据某种编码方式将字节数组转换成字符串
     * 参数 b 字节数组
     * 参数 offset 要转换的起始位置
     * 参数 len 要转换的长度
     * 参数 encoding 编码方式
     * 返回 如果encoding不支持，返回一个缺省编码的字符串
     */
    fun getString(b: ByteArray?, offset: Int, len: Int, encoding: Charset): String {
        return try {
            String(b!!, offset, len, encoding)
        } catch (e: UnsupportedEncodingException) {
            String(b!!, offset, len)
        }
    }

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    @JvmOverloads
    fun toHexString(buf: ByteArray?, sep: String? = null, lineLen: Int = Int.MAX_VALUE): String? {
        var lineLen = lineLen
        if (buf == null) {
            return null
        }
        if (lineLen <= 0) {
            lineLen = Int.MAX_VALUE
        }
        val res = StringBuffer(buf.size * 2)
        for (i in buf.indices) {
            val b = buf[i].toInt()
            res.append(HEX_DIGITS[b shr 4 and 0xf])
            res.append(HEX_DIGITS[b and 0xf])
            if (i > 0 && i % lineLen == 0) {
                res.append('\n')
            } else if (sep != null && i < lineLen - 1) {
                res.append(sep)
            }
        }
        return res.toString()
    }

    private fun charToNibble(c: Char): Int {
        return if (c >= '0' && c <= '9') {
            c - '0'
        } else if (c >= 'a' && c <= 'f') {
            0xa + (c - 'a')
        } else if (c >= 'A' && c <= 'F') {
            0xA + (c - 'A')
        } else {
            -1
        }
    }

    fun fromHexString(text: String): ByteArray? {
        var text = text
        text = text.trim { it <= ' ' }
        if (text.length % 2 != 0) {
            text = "0$text"
        }
        val resLen = text.length / 2
        var loNibble: Int
        var hiNibble: Int
        val res = ByteArray(resLen)
        for (i in 0 until resLen) {
            val j = i shl 1
            hiNibble = charToNibble(text[j])
            loNibble = charToNibble(text[j + 1])
            if (loNibble == -1 || hiNibble == -1) {
                return null
            }
            res[i] = (hiNibble shl 4 or loNibble).toByte()
        }
        return res
    }

    fun isChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }

    fun isChinese(c: Char): Boolean {
        if (c.toInt() > 127) {
            val ub = UnicodeBlock.of(c)
            return ub === UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === UnicodeBlock.GENERAL_PUNCTUATION || ub === UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        }
        return false
    }

    /**
     * 清除 小数末尾精度多余0
     * 参数 str(9.12000)
     * 返回 9012
     */
    fun trimZero(str: String): String {
        return if (str.indexOf(".") != -1 && str[str.length - 1] == '0') {
            trimZero(str.substring(0, str.length - 1))
        } else {
            if (str[str.length - 1] == '.') str.substring(0, str.length - 1) else str
        }
    }

    /**
     * List转换String
     *
     *
     * 参数 list
     * :需要转换的List
     * 返回 String转换后的字符串
     */
    fun ListToString(list: List<*>?, sep: String?): String {
        val sb = StringBuffer()
        if (list != null && list.size > 0) {
            for (i in list.indices) {
                if (list[i] == null || list[i] === "") {
                    continue
                }
                sb.append(list[i])
                sb.append(sep)
            }
        }
        return sb.toString()
    }

    /**
     * 把字符串的除了前m位和后n位，其余位用“*”号代替
     * 参数 str 要代替的字符串
     * 参数 m 不做变化的前m位数
     * 参数 n 不做变化后n位数
     * 返回
     */
    fun replaceSubString(str: String?, m: Int, n: Int): String? {
        if (str == null) {
            return null
        }
        var sub = ""
        var subPrefix = ""
        var subSuffix = ""
        val strLen = str.length
        subPrefix = str.substring(0, m)
        subSuffix = str.substring(strLen - n, strLen)
        var sb = StringBuffer()
        for (i in 0 until strLen - m - n) {
            sb = sb.append("*")
        }
        sub = subPrefix + sb.toString() + subSuffix
        return sub
    }

    /**
     * 截取字符串后4位，不满4位返回空串
     * 参数 str
     * 返回
     */
    fun getLast4(str: String?): String {
        return if (str != null && str.length >= 4) {
            str.substring(str.length - 4)
        } else {
            ""
        }
    }

    /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
     */
    fun digitUppercase(n: Double): String {
        var n = n
        val fraction = arrayOf("角", "分")
        val digit = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        val unit = arrayOf(arrayOf("元", "万", "亿"), arrayOf("", "拾", "佰", "仟"))
        val head = if (n < 0) "负" else ""
        n = Math.abs(n)
        var s = ""
        for (i in fraction.indices) {
            s += (digit[(Math.floor(n * 10 * Math.pow(10.0, i.toDouble())) % 10).toInt()] + fraction[i]).replace("(零.)+".toRegex(), "")
        }
        if (s.length < 1) {
            s = "整"
        }
        var integerPart = Math.floor(n).toInt()
        var i = 0
        while (i < unit[0].size && integerPart > 0) {
            var p = ""
            var j = 0
            while (j < unit[1].size && n > 0) {
                p = digit[integerPart % 10] + unit[1][j] + p
                integerPart = integerPart / 10
                j++
            }
            s = p.replace("(零.)*零$".toRegex(), "").replace("^$".toRegex(), "零") + unit[0][i] + s
            i++
        }
        return head + s.replace("(零.)*零元".toRegex(), "元").replaceFirst("(零.)+".toRegex(), "").replace("(零.)+".toRegex(), "零").replace("^整$".toRegex(), "零元整")
    }
}