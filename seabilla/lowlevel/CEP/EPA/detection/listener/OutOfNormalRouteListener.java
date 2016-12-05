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
package seabilla.lowlevel.CEP.EPA.detection.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.apache.log4j.Logger;
import seabilla.lowlevel.CEP.event.alert.OutOfNormalRouteAlert;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class OutOfNormalRouteListener  implements UpdateListener{
    
    static Logger LOG = Logger.getLogger(OutOfNormalRouteListener.class);    
    
    TraceVisualizer visualizer;

    public OutOfNormalRouteListener(TraceVisualizer visualizer) {
        this.visualizer = visualizer;
    }    
    
    @Override
    public void update(EventBean[] ebs, EventBean[] ebs1) {
        if(ebs != null){
            for(EventBean eb : ebs){
                LOG.debug("Out of normal route alert: "+ eb.getUnderlying());
                visualizer.registerEvent((OutOfNormalRouteAlert)eb.getUnderlying());
            }
        }
    }
}
