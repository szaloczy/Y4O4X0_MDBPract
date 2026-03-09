package Y4O4X0DOMParse.src.y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Y4O4X0DomReadOwn {
    public static void main(String[] args) throws Exception {
        File xmlFile = new File("Y4O4X0_0304/Y4O4X0DOMParse/src/xml/Y4O4X0XMLOWN.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        System.out.println("Root element: " + doc.getDocumentElement().getTagName());

        printCeg(doc);
        printGyakornok(doc);
    }

    private static void printCeg(Document doc) {
        NodeList nList = doc.getElementsByTagName("ceg");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                String id = element.getAttribute("ckod");

                Node n1 = element.getElementsByTagName("nev").item(0);
                String name = n1.getTextContent();

                Node n2 = element.getElementsByTagName("varos").item(0);
                String city = n2.getTextContent();

                Node n3 = element.getElementsByTagName("utca").item(0);
                String street = n3.getTextContent();

                Node n4 = element.getElementsByTagName("hazszam").item(0);
                String number = n4.getTextContent();

                System.out.println("Id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Város: " + city);
                System.out.println("Utca: " + street);
                System.out.println("Házszám: " + number);
                
            }

        }
    }

    private static void printGyakornok(Document doc) {
        NodeList nList = doc.getElementsByTagName("gyakornok");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                String id = element.getAttribute("gykod");
            
                String name = element.getElementsByTagName("nev").item(0).getTextContent();
                String start = element.getElementsByTagName("kezdet").item(0).getTextContent();
                String duration = element.getElementsByTagName("idotartam").item(0).getTextContent();

                System.out.println("Id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Kezdet: " + start);
                System.out.println("Idotartam: " + duration);

                NodeList nList2 = element.getElementsByTagName("mentor");
                String tagName = "Mentor";
                getMultipleValues(nList2, tagName);
            }
        }
    }

    private static void printTulajdonos(Document doc) {
        
    }

    private static void getMultipleValues(NodeList nList, String tagName) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node n = (Element) nList.item(i);
            String value = n.getTextContent();
            System.out.println(tagName+ ": " + value);
        }
    }

}
