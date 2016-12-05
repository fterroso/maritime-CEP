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
package seabilla.lowlevel.CEP.event.alert;

/**
 *
 * @author Fernando Terroso-Saenz <fterroso@um.es>
 */
public enum AlertSourceType {
    UMU("University of Murcia", "umu"),
    TNO("TNO", "tno"),
    BAES("BAES", "baes"),
    COS("Corrysys", "cos"),
    FOI("FOI", "foi");
    
    String description;
    String directoryPath;

    public String getDescription() {
        return description;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    private AlertSourceType(String description, String directoryPath) {
        this.description = description;
        this.directoryPath = directoryPath;
    }        
}
