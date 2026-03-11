package y4o4x0;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import y4o4x0.model.Cegek;

public class Main {

    private static File xmlFile = new File("Y4O4X0_0304/Y4O4X0DOMParse/src/xml/Y4O4X0XMLOWN.xml");
    public static void main(String args[]) {
        System.out.println("Hello");
        try {
            Cegek cegek = new Cegek("cegek", xmlFile);
            cegek.printElement();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Feladat lap - Xquery - Mint ahogy Bednarik mester csinálja 

    
}
