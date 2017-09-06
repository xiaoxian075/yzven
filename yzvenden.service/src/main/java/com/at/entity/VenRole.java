package com.at.entity;

import io.swagger.annotations.ApiModelProperty;

public class VenRole {
    /*
    * 自增ID
    */
    @ApiModelProperty(value="自增ID")
    private Integer id;

    /*
    * 名称
    */
    @ApiModelProperty(value="名称")
    private String name;

    /*
    * 状态 0:无效 1:有效
    */
    @ApiModelProperty(value="状态 0:无效 1:有效")
    private Integer state;

    /*
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Long createTime;

    /*
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    private Long updateTime;

    /*
    * 菜单集 JSON格式储存
    */
    @ApiModelProperty(value="菜单集 JSON格式储存")
    private byte[] menuList;

    public static final String ID_ASC = "id ASC";

    public static final String ID_DESC = "id DESC";

    public static final String NAME_ASC = "name ASC";

    public static final String NAME_DESC = "name DESC";

    public static final String STATE_ASC = "state ASC";

    public static final String STATE_DESC = "state DESC";

    public static final String CREATE_TIME_ASC = "create_time ASC";

    public static final String CREATE_TIME_DESC = "create_time DESC";

    public static final String UPDATE_TIME_ASC = "update_time ASC";

    public static final String UPDATE_TIME_DESC = "update_time DESC";

    public static final String MENU_LIST_ASC = "menu_list ASC";

    public static final String MENU_LIST_DESC = "menu_list DESC";

    public Integer getId() {
        return id;
    }

    /**
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getState() {
        return state;
    }

    /**
     * @param state 状态 0:无效 1:有效
     */
    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public byte[] getMenuList() {
        return menuList;
    }

    /**
     * @param menuList 菜单集 JSON格式储存
     */
    public void setMenuList(byte[] menuList) {
        this.menuList = menuList;
    }
}