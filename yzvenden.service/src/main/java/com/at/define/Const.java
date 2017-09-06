package com.at.define;

import java.util.HashMap;
import java.util.Map;

import com.at.hytf.node.NReturn;

public class Const {
	public static final String ADMIN_ID = "admin_user";
	
	public static final int MainCtr = 1;
	
	public static final int OK = 0;		//成功
	public static final int LOGOUT = 1;		//登出
	public static final int PARAM = 1001;	//参数错误
	public static final int LOGIN_NOT_EXIST = 1002;	//登入名不存在
	public static final int PASSWORD = 1003;	//密码错误
	public static final int NO_LOGIN = 1004;	//没有登入
	public static final int SQL_ERROR = 1005;	//SQL执行出错
	
	
	
	

	public static String getDesc(int code) {
		return mapErr.get(code);
	}
	
	public static <T> NReturn<T> getNReturn() {
		return NReturn.createNew(Const.OK,mapErr.get(Const.OK));
	}
	
	public static <T> NReturn<T> getNReturn(int code) {
		return NReturn.createNew(code,mapErr.get(code));
	}
	
	public static <T> NReturn<T> getNReturn(T t) {
		return NReturn.createNew(Const.OK,mapErr.get(Const.OK), t);
	}
	
	public static <T> NReturn<T> getNReturn(int code,T t) {
		return NReturn.createNew(code,mapErr.get(code), t);
	}

	public static <T> boolean isSuc(NReturn<T> nReturn) {
		if (nReturn==null)
			return false;
		if (nReturn.getCode()!=Const.OK)
			return false;
		return true;
	}
	
	public static <T> boolean isErr(NReturn<T> nReturn) {
		if (nReturn==null)
			return true;
		if (nReturn.getCode()==Const.OK)
			return false;
		return true;
	}
	
	
	
	
	
	
	private static Map<Integer,String> mapErr = new HashMap<Integer,String>();
	static {
		mapErr.put(Const.OK, "成功");
		mapErr.put(Const.LOGOUT, "登出");
		mapErr.put(Const.PARAM, "参数错误");
		mapErr.put(Const.LOGIN_NOT_EXIST, "登入名不存在");
		mapErr.put(Const.PASSWORD, "密码错误");
		mapErr.put(Const.NO_LOGIN, "没有登入");
		mapErr.put(Const.SQL_ERROR, "SQL执行出错");
	}
	
	
}
