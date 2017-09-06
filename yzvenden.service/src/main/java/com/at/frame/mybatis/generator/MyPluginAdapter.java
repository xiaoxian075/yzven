package com.at.frame.mybatis.generator;

import com.at.frame.annotation.MapperCustom;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.*;

/**
 * Created by Administrator on 2017/5/3.
 */
public class MyPluginAdapter extends PluginAdapter{
    private Map<String,String> parents = new HashMap<>();

    @Override
    public void setProperties(Properties properties) {
        super.properties = properties;
        String custom = properties.getProperty("custom");
        if(custom != null && custom.length() > 0){
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            try{
                Resource[] resources = resourcePatternResolver.getResources(custom);
                for(Resource resource : resources){
                    MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
                    Class clazz = Class.forName(reader.getClassMetadata().getClassName());
                    MapperCustom mapper = (MapperCustom) clazz.getAnnotation(MapperCustom.class);
                    if(mapper == null) continue;
                    Class children = mapper.value();
                    parents.put(children.getName(),clazz.getName());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public MyPluginAdapter(){

    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

//    @Override
//    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//        topLevelClass.addImportedType("java.util.HashSet");
//        method.addBodyLine(0,"/*");
//        method.addBodyLine("*/");
//        StringBuilder builder = new StringBuilder();
//        method.addBodyLine("SQL sql = new SQL();");
//        method.addBodyLine("HashSet<String> queryFilter = example.queryFilter();");
//        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
//        boolean isNull = false;
//        for(int i=0,s=allColumns.size();i<s;i++){
//            IntrospectedColumn column = allColumns.get(i);
//            if(column == null) continue;
//            String columnName = column.getActualColumnName();
//            if(i == 0){
//                method.addBodyLine("if (example != null && example.isDistinct()) {");
//                method.addBodyLine(builder.append("     sql.SELECT_DISTINCT(\"").append(columnName).append("\");").toString());
//                method.addBodyLine("} else {");
//                builder.setLength(0);
//                method.addBodyLine(builder.append("     sql.SELECT(\"").append(columnName).append("\");").toString());
//                method.addBodyLine("}");
//            }else{
//                if(i == 1 && !isNull){
//                    method.addBodyLine("if ( queryFilter != null) {");
//                }
//                builder.setLength(0);
//                if(!isNull) method.addBodyLine(builder.append("if(!queryFilter.contains(\"").append(columnName).append("\")) sql.SELECT(\"").append(columnName).append("\");").toString());
//                else method.addBodyLine(builder.append("sql.SELECT(\"").append(columnName).append("\");").toString());
//            }
//            if(!isNull && i == s - 1){
//                builder.setLength(0);
//                method.addBodyLine("} else {");
//                i = 0;
//                isNull = true;
//            }
//        }
//        method.addBodyLine("}");
//        builder.setLength(0);
//        method.addBodyLine(builder.append("sql.FROM(\"").append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()).append("\");").toString());
//        method.addBodyLine("applyWhere(sql,example,false);");
//        method.addBodyLine("if (example != null && example.getOrderByClause() != null) {");
//        method.addBodyLine("    sql.ORDER_BY(example.getOrderByClause());");
//        method.addBodyLine("}");
//        method.addBodyLine("return sql.toString();");
//        return super.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
//    }

    /**
     * 重写Criteria类生成
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.setSuperClass("com.at.frame.mybatis.EntityCriteria");
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }


    /**
     * 重写映射实体类生成规则
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String interfaceClazz = topLevelClass.getType().getFullyQualifiedName();
        String parent = parents.get(interfaceClazz);
        if(parent != null){
            FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(parent);
            topLevelClass.addImportedType(fullyQualifiedJavaType);
            topLevelClass.setSuperClass(fullyQualifiedJavaType);
        }
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        if(allColumns != null){
            for(int i=0,size=allColumns.size();i<size;i++){
                IntrospectedColumn column = allColumns.get(i);
                String columnName = column.getActualColumnName();
                StringBuilder sb = new StringBuilder();
                Field fieldAsc = new Field();
                fieldAsc.setFinal(true);
                fieldAsc.setStatic(true);
                fieldAsc.setVisibility(JavaVisibility.PUBLIC);
                fieldAsc.setName(sb.append(columnName.toUpperCase()).append("_ASC").toString());
                fieldAsc.setType(FullyQualifiedJavaType.getStringInstance());
                sb.setLength(0);
                sb.append("\"").append(columnName).append(" ASC\"");
                fieldAsc.setInitializationString(sb.toString());
                topLevelClass.addField(fieldAsc);

                Field fieldDesc = new Field();
                fieldDesc.setFinal(true);
                fieldDesc.setStatic(true);
                fieldDesc.setVisibility(JavaVisibility.PUBLIC);
                sb.setLength(0);
                sb.append(columnName.toUpperCase()).append("_DESC");
                fieldDesc.setName(sb.toString());
                fieldDesc.setType(FullyQualifiedJavaType.getStringInstance());
                sb.setLength(0);
                sb.append("\"").append(columnName).append(" DESC\"");
                fieldDesc.setInitializationString(sb.toString());
                topLevelClass.addField(fieldDesc);
            }
        }
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        if(introspectedColumns != null && introspectedColumns.size() > 0){
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
            IntrospectedColumn primaryKey = introspectedColumns.get(0);
            method.addAnnotation("@Options(useGeneratedKeys = true, keyProperty = \""+primaryKey.getJavaProperty()+"\")");
        }
        return super.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        if(introspectedColumns != null && introspectedColumns.size() > 0){
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
            IntrospectedColumn primaryKey = introspectedColumns.get(0);
            method.addAnnotation("@Options(useGeneratedKeys = true, keyProperty = \""+primaryKey.getJavaProperty()+"\")");
        }
        return super.clientInsertMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String interfaceClazz = interfaze.getType().getFullyQualifiedName();
        String parent = parents.get(interfaceClazz);
        if(parent != null){
            FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(parent);
            interfaze.addImportedType(fullyQualifiedJavaType);
            interfaze.addSuperInterface(fullyQualifiedJavaType);
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select"));

        TreeSet importedTypes = new TreeSet();
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        FullyQualifiedJavaType table = introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(fqjt);
        importedTypes.add(table);

        Method method = new Method("getByExample");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(table);
        method.addParameter(new Parameter(fqjt,"example"));

        StringBuilder sb = new StringBuilder();
        sb.append("@SelectProvider(type=");
        sb.append(introspectedTable.getMyBatis3SqlProviderType());
        sb.append(".class, method=\"");
        sb.append(introspectedTable.getSelectByExampleStatementId());
        sb.append("\")");
        method.addAnnotation(sb.toString());
        addMethod(method,introspectedTable,sb,importedTypes,interfaze);
        sb.setLength(0);

        Method allMethod = new Method("selectAll");
        allMethod.setVisibility(JavaVisibility.PUBLIC);
        allMethod.setReturnType(new FullyQualifiedJavaType("List<" + table.getShortName() + ">"));
        allMethod.addAnnotation("@Select({");
        allMethod.addAnnotation("\"select * from\",");
        sb.append("\"").append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()).append("\"");
        allMethod.addAnnotation(sb.toString());
        allMethod.addAnnotation("})");
        sb.setLength(0);
        addMethod(allMethod,introspectedTable,sb,importedTypes,interfaze);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private void addMethod(Method method,IntrospectedTable introspectedTable,StringBuilder sb,TreeSet importedTypes,Interface interfaze){
        if(introspectedTable.isConstructorBased()) {
            method.addAnnotation("@ConstructorArgs({");
        } else {
            method.addAnnotation("@Results({");
        }

        Iterator iterPk = introspectedTable.getPrimaryKeyColumns().iterator();

        Iterator iterNonPk;
        IntrospectedColumn introspectedColumn;
        for(iterNonPk = introspectedTable.getBaseColumns().iterator(); iterPk.hasNext(); method.addAnnotation(sb.toString())) {
            introspectedColumn = (IntrospectedColumn)iterPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, true, introspectedTable.isConstructorBased()));
            if(iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
        }

        for(; iterNonPk.hasNext(); method.addAnnotation(sb.toString())) {
            introspectedColumn = (IntrospectedColumn)iterNonPk.next();
            sb.setLength(0);
            OutputUtilities.javaIndent(sb, 1);
            sb.append(this.getResultAnnotation(interfaze, introspectedColumn, false, introspectedTable.isConstructorBased()));
            if(iterNonPk.hasNext()) {
                sb.append(',');
            }
        }

        method.addAnnotation("})");

        context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);

        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(
                method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }


    protected String getResultAnnotation(Interface interfaze, IntrospectedColumn introspectedColumn, boolean idColumn, boolean constructorBased) {
        StringBuilder sb = new StringBuilder();
        if(constructorBased) {
            interfaze.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
            sb.append("@Arg(column=\"");
            sb.append(MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", javaType=");
            sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
            sb.append(".class");
        } else {
            sb.append("@Result(column=\"");
            sb.append(MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", property=\"");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append('\"');
        }

        if(StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            interfaze.addImportedType(fqjt);
            sb.append(", typeHandler=");
            sb.append(fqjt.getShortName());
            sb.append(".class");
        }

        sb.append(", jdbcType=JdbcType.");
        sb.append(introspectedColumn.getJdbcTypeName());
        if(idColumn) {
            sb.append(", id=true");
        }

        sb.append(')');
        return sb.toString();
    }

}
