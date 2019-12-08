package roambin.beehivez.log.model;

import java.util.HashMap;

public class ParsedPnml {
    public HashMap<String, Block> blocks = new HashMap<>();
//    public HashMap<String, Block> places = new HashMap<>();
//    public HashMap<String, Block> transitions = new HashMap<>();
    public HashMap<String, Arc> arcs = new HashMap<>();
    public ParsedPnml(){

    }
}
