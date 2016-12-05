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
package tool;

/**
 *
 * @author Global constants of the system
 */
public class Constants {
    
    public static final String KML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"; 
    public static final String KML_COORDINATES_FORMAT = "(-?\\d+\\.?\\d*)(\\s+)(-?\\d+\\.?\\d*)\\s+";
   
//    //Lines to skip in a geolife file
    public static final int GEOLIFE_FILE_HEAD_LINES = 6;
    
//    //Header of the file to visilize traces and events on the GPS Viewer website.
    public static final String GPSVISUALIZER_WEB_HEAD_LINE = "name,desc,color,opacity,symbol,latitude,longitude\n";   
//    //GPX.
    public static final String GPX_HEAD_LINE = "<?xml version='1.0' encoding='UTF-8'?>\n<gpx version=\"1.1\" creator=\"JOSM GPX export\" xmlns=\"http://www.topografix.com/GPX/1/1\"\n xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n";
    public static final String GPX_TAIL_LINE = "</gpx>\n";
//    //KML   
    public static final String KML_GENERAL_HEAD_LINE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n<Document>\n\t<name>UMU - SeaBILLA Tracks and Alerts</name>\n";
    public static final String KML_GENERAL_TAIL_LINE = "</Document>\n</kml>";
    
    public static final String KML_SPECIFIC_HEAD_LINE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n<Document>\n\t<name>ELEMENT_NAME</name>\n";
   
    public static final String KML_ALERT_ICON_STYLE_1 = "<Style id=\"alertHigh\">\n<LabelStyle>\n\t<colorMode>normal</colorMode>\n\t<scale>0</scale></LabelStyle>\n<IconStyle>\n\t<Icon>\n\t\t<href>http://cdn1.iconfinder.com/data/icons/freeapplication/png/24x24/Warning.png</href>\n\t</Icon>\n\t<scale>1</scale>\n\t<hotspot x=\"0.5\" xunits=\"fraction\" y=\"0.5\" yunits=\"fraction\"/>\n</IconStyle>\n<BalloonStyle>\n<text><![CDATA[<b>Vessel ID: $[vss]</b><br /></>$[tm]<br />$[atp]<br />$[alg]]]></text>\n</BalloonStyle>\n</Style>\n";
    public static final String KML_ALERT_ICON_STYLE_2 = "<Style id=\"alertMedium\">\n<LabelStyle>\n\t<colorMode>normal</colorMode>\n\t<scale>0</scale></LabelStyle>\n<IconStyle>\n\t<Icon>\n\t\t<href>http://cdn3.iconfinder.com/data/icons/musthave/24/Information.png</href>\n\t</Icon>\n\t<scale>1</scale>\n\t<hotspot x=\"0.5\" xunits=\"fraction\" y=\"0.5\" yunits=\"fraction\"/>\n</IconStyle>\n<BalloonStyle>\n<text><![CDATA[<b>Vessel ID: $[vss]</b><br /></>$[tm]<br />$[atp]<br />$[alg]]]></text>\n</BalloonStyle>\n</Style>\n";
    
    public static final String KML_SPECIFIC_TRACK_HEAD_LINE = "\t<Placemark id=\"ID_CODE\">\n"+
                                                                "\t\t<name>ID_CODE</name>\n"+
                                                                "\t\t<styleUrl>gps</styleUrl>\n"+
                                                                "\t\t<gx:Track>\n"+
                                                                "\t\t\t<extrude>1</extrude>\n"+
                                                                "\t\t\t<altitudeMode>absolute</altitudeMode>\n";
    
    public static final String KML_GENERAL_TRACK_HEAD_LINE = "<Folder id=\"tracks\">\n"+
                                                                "\t<name>Tracks</name>\n";

    public static final String KML_GENERAL_ALERT_HEAD_LINE = "<Folder id=\"alerts\">\n"+
                                                               "<name>Alerts</name>\n";
    
    public static final String KML_GENERAL_ALERT_TAIL_LINE = "</Folder>\n"; 
    
    public static final String KML_GENERAL_TRACK_TAIL_LINE = "</Folder>\n";                                                                
    
    public static final String KML_SPECIFIC_TRACK_TAIL = "\t\t</gx:Track>\n"+
                                                           "\t</Placemark>";
   
