package gehtsoft.ballisticcalculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import gehtsoft.ballisticcalculator.Data.*;
import gehtsoft.ballisticcalculator.Drag.*;
import gehtsoft.ballisticcalculator.Units.*;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;

public class TestTrajectoryCalculator {
    @ParameterizedTest
    @CsvSource(value = { 
        "0.365, G1, 2600, fps, 100, yd, 5.674",
        "0.365, G1, 2600, fps, 25, yd, 12.84",
        "0.365, G1, 2600, fps, 375, yd, 12.78",
        "0.47, G7, 725, ms, 200, m, 8.171"
    })
    public void zeroAngle(double bcValue, String table, double speed, String speedUnits, double zeroDistance, String distanceUnits, double moaSightAngle) {
        var id = table.trim().compareTo("G7") == 0 ? DragTableId.G7 : DragTableId.G1;
        var bc = new BallisticCoefficient(bcValue, id);
        var mvu = speedUnits.trim().compareTo("ms") == 0 ? SI.METRE_PER_SECOND : BCUnits.FEET_PER_SECOND;
        var mv = Quantities.getQuantity(speed, mvu);
        var du = distanceUnits.trim().compareTo("m") == 0 ? SI.METRE : CLDR.YARD;
        var zd = Quantities.getQuantity(zeroDistance, du);
        var sh = Quantities.getQuantity(3.2, CLDR.INCH);
        var ammo = new Projectile(Quantities.getQuantity(8, SI.GRAM), mv, bc);
        var rifle = new Weapon(new ZeroingInformation(sh, zd), null);
        var atmosphere = new Atmosphere();

        var tc = new TrajectoryCalculator();
        var sa = tc.calculateSightAngle(ammo, rifle, atmosphere);
        assertThat(UnitUtils.in(sa, BCUnits.MOA)).isEqualTo(moaSightAngle, within(0.05));
    }
}
