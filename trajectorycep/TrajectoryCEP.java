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
package trajectorycep;

import CEP.CEPConfigurator;
import EPA.EPA;
import EPA.adaptor.AdaptorEPAFactory;
import EPA.adaptor.AdaptorEPAType;
import EPA.filter.FilterEPAFactory;
import EPA.trajectory.TrajectoryEPAFactory;
import com.espertech.esper.client.EPServiceProvider;
import config.ConfigInfoProvider;
import context.provider.BasicContextProvider;
import context.provider.ContextProvider;
import context.reader.staticData.environment.BasicKMLStaticSpatialContextReader;
import context.reader.staticData.environment.StaticSpatialContextReader;
import context.sender.BasicContextSender;
import context.sender.ContextSender;
import java.io.File;
import org.apache.log4j.Logger;
import seabilla.config.SeabillaConfigInfoProvider;
import seabilla.lowlevel.CEP.EPA.detection.AbnormalBehaviorEPA;
import seabilla.lowlevel.CEP.EPA.detection.SeabillaLowLevelDetectionEPAFactory;
import seabilla.lowlevel.CEP.SeabillaLowLevelCEPConfigurator;
import seabilla.tool.visualizer.SeabillaVisualizerParser;
import tool.trace.visualizer.TraceVisualizer;

/**
 *
 * @author fernando
 */
public class TrajectoryCEP {

    static Logger LOG = Logger.getLogger(TrajectoryCEP.class);    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
        try{
            
            SeabillaConfigInfoProvider.readXMLConfigurationFile(args[0]);
            
            ContextProvider contextProvider = new BasicContextProvider();
            ConfigInfoProvider.setContextProvider(contextProvider);
            
            StaticSpatialContextReader spatialContextReader = new BasicKMLStaticSpatialContextReader(new File(SeabillaConfigInfoProvider.getDataPath() + File.separator + "context"));
            spatialContextReader.readStaticContext();
            
            CEPConfigurator configurator = new SeabillaLowLevelCEPConfigurator();        
            EPServiceProvider cepProvider = configurator.configureCEPEngine();

            TraceVisualizer visualizer = new SeabillaVisualizerParser();
            
            EPA detection = (AbnormalBehaviorEPA) SeabillaLowLevelDetectionEPAFactory.getDetectionEPA();
            detection.start(cepProvider, visualizer);
            
            EPA trajectory = TrajectoryEPAFactory.getTrajectoryEPA();
            trajectory.start(cepProvider, visualizer);

            EPA filter = FilterEPAFactory.getFilterEPAFactory();
            filter.start(cepProvider, visualizer);

            ContextSender contextSender = new BasicContextSender(cepProvider); 
            contextSender.startSendingContextEvents();
            
            AdaptorEPAType type = SeabillaConfigInfoProvider.getTrackFileType();            
            EPA adaptor = AdaptorEPAFactory.getAdaptorEPA(type);            
            adaptor.start(cepProvider, visualizer);
            
            LOG.info("End of the simulation.");
        
        }catch(Exception e){
            LOG.error("Error en main, ", e);
        }

    }
}
