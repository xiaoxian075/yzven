package com.at.entity;

import io.swagger.annotations.ApiModelProperty;

public class VenAdmin {
    /*
    * 自增ID
    */
    @ApiModelProperty(value="自增ID")
    private Long id;

    /*
    * 登入名
    */
    @ApiModelProperty(value="登入名")
    private String loginName;

    /*
    * 密码
    */
    @ApiModelProperty(value="密码")
    private String password;

    /*
    * 用户名
    */
    @ApiModelProperty(value="用户名")
    private String userName;

    /*
    * 状态 0:无效 1:有效
    */
    @ApiModelProperty(value="状态 0:无效 1:有效")
    private Integer state;

    /*
    * 角色
    */
    @ApiModelProperty(value="角色")
    private Integer roleId;

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

    public static final String LOGIN_NAME_ASC = "login_name ASC";

    public static final String LOGIN_NAME_DESC = "login_name DESC";

    public static final String PASSWORD_ASC = "password ASC";

    public static final String PASSWORD_DESC = "password DESC";

    public static final String USER_NAME_ASC = "user_name ASC";

    public static final String USER_NAME_DESC = "user_name DESC";

    public static final String STATE_ASC = "state ASC";

    public static final String STATE_DESC = "state DESC";

    public static final String ROLE_ID_ASC = "role_id ASC";

    public static final String ROLE_ID_DESC = "role_id DESC";

    public static final String CREATE_TIME_ASC = "create_time ASC";

    public static final String CREATE_TIME_DESC = "create_time DESC";

    public static final String UPDATE_TIME_ASC = "update_time ASC";

    public static final String UPDATE_TIME_DESC = "update_time DESC";

    public Long getId() {
        return id;
    }

    /**
     * @param id 自增ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName 登入名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    /**
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserName() {
        return userName;
    }

    /**
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public Integer getRoleId() {
        return roleId;
    }

    /**
     * @param roleId 角色
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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