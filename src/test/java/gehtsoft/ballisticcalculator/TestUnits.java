package gehtsoft.ballisticcalculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import si.uom.SI;
import systems.uom.unicode.CLDR;

public class TestUnits {
    @Test
    public void moa()
    {
        assertEquals(0.000290888208665722, 
                     BallisticSpecificUnits.MOA.getConverterTo(SI.RADIAN).convert(1), 1e-5);

        assertEquals(3437.74677078494,
                     SI.RADIAN
                    .getConverterTo(BallisticSpecificUnits.MOA).convert(1), 1e-5);
    }

    @Test
    public void mil()
    {
        assertEquals(0.0009817477042468104,
                     BallisticSpecificUnits.MIL.getConverterTo(SI.RADIAN).convert(1), 1e-5);

        assertEquals(6400, 
                     SI.RADIAN
                    .getConverterTo(BallisticSpecificUnits.MIL).convert(2 * Math.PI), 1e-5);
    }

    @Test
    public void mrad()
    {
        assertEquals(3.4377492368197,
                    BallisticSpecificUnits.MRad.getConverterTo(BallisticSpecificUnits.MOA).convert(1), 1e-5); 
    }

    @Test
    public void grain()
    {
        assertEquals(7000, 
                      CLDR.POUND.getConverterTo(BallisticSpecificUnits.GRAIN).convert(1), 1e-5);
    }
}
