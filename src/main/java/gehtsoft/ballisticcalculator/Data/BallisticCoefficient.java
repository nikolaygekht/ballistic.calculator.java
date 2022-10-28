package gehtsoft.ballisticcalculator.data;

import gehtsoft.ballisticcalculator.drag.*;

/**
 * Ballistic coefficient of a projectile
 */
public class BallisticCoefficient {
    private double mValue;
    private IDragTable mDragTable;
    private BallisticCoefficientValueType mType;

    /** 
     * Returns the value of the coefficient
     */
    public double getValue() {
        return mValue;
    }

    /**
     * Returns the drag table.
     */
    public IDragTable getDragTable() {
        return mDragTable;
    }

    /**
     * The type of the coefficient value. 
     */
    public BallisticCoefficientValueType getType() {
        return mType;
    }

    /**
     * Constructor for a standard coefficient
     *
     * @param value The value of the coefficient
     * @param type The type of the coefficient
     */
    public BallisticCoefficient(double value, DragTableId tableId)  {
        mValue = value;
        mType = BallisticCoefficientValueType.COEFFICIENT;
        mDragTable = StandardDragTableFactory.getInstance().getTable(tableId);
    }

    public BallisticCoefficient(double value, BallisticCoefficientValueType type, IDragTable dragTable) {
        mValue = value;
        mType = type;
        mDragTable = dragTable;
    }
}