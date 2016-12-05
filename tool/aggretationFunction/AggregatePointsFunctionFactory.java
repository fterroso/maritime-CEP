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

import com.espertech.esper.client.hook.AggregationFunctionFactory;
import com.espertech.esper.epl.agg.AggregationMethod;
import com.espertech.esper.epl.agg.AggregationValidationContext;
import tool.Point;

/**
 *
 * @author fernando
 */
public class AggregatePointsFunctionFactory implements AggregationFunctionFactory {

    private String functionName;
    
    @Override
    public void setFunctionName(String string) {
        functionName = string;
    }

    @Override
    public void validate(AggregationValidationContext avc) {
        if ((avc.getParameterTypes().length != 1) ||
            (avc.getParameterTypes()[0] != Point[].class)) {
            throw new IllegalArgumentException("Points aggregation requires a single parameter of type tool.Point[]");
        }
    }

    @Override
    public AggregationMethod newAggregator() {
        return new AggregatePointsFunction();
    }

    @Override
    public Class getValueType() {
        return Point[].class;
    }
    
}
