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
package event.trajectory;

import event.MapElement;
import event.MapElementType;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import tool.Bearing;
import tool.Point;

/**
 *
 * @author calcifer
 */
public class TrajectoryEvent extends MapElement{
    
    String id;
    long timestamp;
    
    MapElementType type = MapElementType.TRAJECTORY;
    
    int level;
    
    Point[] locations = new Point[20];
    
    double avgSpeed;
    double currentSpeed;
    
    int headNumSeq;
    int tailNumSeq;
    
    double avgBearing;    
    double straightBearing;
        
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getAvgBearing() {
        return avgBearing;
    }

    public void setAvgBearing(double avgBearing) {
        this.avgBearing = avgBearing;
    }

    public double getStraightBearing() {
        return straightBearing;
    }

    public void setStraightBearing(double straightBearing) {
        this.straightBearing = straightBearing;
    }        

    public int getHeadNumSeq() {
        return headNumSeq;
    }

    public void setHeadNumSeq(int headNumSeq) {
        this.headNumSeq = headNumSeq;
    }

    public int getTailNumSeq() {
        return tailNumSeq;
    }

    public void setTailNumSeq(int tailNumSeq) {
        this.tailNumSeq = tailNumSeq;
    }   

    public Point[] getLocations() {
        return locations;
    }

    public void setLocations(Point[] locations) {
        this.locations = locations;
    }
    
    public Point getHead(){
        return locations[locations.length-1];
    }
    
    public void setHead(){}
    
    public Point getTail(){
        return locations[0];
    }
    
    public void setTail(Point p){}

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
    
    @Override
    public MapElementType getType() {
        return type;
    }

    @Override
    public long getTimestampGap(){
        return timestamp - locations[locations.length -1].getTimestamp();
    }
    
    @Override
    protected String toPlainTextFormat(){
        StringBuilder sb = new StringBuilder();
        
        sb.append(headNumSeq);
        sb.append("-");
        sb.append(tailNumSeq);
        sb.append(",te,green,100,start,");
        sb.append(locations[0].getLat());
        sb.append(",");
        sb.append(locations[0].getLon());
        sb.append("\n");
        
        sb.append(headNumSeq);
        sb.append("-");
        sb.append(tailNumSeq);
        sb.append(",te,green,100,start,");
        sb.append(locations[locations.length-1].getLat());
        sb.append(",");
        sb.append(locations[locations.length-1].getLon());
        sb.append("\n");
        
        return sb.toString();
        
    } 

    @Override
    protected String toGPXFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("<trkseg>\n");
        
        sb.append("\t<trkpt lat=\"");
        sb.append(locations[0].getLat());
        sb.append("\"  lon=\"");
        sb.append(locations[0].getLon());
        sb.append("\">\n\t\t<time>");
        
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        sb.append(format.format(date)); 
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n");            
        sb.append("\t<trkpt lat=\"");
        sb.append(locations[locations.length-1].getLat());
        sb.append("\"  lon=\"");
        sb.append(locations[locations.length-1].getLon());
        sb.append("\">\n\t\t<time>");
        sb.append(timestamp);
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n"); 
        
        sb.append("</trkseg>\n");

        return sb.toString();        
    }

    
    @Override
    public String toString(){
        
        StringBuilder des = new StringBuilder();
        des.append("[");
        des.append(id);
        des.append(", ");
        des.append(level);
        des.append(", {");
        des.append(tailNumSeq);
        des.append(",");
        des.append(headNumSeq);
        des.append("}, "); 
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        des.append(twoDForm.format(avgSpeed).replace(",", "."));
        des.append(", "); 
        twoDForm = new DecimalFormat("#.##");
        des.append(twoDForm.format(currentSpeed).replace(",", "."));
        des.append(", "); 
        des.append("{");
//        des.append("\n");
//        for(Point e : locations){
//            des.append(e);
//            des.append(";\n");
//        }
        des.append("}, ");
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(date)); 
        des.append(", ");
        des.append(Bearing.getFineGrainBearingFromValue(straightBearing));
        des.append("(");
        des.append(straightBearing);
        des.append(")]");
        
        return des.toString();
        
    }

    @Override
    protected String toKMLFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
