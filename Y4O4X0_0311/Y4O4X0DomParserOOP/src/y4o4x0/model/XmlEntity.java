package y4o4x0.model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class XmlEntity {
    protected DocumentBuilderFactory factory;
    protected DocumentBuilder dBuilder;
    protected Document document;

    
    public abstract void printElement();
    
    public abstract void getMultipleValues();

    public abstract void normalizeDocument(Document doc);
}
