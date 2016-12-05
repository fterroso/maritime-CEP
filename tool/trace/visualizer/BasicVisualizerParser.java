/*
 * Copyright 2014 University of Murcia (Fernando Terroso-Saenz (fterroso@um.es), Mercedes Valdes-Vela, Antonio F. Skarmeta)
 * 
 * This file is part of Maritime-CEP.
 * 
 * Maritime-CEP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Maritime-CEP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see http://www.gnu.org/licenses/.
 * 
 */
package tool.trace.visualizer;

import event.MapElement;
import event.MapElementType;
import event.alarm.AlarmEvent;
import event.alert.AlertEvent;
import event.location.LocationEvent;
import event.trajectory.TrajectoryChangeEvent;
import event.trajectory.TrajectoryEvent;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import tool.Constants;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BasicVisualizerParser extends TraceVisualizer{

    static Logger LOG = Logger.getLogger(BasicVisualizerParser.class);    

    protected Map<String, Map<Integer, List<MapElement>>> locationEvents;
    protected Map<String, Map<Integer, List<MapElement>>> trajectoryEvents;
    protected Map<String, Map<Integer, List<MapElement>>> trajectoryChangeEvents;
        
    protected PrintWriter generalWriter;
    
    private String dateStr = "";

    public BasicVisualizerParser() {
        
        locationEvents = new HashMap<String, Map<Integer, List<MapElement>>>();       
        trajectoryEvents = new HashMap<String, Map<Integer, List<MapElement>>>();
        trajectoryChangeEvents = new HashMap<String, Map<Integer, List<MapElement>>>();        
    }        

    
    @Override
    protected void registerLocationEvent(LocationEvent event) {
        List<MapElement> events;
        Map<Integer, List<MapElement>> locationsForDevice;
        
        if(locationEvents.containsKey(event.getId())){
            locationsForDevice = locationEvents.get(event.getId());
            if(locationsForDevice.containsKey(event.getLevel())){
                events = locationsForDevice.get(event.getLevel());
            }else{
                events = new LinkedList<MapElement>();
            }
        }else{
            locationsForDevice = new HashMap<Integer, List<MapElement>>();
            events = new LinkedList<MapElement>();
        }
        
        events.add(event);        
        locationsForDevice.put(event.getLevel(), events);
        
        locationEvents.put(event.getId(), locationsForDevice);     
    }
    
    @Override
    protected void registerTrajectoryChangeEvent(TrajectoryChangeEvent event) {
        
        List<MapElement> events;
        Map<Integer, List<MapElement>> locationsForDevice;
        
        if(trajectoryChangeEvents.containsKey(event.getId())){
            locationsForDevice = trajectoryChangeEvents.get(event.getId());
            if(locationsForDevice.containsKey(event.getLevel())){
                events = locationsForDevice.get(event.getLevel());
            }else{
                events = new LinkedList<MapElement>();
            }
        }else{
            locationsForDevice = new HashMap<Integer, List<MapElement>>();
            events = new LinkedList<MapElement>();
        }
        
        events.add(event);        
        locationsForDevice.put(event.getLevel(), events);
        
        trajectoryChangeEvents.put(event.getId(), locationsForDevice);         
               
    }

    @Override
    protected void registerTrajectoryEvent(TrajectoryEvent event) {
        
        List<MapElement> events;
        Map<Integer, List<MapElement>> locationsForDevice;
        
        if(trajectoryEvents.containsKey(event.getId())){
            locationsForDevice = trajectoryEvents.get(event.getId());
            if(locationsForDevice.containsKey(event.getLevel())){
                events = locationsForDevice.get(event.getLevel());
            }else{
                events = new LinkedList<MapElement>();
            }
        }else{
            locationsForDevice = new HashMap<Integer, List<MapElement>>();
            events = new LinkedList<MapElement>();
        }
        
        events.add(event);        
        locationsForDevice.put(event.getLevel(), events);
        
        trajectoryEvents.put(event.getId(), locationsForDevice); 
          
    }   

    @Override
    public void serializeTraceInFilePath(String path, TraceOutputType outputType) {                   
                        
//        printListOfMapElements(path, "trajectoryEvents", outputType, trajectoryEvents);        
//        printListOfMapElements(path, "trajectoryChangeEvents", outputType, trajectoryChangeEvents);        
//        printListOfMapElements(path, "locationEvents", outputType, locationEvents);
        
//        printListOfAlarms(path, outputType, behaviorAlarmEvents);
        
//        printTimeGap(head, path, "locationEvents", locationEvents);
    }
    
    protected String getCurrentDateString(){
        if(dateStr.length() == 0){
            Date date = new Date();
        
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            dateStr = format.format(date);
        }
        
        return dateStr;
        
    }
    
    protected String getGeneralHeaderForOutputType(TraceOutputType type, String id){
        String header = "";
        
        switch(type){
            case PLAIN_TEXT:
                header = Constants.GPSVISUALIZER_WEB_HEAD_LINE;
                break;
            case GPX:
                header = Constants.GPX_HEAD_LINE;
                break;
            case KML:
                header = Constants.KML_GENERAL_HEAD_LINE;
                header += Constants.KML_TRACK_STYLE;

                header += Constants.KML_ALERT_ICON_STYLE_1;
                header += Constants.KML_ALERT_ICON_STYLE_2;
                
                header += Constants.KML_ALARM_TRACK_STYLE;
                
//                header += Constants.KML_ALARM_ICON_STYLE;
                break;
        }
                
        return header;
    }
    
    protected String getSpecificHeaderForOutputType(
            TraceOutputType typeOfOutput, 
            MapElementType typeOfEvent,
            String eventName,
            int level){
        String header = "";
        
        switch(typeOfOutput){
            case GPX:
                header = "<trk><name>"+eventName+"_"+dateStr+"_"+level+"</name>\n";
                if(MapElementType.LOCATION.equals(typeOfEvent)){
                    header +=  "\t<trkseg>\n";
                }
                break;
            case KML:
                header = Constants.KML_SPECIFIC_HEAD_LINE;
                header = header.replace("ELEMENT_NAME", eventName);
                header += Constants.KML_TRACK_STYLE;

                header += Constants.KML_ALERT_ICON_STYLE_1;
                header += Constants.KML_ALERT_ICON_STYLE_2;

                
                header += Constants.KML_ALARM_TRACK_STYLE;

                break;                

        }
        
        return header;
    }
    
    protected String getGeneralTailForOutputType(
            TraceOutputType typeOfOutput,
            MapElementType typeOfEvent){
        
        String header = "";
        
        switch(typeOfOutput){
            case GPX:
                header = Constants.GPX_TAIL_LINE;
                break;
            case KML:
                header = Constants.KML_GENERAL_TAIL_LINE;
                break;
        }
        
        return header;
    }
    
    protected String getSpecificTailForOutputType(
            TraceOutputType typeOfOutput,
            MapElementType typeOfEvent){
        
        String header = "";
        
        switch(typeOfOutput){
            case GPX:
                if(MapElementType.LOCATION.equals(typeOfEvent)){
                    header += "\t</trkseg>\n";
                }
                header += "</trk>\n";
                break;
        }
        return header;
    }
    
    protected void printListOfMapElements(
            String path,             
            String typeOfEvent,
            TraceOutputType traceOutputType,
            Map<String, Map<Integer, List<MapElement>>> mapElements){
        
        if(!mapElements.isEmpty()){
            try{
                MapElementType type = null;
                
                LOG.debug("Serializing "+typeOfEvent +" in "+traceOutputType+" format...");
                
                Set<String> ids = mapElements.keySet();
                
                for(String id : ids){
                    
                    Map<Integer, List<MapElement>> mapElementsForId = mapElements.get(id);
                
                    String traceOutputFilePath = path + id+ "_" + typeOfEvent;// +"_"+dateStr;

                    PrintWriter writerAll = new PrintWriter(traceOutputFilePath+"_all."+traceOutputType.getFileExtension());

                    String generalHeader = getGeneralHeaderForOutputType(traceOutputType, id);
                    writerAll.print(generalHeader);

                    Set<Integer> levels = mapElementsForId.keySet();

                    for(int level : levels){                   
                        try{

                            List<MapElement> mapElementsForLevel = mapElementsForId.get(level);

                            if(mapElementsForLevel.size() > 1){
                                PrintWriter writer = new PrintWriter(traceOutputFilePath+"_"+level+"."+traceOutputType.getFileExtension());
                                writer.append(generalHeader);

                                type = mapElementsForLevel.get(0).getType();

                                String levelHeader = getSpecificHeaderForOutputType(traceOutputType,type,type.toString(),level);
                                writer.print(levelHeader);
                                writerAll.print(levelHeader);

                                int i = 0;
                                while(i < mapElementsForLevel.size()){
                                    MapElement mapElement = mapElementsForLevel.get(i++);

                                    switch(traceOutputType){
                                        case PLAIN_TEXT:
                                        case GPX:
                                            writer.print(mapElement.serialize(traceOutputType));
                                            writerAll.print(mapElement.serialize(traceOutputType));
                                            break;
                                        case KML:
//                                            if(level != 1){
                                                if(i< mapElementsForLevel.size()){
                                                    MapElement aux = mapElementsForLevel.get(i);
                                                    writer.print(mapElement.serialize(traceOutputType, aux));
                                                    writerAll.print(mapElement.serialize(traceOutputType, aux));
                                                }
//                                            }
                                            break;
                                    }
                                }

                                writer.print(getSpecificTailForOutputType(traceOutputType, type));
                                writerAll.print(getSpecificTailForOutputType(traceOutputType, type));

                                writer.print(getGeneralTailForOutputType(traceOutputType, type));
                                writer.close();
                            }

                        }catch(Exception e){
                            LOG.error("Error serializating event trace for level "+level, e);
                        }
                    }

                    writerAll.print(getGeneralTailForOutputType(traceOutputType,type));
                    writerAll.close();
                }

            }catch(Exception e){
                LOG.error("Error serializating events trace ", e);
            }
        }        
    }            

    
    
    protected void printTimeGap(  
            String head,            
            String path,
            String typeOfEvent,
            Map<Integer, List<MapElement>> mapElements){
        
        if(!mapElements.isEmpty()){
            try{                                
                LOG.debug("Serializing gap times for event "+typeOfEvent);
                                                
                String traceOutputFileName = head + "_" + typeOfEvent +"_all";

                String traceOutputDateFileName = traceOutputFileName + ".dat";
                
                PrintWriter writerAll = new PrintWriter(path + traceOutputDateFileName);
                
                Set<Integer> levels = mapElements.keySet();
                
                List<Integer> noShowedElements = new LinkedList<Integer>();
                noShowedElements.add(new Integer(0));
                noShowedElements.add(new Integer(1));
//                noShowedElements.add(new Integer(2));
                noShowedElements.add(new Integer(6));
                
                levels.removeAll(noShowedElements);
                
                Map<Integer, String> values = new HashMap<Integer, String>();
                double maxY = 0;
                for(int level : levels){  
                    try{
                        List<MapElement> mapElementsForLevel = mapElements.get(level);                       
                        
                        int index = 0;
                        for(MapElement element : mapElementsForLevel){
                            String st = "";
                            double value = element.getTimestampGap()/1000;
                            
                            if(value > maxY){
                                maxY = value;
                            }
                            
                            if(values.containsKey(index)){
                                st = values.get(index);
                                st = st + "\t"+ value;
                            }else{
                                st = String.valueOf(value);
                            }
                            
                            values.put(index, st);
                            index++;
                        }
                        
                    }catch(Exception e){
                        LOG.error("Error serializating delays for level "+level, e);
                    }
                }
                
                //Create GNUPlot script
                
                PrintWriter writerGNUPlot = new PrintWriter(path + traceOutputFileName+".gp");
                String header = Constants.GNUPLOT_HEAD_LINE;
          
                StringBuilder xticsLine = new StringBuilder();
                
                int xCoords = (levels.size()/2)+1;
                
                ArrayList<Integer> orderedKeys = new ArrayList(values.keySet());    
                Collections.sort(orderedKeys);
                
                for(int i : orderedKeys){
                    
                    if(xticsLine.length() > 0){
                        xticsLine.append(", ");
                    }else{
                        xticsLine.append("set xtics (");
                    }
                    
                    String value = values.get(i);                    
                    writerAll.println(xCoords + "\t" + value);
                    
                    xticsLine.append("\"");
                    xticsLine.append(i);
                    xticsLine.append("\" ");
                    xticsLine.append(xCoords);
                    
                    xCoords += levels.size();                    
                    xCoords++;
                }
                        
                xticsLine.append(") border out nomirror");
                
                writerAll.close();
                
                header = header.replace("<maxy>", String.valueOf(maxY));
                header = header.replace("<maxx>", String.valueOf(xCoords));
                                              
                writerGNUPlot.print(header);
                writerGNUPlot.println(xticsLine.toString());
                
                StringBuilder plotPartSt = new StringBuilder();
                String beginPlotPart = "plot ";
                plotPartSt.append(beginPlotPart);
                
                StringBuilder plotPartTemplate = new StringBuilder();
                plotPartTemplate.append("'");
                plotPartTemplate.append(traceOutputDateFileName);
                plotPartTemplate.append("' ");
                plotPartTemplate.append("using ($1<val1>):($<val2>):(1) w boxes fs pattern 3 lw 1 title \"Level <val3>\"");
                
                int numLevels = levels.size();
                double inc = 0.5;
                if((numLevels % 2) != 0){
                    inc = 1;
                    numLevels--;
                }
                
                double column1Value = 0 - (inc * (numLevels/2));
                
                for(int i : levels){
                   
                    if(plotPartSt.length()> beginPlotPart.length()){
                        plotPartSt.append(", ");
                    }
                    
                    String plotPart = plotPartTemplate.toString();
                    int newIndex = i-noShowedElements.size()+2;
                    
                    String column1String = "";
                    
                    if(column1Value > 0){
                        column1String = "+" + String.valueOf(column1Value);
                    }else if(column1Value < 0){
                        column1String = String.valueOf(column1Value);                        
                    }
                    
                    plotPart = plotPart.replace("<val1>", column1String);
                    plotPart = plotPart.replace("<val2>", String.valueOf(newIndex+1));
                    plotPart = plotPart.replace("<val3>", String.valueOf(i));
                                        
                    plotPartSt.append(plotPart);
                    
                    column1Value += inc;
                }
                
                writerGNUPlot.println(plotPartSt.toString());
                writerGNUPlot.close();

            }catch(Exception e){
                LOG.error("Error serializating delays ", e);
            }  
        }        
    }

    @Override
    protected void registerAlertEvent(AlertEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void registerAlarmEvent(AlarmEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
