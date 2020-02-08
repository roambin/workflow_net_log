package roambin.beehivez.log.model;

import java.util.*;

public class Node {
    public List<Node> targets = new ArrayList<>();
    public List<Node> sources = new ArrayList<>();
    public int searchedNum = 0;
    public int dependNum = 0;
    public List<List<Block>> list;
    public Node(){

    }
    public Node(Block block){
        list = new ArrayList<>();
        list.add(new ArrayList<>(Collections.singleton(block)));
    }
    public Node(List<List<Block>> list){
        this.list = list;
    }
    private Block getFirst(){
        Block block = null;
        for(List<Block> line: list){
            if(line.size() > 0){
                block = line.get(0);
                break;
            }
        }
        return block;
    }
    public boolean isPlace(){
        return getFirst().isPlace;
    }
    public boolean isParallel(){
        return !getFirst().isPlace && this.targets.size() > 1;
    }
    public boolean isParallelEnd(){
        return !getFirst().isPlace && this.sources.size() > 1;
    }
    public boolean isChoose(){
        return getFirst().isPlace && this.targets.size() > 1;
    }
    public boolean isLoop(){
        return getFirst().isPlace && this.sources.size() > 1;
    }
}
