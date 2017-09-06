package com.at.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.at.define.Const;
import com.at.entity.VenAdmin;
import com.at.entity.VenAdminCriteria;
import com.at.entity.VenMenu;
import com.at.entity.mapper.VenAdminMapper;
import com.at.entity.mapper.VenMenuMapper;
import com.at.hytf.node.NReturn;

@Service
public class MainService implements IMainService{
	private static final Logger LOGGER = LoggerFactory.getLogger(MainService.class);
	
	@Autowired
    private VenAdminMapper venAdminMapper;
	
	@Autowired
    private VenMenuMapper venMenuMapper;

	@Override
	public NReturn<VenAdmin> login(String loginName, String password) {
		
		VenAdminCriteria query = new VenAdminCriteria();
        query.createCriteria().andLoginNameEqualTo(loginName);
        VenAdmin admin = venAdminMapper.getByExample(query);
        if (admin==null) {
        	return Const.getNReturn(Const.LOGIN_NOT_EXIST);
        }
        if (!admin.getPassword().equals(password)) {
        	return Const.getNReturn(Const.PASSWORD);
        }
        
        return Const.getNReturn(admin);
	}

	@Override
	public NReturn<List<VenMenu>> getMenuList(int roleId) {
		List<VenMenu> list = venMenuMapper.selectAll();
		if (list==null) {
			LOGGER.error("select list error. roleId={}",roleId);
			return Const.getNReturn(Const.SQL_ERROR);
		}
		return Const.getNReturn(list);
	}

	@Override
	public NReturn<VenMenu> insertMenu(VenMenu menu) {
		if (1!=venMenuMapper.insert(menu)) {
			LOGGER.error("insert error. menu {}",menu.toString());
			return Const.getNReturn(Const.SQL_ERROR);
		}
		return Const.getNReturn(menu);
	}

}
