package roambin.beehivez.log.search;

import roambin.beehivez.log.model.Block;
import roambin.beehivez.log.model.Node;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SerialRecorderFile extends SerialRecorder{
    private FileWriter fw = null;
    private int caseId = 0;
    final private String split = " ";
    //private HashSet<String> set = new HashSet<>();
    public SerialRecorderFile(String filename, boolean append){
        File file = new File(filename);
        try {
            fw = new FileWriter(file, append);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void write(String str){
        try {
            fw.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close(){
        if(fw != null){
            try {
                fw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //output
    @Override
    public void record() {
        innerRecord(line, new ArrayList<>(), 0);
    }
    private void innerRecord(List<Node> line, List<Block> blocks, int index){
        if(index >= line.size()){
            StringBuffer buffer = new StringBuffer();
            blocks.forEach(b -> buffer.append(split).append(b.name));
            buffer.delete(0, split.length());
            String tmp = new String(buffer);
            //if(set.add(tmp)){
                write("Case" + (++caseId) + ": " + tmp + "\n");
            //}
        }else {
            line.get(index).list.forEach(t -> {
                List<Block> newBlocks = new ArrayList<>(blocks);
                newBlocks.addAll(t);
                innerRecord(line, newBlocks, index + 1);
            });
        }
    }
    @Override
    public boolean isEnd(Node node) {
        return node.targets.isEmpty();
    }
}
