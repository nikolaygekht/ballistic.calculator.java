package gehtsoft.ballisticcalculator.drag;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.measure.Quantity;
import javax.measure.quantity.*;

import si.uom.SI;
import tech.units.indriya.quantity.Quantities;

@java.lang.SuppressWarnings("java:S3252") //false positive for java's own SI.*
class DrgFileReader {
    private Quantity<Length> mBulletDiameter = null;
    private Quantity<Mass> mBulletWeight = null;
    private ArrayList<DragTableDataPoint> mList = new ArrayList<>();
    private String mName = null;
    private BufferedReader mSource = null;

    public DrgFileReader(BufferedReader source) {
        mSource = source;
    }

    public DrgFile read() throws IOException, IllegalArgumentException {
        String line;

        while ((line = mSource.readLine()) != null)
            processLine(line);

        DragTableDataPoint[] points = new DragTableDataPoint[mList.size()];
        mList.toArray(points);
        return new DrgFile(mName, mBulletDiameter, mBulletWeight, points);
    } 

    private void processLine(String line) throws IllegalArgumentException {
        if (line.trim().length() == 0)
                return;
                
            if (mName == null) 
                processHeaderLine(line);
            else 
                processDataPointLine(line);
    }

    private void processHeaderLine(String line) throws IllegalArgumentException {
        String[] tokens = line.split(",\\s*");
        if (tokens.length >= 4) {
            if (!tokens[0].equals("CFM") && !tokens[0].equals("BRL")) {
                throw new IllegalArgumentException("Invalid file format: the table must be CFM or BRL format but it is " + tokens[0]);
            }
            mName = tokens[1].trim();
            mBulletWeight = Quantities.getQuantity(Double.parseDouble(tokens[2]), SI.KILOGRAM);
            mBulletDiameter = Quantities.getQuantity(Double.parseDouble(tokens[3]), SI.METRE);
        }
    }
    
    private void processDataPointLine(String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length == 2) {
            double dragCoefficient = Double.parseDouble(tokens[0]);
            double mach = Double.parseDouble(tokens[1]);
            mList.add(new DragTableDataPoint(mach, dragCoefficient));
        }

    }
}
