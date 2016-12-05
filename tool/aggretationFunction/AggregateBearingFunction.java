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
package tool.aggretationFunction;

import com.espertech.esper.epl.agg.AggregationMethod;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import tool.Constants;

/**
 *
 * @author fernando
 */
public class AggregateBearingFunction  implements AggregationMethod {

    List<Double> result = new LinkedList<Double>();
    
    @Override
    public void enter(Object o) {
        if(o != null){
            Double d = (Double) o;
            
            result.add(d);           
        }
    }

    @Override
    public void leave(Object o) {
        if(o != null){
            Double d = (Double) o;
            result.remove(d);
        }
    }

    @Override
    public Object getValue() {
       
        Collections.sort(result,new Comparator<Double>() {
            @Override
            public int compare(Double d1, Double d2) {         
                return  d1.compareTo(d2);
            }
        });
                
        double aux = 0;
        
        double lowerAngle = result.get(0);
        double higherAngle = result.get(result.size()-1);
        
        BearingBorder border = new BearingBorder(lowerAngle, higherAngle, higherAngle-lowerAngle);
        
        if(result.size() >= 2){
            if((lowerAngle >=0 && higherAngle <= Constants.TOTAL_DEGREES/2) ||
                    (lowerAngle >= Constants.TOTAL_DEGREES/2 && higherAngle <= Constants.TOTAL_DEGREES)){
            aux = getAvgBearing(border); 
            
            }else{
                BearingBorder border360 = getBordersFrom360Angle();
                BearingBorder border180 = getBordersFrom180Angle();

                if(border360.getDifference() >= border180.getDifference()){
                    aux = getAvgBearing(border360);                        
                }else{
                    aux = getSpecialAvgBearing(border180);
                }
            }
        }else if(result.size() == 1){            
            aux = result.get(0);
        }         
        
                                       
        return aux;
    }
    
    protected BearingBorder getBordersFrom360Angle(){
        double first = result.get(0);
        double last = result.get(result.size()-1);
        
        double diff = (Constants.TOTAL_DEGREES - last) + first;        
        
        return new BearingBorder(first, last, diff);
       
    }
    
    protected BearingBorder getBordersFrom180Angle(){
        double min = -1;
        double max = -1;
        
        double halfTotalDegrees = Constants.TOTAL_DEGREES/2;
        
        for(double d : result){
            if(d < halfTotalDegrees){
                min = d;
            }else if(d > halfTotalDegrees){
                max = d;
                break;
            }
        }
        
        return new BearingBorder(max, min, (max-(halfTotalDegrees)) + (halfTotalDegrees-min));     
        
        
    }
    
    protected double getAvgBearing(BearingBorder border){
        
        double avgBearing = 0;
                
        for(double d : result){
            avgBearing += (d-border.getStartAngle());
        }
        
        return (avgBearing / result.size()) + border.getStartAngle();

    }
    
    protected double getSpecialAvgBearing(BearingBorder border){
        
        double avgBearing = 0;
                
        for(double d : result){
            if(d <= border.getEndAngle()){
                avgBearing += (d + (Constants.TOTAL_DEGREES - border.getStartAngle()));
            }else {
                avgBearing += (d-border.getStartAngle());
            }
        }
                
        
        avgBearing /= result.size();
        
        avgBearing = (avgBearing + border.getStartAngle()) % Constants.TOTAL_DEGREES;
        
        return avgBearing;

    }
    
    

    @Override
    public Class getValueType() {
        return Double.class;
    }

    @Override
    public void clear() {
        result.clear();
    }
    
    protected class BearingBorder{
        
        private double startAngle;
        private double endAngle;        
        private double difference;

        public BearingBorder(double startAngle, double endAngle, double difference) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
            this.difference = difference;
        }
       
        
        public double getDifference() {
            return difference;
        }

        public double getEndAngle() {
            return endAngle;
        }

        public double getStartAngle() {
            return startAngle;
        }
        
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("[s:");
            sb.append(startAngle);
            sb.append(", e:");
            sb.append(endAngle);
            sb.append(", diff:");
            sb.append(difference);
            sb.append("}");
            
            return sb.toString();
        }
        
    }
    
}
