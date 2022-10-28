package gehtsoft.ballisticcalculator.units;

import javax.measure.*;
import javax.measure.quantity.*;

import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.TransformedUnit;

@java.lang.SuppressWarnings("java:S3252") //false positive for java's own SI.*
/** Ballistic Calculator specific units */
public final class BCUnits {
    /** 
     * Mass: One grain (1/7000 of pound) 
     */
    public static final Unit<Mass> GRAIN =
            new TransformedUnit<>("gr", CLDR.POUND, MultiplyConverter.ofRational(1, 7000));   

    /**
     * Speed: feed per second
     */
    public static final Unit<Speed> FEET_PER_SECOND =            
            new TransformedUnit<>("ft/s", SI.METRE_PER_SECOND, MultiplyConverter.of(1 / 3.2808399));

    /** 
     * Minute of arc (21600 per full circle)
     */
    public static final Unit<Angle> MOA = 
        new TransformedUnit<>("moa", CLDR.RADIAN,
                              MultiplyConverter.ofPiExponent(1)
                                               .concatenate(MultiplyConverter.ofRational(1l, 180l * 60l)));


    /** 
     * Arc mil (6400 mil full circle)
     */
    public static final Unit<Angle> MIL = 
        new TransformedUnit<>("mil", CLDR.RADIAN,
                              MultiplyConverter.ofPiExponent(1)
                                               .concatenate(MultiplyConverter.ofRational(1, 3200)));

    /** 
     * Milliradian (1/1000 of a radian)
     */
    public static final Unit<Angle> MRAD = 
        new TransformedUnit<>("mrad", CLDR.RADIAN,
                                      MultiplyConverter.ofRational(1, 1000));

    /**
     * Inches per 100 yards
     */                    
    public static final Unit<Angle> INCHES_PER_100YARDS =
        new TransformedUnit<>("in/100yd", CLDR.RADIAN,
                                          new AtanConverter(3600.0));

    /**
     * Cm per 100 meters
     */                    
    public static final Unit<Angle> CENTIMETERS_PER_100METRES =
        new TransformedUnit<>("cm/100m", CLDR.RADIAN,
                                         new AtanConverter(10000));

    public static final Unit<Energy> FOOT_POUND = 
        new TransformedUnit<>("ft-lb", CLDR.JOULE,
                                       MultiplyConverter.of(1.3558179483314004));

    private BCUnits() {}
}
