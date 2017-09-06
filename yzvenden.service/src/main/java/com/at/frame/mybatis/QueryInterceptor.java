package com.at.frame.mybatis;

import com.at.frame.AtFrame;
import com.at.frame.utils.IPUtil;
import com.at.frame.utils.PVTransverter;
import com.github.pagehelper.PageException;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/16.
 */
@ConditionalOnProperty(prefix = "at.frame" ,name = "mybatis-query-interceptor" ,havingValue = "true")
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Component
public class QueryInterceptor implements Interceptor{
      public static void main(String[] args){
          String originalSql = "SELECT    name1    AS    名称,NAME2 nnn,    lName,id,  login_name," +
                  "create_time,nick_name, user_name, sex, mobile, " +
                  "lev, balance, integral, earnings, last_time, " +
                  "status, device_code, lon, lat, ip, token, " +
                  "birthday, head_pic FROM t_account WHERE ((id = ?))";
          HashSet<String> set = new HashSet<>();
          set.add("name1");
          set.add("head_pic");
          set.add("name2");
          System.out.println(filterSQL(originalSql,set));
          System.out.println(filterSQL("select count(1),head_pic from abc",set));
      }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        HashSet<String> resultSetFilter = AtFrame.getResultSetFilter();
        if (resultSetFilter != null && resultSetFilter.size() > 0) {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            if(args.length == 4){
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }

            String originalSql = boundSql.getSql();
            originalSql = filterSQL(originalSql,resultSetFilter);
//                String lowerCase = originalSql.toLowerCase();
//                String filterStr = "";
//                String whereStr = "";
//                int split;
//                if((split = lowerCase.indexOf("where")) != -1){
//                    filterStr = originalSql.substring(0,split);
//                    whereStr = originalSql.substring(split,originalSql.length());
//                }else{
//                    filterStr = originalSql;
//                }
//                for (String filter : resultSetFilter) {
//                    filterStr = filterStr.replace(filter, "");
//                }
//                filterStr = filterStr.replaceAll("(,( ){0,}){1,}|(,(.*\\.)?,)", ",");
//                filterStr = filterStr.replaceAll(",[\n|\\s]{0,}((?i)from)"," FROM");
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), originalSql, boundSql.getParameterMappings(), parameter);
            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, newBoundSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private static String filterSQL(String originalSql,HashSet<String> filterFields){
        char[][] filters = new char[filterFields.size()][];
        int fieldIndex = 0;
        for(String fields : filterFields){
            filters[fieldIndex++] = fields.toUpperCase().toCharArray();
        }
        char[] chars = originalSql.toCharArray();
        StringBuilder builder = new StringBuilder();
        int prev = 0;//切割开始索引
        int pos = 0;//字符串索引
        char c;//字符
        boolean insert;
        boolean end = false;
        char[] column;//查询字段
        loop : for(int i = 0, len = chars.length; i < len; i++){
            insert = true;
            c = chars[i];
            if(!end){
                switch (c){
                    case ' '://空格
                        if(prev == 0) prev = pos;//第一个空格位置
                        if(chars[i+1] == ' ' || (i > 0 && builder.charAt(pos-1) == ',')){
                            --pos;
                            insert = false;
                        }
                        break;
                    case ','://逗号
                        builder.append(',');
                        insert = false;
                        column = new char[pos - prev];
                        builder.getChars(prev,pos,column,0);
                        prev = pos + 1;//切割位向下
                        for(char[] filter : filters){
                            int s = 0;
                            int e = filter.length;
                            if(column[0] == ' '){
                                s = 1;
                                e += 1;
                            }
                            if(e > column.length) continue ;
                            char[] b = Arrays.copyOfRange(column,s,e);
                            if(Arrays.equals(b,filter)){
                                //要过滤的
                                int l =  column.length + 1 - s;
                                builder.setLength(builder.length() - l);
                                pos -= l;
                                prev -= l;
                            }
                        }
                        break;
                    case 'F':
                    case 'f':
                        int e = i + 4;
                        if(e >= len) break;
                        char[] from = Arrays.copyOfRange(chars,i,i+4);
                        if((from[0] & 0x5F) == 'F'
                                && (from[1] & 0x5F) == 'R'
                                && (from[2] & 0x5F) == 'O'
                                && (from[3] & 0x5F) == 'M'){
                            end = true;
                            //向前寻找,
                            for(int j=pos-1;j>0;j--){
                                if(builder.charAt(j) == ','){
                                    column = new char[pos - j - 2];
                                    builder.getChars(j+1,pos-1,column,0);
                                    for(char[] filter : filters){
                                        if(Arrays.equals(column,filter)){
                                            //要过滤的
                                            int l =  column.length + 2;
                                            builder.setLength(builder.length() - l);
                                            builder.append(' ');
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                }
            }
            if(++pos > 0 && insert){
                builder.append(c);
            }
        }
        return builder.toString();
    }
}

