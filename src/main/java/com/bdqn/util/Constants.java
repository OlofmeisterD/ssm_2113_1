package com.bdqn.util;

public class Constants {
	public final static String USER_SESSION = "userSession";
	public final static String SYS_MESSAGE = "message";
	public final static int pageSize = 5;
	/*
	* REST:使用REST风格请求容易解析,效率比较高,缺点:请求数据包含中文
    处理乱码比较麻烦,通常系统会采用REST风格与传统的请求方式相结合
    传统风格:http://localhost:8080/user/login.html?username=list
    REST风格:http://localhost:8080/user/login.html/username/lisi
	* */
}
