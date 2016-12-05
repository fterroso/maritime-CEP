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
package seabilla.lowlevel.CEP.event.alert;

import event.MapElement;
import event.MapElementType;
import event.alert.AlertEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import tool.Constants;
import tool.Point;
import tool.trace.visualizer.TraceOutputType;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public abstract class BehaviorAlertEvent extends AlertEvent{
        
    static Logger LOG = Logger.getLogger(BehaviorAlertEvent.class); 
    
    String id;
    long timestamp;
    Point location;
    
    BehaviorAlertType alertType;
    
    boolean closeToPort;
    
    AlertSourceType sourceType;
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }        

    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    } 
    
    public abstract BehaviorAlertType getAlertType();

    public void setAlertType(BehaviorAlertType alertType) {
        this.alertType = alertType;
    }        
    
    public boolean isCloseToPort(){
        return closeToPort;
    }

    public AlertSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(AlertSourceType sourceType) {
        this.sourceType = sourceType;
    }         
    
    @Override
    public String serialize(TraceOutputType format, MapElement... aux) {
        String result = "";
        switch(format){
            case PLAIN_TEXT:
                result = toPlainTextFormat();
                break;
            case KML:
                result = toKMLFormat();
                break;
        }
        
        return result;
    }
    
    @Override
    protected String toKMLFormat(){
        
        StringBuilder sb = new StringBuilder();
        
        DateFormat format = new SimpleDateFormat(Constants.KML_DATE_FORMAT);

        
        //Header
        sb.append("<Placemark>\n");
        
        sb.append("\t<styleUrl>#");
        sb.append(getAlertType().getLevel().getDescription());
        sb.append("</styleUrl>\n");
        
        sb.append("\t<name>");
        String alertName = getAlertType().getDescription();
        sb.append(alertName);
        sb.append(" :");
        sb.append(getId());
        sb.append("</name>\n");
        
        //Description
        sb.append("\t<ExtendedData>\n");
        
        sb.append("\t\t<Data name=\"atp\">\n");
        sb.append("\t\t\t<value>");
        sb.append(alertName);
        sb.append(" ");
        sb.append(getDetailedDescription());
        sb.append("</value>\n");
        sb.append("\t\t</Data>\n");
        
        sb.append("\t\t<Data name=\"vss\">\n");
        sb.append("\t\t\t<value>");
        sb.append(getId());
        sb.append("</value>\n");
        sb.append("\t\t</Data>\n");
        
        sb.append("\t\t<Data name=\"tm\">\n");
        sb.append("\t\t\t<value>");
        sb.append(format.format(timestamp));
        sb.append("</value>\n");
        sb.append("\t\t</Data>\n");
        
        sb.append("\t\t<Data name=\"alg\">\n");
        sb.append("\t\t\t<value>");
        sb.append("UMU-CEP-traj");
        sb.append("</value>\n");
        sb.append("\t\t</Data>\n");
                
        sb.append("\t</ExtendedData>\n");
        
//        sb.append("\t<TimeStamp>\n");
//        sb.append("\t\t<when>");
//        sb.append(format.format(timestamp));
//        sb.append("\t\t</when>\n");
//        sb.append("\t</TimeStamp>\n");
        
        sb.append("\t<Point>\n");
        sb.append("\t\t<coordinates>");
        sb.append(location.getLon());
        sb.append(",");
        sb.append(location.getLat());
        sb.append(",0");
        sb.append("</coordinates>\n");
        sb.append("\t</Point>\n");
        sb.append("</Placemark>\n");
                
        return sb.toString();
    }
    
    @Override
    public MapElementType getType() {
        return MapElementType.BEHAVIOR_ALERT;
    }
    
    public static AlertEvent alertFromKML(Element kmlElement) throws Exception{
        
        AlertEvent alert = null;
        
        Namespace ns = kmlElement.getNamespace();

        Element documentNameE = kmlElement.getChild("name",ns);

        String alertTypeSt = documentNameE.getText().toUpperCase().replace(" ", "_");

        BehaviorAlertType alertType = BehaviorAlertType.valueOf(alertTypeSt);
        
//        List<CollisionAlert> generatedCollisionAlerts = new LinkedList<CollisionAlert>();
        switch(alertType){
            case SPEED_CHANGE_ALERT:
                alert = SpeedChangeAlert.fromKML(kmlElement);
                break;
            case BEARING_CHANGE_ALERT:
                alert = BearingChangeAlert.fromKML(kmlElement);
                break;
            case BEARING_SPEED_CHANGE_ALERT:
                alert = BearingSpeedChangeAlert.fromKML(kmlElement);
                break;
            case OUT_OF_NORMAL_ROUTE_ALERT:
                alert = OutOfNormalRouteAlert.fromKML(kmlElement);
                break;
            case COLLISION_ALERT:  
                CollisionAlert collisionAlert = CollisionAlert.fromKML(kmlElement);
                
//                if(!generatedCollisionAlerts.isEmpty()){
//                    for(CollisionAlert generatedCollisionAlert: generatedCollisionAlerts){
//                        if((!collisionAlert.getId().equals(generatedCollisionAlert.getId()) &&
//                                !collisionAlert.getId().equals(generatedCollisionAlert.getId())) || 
//                                collisionAlert.getTimestamp() != generatedCollisionAlert.getTimestamp()){
                            alert = collisionAlert;
//                            LOG.debug("Created Collision alert "+ collisionAlert);
//
//                            generatedCollisionAlerts.add(collisionAlert);
//                        }else{
//                            LOG.debug("Collision alert "+ collisionAlert + "has been deleted");
//                        }       
//                    }
//                }else{
//                    alert = collisionAlert;
//                    generatedCollisionAlerts.add(collisionAlert);
//                }
                break;
        }
        
        return alert;        
    }   
    
    protected abstract String getDetailedDescription();
}
