package roambin.beehivez.log;

import roambin.beehivez.log.model.Node;
import roambin.beehivez.log.model.ParsedPnml;
import roambin.beehivez.log.utils.Dom4jUtils;
import roambin.beehivez.log.utils.WarppedFileWriter;
import java.util.HashMap;
import java.util.HashSet;

public class LogOfModel {
    public static void getLogOfModel(String modelFile){
        String logFile = modelFile.substring(0, modelFile.lastIndexOf('.')) + ".txt";
        getLogOfModel(modelFile, logFile);
    }
    public static void getLogOfModel(String modelFile, String logFile){
        WarppedFileWriter fw = new WarppedFileWriter(logFile, false);
        ParsedPnml parsedPnml = Dom4jUtils.getGraph(modelFile);
        HashMap<String, Node> nodeMap = new HashMap<>();
        //node map
        parsedPnml.arcs.values().forEach(arc -> {
            nodeMap.putIfAbsent(arc.target, new Node(parsedPnml.blocks.get(arc.target)));
            nodeMap.putIfAbsent(arc.source, new Node(parsedPnml.blocks.get(arc.source)));
            Node target = nodeMap.get(arc.target);
            Node source = nodeMap.get(arc.source);
            target.sources.add(source);
            source.targets.add(target);
        });
        //find start node
        Node start = null;
        for(Node node: nodeMap.values()){
            if(node.sources.isEmpty()){
                start = node;
                break;
            }
        }
        //find loop node
        HashSet<Node> searchedNodes = new HashSet<>();
        findLoopNode(searchedNodes, start);
        //generate log
        generateLog(fw, new StringBuffer(), start, new int[]{1});
        fw.close();
    }
    protected static void findLoopNode(HashSet<Node> searchedNodes, Node node){
        searchedNodes.add(node);
        node.targets.forEach(targetNode -> {
            if(searchedNodes.contains(targetNode)){
                node.isLoopedMap.put(targetNode, false);
            }else {
                findLoopNode(searchedNodes, targetNode);
            }
        });
        searchedNodes.remove(node);
    }
    protected static void generateLog(WarppedFileWriter fw, StringBuffer buffer, Node node, int[] caseId){
        if(node.targets.isEmpty()){
//            buffer.append(node.elem.id);
            fw.write(caseId[0]++ + ": " + new String(buffer).substring(0, buffer.length() - 4) + "\n");
        }else {
            int bufferLen = buffer.length();
            if(!node.elem.isPlace)  buffer.append(node.elem.id).append(" -> ");
            node.targets.forEach(targetNode -> {
                if(!node.isLoopedAndSetLoop(targetNode)){
                    generateLog(fw, buffer, targetNode, caseId);
                }
            });
            if(!node.elem.isPlace)  buffer.delete(bufferLen, buffer.length());
        }
    }
    public static void main(String[] args){
        getLogOfModel("Model1.pnml");
    }
}
