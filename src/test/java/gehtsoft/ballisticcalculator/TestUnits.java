package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


import si.uom.SI;
import systems.uom.unicode.CLDR;

public class TestUnits {
    @Test
    public void moa()
    {
        assertThat(UnitUtils.convert(BCUnits.MOA, SI.RADIAN, 1))
            .isEqualTo(0.000290888208665722, within(1e-5));

        assertThat(UnitUtils.convert(SI.RADIAN, BCUnits.MOA, 1))
            .isEqualTo(3437.74677078494, within(1e-5));
    }

    @Test
    public void mil()
    {
        assertThat(BCUnits.MIL.getConverterTo(SI.RADIAN).convert(1))
            .isEqualTo(0.0009817477042468104, within(1e-5));

        assertThat(SI.RADIAN.getConverterTo(BCUnits.MIL).convert(2 * Math.PI))
            .isEqualTo(6400, within(1e-5));

    }

    @Test
    public void mrad()
    {
        assertThat(BCUnits.MRad.getConverterTo(BCUnits.MOA).convert(1))
            .isEqualTo(3.4377492368197, within(1e-5));
    }

    @Test
    public void grain()
    {
        assertThat(CLDR.POUND.getConverterTo(BCUnits.GRAIN).convert(1))
            .isEqualTo(7000, within(1e-5));
        assertThat(BCUnits.GRAIN.getConverterTo(SI.GRAM).convert(12))
            .isEqualTo(0.777587, within(1e-5));
    }
}
