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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tool.Constants;
import tool.Point;
import tool.trace.visualizer.TraceOutputType;
import org.jdom.Element;
import org.jdom.Namespace;


/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class SpeedChangeAlert extends BehaviorAlertEvent{
    
    Point initialLocation;
    Point finalLocation;
    
    double avgSpeed;
    double currentSpeed;
    
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

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setCloseToPort(boolean closeToPort) {
        this.closeToPort = closeToPort;
    }
    
    @Override
    public BehaviorAlertType getAlertType(){
        return BehaviorAlertType.SPEED_CHANGE_ALERT;
    }

    @Override
    public long getTimestampGap() {
        return timestamp - finalLocation.getTimestamp();
    }

    @Override
    public String serialize(TraceOutputType format, MapElement... aux) {
        String result = "";
        switch(format){
            case GPX:
                result = toGPXFormat();
                break;
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
    public String toPlainTextFormat(){
        
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        
        StringBuilder des = new StringBuilder();
        des.append("SC[");
        des.append(id);  
        des.append(", ");
        des.append(format.format(timestamp)); 
        des.append(", {");
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        des.append(twoDForm.format(avgSpeed).replace(",", "."));
        des.append("->");
        des.append(twoDForm.format(currentSpeed).replace(",", "."));
        des.append("}, ");
        if(initialLocation != null){
        des.append("{");        
            Date date = new Date(initialLocation.getTimestamp());
            des.append(format.format(date)); 
        }else{
            des.append("x");
        }
        des.append("->");
        if(finalLocation != null){
            Date date = new Date(finalLocation.getTimestamp());
            des.append(format.format(date)); 
        }else{
            des.append("x");
        }
        des.append("}");
        String port = (closeToPort) ? ", close_to_port" : "";
        des.append(port);
        des.append("]\n");
        
        return des.toString();
    }
    
    
    @Override
    protected String toGPXFormat(){
        
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        StringBuilder sb = new StringBuilder();
        sb.append("<trkseg>\n");
        
        if(initialLocation != null){
            sb.append("\t<trkpt lat=\"");
            sb.append(initialLocation.getLat());
            sb.append("\"  lon=\"");
            sb.append(initialLocation.getLon());
            sb.append("\">\n\t\t<time>");
            sb.append(format.format(date)); 
            sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n");            
        }
        
        sb.append("\t<trkpt lat=\"");
        sb.append(finalLocation.getLat());
        sb.append("\"  lon=\"");
        sb.append(finalLocation.getLon());
        sb.append("\">\n\t\t<time>");
        sb.append(format.format(date));
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n"); 
        
        sb.append("</trkseg>\n");

        return sb.toString();        
    }
    
    protected static SpeedChangeAlert fromKML(Element placemarkElement) throws Exception{
        
        Pattern shipIDPattern = Pattern.compile("Ship ID: \\w+;");
        Pattern avgSpeedPattern = Pattern.compile("Avg. speed: \\d+\\.?\\d*;");        
        Pattern currentSpeedPattern = Pattern.compile("Current speed: \\d+\\.?\\d*;");
        
        SpeedChangeAlert alert = new SpeedChangeAlert();
                
        Namespace ns = placemarkElement.getNamespace();
        
        String description = placemarkElement.getChildText("description", ns);
        
        Matcher m = shipIDPattern.matcher(description);

        String shipID = "";
        if(m.find()){
            String shipIDSt = m.group(0);

            int i = shipIDSt.indexOf(":");
            shipID = shipIDSt.substring(i+1, shipIDSt.length()-1).trim();

        }
        
        m = avgSpeedPattern.matcher(description);

        double avgSpeed = 0;
        if(m.find()){
            String avgSpeedSt = m.group(0);

            int i = avgSpeedSt.indexOf(":");
            avgSpeedSt = avgSpeedSt.substring(i+1, avgSpeedSt.length()-1).trim();

            avgSpeed = Double.valueOf(avgSpeedSt);

        }
        
        m = currentSpeedPattern.matcher(description);

        double currentSpeed = 0;
        if(m.find()){
            String currentSpeedSt = m.group(0);

            int i = currentSpeedSt.indexOf(":");
            currentSpeedSt = currentSpeedSt.substring(i+1, currentSpeedSt.length()-1).trim();

            currentSpeed = Double.valueOf(currentSpeedSt);

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
        
        alert.setAvgSpeed(avgSpeed);
        alert.setCurrentSpeed(currentSpeed);
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
