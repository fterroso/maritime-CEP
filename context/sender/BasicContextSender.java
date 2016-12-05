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
package context.sender;

import com.espertech.esper.client.EPServiceProvider;
import config.ConfigInfoProvider;
import context.data.staticContext.StaticDeviceContext;
import context.data.staticContext.environment.BasicStaticSpatialContext;
import org.apache.log4j.Logger;
import seabilla.context.data.staticContext.BoatInfoEvent;
import tool.areaOfInterest.NormalRouteArea;

/**
 *  Class that feeds the CEP engine with the contextual information for the 
 * Low Level CEP.
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BasicContextSender implements ContextSender{

    static protected Logger LOG = Logger.getLogger(ContextSender.class);    
    
    EPServiceProvider cepProvider;

    public BasicContextSender(EPServiceProvider cepProvider) {
        this.cepProvider = cepProvider;
    }  
    
    @Override
    public void startSendingContextEvents() {
        sendDeviceContextEvents();
        sendNormalRouteAreaEvents();
    }
    
    protected void sendDeviceContextEvents(){
        StaticDeviceContext[] deviceContexts = (StaticDeviceContext[])ConfigInfoProvider.getContextProvider().getAllStaticDeviceContext();
        
        LOG.info("Sending static device context events...");
        for(StaticDeviceContext deviceContext : deviceContexts){            
            cepProvider.getEPRuntime().sendEvent((BoatInfoEvent)deviceContext);
        }
    }
    
    protected void sendNormalRouteAreaEvents(){
        BasicStaticSpatialContext spatialContext = (BasicStaticSpatialContext) ConfigInfoProvider.getContextProvider().getStaticSpatialContext();
    
        LOG.info("Sending normal routes as events...");
        for(NormalRouteArea route : spatialContext.getNormalRoutes()){
            cepProvider.getEPRuntime().sendEvent(route);
        }
    }    
}
