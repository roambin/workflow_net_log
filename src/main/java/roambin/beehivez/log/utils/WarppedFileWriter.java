package roambin.beehivez.log.utils;

import java.io.*;

public class WarppedFileWriter {
    private FileWriter fw = null;
    public WarppedFileWriter(String filename, boolean append){
        File file = new File(filename);
        try {
            fw = new FileWriter(file, append);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void write(String str){
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
    public static void main(String[] args){
        WarppedFileWriter fw = new WarppedFileWriter("a", false);
        fw.write("1");
        fw.write("2");
        fw.close();
        fw = new WarppedFileWriter("a", true);
        fw.write("3");
        fw.close();
    }
}
