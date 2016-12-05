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
package seabilla.lowlevel.CEP.event.alert;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public enum BehaviorAlertType {
    
    UNSPECIFIED (-1, "Unspecfied", "", BehaviorAlertLevel.MEDIUM),
    
    SPEED_CHANGE_ALERT (0, "Speed change alert", "", BehaviorAlertLevel.HIGH),
    BEARING_CHANGE_ALERT (1, "Bearing change alert", "detourAtSea", BehaviorAlertLevel.HIGH),
    COLLISION_ALERT (2, "Collision alert", "", BehaviorAlertLevel.HIGH),
    BEARING_SPEED_CHANGE_ALERT (3, "Speed speed change alert", "", BehaviorAlertLevel.HIGH),
    ABNORMAL_PORT_EXIT_ALERT(4, "Abnormal port exit alert", "", BehaviorAlertLevel.HIGH),
    SPEED_TOO_HIGH_ALERT(5, "Speed too high alert", "", BehaviorAlertLevel.HIGH),
    DEPART_FROM_COASTLINE_ALERT(6, "Depart from coaslint alert", "", BehaviorAlertLevel.HIGH),
    IN_RESCTRICTED_AREA_ALERT(7, "In restricted area alert", "", BehaviorAlertLevel.HIGH),
    LOITER_AT_SEA_ALERT(8, "Loiter at sea alert", "loiterAtSea", BehaviorAlertLevel.HIGH),
    HEAD_TO_COASTLINE_ALERT(9, "Head to coastline alert", "", BehaviorAlertLevel.HIGH),
    OUT_OF_NORMAL_ROUTE_ALERT(10,"Out of normal route alert", "", BehaviorAlertLevel.HIGH),
    DELAYED_RENDEZ_VOUS_ALERT (11, "Delayed rendez-vous alert", "", BehaviorAlertLevel.HIGH),
    RENDEZ_VOUS_ALERT (12, "Rendez-vous alert", "", BehaviorAlertLevel.HIGH);

    int intValue;
    String description;
    String cosValue;
    BehaviorAlertLevel level;

    private BehaviorAlertType(
            int intValue,
            String description,
            String cosValue,
            BehaviorAlertLevel level) {
        
        this.intValue = intValue;
        this.description = description;
        this.cosValue = cosValue;
        this.level = level;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getDescription() {
        return description;
    }

    public String getCosValue() {
        return cosValue;
    }

    public BehaviorAlertLevel getLevel() {
        return level;
    }

    public static BehaviorAlertType getAlertTypeFromInt(int i){
        
        BehaviorAlertType type = null;
        
        for(BehaviorAlertType alarm: BehaviorAlertType.values()){
            if(alarm.getIntValue() == i){
                type = alarm;
                break;
            }
        }
        
        return type;
    }
    
    public static BehaviorAlertType getAlertTypeFromCosValue(String cosValue){
        
        BehaviorAlertType type = UNSPECIFIED;
        
        for(BehaviorAlertType alarm: BehaviorAlertType.values()){
            if(alarm.getCosValue().equals(cosValue)){
                type = alarm;
                break;
            }
        }
        
        return type;
    }
      
    public static BehaviorAlertType getAlertTypeFromDescription(String description){
        
        BehaviorAlertType type = UNSPECIFIED;
        
        for(BehaviorAlertType alarm: BehaviorAlertType.values()){
            if(alarm.getDescription().equals(description)){
                type = alarm;
                break;
            }
        }
        
        return type;
    }
}
