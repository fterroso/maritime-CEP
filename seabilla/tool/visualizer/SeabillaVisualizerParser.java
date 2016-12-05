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
package seabilla.tool.visualizer;

import event.MapElement;
import event.MapElementType;
import event.alert.AlertEvent;
import event.location.LocationEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import org.apache.log4j.Logger;
import seabilla.lowlevel.CEP.event.alert.BehaviorAlertEvent;
import seabilla.lowlevel.CEP.event.alert.BehaviorAlertType;
import seabilla.lowlevel.CEP.event.alert.CollisionAlert;
import tool.Constants;
import tool.Point;
import tool.trace.visualizer.BasicVisualizerParser;
import tool.trace.visualizer.TraceOutputType;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class SeabillaVisualizerParser extends BasicVisualizerParser{
    
    static Logger LOG = Logger.getLogger(BasicVisualizerParser.class);    

    /*Abnormal behavior detection */
    Map<String, Map<Integer, List<MapElement>>> behaviorAlertEvents;    
    
    List<MapElement> locationTraces;
    
    public SeabillaVisualizerParser() {
        super();      
        behaviorAlertEvents = new HashMap<String, Map<Integer, List<MapElement>>>();
    }
    
    @Override
    protected void registerAlertEvent(AlertEvent alert){

        storeAlert(alert);

        BehaviorAlertEvent event = (BehaviorAlertEvent) alert;
        
        if(BehaviorAlertType.COLLISION_ALERT.equals(event.getAlertType())){
            
            CollisionAlert collAlarm = (CollisionAlert)event;
            CollisionAlert newAlarm = new CollisionAlert();
                        
            String newID = collAlarm.getOtherId();
            String otherID = collAlarm.getId();
            
            long timestamp = collAlarm.getTimestamp();
            Point locationCollision = collAlarm.getLocationOfCollision();
            boolean closeToPort = collAlarm.isCloseToPort();
            
            Point newLoc = collAlarm.getOtherLocation();
            Point otherLoc = collAlarm.getLocation();
            
            if(newLoc != null){
            
                newAlarm.setId(newID);
                newAlarm.setOtherId(otherID);

                newAlarm.setLocation(newLoc);
                newAlarm.setOtherLocation(otherLoc);

                newAlarm.setLocationOfCollision(locationCollision);

                newAlarm.setTimestamp(timestamp);
                newAlarm.setCloseToPort(closeToPort);            

                storeAlert(newAlarm);
            }
        }
        
    } 
 
    private void storeAlert(AlertEvent alert){
        
        BehaviorAlertEvent event = (BehaviorAlertEvent) alert;
        
        List<MapElement> events;
        Map<Integer, List<MapElement>> alertsForDevice;
        
        if(behaviorAlertEvents.containsKey(event.getId())){
            alertsForDevice = behaviorAlertEvents.get(event.getId());
            if(alertsForDevice.containsKey(event.getAlertType().getIntValue())){
                events = alertsForDevice.get(event.getAlertType().getIntValue());
            }else{
                events = new LinkedList<MapElement>();
            }
        }else{
            alertsForDevice = new HashMap<Integer, List<MapElement>>();
            events = new LinkedList<MapElement>();
        }
        
        events.add(event);        
        alertsForDevice.put(event.getAlertType().getIntValue(), events);
      
        behaviorAlertEvents.put(event.getId(), alertsForDevice); 

    }
    
    @Override
    public void serializeTraceInFilePath(String path, TraceOutputType outputType){
        
        try{
            super.serializeTraceInFilePath(path, outputType);

            if(!path.endsWith(File.separator)){
                path = path.concat(File.separator);
            }
            
            generalWriter = new PrintWriter(getGeneralOutputFileName(path) +"."+outputType.getFileExtension());
            String generalHeader = getGeneralHeaderForOutputType(outputType, "General");
            generalWriter.print(generalHeader);
           
            printListOfLocations();
            printListOfAlerts(path+"/alerts/", outputType, behaviorAlertEvents);

            generalWriter.print(Constants.KML_GENERAL_TAIL_LINE);
            generalWriter.close();
        }catch(Exception e){
            LOG.error("Error serializating events trace ", e);
        }   
     }

    public List<MapElement> getLocationTraces() {
        return locationTraces;
    }

    public void setLocationTraces(List<MapElement> locationTraces) {
        this.locationTraces = locationTraces;
    }
    
    
    
    protected String getGeneralOutputFileName(String path){
        StringBuilder generalOutputPath = new StringBuilder();
        generalOutputPath.append(path);
        generalOutputPath.append("/all/");
        generalOutputPath.append("UMU_results_");
        generalOutputPath.append(getCurrentDateString());
            
        return generalOutputPath.toString();
    }
    
    protected void printListOfLocations(){
        if(locationEvents!= null){
            generalWriter.print(Constants.KML_GENERAL_TRACK_HEAD_LINE);
            
            for(String id : locationEvents.keySet()){
                Map<Integer,List<MapElement>> locationsForDevice = locationEvents.get(id);
                String headerForDevice = Constants.KML_SPECIFIC_TRACK_HEAD_LINE.replace("ID_CODE", id);
                generalWriter.print(headerForDevice);
                List<MapElement> locations = locationsForDevice.get(0);
                
                for(MapElement location : locations){
                    LocationEvent l = (LocationEvent) location;
                    generalWriter.println(l.serialize(TraceOutputType.KML));                    
                }
                
                generalWriter.print(Constants.KML_SPECIFIC_TRACK_TAIL);
            }
            
            generalWriter.print(Constants.KML_GENERAL_TRACK_TAIL_LINE);
        }
    }
    
    protected void printListOfAlerts(
            String path,             
            TraceOutputType traceOutputType,
            Map<String, Map<Integer, List<MapElement>>> mapElements){
        
        if(!mapElements.isEmpty()){
            try{
                MapElementType type = null;
                
                generalWriter.print(Constants.KML_GENERAL_ALERT_HEAD_LINE);
                
                LOG.debug("Serializing behavior alerts in "+traceOutputType+" format...");
                
                Set<String> ids = mapElements.keySet();
                BehaviorAlertType[] behaviorAlertTypes = BehaviorAlertType.values();
                
                for(BehaviorAlertType alertType : behaviorAlertTypes){
                    
                    String traceOutputFilePath = path + alertType.toString().toLowerCase();
                    PrintWriter writerAll = null;
                    String generalHeader = getGeneralHeaderForOutputType(traceOutputType, alertType.toString());
                    
                    int level = alertType.getIntValue();
                    
                    for(String id : ids){
                    
                        Map<Integer, List<MapElement>> mapElementsForId = mapElements.get(id);

                        if(mapElementsForId.containsKey(level)){

                            try{
                                
                                if(writerAll == null){
                                    writerAll = new PrintWriter(traceOutputFilePath+"_all."+traceOutputType.getFileExtension());
                                    writerAll.print(generalHeader);
                                }

                                List<MapElement> mapElementsForLevel = mapElementsForId.get(level);

                                String alertDescName = id + "_"+alertType.toString().toLowerCase();
                                PrintWriter writer = new PrintWriter(path + alertDescName +"."+traceOutputType.getFileExtension());
                                
                                String specificHeader = getSpecificHeaderForOutputType(traceOutputType,type,alertDescName, alertType.getIntValue());
                                writer.print(specificHeader);

                                int i = 0;
                                while(i < mapElementsForLevel.size()){
                                    
                                    MapElement mapElement = mapElementsForLevel.get(i++);

                                    writer.print(mapElement.serialize(traceOutputType));
                                    writerAll.print(mapElement.serialize(traceOutputType));
                                    generalWriter.print(mapElement.serialize(traceOutputType));
                                    
                                }

                                writer.print(getSpecificTailForOutputType(traceOutputType, type));
                                writerAll.print(getSpecificTailForOutputType(traceOutputType, type));

                                writer.print(getGeneralTailForOutputType(traceOutputType, type));
                                writer.close();


                            }catch(Exception e){
                                LOG.error("Error serializating alert event "+BehaviorAlertType.getAlertTypeFromInt(level), e);
                            }
                        }
                    }
                    
                    if(writerAll != null){
                        writerAll.print(getGeneralTailForOutputType(traceOutputType,type));
                        writerAll.close();
                    }
                           
                }
                
                generalWriter.print(Constants.KML_GENERAL_ALERT_TAIL_LINE);

            }catch(Exception e){
                LOG.error("Error serializating events trace ", e);
            }
        }        
    }  
       
    
    private List<MapElement> getElementsForIDAndTimestampRange(
            String id, 
            long initialTimestamp, 
            long finalTimestamp){
        
        List<MapElement> result = new LinkedList<MapElement>();
        
        Map<Integer, List<MapElement>> locationLevels =  locationEvents.get(id);
        List<MapElement> locations = locationLevels.get(0);
        
        for(MapElement location : locations){
            if(location.getTimestamp() >= initialTimestamp && 
                    location.getTimestamp() <= finalTimestamp){
                result.add(location);
            }
        }
        
        return result;
    }
    
    private String createKMLForListOfElements(List<MapElement> locations){
        StringBuilder involvedLocationSt = new StringBuilder();
        involvedLocationSt.append("<gx:Track>");
        involvedLocationSt.append("<extrude>1</extrude>");
        involvedLocationSt.append("<altitudeMode>absolute</altitudeMode>");                            

        for(MapElement location : locations){
            involvedLocationSt.append(location.serialize(TraceOutputType.KML));
        }
        involvedLocationSt.append("</gx:Track>");
        
        return involvedLocationSt.toString();
    }
}
