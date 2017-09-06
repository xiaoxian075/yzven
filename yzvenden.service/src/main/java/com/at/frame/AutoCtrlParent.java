package com.at.frame;

import com.alibaba.fastjson.JSON;
//import com.at.entity.TSystemCtrl;
//import com.at.entity.TSystemUser;
import com.at.frame.utils.PVTransverter;

/**
 * Created by Administrator on 2017/6/5.
 */
public class AutoCtrlParent {
    private static final Integer ZERO = Integer.valueOf(0);
    private Integer id;
    private String ctrlName="-";
    private String ctrlDesc;
    private String ctrlUrl = "-";
    private Integer parentId = ZERO;
    private Integer ctrlVersion = ZERO;

//    public static class A implements Cloneable{
//        private TSystemCtrl c;
//        private String s;
//        public TSystemCtrl getC() {
//            return c;
//        }
//        public void setC(TSystemCtrl c) {
//            this.c = c;
//        }
//        public String getS() {
//            return s;
//        }
//        public void setS(String s) {
//            this.s = s;
//        }
//    }
//    public static void main(String[] args){
//        AutoCtrlParent p = new AutoCtrlParent(1,"test");
//        TSystemCtrl ctrl = p.to(TSystemCtrl.class);
//        System.out.println(JSON.toJSONString(ctrl));
//
//        A a = new A();
//        a.c = ctrl;
//        a.s = "test";
//        A b = PVTransverter.transfer(a,A.class);
//        ctrl.setId(1111);
//        System.out.println(JSON.toJSONString(b));
//    }

    public AutoCtrlParent(){

    }
    public <T> T to(Class<T> clazz) {
        return PVTransverter.transfer(this,clazz);
    }
    public AutoCtrlParent(int id,String ctrlDesc){
        this.id = Integer.valueOf(id);
        this.ctrlDesc = ctrlDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCtrlName() {
        return ctrlName;
    }

    public void setCtrlName(String ctrlName) {
        this.ctrlName = ctrlName;
    }

    public String getCtrlDesc() {
        return ctrlDesc;
    }

    public void setCtrlDesc(String ctrlDesc) {
        this.ctrlDesc = ctrlDesc;
    }

    public String getCtrlUrl() {
        return ctrlUrl;
    }

    public void setCtrlUrl(String ctrlUrl) {
        this.ctrlUrl = ctrlUrl;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCtrlVersion() {
        return ctrlVersion;
    }

    public void setCtrlVersion(int ctrlVersion) {
        this.ctrlVersion = ctrlVersion;
    }
}
