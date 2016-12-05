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
import tool.Color;
import tool.Point;

/**
 *
 * @author calcifer
 */
public class TrajectoryChangeEvent extends MapElement{
     
    String id;
    long timestamp;
    
    long sourceTimestamp;
    
    MapElementType typeElement = MapElementType.TRAJECTORY_CHANGE;
    
    TrajectoryChangeType type;
    
    int level;
    
    Point tail;
    Point head;
    Point middle;
        
    int headNumSeq;
    int tailNumSeq; 
        
    double initialBearingValue;
    double finalBearingValue;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrajectoryChangeType getChangeType() {
        return type;
    }

    public void setChangeType(TrajectoryChangeType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }        

    public Point getHead() {
        return head;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public Point getMiddle() {
        return middle;
    }

    public void setMiddle(Point middle) {
        this.middle = middle;
    }

    public Point getTail() {
        return tail;
    }

    public void setTail(Point tail) {
        this.tail = tail;
    }  
    
    @Override
    public MapElementType getType() {
        return typeElement;
    }

    public long getSourceTimestamp() {
        return sourceTimestamp;
    }

    public void setSourceTimestamp(long sourceTimestamp) {
        this.sourceTimestamp = sourceTimestamp;
    }        
    
    private String getColorCodeFromLevel(){
        
        return Color.getColorForLevel(level).getName();
    }

    @Override
    public long getTimestampGap(){
        
        return timestamp - middle.getTimestamp();
    }

    
    @Override
    protected String toPlainTextFormat(){
        
        StringBuilder sb = new StringBuilder();
        
//        sb.append(headNumSeq);
//        sb.append("-");
//        sb.append(tailNumSeq); 
//        sb.append("-");
        sb.append(level); 
        sb.append(",");
        sb.append(type.getDescriptor());
        sb.append(",");
        sb.append(getColorCodeFromLevel());
        sb.append(",100,triangle,");                              
        sb.append(middle.getLat());
        sb.append(",");
        sb.append(middle.getLon());
        sb.append("\n");   
        
        return sb.toString();        
    }
    
    @Override
    protected String toGPXFormat() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("<trkseg>\n");
        
        sb.append("\t<trkpt lat=\"");
        sb.append(tail.getLat());
        sb.append("\"  lon=\"");
        sb.append(tail.getLon());
        sb.append("\">\n\t\t<time>");
        sb.append(timestamp);
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n");    
        
        sb.append("\t<trkpt lat=\"");
        sb.append(middle.getLat());
        sb.append("\"  lon=\"");
        sb.append(middle.getLon());
        sb.append("\">\n\t\t<time>");
        
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        sb.append(format.format(date)); 
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n"); 
        
        sb.append("\t<trkpt lat=\"");
        sb.append(head.getLat());
        sb.append("\"  lon=\"");
        sb.append(head.getLon());
        sb.append("\">\n\t\t<time>");
        sb.append(timestamp);
        sb.append("</time>\n\t\t<ele>0</ele>\n\t</trkpt>\n");         
        
        sb.append("</trkseg>\n");

        return sb.toString();  
    }
    
    @Override
    protected String toKMLFormat(){
        
        StringBuilder sb = new StringBuilder();
        
        //Header
        sb.append("<Placemark>\n");
        sb.append("\t<styleUrl>#iconstyle");
        sb.append(level);
        sb.append("</styleUrl>\n");
        
        //Description
        sb.append("\t<description>");
        sb.append("Ship ID: ");
        sb.append(getId());
        sb.append("; Detection time: ");
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        sb.append(format.format(timestamp)); 
        sb.append("; Intial bearing: ");
        sb.append(Bearing.getFineGrainBearingFromValue(initialBearingValue));
        sb.append("(");
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        sb.append(twoDForm.format(initialBearingValue).replace(",", "."));
        sb.append(("); Final bearing: "));
        sb.append(Bearing.getFineGrainBearingFromValue(finalBearingValue));
        sb.append("(");
        sb.append(twoDForm.format(finalBearingValue).replace(",", "."));
        sb.append(")");
        sb.append("; Header: ");
        sb.append(head.getNumSeq());
        sb.append("; Tail: ");
        sb.append(tail.getNumSeq());
        sb.append("</description>\n");
        
        sb.append("\t<TimeStamp>\n");
        sb.append("\t\t<when>");
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sb.append(format.format(timestamp));
        sb.append("\t\t</when>\n");
        sb.append("\t</TimeStamp>\n");
        
        sb.append("\t<Point>\n");
        sb.append("\t\t<tessellate>1</tessellate>\n");
        sb.append("\t\t<coordinates>");
        sb.append(middle.getLon());
        sb.append(",");
        sb.append(middle.getLat());
        sb.append(",0");
        sb.append("</coordinates>\n");
        sb.append("\t</Point>\n");
        sb.append("</Placemark>\n");
                
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
        des.append("}, {");  
        des.append(this.type);
//        des.append(head);
//        des.append(",");
//        des.append(middle);
//        des.append(",");
//        des.append(tail);        
        des.append("}, ");
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(date)); 
        des.append(", ");
        des.append(Bearing.getFineGrainBearingFromValue(initialBearingValue));
        des.append("(");
        des.append(initialBearingValue);
        des.append((") -> "));
        des.append(Bearing.getFineGrainBearingFromValue(finalBearingValue));
        des.append("(");
        des.append(finalBearingValue);
        des.append(")]");
        
        return des.toString();
        
    }
    
}
