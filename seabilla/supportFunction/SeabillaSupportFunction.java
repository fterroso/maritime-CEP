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
package seabilla.supportFunction;

import event.trajectory.TrajectoryEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import seabilla.tool.SeabillaConstants;
import tool.Bearing;
import tool.Point;
import tool.supportFunction.LocationFunction;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class SeabillaSupportFunction {
    
    static Logger LOG = Logger.getLogger(SeabillaSupportFunction.class);    
    
    private static Map<String, String> abnormalPorts = new HashMap<String, String>();            
   
    public static boolean isCloseToPorts(Point p){
        
        boolean result = false;
        for(AbnormalPortType port : AbnormalPortType.values()){
            
            if(LocationFunction.haversineDistance(p, port.getPoint()) <= port.getDistance()){
                result = true;
                break;
            }
        }
        
        return result;
    }
   

    
    public static boolean crash(TrajectoryEvent t1, TrajectoryEvent t2){
        
        boolean result = false;
        
        Point collisionPoint = calculateCollisionPoint(t1,t2);
        
        if(collisionPoint != null){
            double bearing1 = LocationFunction.bearing(t1.getHead(), collisionPoint);
            Bearing b1 = Bearing.getFineGrainBearingFromValue(bearing1);
                        
            Point aux = t1.getLocations()[t1.getLocations().length-2];            
            double bearingAux = LocationFunction.bearing(aux, t1.getHead());
            Bearing b2 = Bearing.getFineGrainBearingFromValue(bearingAux);
                        
            if(b1.equals(b2)){
                
                double bearing2 = LocationFunction.bearing(t2.getHead(), collisionPoint);
                b1 = Bearing.getFineGrainBearingFromValue(bearing2);
                
                aux = t2.getLocations()[t2.getLocations().length-2];                 
                bearingAux = LocationFunction.bearing(aux, t2.getHead());
                b2 = Bearing.getFineGrainBearingFromValue(bearingAux);
                
                if(b1.equals(b2)){
                    result = true;
                }
            }
        }
        
        return result;
    }
    
    public static Point calculateCollisionPoint(TrajectoryEvent t1, TrajectoryEvent t2){
    
        Point result = null;
        
        Point[] locations = t1.getLocations();
        Point lastP = locations[locations.length-1];
        Point penultimate = locations[locations.length-2];       
        
        double[] a1 = new double[]{penultimate.getLat(), penultimate.getLon()};
        double[] a2 = new double[]{lastP.getLat(), lastP.getLon()};

        locations = t2.getLocations();
        lastP = locations[locations.length-1];
        penultimate = locations[locations.length-2];
        
        double[] b1 = new double[]{penultimate.getLat(), penultimate.getLon()};
        double[] b2 = new double[]{lastP.getLat(), lastP.getLon()};
        
        double[] vector1 = new double[]{a1[0]-a2[0], a1[1]-a2[1]};
        double[] vector2 = new double[]{b1[0]-b2[0], b1[1]-b2[1]};
        
        double m1 = vector1[1] / vector1[0];
        double m2 = vector2[1] / vector2[0];
        
        double xIntersec = ((m1*a1[0]) - (m2*b1[0]) + b1[1] - a1[1]) / (m1 -m2);        
        double yIntersec = (m2 *(xIntersec - b1[0])) + b1[1];
        
        if(!Double.isInfinite(xIntersec) && !Double.isInfinite(xIntersec)){
        
            result = new Point();
            result.setLat(xIntersec);
            result.setLon(yIntersec);
        }
              
        return result;
    }
    
    public static boolean isAbnormalPortExit(TrajectoryEvent t){
        
        boolean result = false;    
        String portName = null;
        
        if(!abnormalPorts.containsKey(t.getId())){
            
            Point p1 = t.getTail();
            Point p2 = t.getHead();
            
            AbnormalPortType startPort = null;
            
            for(AbnormalPortType port : AbnormalPortType.values()){
                if(port.getBorderRectangle().contains(p1.getLon(), p1.getLat())){
                    startPort = port;
                    if(!port.getBorderRectangle().contains(p2.getLon(), p2.getLat())){
                                     
                        if(port.getAbnormalExitBearing().equals(Bearing.getFineGrainBearingFromValue(t.getStraightBearing()))){

                            portName = port.toString().toLowerCase();
                            result = true;
                            break;
                        }

                        portName = "";
                    
                    }
                }
            }
            
            if(startPort == null){
                portName = "";
            }
            
            if(portName != null){
                abnormalPorts.put(t.getId(), portName);
            }
                        
        }  
                
        return result;
        
    }
    
    public static String abnormalPortExitName(TrajectoryEvent t){
        
        return (abnormalPorts.containsKey(t.getId())) ? abnormalPorts.get(t.getId()) : "";

    }
    
    public static String getFirstID(String ids){
        return ids.split(SeabillaConstants.BOAT_ID_SEPARATOR)[0];
    }  
    
}
