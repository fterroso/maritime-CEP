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
package seabilla.config;

import EPA.adaptor.AdaptorEPAType;
import config.ConfigInfoProvider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class SeabillaConfigInfoProvider extends ConfigInfoProvider{
    
    static String dataPath;
       
    /* Low level elements */
    static AdaptorEPAType trackFileType;
    static List<CEPRuleConfiguration> alertConfiguration;

    public static AdaptorEPAType getTrackFileType() {
        return trackFileType;
    }

    public static List<CEPRuleConfiguration> getAlertConfiguration() {
        return alertConfiguration;
    }

    public static String getDataPath() {
        return dataPath;
    }
    
    public static void readXMLConfigurationFile(String configFilePath) throws Exception{        
        SAXBuilder builder=new SAXBuilder(false);
        Document doc=builder.build(configFilePath);
        Element root =doc.getRootElement();
        
        dataPath = root.getChild("datapath").getText().trim();
        
        Element lowLevelConfigE = root.getChild("lowLevel");
        processLowLevelElement(lowLevelConfigE);
        
    }
    
    protected static void processLowLevelElement(Element lowLevelE) throws Exception{
        Element lowLevelInputE = lowLevelE.getChild("input");
        processLowLevelInputElement(lowLevelInputE);
        
        Element lowLevelAlertsE = lowLevelE.getChild("alerts");
        processLowLevelAlertsElement(lowLevelAlertsE);
    }
    
    protected static void processLowLevelInputElement(Element lowLevelInputE){
        trackFileType = AdaptorEPAType.valueOf(lowLevelInputE.getChild("trackFileType").getText().trim().toUpperCase());
    }
    
    protected static void processLowLevelAlertsElement(Element lowLevelAlertsE)throws Exception{
        
        alertConfiguration = new LinkedList<CEPRuleConfiguration>();
        
        Element ruleElement = lowLevelAlertsE.getChild("bearingChangeAlert");
        alertConfiguration.add(processCEPRuleElement("bearingChangeAlert", ruleElement));

        ruleElement = lowLevelAlertsE.getChild("speedChangeAlert");
        alertConfiguration.add(processCEPRuleElement("speedChangeAlert", ruleElement));
        
        ruleElement = lowLevelAlertsE.getChild("collisionAlert");
        alertConfiguration.add(processCEPRuleElement("collisionAlert", ruleElement));
        
        ruleElement = lowLevelAlertsE.getChild("bearingSpeedChangeAlert");
        alertConfiguration.add(processCEPRuleElement("bearingSpeedChangeAlert", ruleElement));
        
    }
    
    private static CEPRuleConfiguration processCEPRuleElement(String name, Element ruleElement) throws Exception{
        CEPRuleConfiguration ruleConfig = new CEPRuleConfiguration();
        ruleConfig.setName(name);
        
        String enableSt = ruleElement.getAttributeValue("active");
        
        boolean enable = ("YES".equals(enableSt)) ? true : false;
        ruleConfig.setEnable(enable);
        
        Element paramsE = ruleElement.getChild("params");
        
        if(paramsE != null){            
            ruleConfig.setParams(processParamsElement(paramsE));
        }
        
        return ruleConfig;
    }
    
    protected static  Map<String, Object> processParamsElement(Element paramsElement) throws Exception{
        
        Map<String, Object> ruleParams = new HashMap<String, Object>();
        List<Element> params = paramsElement.getChildren();
        
        for(Element paramE : params){
            String paramName = paramE.getName();
            String type = paramE.getAttributeValue("type");
            String valueSt = paramE.getText();

            Class c = Class.forName(type);
            if(Double.class.equals(c)){
                ruleParams.put(paramName, Double.valueOf(valueSt));
            }else if(Long.class.equals(c)){
                ruleParams.put(paramName, Long.valueOf(valueSt));
            }else if(Integer.class.equals(c)){
                ruleParams.put(paramName, Integer.valueOf(valueSt));
            }                                
        }
        
        return ruleParams;
    }
}
