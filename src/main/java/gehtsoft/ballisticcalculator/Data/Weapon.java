package gehtsoft.ballisticcalculator.data;

/** The information about the weapon */
public class Weapon {
    private ZeroingInformation mZeroingInformation;
    private Rifling mRifling;

    /** Gets the zeroing information */
    public ZeroingInformation getZeroingInformation() {
        return mZeroingInformation;
    }

    /** Gets the rifling 
     * 
     *  The rifling is optional and is used only to calculate drift. 
    */
    public Rifling getRifling() {
        return mRifling;
    }

    /** Constructor 
     * @param zeroingInformation The zeroing information
     * @param rifling The rifling is optional and is used only to calculate drift.
    */
    public Weapon(ZeroingInformation zeroingInformation, Rifling rifling) {
        mZeroingInformation = zeroingInformation;
        mRifling = rifling;
    }
}
