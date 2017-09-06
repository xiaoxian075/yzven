package com.at.frame;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/16.
 */
public abstract class ResultConfiguation {

    /**
     * 返回的数据加密
     * @param data
     * @return
     */
    public abstract Object dataEncrypt(Object data);


}
