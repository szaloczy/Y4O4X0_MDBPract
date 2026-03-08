package Y4O4X0DOMParse.src.y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Y4O4X0DomRead {

    public static void main(String[] args) throws Exception {
        File xmlFile = new File("Y4O4X0_0304/Y4O4X0DomParse/src/xml/Y4O4X0_etterem.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String rooName = root.getTagName();
        System.out.println("Root element: " + rooName);

        NodeList nList = doc.getElementsByTagName("etterem");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: "+ nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                String id = element.getAttribute("ekod");

                Node n1 = element.getElementsByTagName("nev").item(0);
                String name = n1.getTextContent();

                Node n2 = element.getElementsByTagName("varos").item(0);
                String city = n2.getTextContent();

                Node n3 = element.getElementsByTagName("utca").item(0);
                String street = n3.getTextContent();

                Node n4 = element.getElementsByTagName("hazszam").item(0);
                String number = n4.getTextContent();

                Node n5 = element.getElementsByTagName("csillag").item(0);
                String star = n5.getTextContent();

                String address = city + ", " + street + " utca " + number + ".";

                System.out.println("Étterem id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Cím: " + address);
                System.out.println("Csillag: " + star);
            }
        }

        printFoszakacs(doc);
        printSzakacs(doc);
    }

    private static void printFoszakacs(Document doc) {
        NodeList nList = doc.getElementsByTagName("foszakacs");

        for(int i=0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: " + nNode.getNodeName());

            if(nNode.getNodeType() ==Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                String id = element.getAttribute("fkod");

                Node n1 = element.getElementsByTagName("nev").item(0);
                String name = n1.getTextContent();

                Node n2 = element.getElementsByTagName("eletkor").item(0);
                String age = n2.getTextContent();

                System.out.println("Foszakács id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Életkor: " + age);

                NodeList nList2 = element.getElementsByTagName("vegzettseg");
                for(int j = 0; j < nList2.getLength(); j++) {
                    Node n3 = (Element) nList2.item(j);
                    String degree = n3.getTextContent();
                    System.out.println("Végzettség: " + degree);
                }

            }
        }

    }

    private static void printSzakacs(Document doc) {
        NodeList nList = doc.getElementsByTagName("szakacs");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                String id = element.getAttribute("szkod");

                Node n1 = element.getElementsByTagName("nev").item(0);
                String name = n1.getTextContent();

                Node n2 = element.getElementsByTagName("reszleg").item(0);
                String part = n2.getTextContent();

                System.out.println("Szakács id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Részleg: " + part);

                NodeList nList2 = element.getElementsByTagName("vegzettseg");

                getMultipleValues(nList2, name);
            }
        }
    }

    private static void getMultipleValues(NodeList nList, String tagName) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node n = (Element) nList.item(i);
            String value = n.getTextContent();
            System.out.println(tagName+ ": " + value);
        }
    }
}
