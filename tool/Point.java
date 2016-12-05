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
package tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

/**
 *
 * @author Fernando Terroso-Saenz
 */
public class Point {
    
    long timestamp;
    int numSeq;
    
    double speed = 0;
        
    double x = Double.MAX_VALUE;
    double y = Double.MAX_VALUE;

    char latZone;
    int lngZone;
    
    double lat;
    double lon;
    
    public Point(){}
    
    public Point(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public double getX() {
        if(x == Double.MAX_VALUE){
            LatLng conversor = new LatLng(lat,lon);
            UTMRef utm = conversor.toUTMRef();
            x = utm.getEasting();
        }
        
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        if(y == Double.MAX_VALUE){
            LatLng conversor = new LatLng(lat,lon);
            UTMRef utm = conversor.toUTMRef();
            y = utm.getNorthing();
        }
                
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public char getLatZone() {
        return latZone;
    }

    public void setLatZone(char latZone) {
        this.latZone = latZone;
    }

    public int getLngZone() {
        return lngZone;
    }

    public void setLngZone(int lngZone) {
        this.lngZone = lngZone;
    }        

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumSeq() {
        return numSeq;
    }

    public void setNumSeq(int numSeq) {
        this.numSeq = numSeq;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.timestamp != other.timestamp) {
            return false;
        }
        if (this.numSeq != other.numSeq) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lat) != Double.doubleToLongBits(other.lat)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lon) != Double.doubleToLongBits(other.lon)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 41 * hash + this.numSeq;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.lat) ^ (Double.doubleToLongBits(this.lat) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.lon) ^ (Double.doubleToLongBits(this.lon) >>> 32));
        return hash;
    }

    
    
    @Override
    public String toString(){
        
        StringBuilder des = new StringBuilder();
        des.append("[");       
        des.append(numSeq);
        des.append(", ");
        des.append(speed);
        des.append(", ");
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        des.append(format.format(date));        
        des.append(", lat:");
        des.append(lat);
        des.append(", lon:");
        des.append(lon);
        des.append("]");
        
        return des.toString();
    }

    protected String toKMLFormat(){
        StringBuilder sb= new StringBuilder();
        sb.append("<when>");
        DateFormat format = new SimpleDateFormat(Constants.KML_DATE_FORMAT);
        sb.append(format.format(timestamp));
        sb.append("</when>\n");
        sb.append("<gx:coord>");
        sb.append(getLon());
        sb.append(" ");
        sb.append(getLat());
        sb.append(" 0");
        sb.append("</gx:coord>\n");
        sb.append("<gx:angles>0  0  0</gx:angles>\n");
        sb.append("<speed>");
        sb.append(speed);
        sb.append("</speed>\n");
        return sb.toString();
    }
    
}
