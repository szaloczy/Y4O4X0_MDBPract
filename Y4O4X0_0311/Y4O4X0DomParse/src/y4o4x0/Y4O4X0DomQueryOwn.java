package y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Y4O4X0DomQueryOwn {

    private static File xmlFile = new File("Y4O4X0_0311/Y4O4X0DomParse/src/xml/Y4O4X0XMLOwn.xml");

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String rootName = root.getTagName();
        System.out.println("Root element: " + rootName);

        System.out.println("\n=== 1. Miskolci cégek ===");
        NodeList cegek = doc.getElementsByTagName("ceg");

        for (int i = 0; i < cegek.getLength(); i++) {
            Node nNode = cegek.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ceg = (Element) nNode;

                String varos = ceg.getElementsByTagName("varos").item(0).getTextContent();

                if (varos.equals("Miskolc")) {
                    System.out.println("Cégkód: " + ceg.getAttribute("ckod"));
                    System.out.println("Név: " + ceg.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Cím: " + varos + ", " +
                            ceg.getElementsByTagName("utca").item(0).getTextContent() + " " +
                            ceg.getElementsByTagName("hazszam").item(0).getTextContent());
                    System.out.println();
                }
            }

            
        }

         // ===== 2. feladat: Backend fejlesztők =====
        System.out.println("\n=== 2. Backend fejlesztők ===");
        NodeList fejlesztok = doc.getElementsByTagName("fejleszto");

        for (int i = 0; i < fejlesztok.getLength(); i++) {
            Node nNode = fejlesztok.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fejleszto = (Element) nNode;

                String reszleg = fejleszto.getElementsByTagName("reszleg").item(0).getTextContent();

                if (reszleg.equals("Backend")) {
                    System.out.println("FejlesztőID: " + fejleszto.getAttribute("fkod"));
                    System.out.println("Név: " + fejleszto.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Részleg: " + reszleg);
                    System.out.println("Fizetés: " + fejleszto.getElementsByTagName("fizetes").item(0).getTextContent());
                    System.out.println();
                }
            }
        }

        // ===== 3. feladat: 2024 júliusában kezdő gyakornokok =====
        System.out.println("\n=== 3. 2024 júliusában kezdő gyakornokok ===");
        NodeList gyakornokok = doc.getElementsByTagName("gyakornok");

        for (int i = 0; i < gyakornokok.getLength(); i++) {
            Node nNode = gyakornokok.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element gyak = (Element) nNode;

                String kezdet = gyak.getElementsByTagName("kezdet").item(0).getTextContent();

                if (kezdet.startsWith("2024-07")) {
                    System.out.println("GyakornokID: " + gyak.getAttribute("gykod"));
                    System.out.println("Név: " + gyak.getElementsByTagName("nev").item(0).getTextContent());
                    System.out.println("Kezdés: " + kezdet);
                    System.out.println("Időtartam: " + gyak.getElementsByTagName("idotartam").item(0).getTextContent());
                    System.out.println();
                }
            }
        }

        //printFiveStarRestaurant(doc);
        //printPMInterns(doc);
    }

    private static void getMultipleValues(NodeList nList, String tagName) {
        for(int i = 0; i < nList.getLength(); i++) {
            Node n = (Element) nList.item(i);
            String value = n.getTextContent();
            System.out.println(tagName+ ": " + value);
        }
    }
    
}
