package y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Y4O4X0DomQueryOwn {

    private static File xmlFile = new File("Y4O4X0_0311/Y4O4X0DomParse/src/xml/Y4O4X0XMLOwn.xml");

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        
    }
    
}
