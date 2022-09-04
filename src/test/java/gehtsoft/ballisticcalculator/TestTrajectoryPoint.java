package gehtsoft.ballisticcalculator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import si.uom.SI;
import systems.uom.unicode.*;
import tech.units.indriya.quantity.Quantities;

public class TestTrajectoryPoint {

    @Test
    public void calculatedFields() {
        TrajectoryPoint tp = new TrajectoryPoint(
            Quantities.getQuantity(32.0, BallisticSpecificUnits.GRAIN),
            Quantities.getQuantity(150, CLDR.YARD),
            Quantities.getQuantity(1059.7, BallisticSpecificUnits.FEET_PER_SECOND),
            0.95,
            Quantities.getQuantity(-9.88, CLDR.INCH),
            Quantities.getQuantity(-14.38, CLDR.INCH));           

        assertEquals(108.0,
                     tp.getEnergy().getUnit()
                          .getConverterTo(SI.JOULE)
                          .convert(tp.getEnergy().getValue()).doubleValue(), 5e-1);

        assertEquals(-6.29,
                    tp.getHold().getUnit()
                        .getConverterTo(BallisticSpecificUnits.MOA)
                        .convert(tp.getHold().getValue()).doubleValue(), 5e-3);

        assertEquals(-9.15,
                    tp.getWindageAdjustment().getUnit()
                        .getConverterTo(BallisticSpecificUnits.MOA)
                        .convert(tp.getWindageAdjustment().getValue()).doubleValue(), 5e-3);
    }
}
