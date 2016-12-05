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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.Element;
import org.jdom.Namespace;
import tool.Constants;
import tool.Point;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class CollisionAlert extends BehaviorAlertEvent{
 
    String otherId;        
    Point otherLocation;    
    Point locationOfCollision;
    
    
    @Override
    public BehaviorAlertType getAlertType() {
        return BehaviorAlertType.COLLISION_ALERT;
    }

    public Point getOtherLocation() {
        return otherLocation;
    }

    public void setOtherLocation(Point otherLocation) {
        this.otherLocation = otherLocation;
    }

    public void setCloseToPort(boolean isCloseToPort) {
        this.closeToPort = isCloseToPort;
    }
    
    public Point getLocationOfCollision() {
        return locationOfCollision;
    }

    public void setLocationOfCollision(Point locationOfCollision) {
        this.locationOfCollision = locationOfCollision;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }    

    @Override
    public long getTimestampGap() {
        return 0;
    }
    
    @Override
    public String toPlainTextFormat(){
        
        StringBuilder des = new StringBuilder();
        des.append("CA[");
        des.append(id);  
        des.append(", ");
        des.append(otherId); 
        des.append(", ");
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(timestamp));         
        des.append(", {");
        des.append(location.getNumSeq());
        des.append(",");
        if(otherLocation != null){
            des.append(otherLocation.getNumSeq());  
        }else{
            des.append("x");
        }
        des.append("},");
        des.append(location);
        String port = (closeToPort) ? ", close_to_port" : "";
        des.append(port);
        des.append("]\n");
        
        return des.toString();
    }
    
    protected static CollisionAlert fromKML(Element placemarkElement) throws Exception{
        
        Pattern shipIDsPattern = Pattern.compile("Ship IDs: \\w+ and  \\w+;");
        
        CollisionAlert alert = new CollisionAlert();
                
        Namespace ns = placemarkElement.getNamespace();
        
        String description = placemarkElement.getChildText("description", ns);
        
        Matcher m = shipIDsPattern.matcher(description);

        String shipID = "";
        String otherID = "";
        if(m.find()){
            String shipIDsSt = m.group(0);
            
            int i = shipIDsSt.indexOf(":");
            shipIDsSt = shipIDsSt.substring(i+1, shipIDsSt.length()-1).trim();
                        
            String[] IDs = shipIDsSt.split(" and ");            
            
            shipID = IDs[0].trim();
            otherID = IDs[1].trim();

        }
        
           
        Element timestampE = placemarkElement.getChild("TimeStamp",ns);
        String timestampSt = timestampE.getChild("when",ns).getValue();
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.KML_DATE_FORMAT);
        
        long timestamp = dateFormatter.parse(timestampSt).getTime();
    
        Element pointE = placemarkElement.getChild("Point", ns);
        String coordinatesSt = pointE.getChild("coordinates",ns).getValue();
        
        String coordinatesParts[] = coordinatesSt.split(",");
        double lon = Double.valueOf(coordinatesParts[0]);
        double lat = Double.valueOf(coordinatesParts[1]);
        
        Point p = new Point();
        p.setLat(lat);
        p.setLon(lon);
        
        alert.setId(shipID);
        alert.setOtherId(otherID);
        alert.setTimestamp(timestamp);
        alert.setLocation(p);
        
        return alert;
    }

    @Override
    protected String toGPXFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    protected String getDetailedDescription(){
        return "with "+otherId;
    }
}
