package com.at.entity.mapper;

import com.at.entity.VenRole;
import com.at.entity.VenRoleCriteria.Criteria;
import com.at.entity.VenRoleCriteria.Criterion;
import com.at.entity.VenRoleCriteria;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class VenRoleSqlProvider {

    public String countByExample(VenRoleCriteria example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("ven_role");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(VenRoleCriteria example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("ven_role");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(VenRole record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("ven_role");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getState() != null) {
            sql.VALUES("state", "#{state,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=BIGINT}");
        }
        
        if (record.getMenuList() != null) {
            sql.VALUES("menu_list", "#{menuList,jdbcType=LONGVARBINARY}");
        }
        
        return sql.toString();
    }

    public String selectByExampleWithBLOBs(VenRoleCriteria example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("name");
        sql.SELECT("state");
        sql.SELECT("create_time");
        sql.SELECT("update_time");
        sql.SELECT("menu_list");
        sql.FROM("ven_role");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String selectByExample(VenRoleCriteria example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("name");
        sql.SELECT("state");
        sql.SELECT("create_time");
        sql.SELECT("update_time");
        sql.FROM("ven_role");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        VenRole record = (VenRole) parameter.get("record");
        VenRoleCriteria example = (VenRoleCriteria) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("ven_role");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=INTEGER}");
        }
        
        if (record.getName() != null) {
            sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        }
        
        if (record.getState() != null) {
            sql.SET("state = #{record.state,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{record.createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{record.updateTime,jdbcType=BIGINT}");
        }
        
        if (record.getMenuList() != null) {
            sql.SET("menu_list = #{record.menuList,jdbcType=LONGVARBINARY}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExampleWithBLOBs(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("ven_role");
        
        sql.SET("id = #{record.id,jdbcType=INTEGER}");
        sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        sql.SET("state = #{record.state,jdbcType=INTEGER}");
        sql.SET("create_time = #{record.createTime,jdbcType=BIGINT}");
        sql.SET("update_time = #{record.updateTime,jdbcType=BIGINT}");
        sql.SET("menu_list = #{record.menuList,jdbcType=LONGVARBINARY}");
        
        VenRoleCriteria example = (VenRoleCriteria) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("ven_role");
        
        sql.SET("id = #{record.id,jdbcType=INTEGER}");
        sql.SET("name = #{record.name,jdbcType=VARCHAR}");
        sql.SET("state = #{record.state,jdbcType=INTEGER}");
        sql.SET("create_time = #{record.createTime,jdbcType=BIGINT}");
        sql.SET("update_time = #{record.updateTime,jdbcType=BIGINT}");
        
        VenRoleCriteria example = (VenRoleCriteria) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(VenRole record) {
        SQL sql = new SQL();
        sql.UPDATE("ven_role");
        
        if (record.getName() != null) {
            sql.SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getState() != null) {
            sql.SET("state = #{state,jdbcType=INTEGER}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=BIGINT}");
        }
        
        if (record.getMenuList() != null) {
            sql.SET("menu_list = #{menuList,jdbcType=LONGVARBINARY}");
        }
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, VenRoleCriteria example, boolean includeExamplePhrase) {
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