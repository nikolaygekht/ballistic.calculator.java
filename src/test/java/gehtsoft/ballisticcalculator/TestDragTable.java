package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;

import gehtsoft.ballisticcalculator.Drag.DragTableId;
import gehtsoft.ballisticcalculator.Drag.StandardDragTableFactory;
import static org.assertj.core.api.Assertions.*;

public class TestDragTable {
    @Test
    public void FactoryReturnsTableWithCorrectId() {
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G1).getID())
            .isEqualTo(DragTableId.G1);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G2).getID())
            .isEqualTo(DragTableId.G2);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G5).getID())
            .isEqualTo(DragTableId.G5);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G6).getID())
            .isEqualTo(DragTableId.G6);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G7).getID())
            .isEqualTo(DragTableId.G7);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.G8).getID())
            .isEqualTo(DragTableId.G8);
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.GS).getID())
            .isEqualTo(DragTableId.GS); 
        assertThat(StandardDragTableFactory.getInstance().getTable(DragTableId.GI).getID())
            .isEqualTo(DragTableId.GI);
    }
}
