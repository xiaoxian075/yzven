package com.at.entity.mapper;

import com.at.entity.VenMenu;
import com.at.entity.VenMenuCriteria;
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

public interface VenMenuMapper {
    @SelectProvider(type=VenMenuSqlProvider.class, method="countByExample")
    long countByExample(VenMenuCriteria example);

    @DeleteProvider(type=VenMenuSqlProvider.class, method="deleteByExample")
    int deleteByExample(VenMenuCriteria example);

    @Delete({
        "delete from ven_menu",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into ven_menu (id, parent_id, ",
        "url, menu_name, menu_level, ",
        "menu_show, power_list, ",
        "sort, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=INTEGER}, #{parentId,jdbcType=INTEGER}, ",
        "#{url,jdbcType=VARCHAR}, #{menuName,jdbcType=VARCHAR}, #{menuLevel,jdbcType=INTEGER}, ",
        "#{menuShow,jdbcType=INTEGER}, #{powerList,jdbcType=VARCHAR}, ",
        "#{sort,jdbcType=INTEGER}, #{createTime,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VenMenu record);

    @InsertProvider(type=VenMenuSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(VenMenu record);

    @SelectProvider(type=VenMenuSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_level", property="menuLevel", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_show", property="menuShow", jdbcType=JdbcType.INTEGER),
        @Result(column="power_list", property="powerList", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenMenu> selectByExample(VenMenuCriteria example);

    @Select({
        "select",
        "id, parent_id, url, menu_name, menu_level, menu_show, power_list, sort, create_time, ",
        "update_time",
        "from ven_menu",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_level", property="menuLevel", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_show", property="menuShow", jdbcType=JdbcType.INTEGER),
        @Result(column="power_list", property="powerList", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    VenMenu selectByPrimaryKey(Integer id);

    @UpdateProvider(type=VenMenuSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") VenMenu record, @Param("example") VenMenuCriteria example);

    @UpdateProvider(type=VenMenuSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") VenMenu record, @Param("example") VenMenuCriteria example);

    @UpdateProvider(type=VenMenuSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(VenMenu record);

    @Update({
        "update ven_menu",
        "set parent_id = #{parentId,jdbcType=INTEGER},",
          "url = #{url,jdbcType=VARCHAR},",
          "menu_name = #{menuName,jdbcType=VARCHAR},",
          "menu_level = #{menuLevel,jdbcType=INTEGER},",
          "menu_show = #{menuShow,jdbcType=INTEGER},",
          "power_list = #{powerList,jdbcType=VARCHAR},",
          "sort = #{sort,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(VenMenu record);

    @SelectProvider(type=com.at.entity.mapper.VenMenuSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_level", property="menuLevel", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_show", property="menuShow", jdbcType=JdbcType.INTEGER),
        @Result(column="power_list", property="powerList", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    VenMenu getByExample(VenMenuCriteria example);

    @Select({
    "select * from",
    "ven_menu"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.INTEGER),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_name", property="menuName", jdbcType=JdbcType.VARCHAR),
        @Result(column="menu_level", property="menuLevel", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_show", property="menuShow", jdbcType=JdbcType.INTEGER),
        @Result(column="power_list", property="powerList", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenMenu> selectAll();
}