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
package seabilla.context.data.staticContext;

import context.data.staticContext.StaticDeviceContext;
import seabilla.tool.BoatType;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class BoatInfoEvent extends StaticDeviceContext{
    
    BoatType boatType;
    String name;
    String MMSI;
    String IMO;
    double length;
    String callSign;
    String flag;

    public String getMMSI() {
        return MMSI;
    }

    public void setMMSI(String MMSI) {
        this.MMSI = MMSI;
    }

    public BoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIMO() {
        return IMO;
    }

    public void setIMO(String IMO) {
        this.IMO = IMO;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    
    
    

    @Override
    public String toString(){
        StringBuilder des = new StringBuilder();
        
        des.append("[");
        des.append(this.getId());
        des.append("; ");
        des.append("Name: ");
        des.append(name);
        des.append("; ");
        des.append("MMSI: ");
        des.append(MMSI);
        des.append("; ");  
        des.append("Boat type: ");
        des.append(boatType);
        des.append("; ");
        des.append("Flag: ");
        des.append(flag); 
        des.append("; ");
        
        if(callSign != null){
            des.append("Call sign:");
            des.append(callSign);
            des.append("; ");
        }
        if(IMO != null){
            des.append("IMO: ");
            des.append(IMO);
            des.append("; ");
        }
        if(length != -1){
            des.append("Length: ");
            des.append(length);
        }
        des.append("]");
        
        return des.toString();
    }
    
}
