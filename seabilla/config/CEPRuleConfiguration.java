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

import java.util.Map;

/**
 * Class that represents the particular configuration of a CEP rule (pattern) 
 * in terms of parameters and whether it is enabled or not in the CEP engine. *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class CEPRuleConfiguration {
    
    String name;
    boolean enable;
    Map<String, Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
 
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(name);
        sb.append(",");
        sb.append(enable);
        sb.append(",");
        if(params != null){
            for(String paramName : params.keySet()){
                sb.append("{");
                sb.append(paramName);
                sb.append(",");
                sb.append(params.get(paramName));
                sb.append("}");
            }
        }
        sb.append("]");
        
        return sb.toString();
    }
}
