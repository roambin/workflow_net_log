package roambin.beehivez.log.model;

import java.util.ArrayList;
import java.util.List;

public class Arc {
    public String id;
    public String source;
    public String target;
    public List<Point> points = new ArrayList<>();
    public Arc(){

    }
    public Arc(String id, String source, String target, List<Point> points){
        this.id = id;
        this.source = source;
        this.target = target;
        this.points.addAll(points);
    }
}
