package gehtsoft.ballisticcalculator.drag;

import java.io.BufferedReader;
import java.io.IOException;
import javax.measure.*;
import javax.measure.quantity.*;

@java.lang.SuppressWarnings("java:S3252") //false positive for java's own SI.*
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

    DrgFile(String name, Quantity<Length> bulletDiameter, 
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
        DrgFileReader reader = new DrgFileReader(source);
        return reader.read();
    }   
}
