package com.at.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import com.at.define.Const;
import com.at.entity.VenAdmin;
import com.at.entity.VenMenu;
import com.at.frame.annotation.ApiImplicitParam;
import com.at.frame.annotation.AutoCtrl;
import com.at.frame.annotation.AutoMethod;
import com.at.frame.utils.Result;
import com.at.frame.utils.VerifyUtil;
import com.at.hytf.node.NReturn;
import com.at.service.IMainService;

@AutoCtrl(
        id = Const.MainCtr,//控制器Id
        name = "admin",//该配置只暴露给admin组查看
        prefix = "adminMain",//控制器的方法生成时的方法名称前缀
        desc = "后台管理系统公共接口",//控制器描述
        api2Db = false,//该控制器的信息不保存到数据库
        version = 3//控制器的版本号信息
)
public class MainCtr {
    private static Logger LOGGER = LoggerFactory.getLogger(MainCtr.class);

    @Autowired
    private IMainService mainService;
    

	@AutoMethod(
            id = 1, name = "login", desc = "登入", method = RequestMethod.POST,
            implicitParams = {
            		@ApiImplicitParam(name = "loginName",value = "登录名 长度：4-16位"),
      	          @ApiImplicitParam(name = "password", value = "密码 长度：6-12位")
            }
    )
	public Result<String> login(HttpServletRequest request,String loginName, String password){
		if(VerifyUtil.isAnyLengthNotIn(4,16,loginName)){
            LOGGER.error("错误的参数：loginName:{}",loginName);
            return Result.fail(Const.PARAM);
        }
		if(VerifyUtil.isAnyLengthNotIn(6,12,password)){
            LOGGER.error("错误的参数：passowd:{}",password);
            return Result.fail(Const.PARAM);
        }
		
		NReturn<VenAdmin> nReturn = mainService.login(loginName,password);
		if (Const.isErr(nReturn)) {
			return Result.fail(nReturn.getDesc());
		}
		request.getSession().setAttribute(Const.ADMIN_ID,nReturn.getT());
	  
		return Result.fail(nReturn.getDesc());
	}
	
	@AutoMethod(
            id = 2, name = "logout", desc = "登出", method = RequestMethod.POST
    )
	public Result<String> logout(HttpServletRequest request){

		VenAdmin admin = (VenAdmin)request.getSession().getAttribute(Const.ADMIN_ID);
		if (admin==null) {
			return Result.fail(Const.getDesc(Const.NO_LOGIN));
		}

		request.getSession().removeAttribute(Const.ADMIN_ID);
	  
		return Result.suc(Const.getDesc(Const.OK));
	}
	
	@AutoMethod(
            id = 3, name = "insertMenu", desc = "增加菜单", method = RequestMethod.POST
    )
	public Result<VenMenu> insertMenu(HttpServletRequest request){
		VenAdmin admin = (VenAdmin)request.getSession().getAttribute(Const.ADMIN_ID);
		if (admin==null) {
			return Result.fail(Const.getDesc(Const.NO_LOGIN));
		}
		//Integer id, Integer parentId, String url, String name, Integer level, Integer show, Integer sort,
		//Long createTime, Long updateTime, String powerList
		VenMenu menu = new VenMenu(/*2,0,"/admin","测试admin",1,1,2,"[1,2,3]",1504663930363L,1504663930363L*/);
		menu.setId(2);
		menu.setParentId(0);
		menu.setUrl("admin");
		menu.setMenuName("测试admin");
		menu.setMenuLevel(1);
		menu.setMenuShow(1);
		menu.setSort(2);
		menu.setCreateTime(1504663930363L);
		menu.setUpdateTime(1504663930363L);
		menu.setPowerList("[1,2,3]");
		NReturn<VenMenu> nReturn = mainService.insertMenu(menu);
		if (Const.isErr(nReturn)) {
			return Result.fail(nReturn.getDesc());
		}
	  
		return Result.suc(nReturn.getT(),nReturn.getDesc());
	}
	
	@AutoMethod(
            id = 4, name = "getMenuList", desc = "获取菜单列表", method = RequestMethod.POST
    )
	public Result<List<VenMenu>> getMenuList(HttpServletRequest request){
		VenAdmin admin = (VenAdmin)request.getSession().getAttribute(Const.ADMIN_ID);
		if (admin==null) {
			return Result.fail(Const.getDesc(Const.NO_LOGIN));
		}
		
		NReturn<List<VenMenu>> nReturn = mainService.getMenuList(admin.getRoleId());
		if (Const.isErr(nReturn)) {
			return Result.fail(nReturn.getDesc());
		}
	  
		return Result.suc(nReturn.getT(),nReturn.getDesc());
	}
}
