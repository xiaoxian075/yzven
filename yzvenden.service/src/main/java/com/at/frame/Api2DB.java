package com.at.frame;

import com.at.frame.annotation.AutoCtrl;
import com.at.frame.annotation.AutoMethod;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */
public interface Api2DB {
    public void beforeSave();
    public void save(AutoCtrl ctrl, List<AutoMethod> methods);
}
