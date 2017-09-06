package com.at.entity.mapper;

import com.at.entity.VenAdmin;
import com.at.entity.VenAdminCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface VenAdminMapper {
    @SelectProvider(type=VenAdminSqlProvider.class, method="countByExample")
    long countByExample(VenAdminCriteria example);

    @DeleteProvider(type=VenAdminSqlProvider.class, method="deleteByExample")
    int deleteByExample(VenAdminCriteria example);

    @Delete({
        "delete from ven_admin",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ven_admin (id, login_name, ",
        "password, user_name, ",
        "state, role_id, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=BIGINT}, #{loginName,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{userName,jdbcType=CHAR}, ",
        "#{state,jdbcType=INTEGER}, #{roleId,jdbcType=INTEGER}, #{createTime,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VenAdmin record);

    @InsertProvider(type=VenAdminSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(VenAdmin record);

    @SelectProvider(type=VenAdminSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="login_name", property="loginName", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.CHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenAdmin> selectByExample(VenAdminCriteria example);

    @Select({
        "select",
        "id, login_name, password, user_name, state, role_id, create_time, update_time",
        "from ven_admin",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="login_name", property="loginName", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.CHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    VenAdmin selectByPrimaryKey(Long id);

    @UpdateProvider(type=VenAdminSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") VenAdmin record, @Param("example") VenAdminCriteria example);

    @UpdateProvider(type=VenAdminSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") VenAdmin record, @Param("example") VenAdminCriteria example);

    @UpdateProvider(type=VenAdminSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(VenAdmin record);

    @Update({
        "update ven_admin",
        "set login_name = #{loginName,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "user_name = #{userName,jdbcType=CHAR},",
          "state = #{state,jdbcType=INTEGER},",
          "role_id = #{roleId,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(VenAdmin record);

    @SelectProvider(type=com.at.entity.mapper.VenAdminSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="login_name", property="loginName", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.CHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    VenAdmin getByExample(VenAdminCriteria example);

    @Select({
    "select * from",
    "ven_admin"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="login_name", property="loginName", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.CHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenAdmin> selectAll();
}