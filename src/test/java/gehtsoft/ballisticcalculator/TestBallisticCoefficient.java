package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import gehtsoft.ballisticcalculator.data.BallisticCoefficient;
import gehtsoft.ballisticcalculator.data.BallisticCoefficientValueType;
import gehtsoft.ballisticcalculator.data.Projectile;
import gehtsoft.ballisticcalculator.drag.DragTableId;
import gehtsoft.ballisticcalculator.drag.DrgFile;
import gehtsoft.ballisticcalculator.units.BCUnits;
import tech.units.indriya.quantity.Quantities;



import java.io.IOException;

class TestBallisticCoefficient {
    @Test
    void defaultConstructor() {
        BallisticCoefficient bc = new BallisticCoefficient(0.521, DragTableId.G1);
        assertThat(bc.getValue()).isEqualTo(0.521);
        assertThat(bc.getType()).isEqualTo(BallisticCoefficientValueType.COEFFICIENT);
        assertThat(bc.getDragTable().getID()).isEqualTo(DragTableId.G1);

        Projectile projectile = new Projectile(Quantities.getQuantity(52, BCUnits.GRAIN), 
                                               Quantities.getQuantity(3000, BCUnits.FEET_PER_SECOND), 
                                               bc);
        assertThat(projectile.getBallisticCoefficientValue()).isEqualTo(0.521);
    }

    @Test
    void customTableConstructor() throws IOException {
        DrgFile table = DrgFileLoader.loadDragTable("drg.txt");
        BallisticCoefficient bc = new BallisticCoefficient(1, 
                BallisticCoefficientValueType.FORMFACTOR, 
                table);
        assertThat(bc.getValue()).isEqualTo(1);
        assertThat(bc.getType()).isEqualTo(BallisticCoefficientValueType.FORMFACTOR);
        assertThat(bc.getDragTable().getID()).isEqualTo(DragTableId.GC);

        Projectile projectile = new Projectile(table.getBulletWeight(), 
                                               Quantities.getQuantity(3000, BCUnits.FEET_PER_SECOND), 
                                               bc, table.getBulletDiameter(), null);

        assertThat(projectile.getBallisticCoefficientValue()).isEqualTo(0.2482, within(0.00005));
    }
}
