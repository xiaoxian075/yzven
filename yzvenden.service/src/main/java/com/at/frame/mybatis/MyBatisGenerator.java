package com.at.frame.mybatis;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
public class MyBatisGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisGenerator.class);

    public static void main(String[] args){
        generator();
    }

    private static String generatorConfigFile = "/mybatis-generator.xml" ;

    public static void setGeneratorConfigFile(String filePath){
        generatorConfigFile = filePath;
    }


    public static String getGeneratorConfigFile() {
        return generatorConfigFile;
    }

    public static void generator() {
        String configFilePath = System.getProperty("user.dir").concat(getGeneratorConfigFile());
        File configFile = null;
        try {
            Path path = Paths.get(MyBatisGenerator.class.getClassLoader().getResource("").toURI());
            Path fullPath = Paths.get(path.toString(),getGeneratorConfigFile());
            LOGGER.info("mybatis generator . file path == {}",fullPath);
            configFile = fullPath.toFile();
        } catch (URISyntaxException e) {
            LOGGER.error("找不到文件",e);
        }
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            org.mybatis.generator.api.MyBatisGenerator myBatisGenerator = new org.mybatis.generator.api.MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (IOException | XMLParserException |InterruptedException | InvalidConfigurationException | SQLException e) {
            LOGGER.error("exception",e);
        }
    }
}
