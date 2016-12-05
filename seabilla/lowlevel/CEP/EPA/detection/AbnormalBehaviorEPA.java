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
package seabilla.lowlevel.CEP.EPA.detection;

import EPA.detection.DetectionEPA;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import seabilla.lowlevel.CEP.EPA.detection.listener.*;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class AbnormalBehaviorEPA extends DetectionEPA{

    private static String SPEED_CHANGE_ALERT_EPL =    "INSERT INTO SpeedChangeAlert "+
                                                       "SELECT "+
                                                       "       id,"+
                                                       "       timestamp, "+
                                                       "       location, "+
                                                       "       avgSpeed, "+                                                       
                                                       "       currentSpeed, "+
                                                       "       closeToPort, "+            
                                                       "       initialLocation, "+                     
                                                       "       finalLocation "+
                                                       "FROM TrajectoryEvent(level=1).win:time(speedChangeWinTime msec) "+
                                                       "MATCH_RECOGNIZE ( "+
                                                       "    PARTITION BY id "+
                                                       "    MEASURES current_timestamp as timestamp, "+
                                                       "             C.id as id, "+
                                                       "             C.head as location, "+
                                                       "             A.avgSpeed as avgSpeed, "+
                                                       "             C.currentSpeed as currentSpeed, "+
                                                       "             isCloseToPorts(C.head) as closeToPort, "+
                                                       "             A.tail as initialLocation, "+
                                                       "             C.head as finalLocation "+
                                                       "    PATTERN (A B?? C)  "+
                                                       "    DEFINE "+
                                                       "           A as A.avgSpeed > 0, "+
                                                       "           B as B.currentSpeed > A.avgSpeed, "+
                                                       "           C as C.currentSpeed > 0 and "+
                                                       "                C.currentSpeed >= (A.avgSpeed * speedMinChange) "+
                                                       " )"; 
    
    private static String BEARING_CHANGE_ALERT_1_EPL =      "INSERT INTO BearingChangeAlert "+
                                                           "SELECT "+
                                                           "       id,"+
                                                           "       timestamp,"+
                                                           "       location, "+
                                                           "       closeToPort, "+                        
                                                           "       initialLocation, "+                     
                                                           "       finalLocation, "+                                                           
                                                           "       initialBearing,"+
                                                           "       finalBearing "+
                                                           "FROM TrajectoryEvent(level=1).win:time(bearingChangeWinTime msec) "+
                                                           "MATCH_RECOGNIZE ( "+
                                                           "    PARTITION BY id "+
                                                           "    MEASURES current_timestamp as timestamp, "+
                                                           "             A.id as id, "+
                                                           "             A.tail as initialLocation, "+
                                                           "             C.head as finalLocation, "+
                                                           "             isCloseToPorts(C.head) as closeToPort, "+            
                                                           "             A.straightBearing as initialBearing, "+
                                                           "             C.straightBearing as finalBearing, "+
                                                           "             CASE WHEN count(B.locations) >= 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as location "+
                                                           "    PATTERN (A B*? C)  "+
                                                           "    DEFINE "+
                                                           "           B as not isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                           "                not isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                           "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingAlertChange and "+
                                                           "                not isIncreasingBearing(C.straightBearing, A.straightBearing) " +            
                                                           " )";    

    private static String BEARING_CHANGE_ALERT_2_EPL =     "INSERT INTO BearingChangeAlert "+
                                                           "SELECT "+
                                                           "       id,"+
                                                           "       timestamp,"+
                                                           "       location, "+
                                                           "       closeToPort, "+  
                                                           "       initialLocation, "+                     
                                                           "       finalLocation, "+             
                                                           "       initialBearing,"+
                                                           "       finalBearing "+
                                                           "FROM TrajectoryEvent(level=1).win:time(bearingChangeWinTime msec)  "+
                                                           "MATCH_RECOGNIZE ( "+
                                                           "    PARTITION BY id "+
                                                           "    MEASURES current_timestamp as timestamp, "+
                                                           "             A.id as id, "+
                                                           "             A.tail as initialLocation, "+
                                                           "             C.head as finalLocation, "+
                                                           "             isCloseToPorts(C.head) as closeToPort, "+                        
                                                           "             A.straightBearing as initialBearing, "+
                                                           "             C.straightBearing as finalBearing, "+
                                                           "             CASE WHEN count(B.locations) >= 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as location "+
                                                           "    PATTERN (A B*? C)  "+
                                                           "    DEFINE " +
                                                           "           B as isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                           "                isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                           "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingAlertChange and "+
                                                           "                isIncreasingBearing(C.straightBearing, A.straightBearing) " +            
                                                           " )"; 
    
    private static String BEARING_SPEED_CHANGE_ALERT_1_EPL =   "INSERT INTO BearingSpeedChangeAlert "+
                                                               "SELECT "+
                                                               "       id,"+
                                                               "       timestamp,"+
                                                               "       location, "+
                                                               "       closeToPort, "+                                                
                                                               "       initialLocation, "+                     
                                                               "       finalLocation, "+       
                                                               "       avgSpeed, "+
                                                               "       currentSpeed, "+
                                                               "       initialBearing,"+
                                                               "       finalBearing "+
                                                               "FROM TrajectoryEvent(level=1).win:time(bearingChangeWinTime msec) "+
                                                               "MATCH_RECOGNIZE ( "+
                                                               "    PARTITION BY id "+
                                                               "    MEASURES current_timestamp as timestamp, "+
                                                               "             A.id as id, "+
                                                               "             isCloseToPorts(C.head) as closeToPort, "+            
                                                               "             A.tail as initialLocation, "+
                                                               "             C.head as finalLocation, "+  
                                                               "             A.avgSpeed as avgSpeed, "+
                                                               "             C.currentSpeed as currentSpeed, "+
                                                               "             A.straightBearing as initialBearing, "+
                                                               "             C.straightBearing as finalBearing, "+
                                                               "             CASE WHEN count(B.locations) >= 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as location "+
                                                               "    PATTERN (A B*? C)  "+
                                                               "    DEFINE "+
                                                               "           A as A.avgSpeed > 0, "+
                                                               "           B as not isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                               "                not isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                               "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingAlertChange and "+
                                                               "                not isIncreasingBearing(C.straightBearing, A.straightBearing) and " +            
                                                               "                C.currentSpeed >= (A.avgSpeed * speedMinChange) "+
                                                               " )";    

    private static String BEARING_SPEED_CHANGE_ALERT_2_EPL =   "INSERT INTO BearingSpeedChangeAlert "+
                                                               "SELECT "+
                                                               "       id,"+
                                                               "       timestamp,"+
                                                               "       location, "+
                                                               "       closeToPort, "+                                               
                                                               "       initialLocation, "+                     
                                                               "       finalLocation, "+      
                                                               "       avgSpeed, "+
                                                               "       currentSpeed, "+
                                                               "       initialBearing,"+
                                                               "       finalBearing "+
                                                               "FROM TrajectoryEvent(level=1).win:time(bearingChangeWinTime msec) "+
                                                               "MATCH_RECOGNIZE ( "+
                                                               "    PARTITION BY id "+
                                                               "    MEASURES current_timestamp as timestamp, "+
                                                               "             A.id as id, "+
                                                               "             isCloseToPorts(C.head) as closeToPort, "+                        
                                                               "             A.tail as initialLocation, "+
                                                               "             C.head as finalLocation, "+    
                                                               "             A.avgSpeed as avgSpeed, "+
                                                               "             C.currentSpeed as currentSpeed, "+
                                                               "             A.straightBearing as initialBearing, "+
                                                               "             C.straightBearing as finalBearing, "+
                                                               "             CASE WHEN count(B.locations) >= 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as location "+
                                                               "    PATTERN (A B*? C)  "+
                                                               "    DEFINE "+
                                                                "           A as A.avgSpeed > 0, "+
                                                               "           B as isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                               "                isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                               "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingAlertChange and "+
                                                               "                isIncreasingBearing(C.straightBearing, A.straightBearing) and " +            
                                                               "                C.currentSpeed >= (A.avgSpeed * speedMinChange) "+
                                                               " )";    

    private static String COLLISION_ALERT_EPL =   "INSERT INTO CollisionAlert "+
                                                    "SELECT "+
                                                    "   A.id || \"_\" || B.id as id, "+
                                                    "   B.id as otherId, "+
                                                    "   A.head as location, "+
                                                    "   B.head as otherLocation, "+
                                                    "   current_timestamp as timestamp, "+
                                                    "   calculateCollisionPoint(A, B) as locationOfCollision, "+
                                                    "   (isCloseToPorts(A.head) OR isCloseToPorts(B.head)) as closeToPort "+
                                                    "FROM "+
                                                    "   TrajectoryEvent(level = 1) A unidirectional, "+
                                                    "   TrajectoryEvent(level = 1).std:unique(id) B "+
                                                    "WHERE "+
                                                    "   A.id != B.id and "+
                                                    "   (A.currentSpeed > 0 or B.currentSpeed > 0) and "+
                                                    "   euclideanDist(A.head, B.head) <= collisionMaxDist and "+
                                                    "   Math.abs(A.head.timestamp - B.head.timestamp) <= collisionMaxTime and "+
                                                    "   crash(A, B) ";                                                      
        
    
    private static String OUT_OF_NORMAL_ROUTE_EPL = "INSERT INTO OutOfNormalRouteAlert "+
                                                    "SELECT "+
                                                    "       A.id as id, "+
                                                    "       current_timestamp as timestamp,"+
                                                    "       A.head as location, "+
                                                    "       B as area "+
                                                    "FROM "+
                                                    "       TrajectoryEvent A unidirectional, "+
                                                    "       NormalRouteArea.std:unique(name) B "+
                                                    "WHERE "+
                                                    "       A.id = B.name AND "+
                                                    "       not B.area.contains(A.head)";
    
    EPStatement speedChangeAlertSt;
    
    EPStatement bearingChangeAlertSt1;
    EPStatement bearingChangeAlertSt2;
    
    EPStatement bearingSpeedChangeAlertSt1;
    EPStatement bearingSpeedChangeAlertSt2;    
    
    EPStatement collisionAlertSt;
       
    EPStatement outOfNormalRouteSt;
    
    @Override
    public void start(EPServiceProvider CEPProvider, TraceVisualizer visualizer) {
        
        CEPEngine = CEPProvider;
        
        speedChangeAlertSt = CEPEngine.getEPAdministrator().createEPL(SPEED_CHANGE_ALERT_EPL);
        SpeedChangeListener speedChList = new SpeedChangeListener(visualizer);
        speedChangeAlertSt.addListener(speedChList);
        
        bearingChangeAlertSt1 = CEPEngine.getEPAdministrator().createEPL(BEARING_CHANGE_ALERT_1_EPL);
        BearingChangeListener bearingChList = new BearingChangeListener(visualizer);
        bearingChangeAlertSt1.addListener(bearingChList);
        
        bearingChangeAlertSt2 = CEPEngine.getEPAdministrator().createEPL(BEARING_CHANGE_ALERT_2_EPL);
        bearingChangeAlertSt2.addListener(bearingChList); 
        
        bearingSpeedChangeAlertSt1 = CEPEngine.getEPAdministrator().createEPL(BEARING_SPEED_CHANGE_ALERT_1_EPL);
        BearingSpeedChangeListener bearingSpeedChList = new BearingSpeedChangeListener(visualizer);
        bearingSpeedChangeAlertSt1.addListener(bearingSpeedChList);
        
        bearingSpeedChangeAlertSt2 = CEPEngine.getEPAdministrator().createEPL(BEARING_SPEED_CHANGE_ALERT_2_EPL);       
        bearingSpeedChangeAlertSt2.addListener(bearingSpeedChList);
        
        collisionAlertSt = CEPEngine.getEPAdministrator().createEPL(COLLISION_ALERT_EPL); 
        CollisionListener collList = new CollisionListener(visualizer);
        collisionAlertSt.addListener(collList);
        
        outOfNormalRouteSt = CEPEngine.getEPAdministrator().createEPL(OUT_OF_NORMAL_ROUTE_EPL); 
        OutOfNormalRouteListener outOfRouteList = new OutOfNormalRouteListener(visualizer);
        outOfNormalRouteSt.addListener(outOfRouteList);
    }
    
}
