//package com.at.frame.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.at.frame.AtFrame;
//import com.at.frame.properties.AtFrameProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//
///**
// * Created by Administrator on 2017/5/9.
// * 提供统一的标准化数据返回格式
// */
//public class Result<T>{
//
//    protected static final Logger LOGGER = LoggerFactory.getLogger(Result.class);
//    public static final int SUCCESS = 0;//成功
//    public static final int FAIL = 1;//失败
//    public static final int EXCEPTION = 2;//异常
//
//    private static final BlockingQueue<Result> QUEUE;
//    static {
//        AtFrameProperties properties = ApplicationContext.getBean(AtFrameProperties.class);
//        int poolSize = properties.getObjectPoolCapacity();
//        QUEUE = new ArrayBlockingQueue<Result>(poolSize);
//        for(int i=0;i<poolSize;i++){
//            QUEUE.offer(new Result(true));
//        }
//    }
//
//    public static Result suc(String info){
//        return get("",info,SUCCESS);
//    }
//    public static Result suc(Object data,String info){
//        return get(data,info,SUCCESS);
//    }
//
//    public static Result fail(Object data,String info){
//        return get(data,info,FAIL);
//    }
//
//    public static Result fail(Object data,int code){
//        return get(data,"",code);
//    }
//
//    public static Result fail(String info){
//        return get(null,info,FAIL);
//    }
//
//    public static Result fail(String info,int code){
//        return get(null,info,code);
//    }
//
//    public static Result fail(int code){
//        return get(null,"",code);
//    }
//
//    public static Result fail(Object data,String info,int code){
//       return get(data,info,code);
//    }
//
//    private static Result get(Object data,String info,int code){
//        Result result = QUEUE.poll();
//        if(result == null){
//            result = new Result();
//        }
//        result.setCode(code);
//        result.setData(data);
//        result.setInfo(info);
//        return result;
//    }
//
//    protected T data;//返回结果
//    protected int code;//信息代码
//    protected String info;//描述信息
//    private long date;//系统时间，毫秒级
//    transient boolean pool;
//
//    private Result(){
//        this(false);
//    }
//
//    private Result(boolean pool){
//        this.pool = pool;
//        this.info = "";
//    }
//
//    public static void write(HttpServletResponse response,String data){
//        response.setCharacterEncoding(AtFrame.ENCODE);
//        try(PrintWriter pw = response.getWriter()){
//            pw.write(data);
//            pw.flush();
//            pw.close();
//        } catch (IOException e) {
//            LOGGER.error("输出结果异常",e);
//        }
//    }
//
//    public void write(HttpServletResponse response){
//        response.setCharacterEncoding(AtFrame.ENCODE);
//        try(PrintWriter pw = response.getWriter()){
//            pw.write(JSON.toJSONString(this));
//            pw.flush();
//            pw.close();
//        } catch (IOException e) {
//            LOGGER.error("输出结果异常",e);
//        }finally {
//            this.close();
//        }
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }
//
//    public long getDate() {
//        return System.currentTimeMillis();
//    }
//
//    public void close(){
//        if(this.pool == true){
//            this.info = "";
//            this.data = null;
//            this.code = 0;
//            QUEUE.offer(this);
//        }
//    }
//}


package com.at.frame.utils;

import com.alibaba.fastjson.JSON;
import com.at.frame.AtFrame;
import com.at.frame.properties.AtFrameProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2017/5/9.
 * 提供统一的标准化数据返回格式
 */
public class Result<T>{

    protected static final Logger LOGGER = LoggerFactory.getLogger(Result.class);
    public static final int SUCCESS = 0;//成功
    public static final int FAIL = 1;//失败
    public static final int EXCEPTION = 2;//异常

    private static final BlockingQueue<Result> QUEUE;
    static {
        AtFrameProperties properties = ApplicationContext.getBean(AtFrameProperties.class);
        int poolSize = properties.getObjectPoolCapacity();
        QUEUE = new ArrayBlockingQueue<Result>(poolSize);
        for(int i=0;i<poolSize;i++){
            QUEUE.offer(new Result(true));
        }
    }

    public static Result<String> suc(String info){
        return get("",info,SUCCESS);
    }
    public static <T> Result<T> suc(T t,String info){
        return get(t,info,SUCCESS);
    }

    public static <T> Result<T> fail(T t,String info){
        return get(t,info,FAIL);
    }

    public static <T> Result<T> fail(T t,int code){
        return get(t,"",code);
    }

    public static <T> Result<T> fail(String info){
        return get(null,info,FAIL);
    }

    public static Result<String> fail(String info,int code){
        return get(null,info,code);
    }

    public static Result<String> fail(int code){
        return get(null,"",code);
    }

    public static <T> Result<T> fail(T t,String info,int code){
       return get(t,info,code);
    }

    private static <T> Result<T> get(T t,String info,int code){
        Result<T> result = QUEUE.poll();
        if(result == null){
            result = new Result<T>();
        }
        result.setCode(code);
        result.setData(t);
        result.setInfo(info);
        return result;
    }

    protected T data;//返回结果
    protected int code;//信息代码
    protected String info;//描述信息
    private long date;//系统时间，毫秒级
    transient boolean pool;

    private Result(){
        this(false);
    }

    private Result(boolean pool){
        this.pool = pool;
        this.info = "";
    }

    public static void write(HttpServletResponse response,String data){
        response.setCharacterEncoding(AtFrame.ENCODE);
        try(PrintWriter pw = response.getWriter()){
            pw.write(data);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            LOGGER.error("输出结果异常",e);
        }
    }

    public void write(HttpServletResponse response){
        response.setCharacterEncoding(AtFrame.ENCODE);
        try(PrintWriter pw = response.getWriter()){
            pw.write(JSON.toJSONString(this));
            pw.flush();
            pw.close();
        } catch (IOException e) {
            LOGGER.error("输出结果异常",e);
        }finally {
            this.close();
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getDate() {
        return System.currentTimeMillis();
    }

    public void close(){
        if(this.pool == true){
            this.info = "";
            this.data = null;
            this.code = 0;
            QUEUE.offer(this);
        }
    }
}
