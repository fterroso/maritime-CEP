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
package EPA.adaptor.deliver;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;
import event.MapElement;
import event.trajectory.EndTrajectoryEvent;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import tool.Constants;
import tool.trace.visualizer.TraceVisualizer;

/**
 * Class that sends all the MapElement events (alerts, alarms and locations) to 
 * the CEP engine.
 *
 * @author fernando
 */
public class EventDeliverer implements Runnable{

    static Logger LOG = Logger.getLogger(EventDeliverer.class);    
    
    private List<MapElement> locations;
    EPServiceProvider CEPProvider;
    TraceVisualizer visualizer;

    public EventDeliverer(
            List<MapElement> positions, 
            EPServiceProvider CEPProvider,
            TraceVisualizer visualizer){

        this.locations = positions;
        this.CEPProvider = CEPProvider;
        this.visualizer = visualizer;

    }

    @Override
    public void run() {
        try{
            
            Collections.sort(locations);
            
            long startTime = locations.get(0).getTimestamp();
            CEPProvider.getEPRuntime().sendEvent(new CurrentTimeEvent(startTime));
                        
            long prevTimestamp = 0;
            for(MapElement e : locations){
                try{
                    LOG.debug("Sending event "+ e);
                    if(e.getTimestamp() != prevTimestamp){
                        CEPProvider.getEPRuntime().sendEvent(new CurrentTimeEvent(e.getTimestamp()));
                    }
                    
                    CEPProvider.getEPRuntime().sendEvent(e);                    
                    visualizer.registerEvent(e);
                    prevTimestamp = e.getTimestamp();
                }catch(Exception err){
                    LOG.error("Error while sending event " +e, err);
                }
            }

            long lastTimestamp = locations.get(locations.size()-1).getTimestamp()+1;
            CEPProvider.getEPRuntime().sendEvent(new CurrentTimeEvent(lastTimestamp));

            EndTrajectoryEvent endItinerary = new EndTrajectoryEvent();
            endItinerary.setTimestamp(lastTimestamp);
            endItinerary.setLevel(1);
            endItinerary.setId(locations.get(0).getId());

            CEPProvider.getEPRuntime().sendEvent(endItinerary);

            CEPProvider.getEPRuntime().sendEvent(new CurrentTimeSpanEvent(lastTimestamp + 10*60*1000, 100));
         
            TimeUnit.MILLISECONDS.sleep(Constants.WAITING_TIME_TO_FINISH);
        }catch(Exception e){
            LOG.error("Something wrong happend in sender thread ", e);
        }
    }        
        
}
