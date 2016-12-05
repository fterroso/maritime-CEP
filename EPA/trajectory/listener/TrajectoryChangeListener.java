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
package EPA.trajectory.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import event.trajectory.TrajectoryChangeEvent;
import org.apache.log4j.Logger;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author fernando
 */
public class TrajectoryChangeListener implements UpdateListener{

    static Logger LOG = Logger.getLogger(TrajectoryChangeListener.class);    
    TraceVisualizer visualizer;

    public TrajectoryChangeListener(TraceVisualizer visualizer) {
        this.visualizer = visualizer;
    }        
    
    @Override
    public void update(EventBean[] ebs, EventBean[] ebs1) {
        LOG.debug("Trajectory change event: "+ ebs[0].getUnderlying());
        visualizer.registerEvent((TrajectoryChangeEvent)ebs[0].getUnderlying());
    }
}
