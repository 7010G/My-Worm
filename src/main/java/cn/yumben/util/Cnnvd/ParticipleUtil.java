package cn.yumben.util.Cnnvd;

import cn.yumben.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

/**
 * Copyright © 2018yumben. All rights reserved.
 * <p>
 *"[a-zA-Z + - * _  中]"
 * @Description: TODO
 * 2019/8/23/08:51
 * @author: ZZG
 * @version: 1.0
 */
public class ParticipleUtil {

    private final  Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    private final String regex = "[a-zA-Z + - * _  中]";
    /**
     * 分词
     *
     * @param content 漏洞报告
     * @return 分词结果
     * @throws IOException
     */
    public  List<String> getParticipleList(String content) throws IOException {
        //匹配数字
        Pattern pattern = Pattern.compile(".*\\d+.*");
        //歧义词组
        ArrayList<String> homographs = new ArrayList<String>();
        homographs.add("至");
        homographs.add("及之前");
        homographs.add("之前");
        homographs.add("到");
        homographs.add("之前的");

        ArrayList<String> stringArrayList = new ArrayList<>();
        //使用lucene实现
        String text = content;
        //开始分词
        StringReader sr = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            String lexemeText = lex.getLexemeText();
            System.out.print(lexemeText + "|");
            //计算.出现的次数
            int count = (lexemeText.length() - lexemeText.replace(".", "").length()) / ".".length();
            //截取版本号
            if (pattern.matcher(lexemeText).matches() && lexemeText.length() >= 2 && count >= 1) {
                //截取如7.0.0,7.0.93的版本号
                if (lexemeText.contains(",")) {
                    String[] split = lexemeText.split(",");
                    for (int i = 0; i < split.length; i++) {
                        stringArrayList.add(split[i].replaceAll(regex, ""));
                    }
                    //截取如5.5.0-5.5.35 的版本号一个while循环只包含一组
                } else if (lexemeText.contains("-")) {
                    String[] split = lexemeText.split("-");
                    for (int i = 0; i < split.length; i++) {
                        //如果不是最后一个
                        if (i + 1 != split.length) {
                            //按照-拆分的 如 7.0.1-7.0.5 数据结构为[7.0.1,7.0.5]
                            if (pattern.matcher(split[i]).matches() && pattern.matcher(split[i + 1]).matches() && split[i + 1].contains(".")) {
                                stringArrayList.add(split[i].replaceAll(regex, ""));
                                stringArrayList.add("至");
                            }
                        } else {
                            stringArrayList.add(split[i].replaceAll(regex, ""));
                        }
                    }
                } else {
                    //将版本号中的字母换成""
                    stringArrayList.add(lexemeText.replaceAll(regex, ""));
                }
            }
            //截取歧义词组
            for (String word : homographs) {
                if (lexemeText.equals(word)) {
                    stringArrayList.add(lexemeText);
                }
            }
        }
        for (String s : stringArrayList) {
            logger.info(s);
        }
        return stringArrayList;
    }

    /**
     * 匹配版本，如果匹配，返回true
     *
     * @param versionObject   产品版本号
     * @param stringArrayList 漏洞报告内的版本信息
     * @return
     */
    public  boolean matchVersion(String versionObject, List<String> stringArrayList) {
        //匹配数字
        Pattern pattern = Pattern.compile(".*\\d+.*");
        double version = getVersion(versionObject);
        logger.info("versionObject:" + version);
        for (int i = 0; i < stringArrayList.size(); i++) {
            if (stringArrayList.size() > 1 && i + 1 != stringArrayList.size()) {
                String listVersion = stringArrayList.get(i);
                String homographsText = stringArrayList.get(i + 1);
                if (listVersion.contains(".")&&listVersion.length()!=1) {
                    logger.info("listVersion:" + listVersion);
                    double doubleVersion = getVersion(listVersion);
                    if (homographsText.equals("之前")) {
                        if (doubleVersion > version) {
                            return true;
                        }
                    } else if (homographsText.equals("及之前")) {
                        if (doubleVersion >= version) {
                            return true;
                        }
                    } else if (homographsText.equals("至") || homographsText.equals("到")) {
                        if (i + 2 < stringArrayList.size()) {
                            String versionText = stringArrayList.get(i + 2);
                            //避免出现因截取失误而出现的异常 如 1.0至至至
                            if (pattern.matcher(versionText).matches()) {
                                Double end = getVersion(versionText);
                                if (doubleVersion <= version && version <= end) {
                                    return true;
                                }
                            }
                        }

                    }else if (homographsText.equals("之前的")){
                        if (i + 2 < stringArrayList.size()) {
                            String versionText = stringArrayList.get(i + 2);
                            if (pattern.matcher(versionText).matches()) {
                                Double end = getVersion(versionText);
                                if (doubleVersion > version && version >= end && Objects.equals(getVersion(versionObject.substring(0, 1)), getVersion(versionText.substring(0, 1)))) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 转换版本号
     *
     * @param versionText 版本号String
     * @return 版本号Double
     */
    public  Double getVersion(String versionText) {
        Double version = null;
        int count = (versionText.length() - versionText.replace(".", "").length()) / ".".length();
        StringBuilder stringBuilder = new StringBuilder(versionText.replace("_",""));
        int i = versionText.lastIndexOf(".");

        if (count == 2) {
            String replaceContent = stringBuilder.replace(i, i + 1, "").toString();
            version = parseDouble(replaceContent);
        } else if (count > 2) {
            String replaceContent = stringBuilder.replace(i,i + 1, "").toString();
            version = getVersion(replaceContent);
        } else if (count < 2) {
            version = parseDouble(versionText);
        }
        return version;
    }
}
