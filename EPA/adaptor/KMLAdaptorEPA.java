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
import event.location.RawLocationEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import tool.Constants;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public class KMLAdaptorEPA extends AdaptorEPA{
    
    File KMLFile;    
    
    public KMLAdaptorEPA(File KMLFile){
        this.KMLFile = KMLFile;  
     }
    
    @Override
    public List<MapElement> generateTargetEvents() {
               
        List<MapElement> locations = new LinkedList<MapElement>();

        Pattern p = Pattern.compile("Speed: \\d+\\.?\\d*");
                
        try{
            List<File> files = new LinkedList<File>();
            if(KMLFile.isDirectory()){
                File[] filesInPath = KMLFile.listFiles();
                for(File f : filesInPath){
                    if(f.getName().endsWith(".kml")){
                        files.add(f);
                    }
                }
            }else{
                files.add(KMLFile);
            }
            
            for(File f : files){

                LOG.info("Parsing file for tracks "+ f.getName());
                
                SAXBuilder builder=new SAXBuilder(false);
                Document doc= builder.build(f);

                Element root =doc.getRootElement();
                Namespace ns = root.getNamespace();

                Element documentE = root.getChild("Document",ns);
                Element nameE = documentE.getChild("name",ns);

                String id = nameE.getText();

                List<Element> placemarks = documentE.getChildren("Placemark",ns);

                SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.KML_DATE_FORMAT);

                for(Element placemark : placemarks){

                    nameE = placemark.getChild("name",ns);               

                    if(nameE != null){

                        try{

                            int counter = Integer.valueOf(nameE.getText());

                            String description = placemark.getChild("description",ns).getText();
                                                        
                            Matcher m = p.matcher(description);
                            
                            double speed = 0;
                            if(m.find()){
                                String speedStr = m.group(0);
                                
                                int i = speedStr.indexOf(":");
                                speedStr = speedStr.substring(i+1, speedStr.length()).trim();
                                
                                speed = Double.valueOf(speedStr);

                            }
                                                       
                            
                            Element timestamp = placemark.getChild("TimeStamp",ns);
                            String when = timestamp.getChild("when",ns).getText();

                            Date d = dateFormatter.parse(when);

                            Element point = placemark.getChild("Point",ns);
                            String coordinates = point.getChild("coordinates",ns).getText();

                            String coordinatesParts[] = coordinates.split(",");
                            double lon = Double.valueOf(coordinatesParts[0]);
                            double lat = Double.valueOf(coordinatesParts[1]);

                            RawLocationEvent event = makeUpLocationEvent(id,lat, lon, d, counter);
                            event.getLocation().setSpeed(speed);
                               
                            locations.add(event);                                                      
                            
                        }catch(Exception e){
                        }
                            
                    }
                }
            }
                                
        }catch(Exception e){
            LOG.error("Error accessing KML file "+KMLFile.getName(), e);            
        }        
        
        return locations;
    }
    
}
