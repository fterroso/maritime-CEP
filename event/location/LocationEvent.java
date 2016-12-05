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
package event.location;

import event.MapElement;
import event.MapElementType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import tool.Color;
import tool.Constants;
import tool.Point;
import tool.trace.visualizer.TraceOutputType;

/**
 *
 * @author calcifer
 */
public class LocationEvent extends MapElement{
        
    String id;
    long timestamp;
    
    long sourceTimestamp;
    
    MapElementType type = MapElementType.LOCATION;
    
    int level;
    boolean isLast = false;
    
    Point location;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }        
    
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getSourceTimestamp() {
        return sourceTimestamp;
    }

    public void setSourceTimestamp(long sourceTimestamp) {
        this.sourceTimestamp = sourceTimestamp;
    }           

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public long getTimestampGap(){
        
        return timestamp - location.getTimestamp();
    }

    
    @Override
    public String serialize(TraceOutputType format, MapElement... next){
        String result = "";
        switch(format){
            case GPX:
                result = toGPXFormat();
                break;
            case PLAIN_TEXT:
                result = toPlainTextFormat();
                break;
            case KML:
                if(next.length > 0){
                    result = toKMLFormat((LocationEvent) next[0]);                
                }else{
                    result = toKMLFormat();
                }
                break;
        }
        
        return result;
    }
    
    @Override
    protected String toPlainTextFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append(location.getNumSeq());
        sb.append(",te,");
        sb.append(Color.getColorForLevel(level).getName());
        sb.append(",cross,100,");
        sb.append(location.getLat());
        sb.append(",");
        sb.append(location.getLon());
        sb.append("\n");
        
        return sb.toString();
        
    }
        
    @Override
    protected String toGPXFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t<trkpt lat=\"");
        sb.append(location.getLat());
        sb.append("\"  lon=\"");
        sb.append(location.getLon());
        sb.append("\">\n\t\t<time>");
        
        Date date = new Date(getTimestamp());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sb.append(format.format(date));        
        
        sb.append("</time>\n");
        sb.append("\t\t<desc>");
        sb.append(location.getNumSeq());
        sb.append(", ");
        sb.append(format.format(location.getTimestamp()));
        sb.append("</desc>\n\t</trkpt>\n");
                
        return sb.toString();
        
    }
    
    protected String toKMLFormat(LocationEvent next){
        StringBuilder sb = new StringBuilder();
        
        sb.append("<Placemark>\n\t<name>unextruded</name>\n\t<styleUrl>#linestyle");
        sb.append(getLevel());
        sb.append("</styleUrl>\n\t<TimeSpan>\n\t\t<begin>");
        
        Date date = new Date(next.getTimestamp());
        DateFormat format = new SimpleDateFormat(Constants.KML_DATE_FORMAT);
        sb.append(format.format(date)); 
        
        sb.append("</begin>\n\t</TimeSpan>\n\t<LineString>\n\t\t<extrude>1</extrude>\n\t\t<tessellate>1</tessellate>\n\t\t<coordinates>");
        sb.append(getLocation().getLon());
        sb.append(",");
        sb.append(getLocation().getLat());
        sb.append(",0,");
        sb.append(next.getLocation().getLon());
        sb.append(",");
        sb.append(next.getLocation().getLat());
        sb.append(",0");
        
        sb.append("</coordinates>\n\t</LineString>\n</Placemark>\n");
                
        return sb.toString();
    }
    
    @Override
    protected String toKMLFormat(){
        StringBuilder sb= new StringBuilder();
        sb.append("<when>");
        DateFormat format = new SimpleDateFormat(Constants.KML_DATE_FORMAT);
        sb.append(format.format(timestamp));
        sb.append("</when>\n");
        sb.append("<gx:coord>");
        sb.append(location.getLon());
        sb.append(" ");
        sb.append(location.getLat());
        sb.append(" 0");
        sb.append("</gx:coord>\n");
        sb.append("<gx:angles>0  0  0</gx:angles>\n");
        sb.append("<speed>");
        sb.append(location.getSpeed());
        sb.append("</speed>\n");
        
        return sb.toString();
    }
    
    @Override
    public String toString(){
        
        StringBuilder des = new StringBuilder();
        des.append("[");
        des.append(id);
        des.append(", ");
        des.append(location.getNumSeq());
        des.append(", ");        
        des.append(level);
        des.append(", ");        
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(date));  
        des.append(", ");
        des.append(location);
        des.append("]");
        
        return des.toString();
        
    }

    @Override
    public MapElementType getType() {
        return type;
    }
    
}
