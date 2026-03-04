import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, IllegalArgumentException {
        File xmlFile = new File("C:\\Users\\au088965\\dev\\Y4O4X0_MDBPract\\Y4O4X0_0304\\Y4O4X0_XML.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String rootName = root.getTagName();


        NodeList nList = doc.getElementsByTagName("ceg");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nCurrent element: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String id = elem.getAttribute("ckod");

                Node node1 = elem.getElementsByTagName("nev").item(0);
                String name = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("varos").item(0);
                String city = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("utca").item(0);
                String street = node3.getTextContent();

                Node node4 = elem.getElementsByTagName("hazszam").item(0);
                String number = node4.getTextContent();

                String address = city + ", " + street + " utca" + number + ".";

                System.out.println("Ceg Id: " + id);
                System.out.println("Név: " + name);
                System.out.println("Cím: " + address);
            }
        }
    }
}