    public static final String KML_TRACK_STYLE = "<Style id=\"gps\">\n"+
                                                    "\t<IconStyle><color>ff009900</color>\n"+
                                                            "\t\t<scale>0.7</scale>\n"+
                                                            "\t\t<Icon>\n"+
                                                            "\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/track.png</href>\n"+
                                                            "\t\t</Icon>\n"+
                                                            "\t\t<hotSpot x=\"32\" xunits=\"pixels\" y=\"1\" yunits=\"pixels\"/>\n"+
                                                        "\t</IconStyle>\n"+
                                                        "\t<LabelStyle>\n"+
                                                            "\t\t<color>ff009900</color>\n"+
                                                            "\t\t<scale>0.7</scale>\n"+
                                                        "\t</LabelStyle>\n"+
                                                        "\t<ListStyle/>\n"+
                                                        "\t<LineStyle>\n"+
                                                            "\t\t<color>ff009900</color>\n"+
                                                            "\t\t<width>2</width>\n"+
                                                        "\t</LineStyle>\n"+
                                                        "\t<PolyStyle>\n"+
                                                            "\t\t<color>ff009900</color>\n"+
                                                        "\t</PolyStyle>\n"+
                                                    "</Style>\n";
    
    public static final String KML_ALARM_TRACK_STYLE = "<StyleMap id=\"alarm_track\">\n"+
                                                         "<Pair>\n"+
                                                            "<key>normal</key>"+
                                                            "<styleUrl>#alarm_trackn</styleUrl>\n"+
                                                         "</Pair>\n"+
                                                         "<Pair>\n"+
                                                            "<key>highlight</key>\n"+
                                                            "<styleUrl>#alarm_trackh</styleUrl>\n"+
                                                         "</Pair>\n"+
                                                      "</StyleMap>\n"+
                                                      "<Style id=\"alarm_trackn\">\n"+
                                                         "<IconStyle>\n"+
                                                            "<Icon><href>http://cdn1.iconfinder.com/data/icons/icojoy/noshadow/standart/gif/24x24/001_30.gif</href></Icon>\n"+
                                                         "</IconStyle>\n"+
                                                         "<LabelStyle>\n"+
                                                            "<color>ff009900</color>\n"+
                                                            "<scale>0.7</scale>\n"+
                                                         "</LabelStyle>\n"+
                                                         "<LineStyle>\n"+
                                                            "<color>ff009900</color>\n"+
                                                            "<width>2</width>\n"+
                                                         "</LineStyle>\n"+
                                                         "<PolyStyle>\n"+
                                                            "<color>ff009900</color>\n"+
                                                         "</PolyStyle>\n"+
                                                      "</Style>\n"+
                                                      "<Style id=\"alarm_trackh\">\n"+
                                                         "<IconStyle>\n"+
                                                            "<color>7f00ffff</color>\n"+
                                                            "<Icon><href>http://cdn1.iconfinder.com/data/icons/icojoy/noshadow/standart/gif/24x24/001_30.gif</href></Icon>\n"+
                                                         "</IconStyle>\n"+
                                                         "<LabelStyle>\n"+
                                                            "<color>7f00ffff</color>\n"+
                                                            "<scale>0.7</scale>\n"+
                                                         "</LabelStyle>\n"+
                                                         "<LineStyle>\n"+
                                                            "<color>7f0000ff</color>\n"+
                                                            "<width>6</width>\n"+
                                                         "</LineStyle>\n"+
                                                         "<PolyStyle>\n"+
                                                            "<color>7f00ffff</color>\n"+
                                                         "</PolyStyle>\n"+
                                                      "</Style>\n";
    
    public static final String GNUPLOT_HEAD_LINE =   "set term postscript eps enhanced color\n"+
                                                "set output 'gap_times.eps'\n"+
                                                "set bmargin 6\n"+
                                                "set border 3\n"+
                                                "set yrange [0:<maxy>]\n"+
                                                "set xrange [0:<maxx>]\n"+
                                                "set key top center\n"+
                                                "set key box\n"+
                                                "set key horiz samplen 2\n"+
                                                "set ylabel \"Delay (s)\"\n"+
                                                "set xlabel \"Point number\"\n"+
                                                "set grid ytics\n"+
                                                "set xtics rotate by-45\n"+
                                                "set ytics 500 nomirror\n";
    
    public static final double TOTAL_DEGREES = 360; // in degrees.
    
    public static final double MAX_LAT_VARIATION = 0.00005;
    public static final double MAX_LON_VARIATION = 0.00005;
    
    public static final int INITIAL_POINTS_PER_LEVEL = 2;
    public static final int INITIAL_CURRENT_LEVEL = 1;
    
    public static final long WAITING_TIME_TO_FINISH = 4000;
       
}
