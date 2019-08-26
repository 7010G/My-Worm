package cn.yumben.test.demo;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Copyright © 2018yunben. All rights reserved.
 * <p>
 *
 * @Description: TODO
 * 2019/8/23/08:51
 * @author: ZZG
 * @version: 1.0
 */
public class RegularExpression {


    public static void main(String[] args) throws IOException {

        ArrayList<String> strings = new ArrayList<String>();
        strings.add("至");
        strings.add("及之前");
        strings.add("之前");
        //使用lucene实现
        String text = "Apache Tomcat是美国阿帕奇（Apache）及之前软件基金会的一款轻量级Web应用服务器。该程序实现了对Servlet和JavaServer Page（JSP）的支持。 Apache Tomcat 9.0.0.M1版本至9.0.0.17版本、8.5.0版本至8.5.39版本和7.0.0版本至7.0.93版本中存在跨站脚本漏洞。该漏洞源于WEB应用缺少对客户端数据的正确验证。攻击者可利用该漏洞执行客户端代码。";
        StringReader sr = new StringReader(text.replace("版本",""));
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;
        while ((lex = ik.next()) != null) {
            System.out.print(lex.getLexemeText() + "|");
        }
    }
}
