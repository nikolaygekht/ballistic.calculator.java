package gehtsoft.ballisticcalculator.Drag;

/**
 * Interface to a drag table
 */
public interface IDragTable {
    /**
     * Get the identifier of the drag table
     */
    public DragTableId getID();

    public IDragTableNode find(double mach);
}
