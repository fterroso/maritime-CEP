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
import tool.areaOfInterest.NormalRouteArea;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class OutOfNormalRouteAlert extends BehaviorAlertEvent {

    NormalRouteArea area;

    public NormalRouteArea getArea() {
        return area;
    }

    public void setArea(NormalRouteArea area) {
        this.area = area;
    }        
    
    @Override
    public BehaviorAlertType getAlertType() {
        return BehaviorAlertType.OUT_OF_NORMAL_ROUTE_ALERT;
    }

    @Override
    public long getTimestampGap() {
        return 0;
    }

    @Override
    protected String toGPXFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String toPlainTextFormat() {
        StringBuilder des = new StringBuilder();
        des.append("OONR[");
        des.append(id);  
        des.append(", ");
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(timestamp));         
        des.append(", {"); 
        des.append(area);
        des.append("}]\n");
        
        return des.toString();
    }
    
    protected static OutOfNormalRouteAlert fromKML(Element placemarkElement) throws Exception{
        
        Pattern shipIDPattern = Pattern.compile("Ship ID: \\w+;");
        
        OutOfNormalRouteAlert alert = new OutOfNormalRouteAlert();
        
        Namespace ns = placemarkElement.getNamespace();
        
        String description = placemarkElement.getChildText("description", ns);
        
        Matcher m = shipIDPattern.matcher(description);

        String shipID = "";
        if(m.find()){
            String shipIDSt = m.group(0);

            int i = shipIDSt.indexOf(":");
            shipID = shipIDSt.substring(i+1, shipIDSt.length()-1).trim();

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
        alert.setTimestamp(timestamp);
        alert.setLocation(p);
        
        return alert;    
    }
    
    @Override
    protected String getDetailedDescription(){
        return "";
    }
}
