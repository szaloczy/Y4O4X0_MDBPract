package y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Y4O4X0DomQuery {
    
    public static void main(String[] args) throws Exception {

        File xmlFile = new File("Y4O4X0_0311/Y4O4X0DomParse/src/xml/Y4O4X0_etterem.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String rootName = root.getTagName();
        System.out.println("Root element: " + rootName);

        NodeList nList = doc.getElementsByTagName("szakacs");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                Node node = element.getElementsByTagName("vegzettseg").item(0);
                System.out.println();

                if(node.getTextContent().equals("Szakközépiskola")) {
                    System.out.println("Id: " + element.getAttribute("szkod"));
                    System.out.println("Név: " + element.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Részleg: " + element.getElementsByTagName("reszleg").item(0).getTextContent());

                    NodeList nList2 = element.getElementsByTagName("vegzettseg");
                    getMultipleValues(nList2, "Végzettség");
                }
            }
        }

        printFiveStarRestaurant(doc);
        printPMInterns(doc);
    }

    private static void getMultipleValues(NodeList nList, String tagName) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node n = (Element) nList.item(i);
            String value = n.getTextContent();
            System.out.println(tagName+ ": " + value);
        }
    }

    private static void printFiveStarRestaurant(Document doc) {
        NodeList nList = doc.getElementsByTagName("etterem");

        System.out.println();

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                Node n1 = element.getElementsByTagName("csillag").item(0);
                int star = Integer.parseInt(n1.getTextContent());
                if(star == 5) {
                    System.out.println("Id: " + element.getAttribute("ekod"));
                    System.out.println("Név: " + element.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Cím: " + 
                        element.getElementsByTagName("varos").item(0).getTextContent() + " " +
                        element.getElementsByTagName("utca").item(0).getTextContent() + " " +
                        element.getElementsByTagName("hazszam").item(0).getTextContent()

                    );
                    System.out.println("Csillag: " + element.getElementsByTagName("csillag").item(0).getTextContent());

                }
            }
        }
    }

    private static void printPMInterns(Document doc) {
        NodeList nList = doc.getElementsByTagName("gyakornok");
        System.out.println();

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;

                Node n1 = element.getElementsByTagName("muszak").item(0);
                String muszak = n1.getTextContent();

                if(muszak.equals("délután")) {
                    System.out.println("Id:" + element.getAttribute("gykod"));
                    System.out.println("Nev: " + element.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Gyakorlat kezdete: " + element.getElementsByTagName("kezdete").item(0).getTextContent());
                    System.out.println("Műszak: " + muszak);
                }
            }
        }
    }
}
