package test.jsoup;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

public class JsoupTest {

    public static void main(String[] args) throws Exception {


        //testUrl();
        //testString();
        //testFile();
        //testDOM();
        //testData();
        //testSelector();
        testSelector2();


    }

    private static void testSelector2() throws  Exception {

        //通过组合选择器查找元素

        //解析文件获取Document
        Document document = Jsoup.parse(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        //1.元素+ID组合
        Elements select = document.select("div#at_the_end");
        System.out.println(select.text());
        //2.元素+Class组合
        Elements select1 = document.select("div.center");
        System.out.println(select1);
        //3.元素+属性组合
        Elements select2 = document.select("div[abc]");
        System.out.println(select2.text());
        //任意组合
        Elements select3 = document.select("div#at_the_end.center[abc]");
        System.out.println(select3);
        //层级选择
        Elements table = document.select("[method=post] tr");

        for (Element element :table){
            System.out.println(element);
        }
        //查找直接子元素
        Elements select4 = document.select("tr > td > label");
        for (Element element:select4){
            System.out.println(element.text());
        }
        //查找某个父元素下所有直接子元素
        Elements select5 = document.select("table >*");
        for (Element element:select5){
            System.out.println(element.text());
        }



    }

    private static void testSelector()throws Exception {
        //通过选择器查找元素

        //解析文件获取Document
        Document document = Jsoup.parse(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        //1.通过标签查找元素
        Elements div = document.select("div");
        System.out.println(div.text());
        //2.通过ID查找元素
        Elements at_the_end = document.select("#at_the_end");
        System.out.println(at_the_end.text());
        //3.通过Class查找元素
        Elements select = document.select(".v-spacer");
        System.out.println(select.text());
        //4.通过属性查找元素
        Elements elements = document.select("[abc]");
        System.out.println(elements.text());
        //5.通过属性值查找元素
        Elements elements1 = document.select("[abc=abc]");
        for (Element element:elements1){
            System.out.println(element);
        }

    }

    private static void testData() throws  Exception{
        //解析文件获取Document
        Document document = Jsoup.parse(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        //1.根据ID获取元素
        Element at_the_end = document.getElementById("at_the_end");
        //从元素中获取数据


        //1.从元素中获取ID
        String id = at_the_end.id();
        System.out.println(id);
        //2.从元素中获取ClassName
        Set<String> classSet = at_the_end.classNames();
        for (String str:classSet){
            System.out.println(str);
        }
        //3.从原数据中获取属性的值
        String attr = at_the_end.attr("attr");
        System.out.println(attr);
        //4.从原数据获取所有属性
        Attributes attributes = at_the_end.attributes();
        System.out.println(attributes.toString());
        //5.从元素中获取文本内容
        String text = at_the_end.text();
        System.out.println(text);


    }

    private static void testDOM() throws IOException {
        //解析文件获取Document
        Document document = Jsoup.parse(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        //获取元素
        
        //1.根据ID获取元素
        Element at_the_end = document.getElementById("at_the_end");
        System.out.println(at_the_end);
        //2.根据标签获取元素
        Elements input = document.getElementsByTag("input");
        System.out.println(input);
        //3.根据Class获取元素
        Elements center = document.getElementsByClass("center");
        System.out.println(center);
        //4.根据属性获取元素
        Elements colspan = document.getElementsByAttribute("colspan");
        System.out.println(colspan);
        Elements elementsByAttributeValue = document.getElementsByAttributeValue("abc", "abc");
        System.out.println(elementsByAttributeValue);


    }

    private static void testFile() throws IOException {
        //Jsoup解析文件
        //解析文件
        Document document = Jsoup.parse(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        Element first = document.getElementsByTag("h1").first();
        System.out.println(first.text());
    }

    private static void testString() throws Exception {
        //Jsoup解析字符串
        //使用工具类读取文件获取字符串
        String content = FileUtils.readFileToString(new File(JsoupTest.class.getClassLoader().getResource("JsoupTestHtml.html").getPath()), "utf8");
        Document document = Jsoup.parse(content);
        Element element = document.getElementsByTag("h1").first();
        System.out.println(element.text());


    }

    private static void testUrl() throws Exception {
        //Jsoup解析URL
        //解析url地址第一个参数是访问的url 第二个参数是访问时的超时时间
        Document document = Jsoup.parse(new URL("http://120.79.213.36/2.6/web/pages/UI.php"), 5000);

        Element elements = document.getElementsByTag("h1").first();
        System.out.println(elements.text());

    }




}
