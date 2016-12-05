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
package event;

import tool.trace.visualizer.TraceOutputType;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public abstract class MapElement implements Comparable{
    
    public String serialize(TraceOutputType format, MapElement... aux){
        
        String result = "";
        switch(format){
            case GPX:
                result = toGPXFormat();
                break;
            case PLAIN_TEXT:
                result = toPlainTextFormat();
                break;
            case KML:
                result = toKMLFormat();
                break;                    
        }
        
        return result;
    }

    @Override
    public int compareTo(Object o) {
        MapElement l = (MapElement)o;
        
        long d = l.getTimestamp();        
        int result = (int)(getTimestamp()-d);
                
        return result;
    }
    
    @Override
    public String toString(){
        return toPlainTextFormat();
    }
    
    public abstract String getId();    
    public abstract long getTimestamp();    
    public abstract MapElementType getType();    
    public abstract long getTimestampGap();    
    protected abstract String toGPXFormat();    
    protected abstract String toPlainTextFormat();    
    protected abstract String toKMLFormat();

}
