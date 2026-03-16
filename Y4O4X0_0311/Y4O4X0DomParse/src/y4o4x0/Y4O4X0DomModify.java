package y4o4x0;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Y4O4X0DomModify {

    private static final File xmlFile = new File("Y4O4X0_0311\\Y4O4X0DomParse\\src\\xml\\Y4O4X0_etterem.xml");
    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuiler = factory.newDocumentBuilder();

        Document doc = dBuiler.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList venedegList = doc.getElementsByTagName("vendeg");

        Node vendegToModify = venedegList.item(0);
        System.out.println(vendegToModify.getTextContent());

        if(vendegToModify != null  && vendegToModify.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) vendegToModify;

            Node n1 = e.getElementsByTagName("nev").item(0);

            n1.setTextContent("Kovács Petra");

            System.out.println(n1.getTextContent());
        }

        /* 
        for(int i = 0; i < venedegList.getLength(); i++) {
            
        }*/

        
    }
}