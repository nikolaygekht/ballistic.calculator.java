package gehtsoft.ballisticcalculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import gehtsoft.ballisticcalculator.Data.*;
import gehtsoft.ballisticcalculator.Drag.*;
import gehtsoft.ballisticcalculator.Tools.TrajectoryLoader;
import gehtsoft.ballisticcalculator.Units.*;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

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

    private void assertThatTrajectoryPointsMatch(TrajectoryPoint value, TrajectoryPoint expected, double moaAccuracy, double velocityAccuracy, double energyAccuracy, double timeAccuracy, Boolean ignoreMach) {
        assertThat(UnitUtils.in(value.getDistance(), expected.getDistance().getUnit()))
            .isEqualTo(expected.getDistance().getValue().doubleValue(), within(0.5));

        assertThat(UnitUtils.in(value.getVelocity(), expected.getVelocity().getUnit()))
            .isEqualTo(expected.getVelocity().getValue().doubleValue(), within(velocityAccuracy));            

        if (!ignoreMach)
            assertThat(value.getMach()).isEqualTo(expected.getMach(), within(0.005));

        assertThat(UnitUtils.in(value.getFlightTime(), SI.SECOND))
            .isEqualTo(UnitUtils.in(expected.getFlightTime(), SI.SECOND), within(timeAccuracy));            

        if (energyAccuracy != 0) {
            var ev = UnitUtils.in(value.getEnergy(), expected.getEnergy().getUnit());
            assertThat(UnitUtils.in(value.getEnergy(), expected.getEnergy().getUnit()))
                .isEqualTo(expected.getEnergy().getValue().doubleValue(), within(ev * energyAccuracy / 100));
        }

        var toleranceForDrop = UnitUtils.in(Quantities.getQuantity(moaAccuracy, BCUnits.MOA), BCUnits.INCHES_PER_100YARDS) * 
                               (UnitUtils.in(value.getDistance(), CLDR.YARD) / 100);

        if (toleranceForDrop < 1e-7)
            toleranceForDrop = 1e-7;

        assertThat(UnitUtils.in(value.getDrop(), CLDR.INCH))
            .isEqualTo(UnitUtils.in(expected.getDrop(), CLDR.INCH), within(toleranceForDrop));

        assertThat(UnitUtils.in(value.getWindage(), CLDR.INCH))
            .isEqualTo(UnitUtils.in(expected.getWindage(), CLDR.INCH), within(toleranceForDrop));
    }

    @ParameterizedTest
    @CsvSource(value = { 
        "g1_nowind.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "g1_wind.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "g1_wind_cold.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "g1_wind_hot.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "g1_twist.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "g7_nowind.txt, 0.1, 0.5, 1, 0.005, false,  ",
        "custom.txt, 0.75, 10, 0, 0.05, true, drg.txt",
        "custom2.txt, 0.75, 10, 0, 0.05, true, drg2.txt",
    })
    public void trajectory(String testFile, double moaAccuracy, double velocityAccuracy, double energyAccuracy, double timeAccuracy, Boolean ignoreMach, String customDragTableName) 
        throws IOException {
        DrgFile customDrg = null;
        if (customDragTableName != null) {
            customDrg = DrgFileLoader.loadDragTable(customDragTableName);
        }
        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load(testFile, customDrg);
        var expectedTrajectory = loader.getTrajectory();
        var maxDistance = expectedTrajectory.get(expectedTrajectory.size() - 1).getDistance();
        var pt0 = expectedTrajectory.get(0);
        var pt1 = expectedTrajectory.get(1);
        var step = Quantities.getQuantity(pt1.getDistance().getValue().doubleValue() - pt0.getDistance().getValue().doubleValue(), pt0.getDistance().getUnit());

        var tc = new TrajectoryCalculator();
        var sa = tc.calculateSightAngle(loader.getProjectile(), loader.getWeapon(), loader.getAtmosphere());

        var shot = new ShotParameters(maxDistance, step, sa);
        var trajectory = tc.calculate(loader.getProjectile(), loader.getWeapon(), loader.getAtmosphere(), shot, loader.getWind());

        assertThat(trajectory.length).isEqualTo(expectedTrajectory.size());

        for (int i = 0; i < trajectory.length; i++) {
            var point = trajectory[i];
            var expectedPoint = expectedTrajectory.get(i);
            assertThat(point).isNotNull();
            assertThat(expectedPoint).isNotNull();
            assertThatTrajectoryPointsMatch(point, expectedPoint, moaAccuracy, velocityAccuracy, energyAccuracy, timeAccuracy, ignoreMach);
        }
    }
}
