package roambin.beehivez.log.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import roambin.beehivez.log.model.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class PnmlParser {
    public static ParsedPnml getGraph(String filename) {
        return getGraph(load(filename));
    }
    protected static Document load(String filename) {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
    protected static ParsedPnml getGraph(Document document) {
        ParsedPnml parsedPnml = new ParsedPnml();
        NodeList nodeList = document.getElementsByTagName("place");
        for(int i = 0; i < nodeList.getLength(); i++){
            Block block = getBlock((Element)nodeList.item(i), true);
            parsedPnml.blocks.put(block.id, block);
        }
        nodeList = document.getElementsByTagName("transition");
        for(int i = 0; i < nodeList.getLength(); i++){
            Block block = getBlock((Element)nodeList.item(i), false);
            parsedPnml.blocks.put(block.id, block);
        }
        nodeList = document.getElementsByTagName("arc");
        for(int i = 0; i < nodeList.getLength(); i++){
            Arc arc = getArc((Element)nodeList.item(i));
            parsedPnml.arcs.put(arc.id, arc);
        }
        return parsedPnml;
    }
    protected static Block getBlock(Element element, boolean isPlace){
        Block block = new Block();
        block.id = element.getAttribute("id");
        Element elemName = getSubElement(element, "name", "text");
        block.name = elemName == null ? block.id : elemName.getTextContent();
        Element elemLogevent = getSubElement(element, "toolspecific", "logevent", "name");
        block.logevent = elemLogevent == null ? block.name : elemLogevent.getTextContent();
        block.position = getPoint(getSubElement(element, "graphics", "position"));
        block.dimension = getPoint(getSubElement(element, "graphics", "dimension"));
        block.isPlace = isPlace;
        return block;
    }
    protected static Arc getArc(Element element){
        Arc arc = new Arc();
        arc.id = element.getAttribute("id");
        arc.source = element.getAttribute("source");
        arc.target = element.getAttribute("target");
        Element elemSpline = getSubElement(element, "toolspecific", "spline");
        if(elemSpline != null){
            NodeList nodeList = elemSpline.getElementsByTagName("point");
            for(int i = 0; i < nodeList.getLength(); i++){
                arc.points.add(getPoint((Element)nodeList.item(i)));
            }
            nodeList = elemSpline.getElementsByTagName("end");
            for(int i = 0; i < nodeList.getLength(); i++){
                arc.points.add(getPoint((Element)nodeList.item(i)));
            }
        }
        return arc;
    }
    protected static Point getPoint(Element element){
        Point point = new Point();
        try{
            point.x = Integer.parseInt(element.getAttribute("x"));
            point.y = Integer.parseInt(element.getAttribute("y"));
        }catch (Exception e){

        }
        return point;
    }
    private static Element getSubElement(Element rootElement, String... tagNames){
        Element subElement = rootElement;
        for(String tagName: tagNames){
            NodeList nodeList = subElement.getElementsByTagName(tagName);
            if(nodeList.getLength() != 0){
                subElement = (Element)nodeList.item(0);
            }else {
                return null;
            }
        }
        return subElement;
    }

    public static void main(String[] args){
        ParsedPnml parsedPnml = getGraph("data" + File.separator + "Model1.pnml");
    }
}
