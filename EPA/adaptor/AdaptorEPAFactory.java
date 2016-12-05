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
package EPA.adaptor;

import java.io.File;
import org.apache.log4j.Logger;
import seabilla.config.SeabillaConfigInfoProvider;

/**
 *
 * @author Fernando Terroso-Saenz
 */
public class AdaptorEPAFactory {
    
    static Logger LOG = Logger.getLogger(AdaptorEPAFactory.class);    
  
    public static AdaptorEPA getAdaptorEPA(AdaptorEPAType type){
        
        AdaptorEPA targetEPA = null;
                     
        String path =  SeabillaConfigInfoProvider.getDataPath();
               
        boolean modify = false;
        switch (type){                           
            case GPX:
                targetEPA = new GPXAdaptorEPA(new File(path + File.separator + "gpx"),modify);
                break;
            case KML:
                targetEPA = new KMLAdaptorEPA(new File(path + File.separator + "kml"));
                break;
        }
        
        return targetEPA;
    }    
}
