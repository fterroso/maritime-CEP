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
import java.awt.geom.Rectangle2D;
import tool.Bearing;
import tool.Point;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public enum AbnormalPortType {
    
    PORTSMOUTH (50.81152, -1.0956, 5000, new Rectangle2D.Double(-1.1401, 50.7731, 0.051, 0.0554), Bearing.S, new Point(50.7731, -1.1401), new Point(50.7731, -1.0880)),
    FAWLEY (50.8364,  -1.3238, 3000, new Rectangle2D.Double(-1.332, 50.7855, 0.126, 0.0531), Bearing.SE, new Point(50.7855, -1.332), new Point(50.7855, -1.188)),
    COWES (50.76173, -1.2951, 900, new Rectangle2D.Double(-1.3025, 50.7498, 0.0247, 0.0201), Bearing.N, new Point(50.76833, -1.3039), new Point(50.76833, 1.2781)),
    COWES_SOUTH(50.75104,  -1.29079, 2000, new Rectangle2D.Double(-1.3025, 50.7498, 0.0247, 0.0201), Bearing.N, new Point(50.76833, -1.3039), new Point(50.76833, 1.2781));
    
    double lat;
    double lon;
    
    double dist;
    
    Rectangle2D borderRectangle;
    
    Bearing abnormalExitBearing;
    
    Point head;
    Point tail;
    
    private AbnormalPortType(
            double lat, 
            double lon, 
            double dist, 
            Rectangle2D borderRectangle, 
            Bearing abnormalExitBearing, 
            Point tail, 
            Point head) {
        
        this.lat = lat;
        this.lon = lon;
        this.dist = dist;
        this.borderRectangle = borderRectangle;
        this.abnormalExitBearing = abnormalExitBearing;
        this.tail = tail;
        this.head = head;
    }
    
    public Point getPoint(){
        Point p = new Point();
        p.setLat(lat);
        p.setLon(lon);
        
        return p;
    }

    public double getDistance() {
        return dist;
    }

    public Rectangle2D getBorderRectangle() {
        return borderRectangle;
    }

    public Bearing getAbnormalExitBearing() {
        return abnormalExitBearing;
    }
    
    public TrajectoryEvent generateTrajectoryEventForBorder(){
        TrajectoryEvent t = new TrajectoryEvent();
        t.setTimestamp(System.currentTimeMillis());       
        
        Point[] locations = new Point[]{tail,head};
        
        t.setLocations(locations);
        
        return t;
       
    }
    
}
