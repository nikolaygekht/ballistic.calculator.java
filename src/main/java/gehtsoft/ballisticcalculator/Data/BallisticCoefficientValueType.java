package gehtsoft.ballisticcalculator.data;

/**  
  * The type of the ballistic coefficient value
  */
public enum BallisticCoefficientValueType
{
    /**
     * Coefficient.
     *
     * The typical BC value. 
     * 
     * It is proportion of the bullet sectional density to 
     * the sectional density of the original table's bullet
     */
    COEFFICIENT,
    
    /**
     * The form factor
     * 
     * The coefficient showing how the bullet's behavior rely to
     * the the original bullet. 
     * 
     * If you use form factor, make sure that the bullet diameter and
     * bullet weight are specified. 
     */
    FORMFACTOR,
}
