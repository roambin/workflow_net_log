package roambin.beehivez.log.search;

import roambin.beehivez.log.model.Block;
import roambin.beehivez.log.model.Node;

import java.util.*;

public class ParallelRecorder{
    private List<List<Block>> list = null;
    private List<Node> set = new ArrayList<>();
    private Node end = null;
    public List<List<Block>> getList(){
        if(list != null){
            return list;
        }
        list = new ArrayList<>(Collections.singleton(new ArrayList<>()));
        set.forEach(node -> {
            List<List<Block>> tempList = list;
            list = new ArrayList<>();
            tempList.forEach(line -> {
                int start = -1, end = line.size();
                loop1: for(int i = line.size() - 1; i >= 0; i--){
                    for(Node s: node.sources){
                        if(s.sources.size() == 0) break loop1;
                        for(List<Block> blist: s.sources.get(0).list){
                            if(!blist.isEmpty() && blist.get(blist.size() - 1) == line.get(i)){
                                start = i;
                                break loop1;
                            }
                        }
                    }
                }
//                loop2: for(int i = 0; i < line.size(); i++){
//                    for(Node s: node.targets){
//                        if(s.targets.size() == 0) break loop2;
//                        for(List<Block> blist: s.targets.get(0).list){
//                            if(blist.get(0) == line.get(i)){
//                                end = i;
//                                break loop2;
//                            }
//                        }
//                    }
//                }
                for(List<Block> line2: node.list){
                    List<List<Block>> comList = new ArrayList<>();
                    combine(line2, start, end, comList, new ArrayList<>(line), 0);
                    list.addAll(comList);
                }
            });
        });
        return list;
    }
    private void combine(List<Block> line2, int start, int end,
                         List<List<Block>> comList, List<Block> curLine, int index){
        if(index >= line2.size()){
            comList.add(new ArrayList<>(curLine));
        }else{
            for(int i = start + 1; i <= end; i++){
                curLine.add(i, line2.get(index));
                combine(line2, i, end + 1, comList, curLine, index + 1);
                curLine.remove(i);
            }
        }
    }
    public void append(Node e){
        set.add(e);
    }
    public boolean isEnd(Node node) {
        boolean isEnd = node.isParallelEnd();
        if(end == null && isEnd) end = node;
        return isEnd;
    }
    public Node getEnd(){
        return end;
    }
}
