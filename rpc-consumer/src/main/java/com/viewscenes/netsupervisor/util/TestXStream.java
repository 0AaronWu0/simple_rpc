package com.viewscenes.netsupervisor.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.viewscenes.netsupervisor.entity.InfoUser;

import java.util.Map;

public class TestXStream {

    public static void main(String[] args) {
        //采用非注解方式将对象转xml
        String resultXml = beanToXml();
        System.out.println(resultXml);
        System.out.println("-------------------------------------------------------------------");
        InfoUser infoUser = xmlToBean(resultXml);
        System.out.println(infoUser);
        System.out.println("*******************************************************************");

        //resultXml = "<map><stars><star><id>1</id><name>littleflower</name></star><star><id>2</id><name>littleyellow</name></star></stars>   <filename>cnlab</filename>   <ra>147.0</ra>   <dec>0.0</dec>   <plate>0.0</plate>   <mdj>0.0</mdj> </map> ";

        resultXml = "<map>" + resultXml + "</map>";
        //创建xstream对象
        XStream xStream = new XStream();
        xStream.registerConverter(new XmlMapConverter());
        Map<String, Object> map = (Map<String, Object>)xStream.fromXML(resultXml);
        System.out.println(map);
        resultXml = xStream.toXML(map);
        System.out.println(resultXml);
        System.out.println((Map<String, Object>)xStream.fromXML(resultXml));
    }


    private static InfoUser xmlToBean(String resultXml) {
        //创建xstream对象
        XStream xStream = new XStream();
        xStream.processAnnotations(InfoUser.class);
        //将别名与xml名字对应
        //xStream.alias("INFOUSER", InfoUser.class);
        //将字符串类型的xml转换为对象
        InfoUser infoUser = (InfoUser)xStream.fromXML(resultXml);
        return infoUser;
    }

    private static String beanToXml() {
        InfoUser infoUser= new InfoUser();
        infoUser.setId(0L);
        infoUser.setIp("123");
        infoUser.setTimes(0L);
        infoUser.setUserName("@＃$&^％％@&＊!");
        infoUser.setUserSex("123");
        infoUser.setPassWord("44455");

        //创建xstream对象
        XStream xStream = new XStream();
        //给指定类起别名
        //xStream.alias("INFOUSER", InfoUser.class);
        xStream.processAnnotations(InfoUser.class);
        //将对象转换成xml字符串
        String xml = xStream.toXML(infoUser);

        return xml;
    }
}
