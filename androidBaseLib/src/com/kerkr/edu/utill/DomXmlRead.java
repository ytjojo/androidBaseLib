package com.kerkr.edu.utill;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 使用Dom解析xml文件
 */
public class DomXmlRead {
    
    //    /*----银联支付成功返回的xml
    public static String readXML(InputStream inStream) {
        String respCode = null;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(inStream);
            Element root = dom.getDocumentElement();
            
            NodeList childNodes = root.getChildNodes();
            
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(childNodes.item(i).getNodeName() + ":" + childNodes.item(i).getFirstChild().getNodeValue());
                    if ("respCode".equals(childNodes.item(i).getNodeName())) {
                        respCode = childNodes.item(i).getFirstChild().getNodeValue();
                        
                    }
                }
            }
            
            inStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return respCode;
    }
}
