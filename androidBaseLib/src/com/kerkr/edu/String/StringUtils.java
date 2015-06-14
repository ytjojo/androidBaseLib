package com.kerkr.edu.String;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kerkr.edu.reflect.BeanUtil;

/**
 * String Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-7-22
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }


    /**
     * @param string
     * @return
     * @see #judgeNotNull(String, String...)
     */
    public static boolean judgeNotNull(String string) {
        // return string != null && !string.equals("") && !string.equals("null") ? true : false;

        return judgeNotNull(string, new String[0]);
    }

    /**
     * Judge if a variable of String or String[] is null or ""
     *
     * @param string
     * @param strings
     * @return
     */
    public static boolean judgeNotNull(String string, String... strings) {
        boolean flag = true;
        if (!(string != null && string.trim().length() > 0 && !string.equals("null") && !string.trim().equals("")))
            return false;
        for (String s : strings) {
            if (s == null || string.trim().length() == 0 || s.equals("null")) {
                flag = false;
                break;
            }
        }

        return flag;
    }
    
    /**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * 
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     * 
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return BeanUtil.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     * 
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     * 
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    
    /**
     * 这是一个字符串注入（或叫做字符串模板替换）方法. 与{@link #inject(String, String...)}
     * 不同的是，此方法可以自已定义占位符. <br />
     * <strong>下面是一个示例性的代码：</strong><br/>
     * String log = "对不起，要更新的字段出错。字段名:?, 字段值:?, 错误原因：?";<br />
     * String log2 = Txt.injectBy(log, "?", "name", "ziquee", "无此字段");<br />
     * //执行结果如下：<br />
     * log2="对不起，要更新的字段出错。字段名:name, 字段值:ziquee, 错误原因:无此字段";
     * 
     * @param inputString
     *            原始字符串模板.
     * @param injectString
     *            字符串占位符.
     * @param injects
     *            注入进去的字符串列表.
     * @return 执行注入后的字符串.
     * @see #inject(String, String...)
     */
    public static String injectBy(String inputString, String injectString, String... injects)
    {
        if (inputString == null || injects == null || injects.length == 0) return inputString;
        if (injectString == null || "".equals(injectString)) injectString = "{}";
        StringBuilder sb = new StringBuilder();
        int begin = 0, replaced = 0, len = injectString.length(), len1 = injects.length;
        while (true)
        {
            int sub = inputString.indexOf(injectString, begin);
            if (sub != -1)
            {
                if (replaced < len1)
                {
                    sb.append(inputString.substring(begin, sub)).append(injects[replaced++]);
                    begin = sub + len;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
        sb.append(inputString.substring(begin));
        return sb.toString();
    }

    /**
     * 将字符串数组元素按指定的字符串分隔符连成一个新的字符串。
     * 
     * @param arr
     *            要连接的字符串数组.
     * @param joinStr
     *            连接字符串. 如果此字符串为空(null)，则使用逗号加一个空格(, )进行连接。
     * @return 返回arr的每个下标元素按joinStr连接成的新的字符串. 如果数组arr为空(null),则返回结果也将为null.
     */
    public static String join(String[] arr, String joinStr)
    {
        if (null == arr) return null;
        if (null == joinStr) joinStr = ", ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++)
        {
            if (i > 0) sb.append(joinStr);
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param array
     * @param separator
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String joinObjects(Object[] array, String separator) {  
        if (array == null) {  
            return null;  
        }  
        int arraySize = array.length;  
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);  
        StringBuffer buf = new StringBuffer(bufSize);  
  
        for (int i = 0; i < arraySize; i++) {  
            if (i > 0) {  
                buf.append(separator);  
            }  
            if (array[i] != null) {  
                buf.append(array[i]);  
            }  
        }  
        return buf.toString();  
    }  
    
    /**
     * 将字符串按指定的分隔符分割成为一个数组. <br />
     * 与String.split(String s)、StringTokenizer不同的是，此方法将比它们更加高效快速.
     * 在需要将字符串split转换为数组时，应该优先使用此方法.
     * 
     * @param str
     *            要转换的字符串.
     * @param sp
     *            分隔字符串.
     * @return 转换后的String数组.
     * @since JDiy-1.6 及以上版本新增的方法.
     */
    public static String[] split(String str, String sp)
    {
        List<String> arrayList = new ArrayList<String>();
        int index = 0, offset = 0, len = sp.length();
        while ((index = str.indexOf(sp, index + len)) != -1)
        {
            arrayList.add(str.substring(offset, index));
            offset = index + len;
        }
        if (offset < str.length()) arrayList.add(str.substring(offset));
        return arrayList.toArray(new String[arrayList.size()]);
    }

    /**
     * 此方法将HTML内容转换为普通文本. <br/>
     * 此方法将通过对正则替换，删除inputString中的HTML标签，并返回纯文本内容．
     * 
     * @param inputString
     *            要转换的HTML代码.
     * @return 转换后的纯文本内容.
     * @since 这是JDiy-2.1及后续版本新增的方法.
     */
    public static String htmlToText(String inputString)
    {
        String htmlStr = inputString;
        String textStr = "";
        String scriptRegEx = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        String styleRegEx = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        String htmlRegEx1 = "<[^>]*>";
        String htmlRegEx2 = "<[^>]*";
        try
        {
            Pattern scriptPattern = Pattern.compile(scriptRegEx, Pattern.CASE_INSENSITIVE);
            Matcher scriptMatcher = scriptPattern.matcher(htmlStr);
            htmlStr = scriptMatcher.replaceAll("");
            Pattern stylePattern = Pattern.compile(styleRegEx, Pattern.CASE_INSENSITIVE);
            Matcher styleMatcher = stylePattern.matcher(htmlStr);
            htmlStr = styleMatcher.replaceAll("");
            Pattern htmlPattern1 = Pattern.compile(htmlRegEx1, Pattern.CASE_INSENSITIVE);
            Matcher htmlMatcher1 = htmlPattern1.matcher(htmlStr);
            htmlStr = htmlMatcher1.replaceAll("");
            Pattern htmlPattern2 = Pattern.compile(htmlRegEx2, Pattern.CASE_INSENSITIVE);
            Matcher htmlMatcher2 = htmlPattern2.matcher(htmlStr);
            htmlStr = htmlMatcher2.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e)
        {
            System.err.println("->Txt.htmlToText(String inputString) ERROR:" + e.getMessage());
        }
        textStr = textStr.replaceAll("&acute;", "\'");
        textStr = textStr.replaceAll("&quot;", "\"");
        textStr = textStr.replaceAll("&lt;", "<");
        textStr = textStr.replaceAll("&gt;", ">");
        textStr = textStr.replaceAll("&nbsp;", " ");
        textStr = textStr.replaceAll("&amp;", "&");
        return textStr;
    }

    /**
     * 将指定的字符串内容进行HTML标签转换过滤. <br />
     * 此方法会将内容中的HTML标签转换成普通字符输出以防止非法的HTML代码被执行; 同时此方法自动识别URL地址、邮件地址并为其添加链接.<br />
     * <strong>注意：</strong> 由在线WEB编辑器添加的内容，不应调用此方法，否则编辑器上传的所有HTML代码将被转换成普通字符输出。
     * 
     * @param str
     *            要进行HTML标签过滤处理处理的原字符串。
     * @return 转换之后的字符串。
     */
    @SuppressWarnings("unused")
    public static String encodeHTML(String str)
    {
        String s = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&acute; ")
                .replaceAll("  ", " &nbsp;").replaceAll("\\r\\n", "<br/>").replaceAll("\\n", "<br/>");
        
        str =  replace("http://([%#=&\\?\\./a-zA-Z0-9]+)", str,"<a href=\"http://$1\" target=\"_blank\">http://$1</a>");
        str = replace("([-_a-z0-9]+?)@([-_\\.a-zA-Z0-9]{5,})", str,"<a href=\"mailto:$1@$2\" target=\"_blank\">$1@$2</a>");
        return str;
    }
    
    /**
     * 将字符串中符合regStr正则模式的字符串替换为s1. 此操作将替换所有符合正则匹配条件的字符串.
     * 此方法实现的功能与String对象的replaceAll(String, String)方法类似.
     * 
     * @param regStr
     *            用于匹配的正则表达式.
     * @param s1
     *            要替换符合匹配内容的字符串.
     * @return 当前的Txt对象.
     * @see #replaceFirst(String, String)
     * @see #restr(String, String)
     */
    public static String replace(String regStr,String src ,String s1)
    {
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(src);
        return  matcher.replaceAll(s1);
    }
}