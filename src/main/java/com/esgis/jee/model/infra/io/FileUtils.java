/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esgis.jee.model.infra.io;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author gyldas_atta_kouassi
 */
public class FileUtils {
    
    /* public static void deepCreateDirectory(Path filePath) throws IOException {
        String[] filePathStringArr = filePath.toAbsolutePath().toString().split(File.separator);
        String filePathString = "";
        for(String path : filePathStringArr) {
            filePathString += String.format("%s%s", File.separator, path);
            filePath = Paths.get(filePathString);
            Files.createDirectories(filePath);
        }
    } */
    
    /**
     * <pre>Construit un chemin avec le nom d'un dossier et ceux d'une liste d'objets sérialisables (dossier ou fichier) imbriqués dans l'ordre, l'un à l'autre</pre>
     * @param directoryName
     * @param serialisableObjectsNames
     * @return 
     */
    public static String createStringPath(String directoryName, String... serialisableObjectsNames) {
        if(serialisableObjectsNames == null || serialisableObjectsNames.length == 0) {
            return directoryName;
        }
        else if(serialisableObjectsNames.length == 1) {
            return String.format("%s%s%s", directoryName, File.separator, serialisableObjectsNames[0]);
        }
        else {
            return createStringPath((createStringPath(directoryName, serialisableObjectsNames[0])), Arrays.copyOfRange(serialisableObjectsNames, 0, serialisableObjectsNames.length - 1));
        }
    }
}
