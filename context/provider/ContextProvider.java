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
package context.provider;

import context.data.dynamicContext.DynamicDeviceContext;
import context.data.dynamicContext.environment.DynamicSpatialContext;
import context.data.dynamicContext.environment.DynamicTemporalContext;
import context.data.staticContext.StaticDeviceContext;
import context.data.staticContext.environment.StaticSpatialContext;
import context.data.staticContext.environment.StaticTemporalContext;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public interface ContextProvider {
    
    public void addNewStaticDeviceContext(StaticDeviceContext data);
    
    public void addNewDynamicDeviceContext(DynamicDeviceContext data);
    
    public void addNewStaticSpatialContext(StaticSpatialContext data);
    
    public void addNewDynamicSpatialContext(DynamicSpatialContext data);
    
    public void addNewStaticTemporalContext(StaticTemporalContext data);
    
    public void addNewDynamicTemporalContext(DynamicTemporalContext data);    
        
    public StaticDeviceContext getStaticContextForDevice(String id);
    
    public StaticDeviceContext[] getAllStaticDeviceContext();
    
    public DynamicDeviceContext getCurrentDynamicContextForDevice(String id);
    
    public StaticSpatialContext getStaticSpatialContext();

    public DynamicSpatialContext getCurrentDynamicSpatialContext();
    
    public StaticTemporalContext getStaticTemporalContext();

    public DynamicTemporalContext getCurrentDynamicTemporalContext();

}
