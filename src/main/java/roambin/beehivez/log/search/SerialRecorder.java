package roambin.beehivez.log.search;

import roambin.beehivez.log.model.Block;
import roambin.beehivez.log.model.Node;
import java.util.ArrayList;
import java.util.List;

public class SerialRecorder{
    private List<List<Block>> list = new ArrayList<>();
    protected List<Node> line = new ArrayList<>();
    private Node end = null;
    private Node endTran = null;
    public List<List<Block>> getList(){
        return list;
    }
    public void append(Node e){
        line.add(e);
    }
    public void deleteLast(){
        line.remove(line.size() -1);
    }
    public void record() {
        innerRecord(line, new ArrayList<>(), list, 0);
    }
    private void innerRecord(List<Node> line, List<Block> blocks, List<List<Block>> list, int index){
        if(index >= line.size()){
            list.add(blocks);
        }else {
            line.get(index).list.forEach(t -> {
                List<Block> newBlocks = new ArrayList<>(blocks);
                newBlocks.addAll(t);
                innerRecord(line, newBlocks, list, index + 1);
            });
        }
    }
    public boolean isEnd(Node node) {
        boolean isEnd = false;
        for(Node t: node.targets){
            if(node.isPlace() && t.sources.size() > 1){
                isEnd = true;
                if(end == null) end = node;
                if(endTran == null) endTran = t;
                break;
            }
        }
        return isEnd;
    }
    public Node getEnd() {
        return end;
    }
    public Node getEndTran() {
        return endTran;
    }
}
