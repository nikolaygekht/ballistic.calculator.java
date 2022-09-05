package gehtsoft.ballisticcalculator.Drag;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.measure.*;
import javax.measure.quantity.*;

import si.uom.SI;
import tech.units.indriya.quantity.Quantities;

public class DrgFile extends AdvancedDragTable {
    private String mName;
    private Quantity<Length> mBulletDiameter;
    private Quantity<Mass> mBulletWeight;

    /** Returns the name */
    public String getName() {
        return mName;
    }

    /** Returns the bullet diameter */
    public Quantity<Length> getBulletDiameter() {
        return mBulletDiameter;
    }

    /** Returns the bullet weight */
    public Quantity<Mass> getBulletWeight() {
        return mBulletWeight;
    }

    private DrgFile(String name, Quantity<Length> bulletDiameter, 
                    Quantity<Mass> bulletWeight, 
                    DragTableDataPoint[] points)
    {
        super(DragTableId.GC, points);
        mName = name;
        mBulletDiameter = bulletDiameter;
        mBulletWeight = bulletWeight;
    }

    /**
     * Reads a drag table from a file
     * @param source file reader
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public static DrgFile read(BufferedReader source)
        throws IOException, IllegalArgumentException
    {
        String name = null;
        Quantity<Length> bulletDiameter = null;
        Quantity<Mass> bulletWeight = null;
        ArrayList<DragTableDataPoint> list = new ArrayList<DragTableDataPoint>();

        String line;

        while ((line = source.readLine()) != null)
        {
            if (line.trim().length() == 0)
                continue;
            if (name == null) {
                String[] tokens = line.split(",\\s*");
                if (tokens.length >= 4) {
                    if (!tokens[0].equals("CFM") && !tokens[0].equals("BRL")) {
                        throw new IllegalArgumentException("Invalid file format: the table must be CFM or BRL format but it is " + tokens[0]);
                    }
                    name = tokens[1].trim();
                    bulletWeight = Quantities.getQuantity(Double.parseDouble(tokens[2]), SI.KILOGRAM);
                    bulletDiameter = Quantities.getQuantity(Double.parseDouble(tokens[3]), SI.METRE);
                }
            } else {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 2) {
                    double dragCoefficient = Double.parseDouble(tokens[0]);
                    double mach = Double.parseDouble(tokens[1]);
                    list.add(new DragTableDataPoint(mach, dragCoefficient));
                }
            }
        }

        DragTableDataPoint[] points = new DragTableDataPoint[list.size()];
        list.toArray(points);
        return new DrgFile(name, bulletDiameter, bulletWeight, points);
    }
}
