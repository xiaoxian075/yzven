package com.at.entity;

import io.swagger.annotations.ApiModelProperty;

public class VenMenu {
    /*
    * 自增ID
    */
    @ApiModelProperty(value="自增ID")
    private Integer id;

    /*
    * 父ID
    */
    @ApiModelProperty(value="父ID")
    private Integer parentId;

    /*
    * 链接地址
    */
    @ApiModelProperty(value="链接地址")
    private String url;

    /*
    * 名字
    */
    @ApiModelProperty(value="名字")
    private String menuName;

    /*
    * 等级
    */
    @ApiModelProperty(value="等级")
    private Integer menuLevel;

    /*
    * 是否显示 0:不显示  1:显示
    */
    @ApiModelProperty(value="是否显示 0:不显示  1:显示")
    private Integer menuShow;

    /*
    * 权限集
    */
    @ApiModelProperty(value="权限集")
    private String powerList;

    /*
    * 排序
    */
    @ApiModelProperty(value="排序")
    private Integer sort;

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

    public static final String ID_ASC = "id ASC";

    public static final String ID_DESC = "id DESC";

    public static final String PARENT_ID_ASC = "parent_id ASC";

    public static final String PARENT_ID_DESC = "parent_id DESC";

    public static final String URL_ASC = "url ASC";

    public static final String URL_DESC = "url DESC";

    public static final String MENU_NAME_ASC = "menu_name ASC";

    public static final String MENU_NAME_DESC = "menu_name DESC";

    public static final String MENU_LEVEL_ASC = "menu_level ASC";

    public static final String MENU_LEVEL_DESC = "menu_level DESC";

    public static final String MENU_SHOW_ASC = "menu_show ASC";

    public static final String MENU_SHOW_DESC = "menu_show DESC";

    public static final String POWER_LIST_ASC = "power_list ASC";

    public static final String POWER_LIST_DESC = "power_list DESC";

    public static final String SORT_ASC = "sort ASC";

    public static final String SORT_DESC = "sort DESC";

    public static final String CREATE_TIME_ASC = "create_time ASC";

    public static final String CREATE_TIME_DESC = "create_time DESC";

    public static final String UPDATE_TIME_ASC = "update_time ASC";

    public static final String UPDATE_TIME_DESC = "update_time DESC";

    public Integer getId() {
        return id;
    }

    /**
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId 父ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    /**
     * @param url 链接地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    /**
     * @param menuName 名字
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    /**
     * @param menuLevel 等级
     */
    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getMenuShow() {
        return menuShow;
    }

    /**
     * @param menuShow 是否显示 0:不显示  1:显示
     */
    public void setMenuShow(Integer menuShow) {
        this.menuShow = menuShow;
    }

    public String getPowerList() {
        return powerList;
    }

    /**
     * @param powerList 权限集
     */
    public void setPowerList(String powerList) {
        this.powerList = powerList == null ? null : powerList.trim();
    }

    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
}