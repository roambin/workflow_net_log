package roambin.beehivez.log;

import roambin.beehivez.log.model.Node;
import roambin.beehivez.log.model.ParsedPnml;
import roambin.beehivez.log.search.NodeSearcher;
import roambin.beehivez.log.search.SerialRecorderFile;
import roambin.beehivez.log.utils.PnmlParser;

import java.io.File;

public class LogOfModel {
    public static void getLogOfModel(String modelFile){
        String logFile = modelFile.substring(0, modelFile.lastIndexOf('.')) + ".log";
        getLogOfModel(modelFile, logFile);
    }
    public static void getLogOfModel(String modelFile, String logFile){
        SerialRecorderFile srf = new SerialRecorderFile(logFile, false);
        ParsedPnml parsedPnml = PnmlParser.getGraph(modelFile);
        Node start = NodeSearcher.getStart(parsedPnml);
        //generate log
        NodeSearcher.dfsSerial(srf, start);
        srf.close();
    }

    public static void main(String[] args){
//        getLogOfModel("data" + File.separator + "Model1.pnml");
//        getLogOfModel("data" + File.separator + "Model2.pnml");
//        getLogOfModel("data" + File.separator + "Model3.pnml");
//        getLogOfModel("data" + File.separator + "Par1.pnml");
//        getLogOfModel("data" + File.separator + "Par2.pnml");
//        getLogOfModel("data" + File.separator + "Par3.pnml");
        getLogOfModel("data" + File.separator + "50.pnml");
    }
}
