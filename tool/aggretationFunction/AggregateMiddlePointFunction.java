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
import org.apache.log4j.Logger;
import tool.Point;

/**
 *
 * @author fernando
 */
public class AggregateMiddlePointFunction implements AggregationMethod{

    static Logger LOG = Logger.getLogger(AggregateMiddlePointFunction.class);    
    
    List<Point> result;

    public AggregateMiddlePointFunction() {
        result = new LinkedList<Point>();
    }        
    
    @Override
    public void enter(Object value) {
        if (value != null) {
            Point[] currentPoints = (Point[])value;
            for(Point p : currentPoints){
                if(!result.contains(p)){
                    result.add(p);
                }
            }
        }
    }

    @Override
    public void leave(Object value) {
        if(value != null){
            Point[] currentPoints = (Point[])value;
            for(Point p : currentPoints){
                result.remove(p);
            }            
        }
    }

    @Override
    public Object getValue() {
        if(!result.isEmpty()){
//            LOG.debug("Function. Tamaño lista: "+result.size()+" "+ result.get(0) + " " + result.get(result.size()-1));            
            return result.get(result.size()/2);
        }
        
        return null;
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
