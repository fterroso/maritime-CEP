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
import java.util.LinkedList;
import java.util.List;
import tool.Point;
import tool.supportFunction.LocationFunction;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class AggregateBearingFromPointsFunction implements AggregationMethod{

    List<Point> result;

    public AggregateBearingFromPointsFunction() {
        result = new LinkedList<Point>();
    }        
    
    @Override
    public void enter(Object value) {
        if (value != null) {
            Point currentPoint = (Point)value;
            if(!result.contains(currentPoint)){
                result.add(currentPoint);
            }            
        }
    }

    @Override
    public void leave(Object value) {
        if(value != null){
            Point currentPoint = (Point)value;
            result.remove(currentPoint);
                        
        }
    }

    @Override
    public Object getValue() {
        double bearing = 0;
        
        if(!result.isEmpty() && result.size() > 1 ){
            for(int i = 0; i< result.size()-2; i++){
                double aux = LocationFunction.bearing(result.get(i), result.get(i+1));
                bearing += aux;
            }
            bearing /= result.size();
        }
        
        return bearing;
    }

    @Override
    public Class getValueType() {
        return Point.class;
    }

    @Override
    public void clear() {
        result.clear();
    }
    
}
