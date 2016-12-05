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
package context.data;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public enum ContextualDataType {
    
    STATIC (null),
    DYNAMIC (null),
    DEVICE_STATIC (STATIC),
    ENVIRONMENT_STATIC (STATIC),
    DEVICE_DYNAMIC (DYNAMIC),
    ENVIRONMENT_DYNAMIC (DYNAMIC),
    SPATIAL_STATIC (ENVIRONMENT_STATIC),
    TEMPORAL_STATIC (ENVIRONMENT_STATIC),
    SPATIAL_DYNAMIC (ENVIRONMENT_DYNAMIC),
    TEMPORAL_DYNAMIC (ENVIRONMENT_DYNAMIC);
    
    private ContextualDataType father;
    
    ContextualDataType(ContextualDataType father){
        this.father = father;
    }

    public ContextualDataType getFather() {
        return father;
    }   
    
}
