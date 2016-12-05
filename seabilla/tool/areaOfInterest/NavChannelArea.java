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
package seabilla.tool.areaOfInterest;

import tool.areaOfInterest.AreaOfInterestType;
import tool.areaOfInterest.AreaOfInterest;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class NavChannelArea extends AreaOfInterest{

    public NavChannelArea() {
        
        setType(AreaOfInterestType.NAV_CHANNEL);
    }

    
    
    public static NavChannelArea fromAreaOfInterest(AreaOfInterest area){
        
        NavChannelArea navChannel = new NavChannelArea();
        navChannel.setArea(area.getArea());
        navChannel.setName(area.getName());

        return navChannel;
                
    }
    
}
