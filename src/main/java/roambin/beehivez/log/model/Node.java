package roambin.beehivez.log.model;

import java.util.*;

public class Node {
    public Block elem;
    public List<Node> targets = new ArrayList<>();
    public List<Node> sources = new ArrayList<>();
    public Map<Node, Boolean> isLoopedMap = new HashMap<>();
    public Node(){

    }
    public Node(Block block){
        this.elem = block;
    }
    public boolean isLoopedAndSetLoop(Node node){
        Boolean isLooped = isLoopedMap.get(node);
        if(isLooped == null){
            return false;
        }else if(isLooped){
            return true;
        }else {
            isLoopedMap.put(node, true);
            return false;
        }
    }
}
