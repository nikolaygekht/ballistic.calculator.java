package gehtsoft.ballisticcalculator.Drag;

/**
 * A node of a drag table
 */
public interface IDragTableNode {
    /** Gets mach speed */
    public double getMach();
    /** Gets drag coefficient */
    public double getDragCoefficient();
    /** Gets the next node */
    public IDragTableNode getNext();
    /** Gets the previous node */
    public IDragTableNode getPrevious();
    /** Calculates for the specified velocity */
    public double CalculateDrag(double mach);
    /** Checks whether the specified velocity is withing the node range */
    public Boolean In(double mach);
}
