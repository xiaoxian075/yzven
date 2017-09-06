package com.at.frame.mybatis.generator;

import io.swagger.annotations.ApiModelProperty;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.Properties;

/**
 * Created by Administrator on 2017/5/3.
 * 重写自动生成类注释
 */
public class MyCommentGenerator implements CommentGenerator{

    /**是否自动填充swagger2api的自动生成注解*/
    private boolean swagger;

    public MyCommentGenerator() {
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    public void addComment(XmlElement xmlElement) {
    }

    public void addRootComment(XmlElement rootElement) {
    }

    public void addConfigurationProperties(Properties properties) {
        swagger = Boolean.parseBoolean(properties.getProperty("swagger"));
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
    }

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if(swagger){
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        }
    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remake = introspectedColumn.getRemarks();
        if(remake != null && remake.length() > 0){
            field.addJavaDocLine("/*");
            field.addJavaDocLine("* " + remake);
            field.addJavaDocLine("*/");
            if(swagger){
                field.addAnnotation("@ApiModelProperty(value=\""+remake+"\")");
            }
        }
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        method.addJavaDocLine("/**");
        Parameter parm = (Parameter)method.getParameters().get(0);
        method.addJavaDocLine(" * @param " + parm.getName() + " " + introspectedColumn.getRemarks());
        method.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
    }
}
