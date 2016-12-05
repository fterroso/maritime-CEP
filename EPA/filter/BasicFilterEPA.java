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
package EPA.filter;

import EPA.EPA;
import EPA.filter.listener.EndItineraryEventListener;
import EPA.filter.listener.FilterListener;
import EPA.filter.listener.FirstEventListener;
import EPA.filter.listener.NewLevelEventListener;
import EPA.filter.listener.NewLevelFirstEventListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import org.apache.log4j.Logger;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author fernando
 */
public class BasicFilterEPA extends EPA{

    static Logger LOG = Logger.getLogger(BasicFilterEPA.class);    
    
    private static final String FILTER_EPL = "INSERT INTO FilteredLocationEvent "+
                                              "SELECT current_timestamp as timestamp, "+
                                              "       B.id as id, "+
                                              "       B.location as location, "+
                                              "       1 as level "+
                                              "FROM RawLocationEvent B unidirectional, "+
                                              "     FilteredLocationEvent(level=1).std:unique(id) A "+
                                              "WHERE euclideanDist(A.location, B.location) > minDist AND"+
                                              "      A.id = B.id";
                                                        
    private static final String LOWER_LEVEL_FIRST_EVENT_EPL =   "INSERT INTO FilteredLocationEvent "+
                                                                "SELECT current_timestamp as timestamp, "+
                                                                "       A.id as id, "+
                                                                "       A.location as location, "+
                                                                "       1 as level "+
                                                                "FROM pattern[every-distinct(A.id) A=RawLocationEvent]"; 
    
    private static final String NEW_LEVEL_FIRST_EVENT_EPL =     "INSERT INTO FilteredLocationEvent "+
                                                                "SELECT current_timestamp as timestamp, "+
                                                                "       A.id as id, "+
                                                                "       A.location as location, "+
                                                                "       B.level+1 as level "+
                                                                "FROM pattern[every-distinct(A.id) A=RawLocationEvent -> every-distinct(B.level, B.id) B=FilteredLocationEvent(level = EventHierarchy.getLevelForId(B.id), id = A.id)]";
    
    private static final String NEW_LEVEL_EPL = "SELECT EventHierarchy.incrementLevelForId(A.id) "+
                                                "FROM pattern[ every-distinct(A.level, A.id) A=FilteredLocationEvent(level = EventHierarchy.getLevelForId(A.id)) -> B= FilteredLocationEvent(level = A.level, id = A.id)]";
                
    private static final String END_ITINERARY_1_EPL = 
                                                    "   INSERT INTO FilteredLocationEvent "+
                                                    "   SELECT "+
                                                    "          current_timestamp as timestamp, "+
                                                    "          A.id as id, "+
                                                    "          A.location as location, "+
                                                    "          1 as level, "+
                                                    "          true as isLast "+
                                                    "  FROM EndTrajectoryEvent.std:lastevent(), "+
                                                    "       FilteredLocationEvent(level=1, isLast = false).std:unique(id) A";            
            
    private static final String END_ITINERARY_2_EPL = 
                                                    "INSERT INTO FilteredLocationEvent "+
                                                    "SELECT current_timestamp as timestamp, "+
                                                    "       A.id as id, "+
                                                    "       A.location as location, "+
                                                    "       A.level +1 as level, "+
                                                    "       true as isLast "+ 
                                                    "FROM FilteredLocationEvent(isLast = true,level < EventHierarchy.getLevelForId(id)).std:unique(id,level) A ";//+//std:lastevent() A "+
    
    EPStatement filterStatement;
    EPStatement lowerLevelFirstEventStatement;
    EPStatement newLevelFirstEventStatement;
    EPStatement newLevelStatement;
    
    EPStatement endItineraryStatement1;
    EPStatement endItineraryStatement2;
    
    @Override
    public void start(EPServiceProvider pCEPEngine, TraceVisualizer visualizer) {
        
        LOG.info("Starting BasicFilterEPA...");
        
        CEPEngine = pCEPEngine;
        
        filterStatement = CEPEngine.getEPAdministrator().createEPL(FILTER_EPL);
        FilterListener filterList = new FilterListener(visualizer);
        filterStatement.addListener(filterList);
        
        lowerLevelFirstEventStatement = CEPEngine.getEPAdministrator().createEPL(LOWER_LEVEL_FIRST_EVENT_EPL);
        FirstEventListener  firstEventList = new FirstEventListener(visualizer);
        lowerLevelFirstEventStatement.addListener(firstEventList);
        
        newLevelFirstEventStatement = CEPEngine.getEPAdministrator().createEPL(NEW_LEVEL_FIRST_EVENT_EPL);
        NewLevelFirstEventListener newLevelEventList = new NewLevelFirstEventListener(visualizer);
        newLevelFirstEventStatement.addListener(newLevelEventList);
        
        newLevelStatement = CEPEngine.getEPAdministrator().createEPL(NEW_LEVEL_EPL);
        NewLevelEventListener newLevelList = new NewLevelEventListener();
        newLevelStatement.addListener(newLevelList);

        endItineraryStatement1 = CEPEngine.getEPAdministrator().createEPL(END_ITINERARY_1_EPL);
        EndItineraryEventListener endList = new EndItineraryEventListener(visualizer);
        endItineraryStatement1.addListener(endList);
        
        endItineraryStatement2 = CEPEngine.getEPAdministrator().createEPL(END_ITINERARY_2_EPL);
        endItineraryStatement2.addListener(endList);
                
        LOG.info("BasicFilterEPA has been started.");
    }
    
}
