package service.net;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RemoteCallUtil {

	public static final String UTF_8 = "utf-8";
	public static final String GBK = "gbk";

	/**
	 * 读取远程网页,默认被读取的网页为GBK编码
	 * 
	 * @param url
	 * @return
	 */
	public static String getUrlValue(String url) {
		return getUrlValue(url, GBK);
	}

	/**
	 * 读取远程网页
	 * 
	 * @param url
	 * @param encode
	 *            远程网页的编码
	 * @return
	 */
	public static String getUrlValue(String url, String encode) {
		return getUrlValue(url, encode, null, null);
	}

	/**
	 * 读取远程网页,默认被读取的网页为GBK编码
	 * 
	 * @param url
	 * @param values
	 *            要在url中传递的参数
	 * @return
	 */
	public static String getUrlValue(String url, Map<String, String> values) {
		return getUrlValue(url, GBK, values, null);
	}

	/**
	 * 
	 * @param url
	 * @param endcode
	 *            远程网页的编码
	 * @param values
	 *            要在url中传递的参数
	 * @return
	 */
	public static String getUrlValue(String url, String endcode, Map<String, String> values, Map<String, String> retMap) {

		StringBuilder bd = new StringBuilder();
		if (values != null) {
			bd.append(url);
			bd.append("?");
			Iterator<String> it = values.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = values.get(key);

				bd.append(key);
				bd.append("=");
				bd.append(value);
				bd.append("&");
			}

			url = bd.toString();
			bd.delete(0, bd.length());
		}

		try {
			java.net.URL netUrl = new java.net.URL(url);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) netUrl.openConnection();
			// conn.addRequestProperty(key, value);
			conn.setRequestMethod("GET");

			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Cache-Control", "max-age=0");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		
			conn.connect();

			/*
			Map<String, List<String>> listMap = conn.getHeaderFields();
			if (retMap != null) {
				retMap.put("Set-Cookie", listMap.get("Set-Cookie").toString());
			}

			Iterator<String> i = listMap.keySet().iterator();
			while (i.hasNext()) {
				String keyString = i.next();
				List<String> list = listMap.get(keyString);
				System.out.println("----------");
				System.out.println(keyString + "=" + list.size());
				for (String s : list) {
					System.out.println(s);
				}
			}
*/
			InputStream in = conn.getInputStream();

			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(in, endcode));
			String line = reader.readLine();

			while (line != null) {
				bd.append(line);
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bd.toString();
	}

}
