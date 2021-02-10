package cn.xanderye.service;

import cn.xanderye.util.TemplateUtil;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author XanderYe
 * @description:
 * @date 2021/2/9 17:52
 */
public class GenerateService {

    private static final List<String> BASE_FILE_LIST = Arrays.asList("BaseMapper", "BaseService", "BaseServiceImpl");

    /**
     * 生成base包
     * @param groupId
     * @param artifactId
     * @param author
     * @param targetPath
     * @return void
     * @author XanderYe
     * @date 2021/2/9
     */
    public void generateBase(String groupId, String artifactId, String author, String date, String targetPath) throws IOException, TemplateException {
        String packageName = getPackageName(groupId, artifactId);
        Map<String, Object> map = new HashMap<>(16);
        map.put("package", packageName);
        map.put("author", author);
        map.put("date", date);
        String packagePath = targetPath + "/" + packageName.replace(".", "/");
        for (String fileName : BASE_FILE_LIST) {
            TemplateUtil.generateFromResources(map, "template", fileName + ".ftl", packagePath + "/base", fileName + ".java");
        }
    }

    /**
     * 生成mapper、service、impl包
     * @param groupId
     * @param artifactId
     * @param author
     * @param date
     * @param name
     * @param targetPath
     * @return void
     * @author XanderYe
     * @date 2021/2/9
     */
    public void generateService(String groupId, String artifactId, String author, String date, String name, String targetPath) throws IOException, TemplateException {
        String packageName = getPackageName(groupId, artifactId);
        // 首字母大写
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        Map<String, Object> map = new HashMap<>(16);
        map.put("package", packageName);
        map.put("author", author);
        map.put("date", date);
        map.put("name", name);
        String packagePath = targetPath + "/" + packageName.replace(".", "/");
        TemplateUtil.generateFromResources(map, "template",  "MapperTemplate.ftl", packagePath + "/mapper", name + "Mapper.java");
        TemplateUtil.generateFromResources(map, "template",  "ServiceTemplate.ftl", packagePath + "/service", name + "Service.java");
        TemplateUtil.generateFromResources(map, "template",  "ServiceImplTemplate.ftl", packagePath + "/service/impl", name + "ServiceImpl.java");
    }

    private String getPackageName(String groupId, String artifactId) {
        String packageName = groupId;
        if (null != artifactId && !"".equals(artifactId)) {
            packageName += "." + artifactId;
        }
        return packageName;
    }
}
