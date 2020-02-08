package roambin.beehivez.log.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PnmlGenerator {
    public static void generate(String inputPath) throws Exception{
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf('.')) + ".pnml";
        generate(inputPath, outputPath);
    }
    public static void generate(String inputPath, String outputPath) throws Exception{
        File input = new File(inputPath);
        File output = new File(outputPath);
        BufferedReader br = new BufferedReader(new FileReader(input));
        String[] places = br.readLine().trim().split(" ", -1);
        String[] transitions = br.readLine().trim().split(" ", -1);
        ArrayList<Arc> arcList = new ArrayList<>();
        String line = readLine(br);
        while(line != null){
            String[] lineSplited = line.trim().split(" ", -1);
            arcList.add(new Arc(lineSplited[0], lineSplited[1]));
            line = readLine(br);
        }
        PrintWriter pw = new PrintWriter(output);
        pw.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<pnml>\n" +
                "<net id=\"workflownet\" type=\"http://www.informatik.hu-berlin.de/top/pnml/basicPNML.rng\">");
        for(String place: places){
            pw.println("    <place id=\"" + place + "\">\n" +
                    "        <name>\n" +
                    "            <text>" + place + "</text>\n" +
                    "         </name>\n" +
                    "    </place>");
        }
        for(String transition: transitions){
            pw.println("    <transition id=\"" + transition + "\">\n" +
                    "        <name>\n" +
                    "            <text>" + transition + "</text>\n" +
                    "         </name>\n" +
                    "        <toolspecific tool=\"ProM\" version=\"5.2\">\n" +
                    "            <logevent>\n" +
                    "                <name>" + transition + "</name>\n" +
                    "                <type>auto</type>\n" +
                    "            </logevent>\n" +
                    "        </toolspecific>\n" +
                    "    </transition>");
        }
        int arcCount = 1;
        for(Arc arc: arcList){
            pw.println("    <arc id=\"" + arcCount++ + "\" source=\"" + arc.source + "\" target=\"" + arc.target + "\">\n" +
                    "    </arc>");
        }
        pw.print("</net>\n" +
                "</pnml>");
        br.close();
        pw.close();
    }
    private static String readLine(BufferedReader br){
        try {
            return br.readLine();
        }catch (Exception e){
            return null;
        }
    }
    static class Arc{
        String source;
        String target;
        Arc(String source, String target){
            this.source = source;
            this.target = target;
        }
    }
    public static void main(String[] args) throws Exception{
        generate("data/generate/Par3.txt");
    }
}
