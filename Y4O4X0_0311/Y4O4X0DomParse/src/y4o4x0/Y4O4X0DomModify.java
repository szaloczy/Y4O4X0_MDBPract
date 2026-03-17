package y4o4x0;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Y4O4X0DomModify {

    private static final File xmlFile = new File("Y4O4X0_0311/Y4O4X0DomParse/src/xml/etterem.xml");
    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuiler = factory.newDocumentBuilder();

        Document doc = dBuiler.parse(xmlFile);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();    

        if (root == null) {
            System.err.println("XML document has no root element");
            return;
        }

        System.out.println("Root element: " + root.getTagName());

        modifyVendeg(root);
        modifyGyakornok(root);
        deleteRendeles(root);

        // Output the modified XML to console
        Transformer transformer = TransformerFactory
            .newInstance()
            .newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        transformer.transform(source, new StreamResult(System.out));
    }


    public static void modifyVendeg(Element element) {
        System.out.println("Vendég módosítása...\n");

        NodeList vendegek = element.getElementsByTagName("vengdeg");

        if (vendegek.getLength() > 0) {
            Element vendeg = (Element) vendegek.item(0);

            Node nev = vendeg.getElementsByTagName("nev").item(0);
            Node kor = vendeg.getElementsByTagName("kor").item(0);

            if ( nev != null) {
                nev.setTextContent("Vendég Viktor");
            }

            if (kor != null) {
                kor.setTextContent("25");
            }

        }
    }

    public static void modifyGyakornok(Element element) {
        System.out.println("Gyakornok módosítása... \n");

        var gyakornokok = element.getElementsByTagName("gyakornok");
        for (int i = 0; i < gyakornokok.getLength(); i++) {
            var gyakornok = (Element) gyakornokok.item(i);
            if (gyakornok.hasAttribute("e_gy")) {
                gyakornok.setAttribute("e_gy", "e3");
                return;
            }
        }
    }

    public static void deleteRendeles(Element element) {
        System.out.println("Rendelés elemek törlése... \n");

        var rendelesek = element.getElementsByTagName("rendeles");
        for (int i = rendelesek.getLength() - 1; i >= 0; i--) {
            var rendeles = rendelesek.item(i);
            rendeles.getParentNode().removeChild(rendeles);
        }
    }
}