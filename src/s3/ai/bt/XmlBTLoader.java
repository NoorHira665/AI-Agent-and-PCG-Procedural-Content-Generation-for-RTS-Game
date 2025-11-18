package s3.ai.bt;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class XmlBTLoader {

    public static Task loadBTfromXML(String filepath) throws Exception {
        //Reads XMl file and calls parseNode on the root node
        //I looked up how to read from XML using this library
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filepath));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        return parseNode(root);
    }

    private static Task parseNode(Element node) {
        String nodeName = node.getNodeName();

        //if the node is composite, returns relevant composite class, giving list of children nodes as the argument
        if (nodeName.equalsIgnoreCase("Selector") || nodeName.equalsIgnoreCase("Sequence")) {
            List<Task> children = new ArrayList<>();
            NodeList childNodes = node.getChildNodes();

            //gets valid child nodes, ignoring whitespace etc.
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i) instanceof Element) {
                    children.add(parseNode((Element) childNodes.item(i)));
                }
            }

            if (nodeName.equalsIgnoreCase("Selector")) {
                return new Selector(children);
            } else {
                return new Sequence(children);
            }

            //if the node is condition/action, creates Task using TaskFactory
        } else if (nodeName.equalsIgnoreCase("Condition") || nodeName.equalsIgnoreCase("Action")) {
                String type = node.getAttribute("type").trim();
                String value = null;
                if (node.hasAttribute("value")) {
                    value = node.getAttribute("value");
                }
                return TaskFactory.createTask(type, value);
        } else {
                throw new RuntimeException("Unknown XML node: " + nodeName);
        }
    }
}


