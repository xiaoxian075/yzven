package com.at.entity.mapper;

import com.at.entity.VenRole;
import com.at.entity.VenRoleCriteria;
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

public interface VenRoleMapper {
    @SelectProvider(type=VenRoleSqlProvider.class, method="countByExample")
    long countByExample(VenRoleCriteria example);

    @DeleteProvider(type=VenRoleSqlProvider.class, method="deleteByExample")
    int deleteByExample(VenRoleCriteria example);

    @Delete({
        "delete from ven_role",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into ven_role (id, name, ",
        "state, create_time, ",
        "update_time, menu_list)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{state,jdbcType=INTEGER}, #{createTime,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=BIGINT}, #{menuList,jdbcType=LONGVARBINARY})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VenRole record);

    @InsertProvider(type=VenRoleSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(VenRole record);

    @SelectProvider(type=VenRoleSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT),
        @Result(column="menu_list", property="menuList", jdbcType=JdbcType.LONGVARBINARY)
    })
    List<VenRole> selectByExampleWithBLOBs(VenRoleCriteria example);

    @SelectProvider(type=VenRoleSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenRole> selectByExample(VenRoleCriteria example);

    @Select({
        "select",
        "id, name, state, create_time, update_time, menu_list",
        "from ven_role",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT),
        @Result(column="menu_list", property="menuList", jdbcType=JdbcType.LONGVARBINARY)
    })
    VenRole selectByPrimaryKey(Integer id);

    @UpdateProvider(type=VenRoleSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") VenRole record, @Param("example") VenRoleCriteria example);

    @UpdateProvider(type=VenRoleSqlProvider.class, method="updateByExampleWithBLOBs")
    int updateByExampleWithBLOBs(@Param("record") VenRole record, @Param("example") VenRoleCriteria example);

    @UpdateProvider(type=VenRoleSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") VenRole record, @Param("example") VenRoleCriteria example);

    @UpdateProvider(type=VenRoleSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(VenRole record);

    @Update({
        "update ven_role",
        "set name = #{name,jdbcType=VARCHAR},",
          "state = #{state,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=BIGINT},",
          "menu_list = #{menuList,jdbcType=LONGVARBINARY}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(VenRole record);

    @Update({
        "update ven_role",
        "set name = #{name,jdbcType=VARCHAR},",
          "state = #{state,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(VenRole record);

    @SelectProvider(type=com.at.entity.mapper.VenRoleSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    VenRole getByExample(VenRoleCriteria example);

    @Select({
    "select * from",
    "ven_role"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="state", property="state", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.BIGINT)
    })
    List<VenRole> selectAll();
}