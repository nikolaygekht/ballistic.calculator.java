package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import gehtsoft.ballisticcalculator.Data.BallisticCoefficient;
import gehtsoft.ballisticcalculator.Data.BallisticCoefficientValueType;
import gehtsoft.ballisticcalculator.Data.Projectile;
import gehtsoft.ballisticcalculator.Drag.DragTableId;
import gehtsoft.ballisticcalculator.Drag.DrgFile;
import tech.units.indriya.quantity.Quantities;



import java.io.IOException;

public class TestBallisticCoefficient {
    @Test
    public void defaultConstructor() {
        BallisticCoefficient bc = new BallisticCoefficient(0.521, DragTableId.G1);
        assertThat(bc.getValue()).isEqualTo(0.521);
        assertThat(bc.getType()).isEqualTo(BallisticCoefficientValueType.Coefficient);
        assertThat(bc.getDragTable().getID()).isEqualTo(DragTableId.G1);

        Projectile projectile = new Projectile(Quantities.getQuantity(52, BCUnits.GRAIN), 
                                               Quantities.getQuantity(3000, BCUnits.FEET_PER_SECOND), 
                                               bc);
        assertThat(projectile.getBallisticCoefficientValue()).isEqualTo(0.521);
    }

    @Test
    public void customTableConstructor() throws IOException {
        DrgFile table = TestDragTable.LoadDragTable("drg.txt");
        BallisticCoefficient bc = new BallisticCoefficient(1, 
                BallisticCoefficientValueType.FormFactor, 
                table);
        assertThat(bc.getValue()).isEqualTo(1);
        assertThat(bc.getType()).isEqualTo(BallisticCoefficientValueType.FormFactor);
        assertThat(bc.getDragTable().getID()).isEqualTo(DragTableId.GC);

        Projectile projectile = new Projectile(table.getBulletWeight(), 
                                               Quantities.getQuantity(3000, BCUnits.FEET_PER_SECOND), 
                                               bc, table.getBulletDiameter(), null);

        assertThat(projectile.getBallisticCoefficientValue()).isEqualTo(0.2482, within(0.00005));
    }



}
