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
package tool.trace.visualizer;

import event.MapElement;
import event.alarm.AlarmEvent;
import event.alert.AlertEvent;
import event.location.LocationEvent;
import event.trajectory.TrajectoryChangeEvent;
import event.trajectory.TrajectoryEvent;
import java.util.List;


/**
 *
 * @author fernando
 */
public abstract class TraceVisualizer {
        
    public void registerEvent(MapElement event) {
        switch(event.getType()){
            case LOCATION:
                registerLocationEvent((LocationEvent) event);
                break;
            case TRAJECTORY:
                registerTrajectoryEvent((TrajectoryEvent) event);
                break;
            case TRAJECTORY_CHANGE:
                registerTrajectoryChangeEvent((TrajectoryChangeEvent) event);
                break;
            case BEHAVIOR_ALERT:
                registerAlertEvent((AlertEvent) event);
                break;
            case BEHAVIOR_ALARM:
                registerAlarmEvent((AlarmEvent) event);
                
        }
    }
    
    public abstract void serializeTraceInFilePath(String path, TraceOutputType type);
        
    protected abstract void registerLocationEvent(LocationEvent event);  
    
    protected abstract void registerTrajectoryChangeEvent(TrajectoryChangeEvent event);   
    
    protected abstract void registerTrajectoryEvent(TrajectoryEvent event);
    
    protected abstract void registerAlertEvent(AlertEvent event);
    
    protected abstract void registerAlarmEvent(AlarmEvent event);
   
}
