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
package tool.supportFunction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import tool.Constants;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class EventHierarchy implements Serializable {
    
    static Logger LOG = Logger.getLogger(EventHierarchy.class);    
    
    private static Map<Integer,Integer> pointsPerLevel = new HashMap<Integer,Integer>();
    private static Map<String, Integer> levelPerId = new HashMap<String, Integer>();
    
    public static int pointsForLevel(int level){
        if(!pointsPerLevel.containsKey(level)){                        
            pointsPerLevel.put(level,Constants.INITIAL_POINTS_PER_LEVEL);
        }
        
        return pointsPerLevel.get(level);
    }
    
    public static void setPointsForLevel(int level, int numPoints){
        pointsPerLevel.put(level,numPoints);                
    }   
    
    public static int getLevelForId(String id){
        if(!levelPerId.containsKey(id)){
            levelPerId.put(id, Constants.INITIAL_CURRENT_LEVEL);
        }
                
        return levelPerId.get(id);
    }
    
    public static void incrementLevelForId(String id){
        int currentLevel = getLevelForId(id);
        
        levelPerId.put(id, ++currentLevel);
        
//        LOG.info("Increment level for "+ id+" to "+currentLevel);
    }
    
    
}
