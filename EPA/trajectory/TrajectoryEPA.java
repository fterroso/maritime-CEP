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
package EPA.trajectory;

import EPA.EPA;
import EPA.trajectory.listener.NewLevelPointListener;
import EPA.trajectory.listener.Trajectory1Listener;
import EPA.trajectory.listener.TrajectoryChangeListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.apache.log4j.Logger;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class TrajectoryEPA extends EPA{
    
    static Logger LOG = Logger.getLogger(TrajectoryEPA.class);    

    private static final String TRAJECTORY_EPL = "INSERT INTO TrajectoryEvent "+
                                             "SELECT  id as id, "+   
                                             "        current_timestamp as timestamp,"+
                                             "        avgBearingFromPoints(A.location) as avgBearing, "+
                                             "        bearing(first(location), last(location)) as straightBearing, "+            
                                             "        A.level as level, "+       
                                             "        window(A.location) as locations, "+
                                             "        avg(A.location.speed) as avgSpeed, "+
                                             "        last(location.speed) as currentSpeed, "+
                                             "        last(location.numSeq) as headNumSeq, "+
                                             "        first(location.numSeq) as tailNumSeq "+
                                             "FROM FilteredLocationEvent.std:groupwin(id,level).win:expr(current_count <= EventHierarchy.pointsForLevel(level)) A "+
                                             "GROUP BY id, level "+
                                             "HAVING count(window(A.location)) >= minLocationsPerTrajectory -1 and"+    
                                             "       first(location.numSeq) != last(location.numSeq) ";
    
    
    private static final String TRAJECTORY_INC_CHANGE_EPL =      "INSERT INTO TrajectoryChangeEvent "+
                                                           "SELECT TrajectoryChangeType.INCREASING as changeType, "+
                                                           "       id,"+
                                                           "       timestamp,"+
                                                           "       level,"+
                                                           "       initialBearing,"+
                                                           "       finalBearing,"+
                                                           "       tail,"+
                                                           "       head,"+            
                                                           "       middle,"+            
                                                           "       tailNumSeq,"+            
                                                           "       headNumSeq "+                                    
                                                           "FROM TrajectoryEvent "+
                                                           "MATCH_RECOGNIZE ( "+
                                                           "    PARTITION BY id,level "+
                                                           "    MEASURES current_timestamp as timestamp, "+
                                                           "             A.id as id, "+
                                                           "             A.level as level, "+
                                                           "             A.straightBearing as initialBearing, "+
                                                           "             C.straightBearing as finalBearing, "+
                                                           "             A.tail as tail, "+
                                                           "             C.head as head, "+
                                                           "             CASE WHEN count(B.locations) > 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as middle, "+
                                                           "             A.tailNumSeq as tailNumSeq, "+
                                                           "             C.headNumSeq as headNumSeq "+
                                                           "    PATTERN (A B* C D)  "+
                                                           "    DEFINE B as isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                           "                isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                           "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingMinChange and "+
                                                           "                isIncreasingBearing(C.straightBearing, A.straightBearing), "+
                                                           "           D as not isIncreasingBearing(D.straightBearing, C.straightBearing) "+
                                                           " )";
    
    private static final String TRAJECTORY_DEC_CHANGE_EPL =      "INSERT INTO TrajectoryChangeEvent "+
                                                           "SELECT TrajectoryChangeType.DECREASING as changeType, "+
                                                           "       id,"+
                                                           "       timestamp,"+
                                                           "       level,"+            
                                                           "       initialBearing,"+
                                                           "       finalBearing,"+
                                                           "       tail,"+
                                                           "       head,"+            
                                                           "       middle,"+            
                                                           "       tailNumSeq,"+            
                                                           "       headNumSeq "+                                    
                                                           "FROM TrajectoryEvent "+
                                                           "MATCH_RECOGNIZE ( "+
                                                           "    PARTITION BY id,level "+
                                                           "    MEASURES current_timestamp as timestamp, "+
                                                           "             A.id as id, "+
                                                           "             A.level as level, "+            
                                                           "             A.straightBearing as initialBearing, "+
                                                           "             C.straightBearing as finalBearing, "+
                                                           "                 A.tail as tail, "+
                                                           "             C.head as head, "+
                                                           "             CASE WHEN count(B.locations) >= 1 THEN aggMiddlePoint(B.locations) ELSE C.locations[0] END as middle, "+
                                                           "             A.tailNumSeq as tailNumSeq, "+
                                                           "             C.headNumSeq as headNumSeq "+
                                                           "    PATTERN (A B* C D)  "+
                                                           "    DEFINE B as not isIncreasingBearing(B.straightBearing, A.straightBearing) and "+
                                                           "                not isIncreasingBearing(B.straightBearing, prev(B.straightBearing,1)), " +
                                                           "           C as bearingDifference(C.straightBearing, A.straightBearing) >= bearingMinChange and "+
                                                           "                not isIncreasingBearing(C.straightBearing, A.straightBearing), " +            
                                                           "           D as isIncreasingBearing(D.straightBearing, C.straightBearing)"+
                                                           " )";    
    
    private static final String NEW_LEVEL_POINT =   "INSERT INTO FilteredLocationEvent "+
                                                    "SELECT id as id, "+
                                                    "       current_timestamp as timestamp, "+
                                                    "       level+1 as level, "+
                                                    "       middle as location "+
                                                    "FROM TrajectoryChangeEvent";
    
    EPStatement trajectoryStatement;
    EPStatement longTrajectoryStatement;

    
    EPStatement trajectoryIncChangeStatement;
    EPStatement trajectoryDecChangeStatement;
    
    EPStatement newLevelPointStatement;
    
    @Override
    public void start(EPServiceProvider CEPProvider, TraceVisualizer visualizer) {
        CEPEngine = CEPProvider;
        
        trajectoryStatement = CEPEngine.getEPAdministrator().createEPL(TRAJECTORY_EPL);
        Trajectory1Listener traj1List = new Trajectory1Listener(visualizer);
        trajectoryStatement.addListener(traj1List); 
            
        trajectoryIncChangeStatement = CEPEngine.getEPAdministrator().createEPL(TRAJECTORY_INC_CHANGE_EPL);
        TrajectoryChangeListener trajChangeList = new TrajectoryChangeListener(visualizer);
        trajectoryIncChangeStatement.addListener(trajChangeList);

        trajectoryDecChangeStatement = CEPEngine.getEPAdministrator().createEPL(TRAJECTORY_DEC_CHANGE_EPL);
        trajectoryDecChangeStatement.addListener(trajChangeList);
        
        newLevelPointStatement = CEPEngine.getEPAdministrator().createEPL(NEW_LEVEL_POINT);
        NewLevelPointListener newPointList = new NewLevelPointListener(visualizer);
        newLevelPointStatement.addListener(newPointList);
    }
    
}
