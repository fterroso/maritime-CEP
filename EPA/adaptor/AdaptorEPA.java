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
package EPA.adaptor;

import EPA.EPA;
import EPA.adaptor.deliver.EventDeliverer;
import com.espertech.esper.client.EPServiceProvider;
import event.MapElement;
import event.location.RawLocationEvent;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import seabilla.config.SeabillaConfigInfoProvider;
import tool.Point;
import tool.trace.visualizer.TraceOutputType;
import tool.trace.visualizer.TraceVisualizer;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public abstract class AdaptorEPA extends EPA{
    
    static protected Logger LOG = Logger.getLogger(AdaptorEPA.class);    
 
    @Override
    public void start(EPServiceProvider CEPProvider, TraceVisualizer visualizer) {
        try{
            
            List<MapElement> incomingEvents = generateTargetEvents();         
                        
            EventDeliverer deliverer = new EventDeliverer(
                    incomingEvents, 
                    CEPProvider,
                    visualizer);
            
            Thread t = new Thread(deliverer);
            t.start();     
            
            t.join();
                        
            visualizer.serializeTraceInFilePath(SeabillaConfigInfoProvider.getDataPath() + "/output", TraceOutputType.KML);            

        }catch(Exception e){
            LOG.error("Error in AdaptorEPA ", e);
        }
    }
    
    public abstract List<MapElement> generateTargetEvents();
            
    protected RawLocationEvent makeUpLocationEvent(
            String id, 
            double lat, 
            double lon, 
            Date d, 
            int counter){
        
        LatLng latLn = new LatLng(lat, lon);
        UTMRef utm = latLn.toUTMRef();                

        RawLocationEvent event = new RawLocationEvent();
        Point p = new Point();
        //UTM coords
        p.setY(utm.getNorthing());
        p.setX(utm.getEasting());
        p.setLatZone(utm.getLatZone());
        p.setLngZone(utm.getLngZone());
        //lat long coords
        p.setLat(lat);
        p.setLon(lon);
        
        p.setTimestamp(d.getTime());
        p.setNumSeq(counter);

        event.setId(id);
        event.setLocation(p);
        event.setTimestamp(d.getTime());
        event.setLevel(0);
                   
        return event;
    }

}
