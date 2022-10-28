package gehtsoft.ballisticcalculator.units;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


import si.uom.SI;
import systems.uom.unicode.CLDR;

class TestUnits {
    @Test
    void moa()
    {
        assertThat(UnitUtils.convert(BCUnits.MOA, SI.RADIAN, 1))
            .isEqualTo(0.000290888208665722, within(1e-5));

        assertThat(UnitUtils.convert(SI.RADIAN, BCUnits.MOA, 1))
            .isEqualTo(3437.74677078494, within(1e-5));
    }

    @Test
    void mil()
    {
        assertThat(BCUnits.MIL.getConverterTo(SI.RADIAN).convert(1))
            .isEqualTo(0.0009817477042468104, within(1e-5));

        assertThat(SI.RADIAN.getConverterTo(BCUnits.MIL).convert(2 * Math.PI))
            .isEqualTo(6400, within(1e-5));
    }

    @Test
    void mrad()
    {
        assertThat(BCUnits.MRAD.getConverterTo(BCUnits.MOA).convert(1))
            .isEqualTo(3.4377492368197, within(1e-5));
    }

    @Test
    void inchesPer100Yd()
    {
        assertThat(BCUnits.MRAD.getConverterTo(BCUnits.INCHES_PER_100YARDS).convert(1))
            .isEqualTo(3.6, within(1e-5));

        assertThat(BCUnits.INCHES_PER_100YARDS.getConverterTo(BCUnits.MRAD).convert(3.6))
            .isEqualTo(1, within(1e-5));
    }

    @Test
    void cmPer100Meters()
    {
        assertThat(BCUnits.MIL.getConverterTo(BCUnits.CENTIMETERS_PER_100METRES).convert(1))
            .isEqualTo(9.817480196, within(1e-5));
    }

    @Test
    void grain()
    {
        assertThat(CLDR.POUND.getConverterTo(BCUnits.GRAIN).convert(1))
            .isEqualTo(7000, within(1e-5));
        
            assertThat(BCUnits.GRAIN.getConverterTo(SI.GRAM).convert(12))
            .isEqualTo(0.777587, within(1e-5));
    }

    @Test
    void fps()
    {
        assertThat(SI.METRE_PER_SECOND.getConverterTo(BCUnits.FEET_PER_SECOND).convert(290))
            .isEqualTo(951.444, within(1e-3));
    }

    @Test
    void ftlb()
    {
        assertThat(SI.JOULE.getConverterTo(BCUnits.FOOT_POUND).convert(5.5))
            .isEqualTo(4.056591821024985, within(1e-6));
    }
}
