package gehtsoft.ballisticcalculator.drag;

/**
 * A drag table for polynomial drag table approximation
 */
public class AdvancedDragTable implements IDragTable {
    private DragTableId mID;
    private DragTableNode[] mNodes;

    /** Returns the table identifier */
    public DragTableId getID() {
        return mID;
    }

    /** Constructor */
    protected AdvancedDragTable(DragTableId identifier, DragTableDataPoint[] points) {
        mID = identifier;

        int numberOfPoints = points.length;
        mNodes = new DragTableNode[numberOfPoints];
        double rate = (points[1].getDragCoefficient() - points[0].getDragCoefficient()) / (points[1].getMach() - points[0].getMach());
        mNodes[0] = new DragTableNode(points[0].getMach(), points[0].getDragCoefficient(), 0, rate, points[0].getDragCoefficient() - points[0].getMach() * rate, null);

        // rest as 2nd degree polynomials on three adjacent points
        for (int i = 1; i < numberOfPoints - 1; i++)
        {
            double x1 = points[i - 1].getMach();
            double x2 = points[i].getMach();
            double x3 = points[i + 1].getMach();

            double y1 = points[i - 1].getDragCoefficient();
            double y2 = points[i].getDragCoefficient();
            double y3 = points[i + 1].getDragCoefficient();

            double a = ((y3 - y1) * (x2 - x1) - (y2 - y1) * (x3 - x1)) / ((x3 * x3 - x1 * x1) * (x2 - x1) - (x2 * x2 - x1 * x1) * (x3 - x1));
            double b = (y2 - y1 - a * (x2 * x2 - x1 * x1)) / (x2 - x1);
            double c = y1 - (a * x1 * x1 + b * x1);

            mNodes[i] = new DragTableNode(points[i].getMach(), points[i].getDragCoefficient(), a, b, c, mNodes[i - 1]);
        }
        mNodes[numberOfPoints - 1] = new DragTableNode(points[numberOfPoints - 1].getMach(), points[numberOfPoints - 1].getDragCoefficient(), 0, 0, points[numberOfPoints - 1].getDragCoefficient(), mNodes[numberOfPoints - 2]);
    }

    public IDragTableNode find(double mach)
    {
        int numberOfPoints = mNodes.length;

        int mlo = 0;
        int mhi = numberOfPoints - 1;
        int mid;

        while ((mhi - mlo) > 1)
        {
            mid = (int)Math.floor((mhi + mlo) / 2.0);
            if (mNodes[mid].getMach() < mach)
                mlo = mid;
            else
                mhi = mid;
        }

        int m;
        if ((mNodes[mhi].getMach() - mach) > (mach - mNodes[mlo].getMach()))
            m = mlo;
        else
            m = mhi;

        return mNodes[m];
    }

    /**
     * Gets count of the data points
     */
    public int length()
    {
        return mNodes.length;
    }

    /**
     * Gets data point by index.
     */
    public IDragTableNode get(int index)
    {
        return mNodes[index];
    }
}
