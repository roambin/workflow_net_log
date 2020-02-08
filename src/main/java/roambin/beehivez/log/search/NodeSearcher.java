package roambin.beehivez.log.search;

import roambin.beehivez.log.model.Block;
import roambin.beehivez.log.model.Node;
import roambin.beehivez.log.model.ParsedPnml;
import java.util.*;

public class NodeSearcher {
    public static Node getStart(ParsedPnml parsedPnml){
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
        return start;
    }

    public static void dfsSerial(SerialRecorder sr, Node node) {
        if (sr.isEnd(node)) {
            sr.record();
            //for loop
            if(node.targets.size() > 1 && node.searchedNum < 1){
                node.searchedNum++;
                node.targets.forEach(t -> {
                    if(t != sr.getEndTran()){
                        dfsSerial(sr, t);
                    }
                });
                node.searchedNum--;
            }
        }else{
            if(node.isParallel()) {
                ParallelRecorder pr = new ParallelRecorder();
                searchPar(pr, node);
                //combine
                node.list = pr.getList();
                node.targets = pr.getEnd().targets;
                pr.getEnd().targets.forEach(t -> {
                    t.sources.remove(pr.getEnd());
                    t.sources.add(node);
                });
            }
            if (node.searchedNum < 2) {
                node.searchedNum++;
                if (!node.isPlace()) sr.append(node);
                node.targets.forEach(targetNode -> {
                    dfsSerial(sr, targetNode);
                });
                if (!node.isPlace()) sr.deleteLast();
                node.searchedNum--;
            }
        }
    }
    private static void searchPar(ParallelRecorder pr, Node node){
        ArrayDeque<Node> deque = new ArrayDeque<>();
        node.targets.forEach(t -> deque.add(t));
        pr.append(node);
        while (deque.size() > 1){
            Node dNode = deque.remove();
            if(dNode.isPlace() || dNode.dependNum == dNode.sources.size()){
                dfsPar(pr, dNode, deque, node);
            }else {
                deque.add(dNode);
            }
        }
        pr.append(pr.getEnd());
    }
    private static void dfsPar(ParallelRecorder pr, Node node, Deque<Node> deque, Node prep){
        if(pr.isEnd(node) && node.dependNum < node.sources.size()){
            if(node.dependNum == 0) deque.add(node);
            node.dependNum++;
        }else {
            if(node.isChoose() || node.isLoop()){
                SerialRecorder sr = new SerialRecorder();
                dfsSerial(sr, node);
                //combine
                Node newNode = new Node(sr.getList());
                Node newEnd = new Node(new Block(true));
                newNode.targets.add(newEnd);
                newNode.sources.add(node);
                newEnd.targets.add(sr.getEndTran());
                newEnd.sources.add(newNode);
                sr.getEndTran().sources.remove(sr.getEnd());
                sr.getEndTran().sources.add(newEnd);
                node.targets.clear();
                node.targets.add(newNode);
                node.sources.clear();
                node.sources.add(prep);
            }
            if(!node.isPlace()) pr.append(node);
            node.targets.forEach(targetNode -> {
                dfsPar(pr, targetNode, deque, node);
            });
        }
    }
}
