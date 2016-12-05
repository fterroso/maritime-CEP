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
package context.reader.staticData.environment;

import context.data.staticContext.environment.BasicStaticSpatialContext;
import context.data.staticContext.environment.StaticSpatialContext;
import java.io.File;
import java.util.List;
import tool.areaOfInterest.AreaOfInterest;
import tool.areaOfInterest.NormalRouteArea;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BasicKMLStaticSpatialContextReader extends KMLStaticSpatialContextReader{

    public BasicKMLStaticSpatialContextReader(File KMLFile) {
        super(KMLFile);
    }   
    
    @Override
    protected StaticSpatialContext getStaticSpatialContext() {
        return new BasicStaticSpatialContext();
    }

   @Override
    protected void processAreasOfInterest(List<AreaOfInterest> areas, StaticSpatialContext context) {
        
        BasicStaticSpatialContext staticSpatialContext = (BasicStaticSpatialContext) context;
                
        if(areas != null){
            for(AreaOfInterest area : areas){
                NormalRouteArea route = NormalRouteArea.fromAreaOfInterest(area);
                staticSpatialContext.addNewNormalRouteArea(route);
            }                        
        }
   }
}
