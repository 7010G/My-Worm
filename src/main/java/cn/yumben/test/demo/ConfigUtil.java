package cn.yumben.test.demo;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Copyright © 2018yunben. All rights reserved.
 * <p>
 * This class contains methods to read the json configuration file and get the
 * data
 *
 * @Title: ConfigUtil.java
 * @Prject: Delete_all_the_amount
 * @Package: com.yunben.common
 * @Description: TODO
 * @author: ZZG
 * @date: 2018年9月21日 上午10:31:24
 * @version: V1.0
 */

public class ConfigUtil {
    private final static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    private static final String URL = ConfigUtil.class.getClassLoader().getResource("Product.json").getPath();

    /* 获得json配置文件 */
    public static JSONObject getJSONObject() {
        logger.info(URL);
        File file = new File(URL);
        JSONObject jsonObject = null;
        try {
            String content = FileUtils.readFileToString(file, "utf-8");
            jsonObject = new JSONObject(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getValue(String strKey, String valueName) {
        JSONObject jsonObject = getJSONObject();
        JSONObject object = (JSONObject) jsonObject.get(strKey);
        String string = object.getString(valueName);
        return string;
    }

    public static JSONArray getValues(String strKey, String valueName) {
        JSONObject jsonObject = getJSONObject();
        JSONObject object = (JSONObject) jsonObject.get(strKey);
        JSONArray jsonArray = object.getJSONArray(valueName);
        return jsonArray;
    }

}
