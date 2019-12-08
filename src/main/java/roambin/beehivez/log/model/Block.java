package roambin.beehivez.log.model;

public class Block {
    public String id;
    public String name;
    public Point position;
    public Point dimension;
    public boolean isPlace;
    public Block(){

    }
    public Block(String id, String name, Point position, Point dimension, boolean isPlace){
        this.id = id;
        this.name = name;
        this.position = position;
        this.dimension = dimension;
        this.isPlace = isPlace;
    }
}
