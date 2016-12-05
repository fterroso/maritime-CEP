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
package EPA.adaptor;

import event.MapElement;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author fernando
 */
public class GPXAdaptorEPA extends AdaptorEPA{

    static protected Logger LOG = Logger.getLogger(GPXAdaptorEPA.class);    
    
    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";    
    
    File gpxTraceFile;
    boolean modifyOriginalTrace = false;
    
    public GPXAdaptorEPA(File traceFile, boolean modifyOriginalTrace){
        gpxTraceFile = traceFile;
        this.modifyOriginalTrace = modifyOriginalTrace;
    }
    
    @Override
    public List<MapElement> generateTargetEvents(){
        
        List<MapElement> locations = null;

        try{
  
            locations = generateOriginalTrace();
         
        }catch(Exception e){
            LOG.error("Error accessing GPX trace file ", e);            
        }
        return locations;
    }
    
    protected List<MapElement> generateOriginalTrace() throws Exception{
        
        List<MapElement> locations = new LinkedList<MapElement>();
        
        try{
            List<File> files = new LinkedList<File>();
            if(gpxTraceFile.isDirectory()){
               
                File[] filesInPath = gpxTraceFile.listFiles();
                for(File f : filesInPath){
                    if(f.getName().endsWith(".gpx")){
                        files.add(f);
                    }
                }
            }else{
                files.add(gpxTraceFile);
            }
            
            for(File f : files){

                LOG.info("Parsing file for tracks "+ f.getName());

                SAXBuilder builder=new SAXBuilder(false);
                Document doc=builder.build(f);
                Element root =doc.getRootElement();

                Namespace ns = root.getNamespace();

                List<Element> trks = root.getChildren("trk",ns); 

                for(Element trk : trks){

                    String id = trk.getChild("name",ns).getText();

                    Element trkSeg = trk.getChild("trkseg",ns);

                    List<Element> trkPts = trkSeg.getChildren("trkpt",ns);

                    int counter = 0;
                    for(Element trkPt : trkPts){
                        double lat = Double.valueOf(trkPt.getAttributeValue("lat"));
                        double lon = Double.valueOf(trkPt.getAttributeValue("lon"));

                        String time = trkPt.getChild("time",ns).getText();                

                        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
                        Date d = dateFormatter.parse(time);

                        locations.add(makeUpLocationEvent(id,lat,lon,d,counter++));                
                    }
                }
            }
        
        }catch(Exception e){
            LOG.error("Error accessing GPX file "+gpxTraceFile.getName(), e);            
        } 
        
        return locations;
    }
       
}
