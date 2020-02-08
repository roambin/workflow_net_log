package roambin.beehivez.log.utils;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import roambin.beehivez.log.model.Arc;
import roambin.beehivez.log.model.Block;
import roambin.beehivez.log.model.ParsedPnml;
import roambin.beehivez.log.model.Point;

public class Dom4jUtils {
    public static ParsedPnml getGraph(String filename) {
        return getGraph(load(filename));
    }
    protected static Document load(String filename) {
        Document document = null;
        try {
            document = new SAXReader().read(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
    protected static ParsedPnml getGraph(Document document) {
        ParsedPnml parsedPnml = new ParsedPnml();
        Element netElem = document.getRootElement().element("net");
        netElem.elementIterator("place").forEachRemaining(e -> {
            Block block = getBlock(e, true);
            parsedPnml.blocks.put(block.id, block);
        });
        netElem.elementIterator("transition").forEachRemaining(e -> {
            Block block = getBlock(e, false);
            parsedPnml.blocks.put(block.id, block);
        });
        netElem.elementIterator("arc").forEachRemaining(e -> {
            Arc arc = getArc(e);
            parsedPnml.arcs.put(arc.id, arc);
        });
        return parsedPnml;
    }
    protected static Block getBlock(Element element, boolean isPlace){
        Block block = new Block();
        block.id = element.attribute("id").getValue();
        block.name = element.element("name").element("text").getText();
        block.logevent = element.element("toolspecific") != null
                ? element.element("toolspecific").element("logevent").element("name").getText()
                : block.name;
        if(element.element("graphics") != null){
            block.position = getPoint(element.element("graphics").element("position"));
            block.dimension = getPoint(element.element("graphics").element("dimension"));
        }
        block.isPlace = isPlace;
        return block;
    }
    protected static Arc getArc(Element element){
        Arc arc = new Arc();
        arc.id = element.attribute("id").getValue();
        arc.source = element.attribute("source").getValue();
        arc.target = element.attribute("target").getValue();
        if(element.element("toolspecific")!= null
                && element.element("toolspecific").element("spline") != null){
            element.element("toolspecific").element("spline").elementIterator()
                    .forEachRemaining(e -> arc.points.add(getPoint(e)));
        }
        return arc;
    }
    protected static Point getPoint(Element element){
        Point point = new Point();
        point.x = Integer.parseInt(element.attribute("x").getValue());
        point.y = Integer.parseInt(element.attribute("y").getValue());
        return point;
    }
    public static void main(String[] args){
        ParsedPnml parsedPnml = getGraph("Model1.pnml");
    }
}
