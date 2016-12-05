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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BasicContextProvider implements ContextProvider{

    Map<String,StaticDeviceContext> staticDeviceContext;
    Map<String,List<DynamicDeviceContext>> dynamicDeviceContext;
    
    StaticSpatialContext staticSpatialContext = null;
    List<DynamicSpatialContext> dynamicSpatialContext;

    StaticTemporalContext staticTemporalContext = null;
    List<DynamicTemporalContext> dynamicTemporalContext;

    public BasicContextProvider() {
        
        staticDeviceContext = new HashMap<String,StaticDeviceContext>();
        dynamicDeviceContext = new HashMap<String,List<DynamicDeviceContext>>();
        
        dynamicSpatialContext = new LinkedList<DynamicSpatialContext>();
        dynamicTemporalContext = new LinkedList<DynamicTemporalContext>();

    }
    
        
    @Override
    public void addNewStaticDeviceContext(StaticDeviceContext data) {
        staticDeviceContext.put(data.getId(), data);
    }

    @Override
    public void addNewDynamicDeviceContext(DynamicDeviceContext data) {
        
        List<DynamicDeviceContext> datas = (dynamicDeviceContext.containsKey(data.getId())) ? dynamicDeviceContext.get(data.getId()) : new LinkedList<DynamicDeviceContext>();
        
        datas.add(data);
        dynamicDeviceContext.put(data.getId(), datas);
    }

    @Override
    public void addNewStaticSpatialContext(StaticSpatialContext data) {
        staticSpatialContext = data;
    }

    @Override
    public void addNewDynamicSpatialContext(DynamicSpatialContext data) {
        dynamicSpatialContext.add(data);
    }

    @Override
    public void addNewStaticTemporalContext(StaticTemporalContext data) {
        staticTemporalContext = data;
    }

    @Override
    public void addNewDynamicTemporalContext(DynamicTemporalContext data) {
        dynamicTemporalContext.add(data);
    }

    @Override
    public StaticDeviceContext getStaticContextForDevice(String id) {
        return staticDeviceContext.get(id);
    }

    @Override
    public DynamicDeviceContext getCurrentDynamicContextForDevice(String id) {
        List<DynamicDeviceContext> datas = dynamicDeviceContext.get(id);
        
        if(datas!= null){
            return datas.get(datas.size()-1);
        }
        
        return null;
    }

    
    @Override
    public StaticDeviceContext[] getAllStaticDeviceContext(){
        
        StaticDeviceContext[] aux = new StaticDeviceContext[staticDeviceContext.values().size()];
        return staticDeviceContext.values().toArray(aux);
    }


    @Override
    public StaticSpatialContext getStaticSpatialContext() {
        return staticSpatialContext;
    }

    @Override
    public DynamicSpatialContext getCurrentDynamicSpatialContext() {
        return dynamicSpatialContext.get(dynamicSpatialContext.size()-1);
    }

    @Override
    public StaticTemporalContext getStaticTemporalContext() {
        return staticTemporalContext;
    }

    @Override
    public DynamicTemporalContext getCurrentDynamicTemporalContext() {
        return dynamicTemporalContext.get(dynamicTemporalContext.size()-1);
    }
    
    
    
}
