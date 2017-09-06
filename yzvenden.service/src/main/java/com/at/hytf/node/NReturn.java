package com.at.hytf.node;

import java.io.Serializable;

public class NReturn<T> implements Serializable{
	
	public static <T> NReturn<T> createNew(int code,String desc) {
		return createNew(code,desc,null);
	}
	
	public static <T> NReturn<T> createNew(int code, String desc, T t) {
		return new NReturn<T>(code,desc,t);
	}

	private static final long serialVersionUID = 7816738592153317793L;
	private int code;
	private String desc;
	private T t;
	public boolean isSucc() {
		return code==0?true:false;
	}
	public boolean isErr() {
		return code==0?false:true;
	}
	public NReturn() {
		super();
	}
	public NReturn(int code, String desc, T t) {
		super();
		this.code = code;
		this.desc = desc;
		this.t = t;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

}

