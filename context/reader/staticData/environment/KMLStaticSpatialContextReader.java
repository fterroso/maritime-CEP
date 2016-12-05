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
package context.reader.staticData.environment;

import config.ConfigInfoProvider;
import context.data.staticContext.environment.StaticSpatialContext;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import tool.Point;
import tool.Polygon2D;
import tool.areaOfInterest.AreaOfInterest;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public abstract class KMLStaticSpatialContextReader implements StaticSpatialContextReader{

    static Logger LOG = Logger.getLogger(KMLStaticSpatialContextReader.class);    

    protected File KMLFile;

    public KMLStaticSpatialContextReader(File KMLFile) {
        this.KMLFile = KMLFile;
    }
    
    @Override
    public void readStaticContext() {
        
        StaticSpatialContext staticSpatialContext = getStaticSpatialContext();
                        
        List<File> kmlFiles = new LinkedList<File>();

        File[] aux = KMLFile.listFiles();
        for(File f : aux){
            if(f.getName().endsWith(".kml")){
                kmlFiles.add(f);
            }
        }

        List<AreaOfInterest> areas = null;
        for(File kmlFile : kmlFiles){
            try{
                areas = processKMLStaticSpatialContextFile(kmlFile);
            }catch(Exception e){
                LOG.error("Error while parsing KML contextual file "+ kmlFile.getName(), e);
            }
        }
        processAreasOfInterest(areas, staticSpatialContext);
        
        ConfigInfoProvider.getContextProvider().addNewStaticSpatialContext(staticSpatialContext);
    }
    
    
    protected List<AreaOfInterest> processKMLStaticSpatialContextFile(File kmlFile) throws Exception{
        
        List<AreaOfInterest> result = new LinkedList<AreaOfInterest>();
        
        SAXBuilder builder=new SAXBuilder(false);
        Document doc= builder.build(kmlFile);

        Element root =doc.getRootElement();
        Namespace ns = root.getNamespace();

        Element documentE = root.getChild("Document",ns);
        Element folderE = documentE.getChild("Folder",ns);

        List<Element> placemarks = folderE.getChildren("Placemark",ns);
        
        for(Element placemark : placemarks){
            Element extendedDataE = placemark.getChild("ExtendedData",ns);
            Element schemaDataE = extendedDataE.getChild("SchemaData",ns);
            List<Element> simpleDatasE = schemaDataE.getChildren("SimpleData",ns);
            
            String nameArea = "id";
            for(Element simpleDataE : simpleDatasE){
                String nameValue = simpleDataE.getAttributeValue("name");
                if("id".equals(nameValue) || "f_code".equals(nameValue)){
                    nameArea = simpleDataE.getValue();
                }
            }
            
            Element polygonE = placemark.getChild("Polygon",ns);
            Element outerBoundaryE = polygonE.getChild("outerBoundaryIs",ns);
            Element linearRingE = outerBoundaryE.getChild("LinearRing",ns);
            String coordinates = linearRingE.getChild("coordinates",ns).getValue();
            List<Point> areaPoints = getPointsFromString(coordinates);
            
            Polygon2D area = new Polygon2D();
            
            for(Point p : areaPoints){
                area.addPoint((float)p.getLat(), (float)p.getLon());
            }
            
            AreaOfInterest areaOfInterest = new AreaOfInterest();
            areaOfInterest.setArea(area);
            areaOfInterest.setName(nameArea);
            
            result.add(areaOfInterest);
            
        }
        
        return result;
    }

    private List<Point> getPointsFromString(String coordinatesSt){
        List<Point> result = new LinkedList<Point>();
        StringTokenizer sb = new StringTokenizer(coordinatesSt, " ");
        
        while(sb.hasMoreTokens()){
            String coordinateSt = sb.nextToken();
            String[] coordinateParts = coordinateSt.split(",");
            
            double lon = Double.valueOf(coordinateParts[0]);
            double lat = Double.valueOf(coordinateParts[1]);
            
            Point p = new Point();
            p.setLat(lat);
            p.setLon(lon);
            
            result.add(p);
            
        }        
        
        return result;
    }
    
    protected abstract StaticSpatialContext getStaticSpatialContext();
    protected abstract void processAreasOfInterest(List<AreaOfInterest> areas, StaticSpatialContext context);
}
