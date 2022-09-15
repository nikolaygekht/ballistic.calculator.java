package gehtsoft.ballisticcalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import gehtsoft.ballisticcalculator.Drag.DrgFile;

public class DrgFileLoader {
    public static DrgFile loadDragTable(String resourceName) throws IOException {
        ClassLoader classLoader = TestDragTable.class.getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                DrgFile drgFile = DrgFile.read(reader);
                return drgFile;
        }
    }
}
