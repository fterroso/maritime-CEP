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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.Element;
import org.jdom.Namespace;
import tool.Constants;
import tool.Point;
import tool.supportFunction.LocationFunction;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BearingChangeAlert extends BehaviorAlertEvent{
    
    Point initialLocation;
    Point finalLocation;
        
    double initialBearingValue;
    double finalBearingValue;
    
    public Point getFinalLocation() {
        return finalLocation;
    }

    public void setFinalLocation(Point finalLocation) {
        this.finalLocation = finalLocation;
    }

    public Point getInitialLocation() {
        return initialLocation;
    }

    public void setInitialLocation(Point initialLocation) {
        this.initialLocation = initialLocation;
    }  
    
    public double getFinalBearing() {
        return finalBearingValue;
    }

    public void setFinalBearing(double finalBearingValue) {
        this.finalBearingValue = finalBearingValue;
    }

    public double getInitialBearing() {
        return initialBearingValue;
    }

    public void setInitialBearing(double initialBearingValue) {
        this.initialBearingValue = initialBearingValue;
    }

    public void setCloseToPort(boolean closeToPort) {
        this.closeToPort = closeToPort;
    }    
        
    @Override
    public BehaviorAlertType getAlertType() {
        return BehaviorAlertType.BEARING_CHANGE_ALERT;
    }        

    @Override
    public long getTimestampGap() {
        return timestamp - location.getTimestamp();
    }
    
    @Override
    public String toPlainTextFormat(){
        
        StringBuilder des = new StringBuilder();
        des.append("BCA[");
        des.append(id);  
        des.append(", ");
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(timestamp));         
        des.append(", {");
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        des.append(twoDForm.format(initialBearingValue).replace(",", "."));
        des.append("->");
        des.append(twoDForm.format(finalBearingValue).replace(",", "."));  
        des.append("}");
        String port = (closeToPort) ? ", close_to_port" : "";
        des.append(port);
        des.append("]\n");
        
        return des.toString();
    }
    
    protected static BearingChangeAlert fromKML(Element placemarkElement) throws Exception{
        
        Pattern shipIDPattern = Pattern.compile("Ship ID: \\w+;");
        Pattern initialBearingPattern = Pattern.compile("Initial bearing: \\w+\\(\\d+\\.?\\d*\\);");        
        Pattern finalBearingPattern = Pattern.compile("Final bearing: \\w+\\(\\d+\\.?\\d*\\);");

        
        BearingChangeAlert alert = new BearingChangeAlert();
                
        Namespace ns = placemarkElement.getNamespace();
        
        String description = placemarkElement.getChildText("description", ns);
        
        Matcher m = shipIDPattern.matcher(description);

        String shipID = "";
        if(m.find()){
            String shipIDSt = m.group(0);

            int i = shipIDSt.indexOf(":");
            shipID = shipIDSt.substring(i+1, shipIDSt.length()-1).trim();

        }
        
        m = initialBearingPattern.matcher(description);

        double initialBearing = 0;
        if(m.find()){
            String initialBearingSt = m.group(0);

            int i = initialBearingSt.indexOf(":");
            initialBearingSt = initialBearingSt.substring(i+1, initialBearingSt.length()-1).trim();
            i = initialBearingSt.indexOf("(");
            initialBearingSt = initialBearingSt.substring(i+1, initialBearingSt.length()-1);

            initialBearing = Double.valueOf(initialBearingSt);

        }
        
        m = finalBearingPattern.matcher(description);

        double finalBearing = 0;
        if(m.find()){
            String finalBearingSt = m.group(0);

            int i = finalBearingSt.indexOf(":");
            finalBearingSt = finalBearingSt.substring(i+1, finalBearingSt.length()-1).trim();
            i = finalBearingSt.indexOf("(");
            finalBearingSt = finalBearingSt.substring(i+1, finalBearingSt.length()-1);

            finalBearing = Double.valueOf(finalBearingSt);

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
        
        alert.setInitialBearing(initialBearing);
        alert.setFinalBearing(finalBearing);
        alert.setId(shipID);
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
        
        StringBuilder sb = new StringBuilder();
 
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        
        sb.append("(");
        sb.append(twoDForm.format(initialBearingValue).replace(",", "."));
        sb.append("->");
        sb.append(twoDForm.format(finalBearingValue).replace(",", "."));
        sb.append(")");
        if(LocationFunction.isIncreasingBearing(finalBearingValue, initialBearingValue)){
            sb.append(" inc.");
        }else{
            sb.append(" dec.");
        }
        
        return sb.toString();
    }
}
