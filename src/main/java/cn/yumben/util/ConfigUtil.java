package cn.yumben.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    private static  String CONTENT;

    static {
        InputStream resourceAsStream = ConfigUtil.class.getResourceAsStream("/Product.json");
        CONTENT = toConvertString(resourceAsStream).trim();
    }
    /**
     *  获得json配置文件
     *  */
    public static JSONObject getJSONObject() {
        logger.info(CONTENT);
        JSONObject jsonObject = new JSONObject(CONTENT);
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

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line + "");
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }


}
