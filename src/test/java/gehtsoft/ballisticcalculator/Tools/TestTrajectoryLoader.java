package gehtsoft.ballisticcalculator.Tools;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import gehtsoft.ballisticcalculator.DrgFileLoader;
import gehtsoft.ballisticcalculator.Data.BallisticCoefficientValueType;
import gehtsoft.ballisticcalculator.Drag.DragTableId;
import gehtsoft.ballisticcalculator.Drag.DrgFile;
import gehtsoft.ballisticcalculator.Units.BCUnits;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;

public class TestTrajectoryLoader {
    @ParameterizedTest
    @CsvSource(value = { "g1_twist.txt, none, 21",
                         "g1_nowind.txt, none, 21",
                         "custom2.txt, drg2.txt, 16",
                        })
    public void loads(String name, String tableName, int count) throws IOException {
        DrgFile table = null;
        name = name.trim();
        tableName = tableName.trim();
        if (!tableName.equals("none")) {
            table = DrgFileLoader.loadDragTable(tableName);
        }

        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load(name, table);
        assertThat(loader.getProjectile()).isNotNull();
        assertThat(loader.getAtmosphere()).isNotNull();
        assertThat(loader.getWeapon()).isNotNull();
        assertThat(loader.getWind()).isNotNull();
        
        assertThat(loader.getTrajectory())
            .isNotNull();
        assertThat(loader.getTrajectory().size())
            .isEqualTo(count);
    }

    @Test
    public void loadAmmoStandardTable() throws IOException {
        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load("g1_twist.txt", null);
        var projectile = loader.getProjectile();
        assertThat(projectile.getBallisticCoefficient()).isNotNull();
        assertThat(projectile.getBallisticCoefficient()
                    .getType()).isEqualTo(BallisticCoefficientValueType.Coefficient);
        assertThat(projectile.getBallisticCoefficient()
                    .getValue()).isEqualTo(0.5);
        assertThat(projectile.getBallisticCoefficient()
                    .getDragTable().getID()).isEqualTo(DragTableId.G1);
        assertThat(projectile.getBulletDiameter())
                    .isEqualTo(Quantities.getQuantity(0.308, CLDR.INCH));
        assertThat(projectile.getBulletLength())
                    .isEqualTo(Quantities.getQuantity(1.5, CLDR.INCH));
        assertThat(projectile.getMuzzleVelocity())
                    .isEqualTo(Quantities.getQuantity(3006.6, BCUnits.FEET_PER_SECOND));
        assertThat(projectile.getBulletWeight())
                    .isEqualTo(Quantities.getQuantity(220, BCUnits.GRAIN));
    }

    @Test
    public void loadAmmoCustomTable() throws IOException {
        DrgFile drgFile = DrgFileLoader.loadDragTable("drg2.txt");
        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load("custom2.txt", drgFile);
        var projectile = loader.getProjectile();
        assertThat(projectile.getBallisticCoefficient()).isNotNull();
        assertThat(projectile.getBallisticCoefficient()
                    .getType()).isEqualTo(BallisticCoefficientValueType.FormFactor);
        assertThat(projectile.getBallisticCoefficient()
                    .getValue()).isEqualTo(1);
        assertThat(projectile.getBallisticCoefficient()
                    .getDragTable()).isEqualTo(drgFile);
        assertThat(projectile.getBulletDiameter())
                    .isEqualTo(Quantities.getQuantity(119.56, CLDR.MILLIMETER));
        assertThat(projectile.getBulletLength())
                    .isEqualTo(Quantities.getQuantity(20, CLDR.INCH));
        assertThat(projectile.getMuzzleVelocity())
                    .isEqualTo(Quantities.getQuantity(555, SI.METRE_PER_SECOND));
        assertThat(projectile.getBulletWeight())
                    .isEqualTo(Quantities.getQuantity(13585, SI.GRAM));                                        
    }

    @Test
    public void loadsTrajectory() throws IOException {
        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load("g1_twist.txt", null);
        var trajectory = loader.getTrajectory();
        assertThat(trajectory).isNotNull();
        assertThat(trajectory.size()).isEqualTo(21);

        var point = trajectory.get(0);
        assertThat(point.getDistance()).isEqualTo(Quantities.getQuantity(0, CLDR.YARD));
        assertThat(point.getVelocity()).isEqualTo(Quantities.getQuantity(3006.6, BCUnits.FEET_PER_SECOND));
        assertThat(point.getMach()).isEqualTo(2.693);
        assertThat(point.getFlightTime()).isEqualTo(Quantities.getQuantity(0, SI.SECOND));
        assertThat(point.getDrop()).isEqualTo(Quantities.getQuantity(-1.5, CLDR.INCH));
        assertThat(point.getWindage()).isEqualTo(Quantities.getQuantity(0, CLDR.INCH));
        assertThat(point.getEnergy()).isEqualTo(Quantities.getQuantity(4415, BCUnits.FOOT_POUND));


        point = trajectory.get(4);
        assertThat(point.getDistance()).isEqualTo(Quantities.getQuantity(200, CLDR.YARD));
        assertThat(point.getVelocity()).isEqualTo(Quantities.getQuantity(2629.2, BCUnits.FEET_PER_SECOND));
        assertThat(point.getMach()).isEqualTo(2.355);
        assertThat(point.getFlightTime()).isEqualTo(Quantities.getQuantity(0.213, SI.SECOND));
        assertThat(point.getDrop()).isEqualTo(Quantities.getQuantity(-2.9, CLDR.INCH));
        assertThat(point.getWindage()).isEqualTo(Quantities.getQuantity(0.2, CLDR.INCH));
        assertThat(point.getEnergy()).isEqualTo(Quantities.getQuantity(3376.2, BCUnits.FOOT_POUND));
    }
}
