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
package tool;

/**
 *
 * @author calcifer
 */
public enum Bearing {
    
    NNE (1),
    NE (2),
    ENE (3),
    E (4),
    ESE (5),
    SE (6),
    SSE (7),
    S (8),
    SSW (9),
    SW (10),
    WSW (11),
    W (12),
    WNW (13),
    NW (14),
    NNW (15),
    N  (16);
    
    
    private int bearingValue;    
    
    private Bearing(int value){
        bearingValue = value;
    }

    public int getBearingValue() {
        return bearingValue;
    }
        
    
    public static Bearing getFineGrainBearingFromValue(double value){
        
        int tmp = (int) Math.round(value / 22.5);
       
        for (Bearing b : values()) {
            if (b.getBearingValue() == tmp) {
                return b;
            }
        }
        
        return Bearing.N;

    }
    
    public static Bearing getCoarseGrainBearingFromValue(double value){
        int tmp = (int) Math.round(value / 22.5);
        
        switch(tmp){
            case 1:
            case 15:
            case 16:
                return N;
            case 2:
                return NE;
            case 3:
            case 4:
            case 5:
                return E;
            case 6:
                return SE;
            case 7:
            case 8:
            case 9:
                return S;
            case 10:
                return SW;
            case 11:
            case 12:
            case 13:
                return W;
            case 14:
                return NW;

        }
        
        return N;

    }
        
}
