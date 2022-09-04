package gehtsoft.ballisticcalculator;

import javax.measure.*;
import javax.measure.quantity.*;

import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.TransformedUnit;

public final class BallisticSpecificUnits {
    /** 
     * Mass: One grain (1/7000 of pound) 
     */
    public static final Unit<Mass> GRAIN =
            new TransformedUnit<Mass>("gr", CLDR.POUND, MultiplyConverter.ofRational(1, 7000));   

    /**
     * Speed: feed per second
     */
    public static final Unit<Speed> FEET_PER_SECOND =            
            new TransformedUnit<Speed>("ft/s", SI.METRE_PER_SECOND, MultiplyConverter.of(1 / 3.2808399));

    /** 
     * Minute of arc (21600 per full circle)
     */
    public static final Unit<Angle> MOA = 
        new TransformedUnit<Angle>("moa", CLDR.RADIAN,
                                   MultiplyConverter.ofPiExponent(1)
                                                    .concatenate(MultiplyConverter.ofRational(1, 180 * 60)));


    /** 
     * Arc mil (6400 mil full circle)
     */
    public static final Unit<Angle> MIL = 
        new TransformedUnit<Angle>("mil", CLDR.RADIAN,
                                   MultiplyConverter.ofPiExponent(1)
                                                    .concatenate(MultiplyConverter.ofRational(1, 3200)));

    /** 
     * Milliradian (1/1000 of a radian)
     */
    public static final Unit<Angle> MRad = 
        new TransformedUnit<Angle>("mrad", CLDR.RADIAN,
                                   MultiplyConverter.ofRational(1, 1000));
}
