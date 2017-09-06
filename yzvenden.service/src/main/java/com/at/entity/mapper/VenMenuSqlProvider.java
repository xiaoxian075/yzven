package com.at.entity.mapper;

import com.at.entity.VenMenu;
import com.at.entity.VenMenuCriteria.Criteria;
import com.at.entity.VenMenuCriteria.Criterion;
import com.at.entity.VenMenuCriteria;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class VenMenuSqlProvider {

    public String countByExample(VenMenuCriteria example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("ven_menu");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(VenMenuCriteria example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("ven_menu");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(VenMenu record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("ven_menu");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getParentId() != null) {
            sql.VALUES("parent_id", "#{parentId,jdbcType=INTEGER}");
        }
        
        if (record.getUrl() != null) {
            sql.VALUES("url", "#{url,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuName() != null) {
            sql.VALUES("menu_name", "#{menuName,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuLevel() != null) {
            sql.VALUES("menu_level", "#{menuLevel,jdbcType=INTEGER}");
        }
        
        if (record.getMenuShow() != null) {
            sql.VALUES("menu_show", "#{menuShow,jdbcType=INTEGER}");
        }
        
        if (record.getPowerList() != null) {
            sql.VALUES("power_list", "#{powerList,jdbcType=VARCHAR}");
        }
        
        if (record.getSort() != null) {
            sql.VALUES("sort", "#{sort,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=BIGINT}");
        }
        
        return sql.toString();
    }

    public String selectByExample(VenMenuCriteria example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("parent_id");
        sql.SELECT("url");
        sql.SELECT("menu_name");
        sql.SELECT("menu_level");
        sql.SELECT("menu_show");
        sql.SELECT("power_list");
        sql.SELECT("sort");
        sql.SELECT("create_time");
        sql.SELECT("update_time");
        sql.FROM("ven_menu");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        VenMenu record = (VenMenu) parameter.get("record");
        VenMenuCriteria example = (VenMenuCriteria) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("ven_menu");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=INTEGER}");
        }
        
        if (record.getParentId() != null) {
            sql.SET("parent_id = #{record.parentId,jdbcType=INTEGER}");
        }
        
        if (record.getUrl() != null) {
            sql.SET("url = #{record.url,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuName() != null) {
            sql.SET("menu_name = #{record.menuName,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuLevel() != null) {
            sql.SET("menu_level = #{record.menuLevel,jdbcType=INTEGER}");
        }
        
        if (record.getMenuShow() != null) {
            sql.SET("menu_show = #{record.menuShow,jdbcType=INTEGER}");
        }
        
        if (record.getPowerList() != null) {
            sql.SET("power_list = #{record.powerList,jdbcType=VARCHAR}");
        }
        
        if (record.getSort() != null) {
            sql.SET("sort = #{record.sort,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{record.createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{record.updateTime,jdbcType=BIGINT}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("ven_menu");
        
        sql.SET("id = #{record.id,jdbcType=INTEGER}");
        sql.SET("parent_id = #{record.parentId,jdbcType=INTEGER}");
        sql.SET("url = #{record.url,jdbcType=VARCHAR}");
        sql.SET("menu_name = #{record.menuName,jdbcType=VARCHAR}");
        sql.SET("menu_level = #{record.menuLevel,jdbcType=INTEGER}");
        sql.SET("menu_show = #{record.menuShow,jdbcType=INTEGER}");
        sql.SET("power_list = #{record.powerList,jdbcType=VARCHAR}");
        sql.SET("sort = #{record.sort,jdbcType=INTEGER}");
        sql.SET("create_time = #{record.createTime,jdbcType=BIGINT}");
        sql.SET("update_time = #{record.updateTime,jdbcType=BIGINT}");
        
        VenMenuCriteria example = (VenMenuCriteria) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(VenMenu record) {
        SQL sql = new SQL();
        sql.UPDATE("ven_menu");
        
        if (record.getParentId() != null) {
            sql.SET("parent_id = #{parentId,jdbcType=INTEGER}");
        }
        
        if (record.getUrl() != null) {
            sql.SET("url = #{url,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuName() != null) {
            sql.SET("menu_name = #{menuName,jdbcType=VARCHAR}");
        }
        
        if (record.getMenuLevel() != null) {
            sql.SET("menu_level = #{menuLevel,jdbcType=INTEGER}");
        }
        
        if (record.getMenuShow() != null) {
            sql.SET("menu_show = #{menuShow,jdbcType=INTEGER}");
        }
        
        if (record.getPowerList() != null) {
            sql.SET("power_list = #{powerList,jdbcType=VARCHAR}");
        }
        
        if (record.getSort() != null) {
            sql.SET("sort = #{sort,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=BIGINT}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, VenMenuCriteria example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}