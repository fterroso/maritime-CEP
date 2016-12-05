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
package CEP;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import event.trajectory.EndTrajectoryEvent;
import event.location.FilteredLocationEvent;
import event.location.RawLocationEvent;
import event.trajectory.LongTrajectoryEvent;
import event.trajectory.TrajectoryChangeEvent;
import event.trajectory.TrajectoryEvent;
import tool.aggretationFunction.AggregateBearingFromPointsFunctionFactory;
import tool.aggretationFunction.AggregateMiddlePointFunctionFactory;
import tool.supportFunction.LocationFunction;
import tool.Constants;

/**
 *
 * @author fernando
 */
public class BasicCEPConfigurator implements CEPConfigurator{

    @Override
    public EPServiceProvider configureCEPEngine() {
        
        Configuration configuration = new Configuration();
        configuration.getEngineDefaults().getExecution().setPrioritized(true);
        configuration.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

        registerEvents(configuration);
        registerVariables(configuration);
        registerAggregationFunctions(configuration);
        registerSingleRowFunctions(configuration);
        
        configuration.getEngineDefaults().getExpression().setUdfCache(false);

        EPServiceProvider epServiceProveedor = EPServiceProviderManager.getProvider("CEPTraj", configuration);
        epServiceProveedor.initialize();
        
        return epServiceProveedor;
    }
    
    protected void registerEvents(Configuration configuration){
        
        /* Trajectory event */
        configuration.addImport("event.trajectory.*");
        configuration.addImport("tool.supportFunction.*");

        configuration.addEventType(FilteredLocationEvent.class);             
        configuration.addEventType(RawLocationEvent.class);
        
        configuration.addEventType(TrajectoryEvent.class);
        configuration.addEventType(LongTrajectoryEvent.class);
        
        configuration.addEventType(TrajectoryChangeEvent.class);                

        configuration.addEventType(EndTrajectoryEvent.class);     

    }
    
    protected void registerVariables(Configuration configuration){
        configuration.addVariable("minDist", Double.class, 30); // meters
        configuration.addVariable("bearingMinChange", Double.class, 45); // radians      

        configuration.addVariable("minLocationsPerTrajectory", Integer.class, Constants.INITIAL_POINTS_PER_LEVEL);
        configuration.addVariable("minLocationsPerLongTrajectory", Integer.class, Constants.INITIAL_POINTS_PER_LEVEL*2);
    }
    
    protected void registerAggregationFunctions(Configuration configuration){
        configuration.addPlugInAggregationFunctionFactory("avgBearingFromPoints", AggregateBearingFromPointsFunctionFactory.class.getName());
        configuration.addPlugInAggregationFunctionFactory("aggMiddlePoint", AggregateMiddlePointFunctionFactory.class.getName());
    }
    
    protected void registerSingleRowFunctions(Configuration configuration){
        configuration.addPlugInSingleRowFunction("bearing", LocationFunction.class.getName(), "bearing");
        configuration.addPlugInSingleRowFunction("euclideanDist", LocationFunction.class.getName(), "euclideanDist");
        configuration.addPlugInSingleRowFunction("haversineDistance", LocationFunction.class.getName(), "haversineDistance");
        configuration.addPlugInSingleRowFunction("isIncreasingBearing", LocationFunction.class.getName(), "isIncreasingBearing");
        configuration.addPlugInSingleRowFunction("bearingDifference", LocationFunction.class.getName(), "bearingDifference");    
    }
    
}
