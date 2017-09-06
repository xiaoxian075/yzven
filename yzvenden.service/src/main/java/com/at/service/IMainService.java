package com.at.service;

import java.util.List;

import com.at.entity.VenAdmin;
import com.at.entity.VenMenu;
import com.at.hytf.node.NReturn;

public interface IMainService {

	/**
	 * 登入
	 * @param loginName 用户名
	 * @param password	密码
	 * @return
	 */
	NReturn<VenAdmin> login(String loginName, String password);

	/**
	 * 获取菜单列表(导航用)
	 * @param loginName
	 * @return
	 */
	NReturn<List<VenMenu>> getMenuList(int roleId);

	NReturn<VenMenu> insertMenu(VenMenu menu);

}
