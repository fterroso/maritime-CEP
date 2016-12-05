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
package tool.supportFunction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import tool.Constants;
import tool.Point;

/**
 *
 * @author Fernando Terroso-Saenz.
 */
public class LocationFunction {
    
    static Logger LOG = Logger.getLogger(LocationFunction.class);    
    
    public static double euclideanDist(Point p1, Point p2){
        
        double a = Math.pow(p1.getX()-p2.getX(), 2);
        double b = Math.pow(p1.getY()-p2.getY(), 2);
                
        return Math.sqrt(a+b);
    }        
    
    public static double haversineDistance(Point p1, Point p2) {
        float lat1 = (float) p1.getLat();
        float lat2 = (float) p2.getLat();
        float lng1 = (float) p1.getLon();
        float lng2 = (float) p2.getLon();
        
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng/2) * Math.sin(dLng/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist * 1000;
    
    }
    
    public static double bearing(Point p1, Point p2){
        double bearing = 0;
        if(p1 != null && p2 != null){        
            double p1LatRad = Math.toRadians(p1.getLat());
            double p2LatRad = Math.toRadians(p2.getLat());
        
            double p1LonRad = Math.toRadians(p1.getLon());
            double p2LonRad = Math.toRadians(p2.getLon());
        
            bearing = (Math.toDegrees((Math.atan2(Math.sin(p2LonRad - p1LonRad) * Math.cos(p2LatRad), Math.cos(p1LatRad) * Math.sin(p2LatRad) - Math.sin(p1LatRad) * Math.cos(p2LatRad) * Math.cos(p2LonRad - p1LonRad)))) + 360) % 360;
        }
        
        return bearing;
        
    }   
    
    public static double bearingDifference(Double b1, Double b2){
        
        if(b1 != null && b2 != null){
            double diff = Math.abs(b1 - b2);        
            return Math.min(diff, Constants.TOTAL_DEGREES-diff);
        }
        
        return -1;
                
    }
    
    public static boolean isIncreasingBearing(double newBearing, double oldBearing){
        
        boolean result = false;
        
        double dist1 = Math.abs(newBearing - oldBearing);
        double dist2 = Constants.TOTAL_DEGREES - dist1;
        
        if(newBearing > oldBearing){
            if(dist1 < dist2) result = true;
        }else{
            if(dist1 > dist2) result = true;
        }
        
        return result;
    }
    
    public static Point findMiddlePoint(Point[] head, Point[] tail){
       
        List<Point> list = new LinkedList<Point>();
        
        list.addAll(Arrays.asList(tail));    
        
        for(Point p : head){
            if(!list.contains(p)){
                list.add(p);
            }
        }
        
        return list.get(list.size()/2);
        
    }    
    
}
