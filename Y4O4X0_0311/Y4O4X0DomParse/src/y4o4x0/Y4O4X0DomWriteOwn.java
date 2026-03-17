package y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Y4O4X0DomWriteOwn {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
    // ===== DOM dokumentum létrehozása =====
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // ===== Root elem =====
        Element root = doc.createElement("cegek");
        doc.appendChild(root);

        // ===== CÉG 1 =====
        Element ceg1 = doc.createElement("ceg");
        ceg1.setAttribute("ckod", "1");
        root.appendChild(ceg1);

        Element nev1 = doc.createElement("nev");
        nev1.setTextContent("AlphaSoft Kft.");
        ceg1.appendChild(nev1);

        Element cim1 = doc.createElement("cim");
        ceg1.appendChild(cim1);

        Element varos1 = doc.createElement("varos");
        varos1.setTextContent("Miskolc");
        cim1.appendChild(varos1);

        Element utca1 = doc.createElement("utca");
        utca1.setTextContent("Fő utca");
        cim1.appendChild(utca1);

        Element hazszam1 = doc.createElement("hazszam");
        hazszam1.setTextContent("12");
        cim1.appendChild(hazszam1);

        // ===== CÉG 2 =====
        Element ceg2 = doc.createElement("ceg");
        ceg2.setAttribute("ckod", "2");
        root.appendChild(ceg2);

        Element nev2 = doc.createElement("nev");
        nev2.setTextContent("BetaTech Zrt.");
        ceg2.appendChild(nev2);

        Element cim2 = doc.createElement("cim");
        ceg2.appendChild(cim2);

        Element varos2 = doc.createElement("varos");
        varos2.setTextContent("Debrecen");
        cim2.appendChild(varos2);

        Element utca2 = doc.createElement("utca");
        utca2.setTextContent("Bem tér");
        cim2.appendChild(utca2);

        Element hazszam2 = doc.createElement("hazszam");
        hazszam2.setTextContent("8");
        cim2.appendChild(hazszam2);

        // ===== XML KIÍRÁSA KONZOLRA ÉS FÁJLBA =====
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Konzolra
        System.out.println("=== Létrehozott XML dokumentum ===");
        transformer.transform(new DOMSource(doc), new StreamResult(System.out));

        // Fájlba
        File outputFile = new File("NeptunkodXML1.xml");
        transformer.transform(new DOMSource(doc), new StreamResult(outputFile));

        System.out.println("\n\nXML fájl sikeresen létrehozva: NeptunkodXML1.xml");

    }
}
