package gehtsoft.ballisticcalculator;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import gehtsoft.ballisticcalculator.units.*;
import si.uom.SI;
import systems.uom.unicode.*;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

class TestTrajectoryPoint {

    @Test
    void calculatedFields() {
        TrajectoryPoint tp = new TrajectoryPoint(
            Quantities.getQuantity(32.0, BCUnits.GRAIN),
            Quantities.getQuantity(150, CLDR.YARD),
            Quantities.getQuantity(1059.7, BCUnits.FEET_PER_SECOND),
            0.95,
            Quantities.getQuantity(-9.88, CLDR.INCH),
            Quantities.getQuantity(-14.38, CLDR.INCH),
            Quantities.getQuantity(0, Units.SECOND));

        assertThat(UnitUtils.in(tp.getEnergy(), SI.JOULE))
            .isEqualTo(108.0, within(5e-1));

        assertThat(UnitUtils.in(tp.getHold(), BCUnits.MOA))
            .isEqualTo(-6.29, within(5e-3));

        assertThat(UnitUtils.in(tp.getWindageAdjustment(), BCUnits.MOA))
            .isEqualTo(-9.15, within(5e-3));
    }
}
