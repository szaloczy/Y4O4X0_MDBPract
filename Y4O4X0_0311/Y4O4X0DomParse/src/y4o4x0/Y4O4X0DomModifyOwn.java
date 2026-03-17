package y4o4x0;

import java.io.File;

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

public class Y4O4X0DomModifyOwn {
     private static File xmlFile = new File("Y4O4X0_0311/Y4O4X0DomParse/src/xml/Y4O4X0XMLOwn.xml");

    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        System.out.println("=== XML módosítások a céges fájlban ===");

        // ===== 1. feladat: első cég adatainak módosítása =====
        NodeList cegek = doc.getElementsByTagName("ceg");

        if (cegek.getLength() > 0) {
            Element ceg = (Element) cegek.item(0);

            ceg.getElementsByTagName("nev").item(0).setTextContent("Módosított Cég Kft.");
            ceg.getElementsByTagName("varos").item(0).setTextContent("Eger");

            System.out.println("Első cég neve és városa módosítva.");
        }

        // ===== 2. feladat: első gyakornok attribútum módosítása =====
        NodeList gyakornokok = doc.getElementsByTagName("gyakornok");

        if (gyakornokok.getLength() > 0) {
            Element gyak = (Element) gyakornokok.item(0);

            gyak.setAttribute("c_gy", "3");

            System.out.println("Első gyakornok c_gy attribútuma módosítva 3-ra.");
        }

        // ===== 3. feladat: összes projekt törlése =====
        NodeList projektek = doc.getElementsByTagName("projekt");

        while (projektek.getLength() > 0) {
            Node p = projektek.item(0);
            p.getParentNode().removeChild(p);
        }

        System.out.println("Összes projekt elem törölve.");

        // ===== 4. feladat: módosított XML kiírása =====
        System.out.println("\n=== Módosított XML tartalma ===");

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(doc), new StreamResult(System.out));
    }
}
