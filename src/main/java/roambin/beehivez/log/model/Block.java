package roambin.beehivez.log.model;

public class Block {
    public String id;
    public String name;
    public String logevent;
    public Point position;
    public Point dimension;
    public boolean isPlace;
    public Block(){

    }
    public Block(Boolean isPlace){
        this.isPlace = isPlace;
    }
    public Block(String id, String name, String logevent, Point position, Point dimension, boolean isPlace){
        this.id = id;
        this.name = name;
        this.logevent = logevent;
        this.position = position;
        this.dimension = dimension;
        this.isPlace = isPlace;
    }
}
