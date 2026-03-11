package y4o4x0.model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Cegek {
    private DocumentBuilderFactory factory;
    private DocumentBuilder dBuilder;
    private Document document;
    private String rootElement;
    private File xmlFile;

    public Cegek(String rootElement, File xmlFile) throws Exception {
        this.factory = DocumentBuilderFactory.newDefaultInstance();
        this.dBuilder = factory.newDocumentBuilder();
        this.document = dBuilder.parse(xmlFile);
        this.rootElement = rootElement;
        this.xmlFile = xmlFile;
    }

 
    public void printElement() {
       System.out.println(this.document.getDocumentElement().getTagName());
    }


    public void getMultipleValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMultipleValues'");
    }


    public void normalizeDocument(Document doc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'normalizeDocument'");
    }
    
}
