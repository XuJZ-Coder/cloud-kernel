package cn.zyszkj.cloud.kernel.core.xss;

import java.util.regex.Pattern;

/**
 * Web防火墙工具类
 * @author   hubin
 */
public class WafKit {

	private final static Pattern[] SCRIPT_PATTERNS = {
			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
	};
	/**
	 * @Description 过滤XSS脚本内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripXss(String value) {
		if (value != null) {
			value = value.replaceAll("\0|\n|\r", "");
			for (Pattern pattern : SCRIPT_PATTERNS) {
				value = pattern.matcher(value).replaceAll("");
			}
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		return value;
	}

	/**
	 * @Description 过滤SQL注入内容
	 * @param value
	 * 				待处理内容
	 * @return 过滤完语句
	 */
	public static String stripSqlInjection(String value) {
		return (null == value) ? null : value.replaceAll("('.+--)|(--)|(%7C)", "");
	}

	/**
	 * @Description 过滤SQL/XSS注入内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripSqlXss(String value) {
		return stripXss(stripSqlInjection(value));
	}

}
