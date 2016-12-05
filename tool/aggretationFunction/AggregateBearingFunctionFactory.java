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

/**
 *
 * @author fernando
 */
public class AggregateBearingFunctionFactory  implements AggregationFunctionFactory
{
    String functionName;

    @Override
    public void setFunctionName(String string) {
        functionName = string;
    }

    @Override
    public void validate(AggregationValidationContext avc) {
        if ((avc.getParameterTypes().length != 1) ||
            (avc.getParameterTypes()[0] != Double.class)) {
            throw new IllegalArgumentException("Bearing aggregation requires a single parameter of type Double");
        }
    }

    @Override
    public AggregationMethod newAggregator() {
        return new AggregateBearingFunction();
    }

    @Override
    public Class getValueType() {
        return Double.class;
    }
    
    
}
