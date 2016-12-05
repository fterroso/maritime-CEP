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
package seabilla.lowlevel.CEP;

import CEP.BasicCEPConfigurator;
import com.espertech.esper.client.Configuration;
import java.util.List;
import java.util.Map;
import seabilla.config.CEPRuleConfiguration;
import seabilla.config.SeabillaConfigInfoProvider;
import seabilla.lowlevel.CEP.event.alert.*;
import seabilla.supportFunction.SeabillaSupportFunction;
import tool.areaOfInterest.NormalRouteArea;

/**
 *  Class to define in the CEP engine the variables and events that are going
 * to be used during the runtime.
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class SeabillaLowLevelCEPConfigurator extends BasicCEPConfigurator{
    @Override
    protected void registerEvents(Configuration configuration){
        super.registerEvents(configuration);
                
        /* Abnormal behavior events */
        configuration.addEventType(SpeedChangeAlert.class);  
        configuration.addEventType(BearingChangeAlert.class);    
        configuration.addEventType(BearingSpeedChangeAlert.class);
        configuration.addEventType(CollisionAlert.class);
        configuration.addEventType(OutOfNormalRouteAlert.class);

        /* Area of interest */
        configuration.addEventType(NormalRouteArea.class);
    }

    
    @Override
    protected void registerVariables(Configuration configuration){
        super.registerVariables(configuration);            
        
        /* Abnormal behavior detection variables */
        List<CEPRuleConfiguration> variablesConfig = SeabillaConfigInfoProvider.getAlertConfiguration();
        registerRulesParameters(variablesConfig, configuration);
             
    }

    protected void registerRulesParameters(
            List<CEPRuleConfiguration> variablesConfig,
            Configuration configuration){
        
        for(CEPRuleConfiguration rc : variablesConfig){
            if(rc.isEnable() && rc.getParams() != null){
                Map<String, Object> params = rc.getParams();
                for(String paramName : params.keySet()){
                    Object paramValue = params.get(paramName);
                    configuration.addVariable(paramName, paramValue.getClass(), paramValue);
                }
            }
        }
    }
    
    @Override
    protected void registerSingleRowFunctions(Configuration configuration){
        
        super.registerSingleRowFunctions(configuration);
        
        configuration.addPlugInSingleRowFunction("isCloseToPorts", SeabillaSupportFunction.class.getName(), "isCloseToPorts");        
        configuration.addPlugInSingleRowFunction("crash", SeabillaSupportFunction.class.getName(), "crash");        
        configuration.addPlugInSingleRowFunction("calculateCollisionPoint", SeabillaSupportFunction.class.getName(), "calculateCollisionPoint");        
        configuration.addPlugInSingleRowFunction("isAbnormalPortExit", SeabillaSupportFunction.class.getName(), "isAbnormalPortExit");        
        configuration.addPlugInSingleRowFunction("abnormalPortExitName", SeabillaSupportFunction.class.getName(), "abnormalPortExitName");        
    }
    
}
