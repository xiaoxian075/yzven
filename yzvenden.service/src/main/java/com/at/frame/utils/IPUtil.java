package com.at.frame.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public final class IPUtil {
	 //ip范围
    private static final int[][] RANGE = {
    	{607649792,608174079},//36.56.0.0-36.63.255.255
        {1038614528,1039007743},//61.232.0.0-61.237.255.255
        {1783627776,1784676351},//106.80.0.0-106.95.255.255
        {2035023872,2035154943},//121.76.0.0-121.77.255.255
        {2078801920,2079064063},//123.232.0.0-123.235.255.255
        {-1950089216,-1948778497},//139.196.0.0-139.215.255.255
        {-1425539072,-1425014785},//171.8.0.0-171.15.255.255
        {-1236271104,-1235419137},//182.80.0.0-182.92.255.255
        {-770113536,-768606209},//210.25.0.0-210.47.255.255
        {-569376768,-564133889}, //222.16.0.0-222.95.255.255
    };
	public static final String[] IP_HEADERS = {"X-Real-IP","X-Forwarded-For","Proxy-Client-IP","WL-Proxy-Client-IP"};
    /**
	 * 取得随机IP地址
     */
    public static String getRandomIp(){
    	Random random = new Random();
		int index = random.nextInt(10);
		int range = RANGE[index][0];
		long ip = random.nextInt(RANGE[index][1] - range);
		return num2ip(ip);
    }
    public static void main(String[] args){
		long l;
		System.out.println((l = parseLong("127.0.0.1")));
		System.out.println(longToIp(l));
	}
	public static long parseLong(String ipAddr){
		if(ipAddr == null || ipAddr.indexOf(".") == -1) return 0L;
		try {
			long ip = 0;
			String[] vals = ipAddr.split("\\.");
			for(int i = 0, l = vals.length; i < l; i++){
				ip += Long.parseLong(vals[i]) <<  (24 - (i << 3));
			}
			return ip;
		} catch (Exception e) {
			return 0L;
		}
	}

	public static long parseIp(HttpServletRequest request){
		return parseLong(getIp(request));
	}
	public static String getIp(HttpServletRequest request){
		String ip = "127.0.0.1";
		for(int i = 0 ,l = IP_HEADERS.length; i < l; ) {
			ip = request.getHeader(IP_HEADERS[++i]);
			if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
	    return ip;
	}
	
	public static long getLongIP(HttpServletRequest request) {
		String ip = getIp(request);
		long lip = parseLong(ip);
		if(lip < 0) lip = parseLong(request.getRemoteAddr());
		return lip;
	}
	//long类型ip转成ip
	public static String longToIp(long ipaddress) {
	   StringBuilder sb = new StringBuilder("");
	   sb.append(ipaddress >>> 24);
	   sb.append(".");
	   sb.append((ipaddress & 0x00FFFFFF) >>> 16);
	   sb.append(".");
	   sb.append((ipaddress & 0x0000FFFF) >>> 8);
	   sb.append(".");
	   sb.append((ipaddress & 0x000000FF));
	   return sb.toString();
   }
	
	/**
	 * 判断IP格式和范围
	 * 作者 whd
	 * @param addr
	 * @return
	 */
	public static boolean isIP(String addr){
		if(addr == null || "".equals(addr) || addr.length() < 7 || addr.length() > 15){
			return false;
		}
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);  
		Matcher mat = pat.matcher(addr);  
		return mat.find();
	}
	/*
	 * 将十进制转换成ip地址
	*/
	public static String num2ip(long ip) {
		 int [] b=new int[4] ;
		 StringBuilder ipAddress = new StringBuilder();
		 b[0] = (int)((ip >> 24) & 0xff);
		 b[1] = (int)((ip >> 16) & 0xff);
		 b[2] = (int)((ip >> 8) & 0xff);
		 b[3] = (int)(ip & 0xff);
		return ipAddress.append(b[0])
				.append(".")
				.append(b[1])
				.append(".")
				.append(b[2])
				.append(".")
				.append(b[3]).toString();
	}
	
	
}
