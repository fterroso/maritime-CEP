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
package seabilla.tool;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public enum BoatType {
    
    SMALL_TUG_OR_PILOT ("Small Tug or Pilot", ""),
    YACHT ("Yacht", ""),
    TANKER ("Tanker", ""),
    CARGO_VASSEL ("Cargo vessel", ""),
    PASSENGER_VASSEL ("Passenger vessel", ""),
    HIGH_SPEED_CRAFT ("High speed craft", ""),    
    UNSPECIFIED ("Unspecified", "0");
    
    String kmlDescription;
    String foixmlDescription;

    private BoatType(String kmlDescription, String foixmlDescription) {
        this.kmlDescription = kmlDescription;
        this.foixmlDescription = foixmlDescription;
    }

    public String getKmlDescription() {
        return kmlDescription;
    }

    public String getFoixmlDescription() {
        return foixmlDescription;
    }
    
        
    public static BoatType getBoatTypeFromKmlDescription(String description){
        BoatType result = null;
        
        for(BoatType type : BoatType.values()){
            if(type.getKmlDescription().equals(description)){
                result = type;
                break;
            }
        }
        
        return result;
        
        
    }
        
    public static BoatType getBoatTypeFromFoiXMLDescription(String description){
        BoatType result = null;
        
        for(BoatType type : BoatType.values()){
            if(type.getFoixmlDescription().equals(description)){
                result = type;
                break;
            }
        }
        
        return result;
                
    }
   
}
