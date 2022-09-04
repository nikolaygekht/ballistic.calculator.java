package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;

import gehtsoft.ballisticcalculator.Drag.*;
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

    @Test
    public void TestNodes() {
        IDragTable table = StandardDragTableFactory.getInstance().getTable(DragTableId.G1);
        
        assertThat(table.length()).isGreaterThan(10);
        for (int i = 0; i < table.length(); i++) {
            IDragTableNode node = table.get(i);
            IDragTableNode node1 = table.find(node.getMach());
            assertThat(node1).isSameAs(node);
            assertThat(node.calculateDrag(node.getMach())).isEqualTo(node.getDragCoefficient(), within(1e-7));

            IDragTableNode next = node.getNext();
            if (next == null)
            {
                assertThat(node.in(node.getMach() - 0.01)).isFalse();
                assertThat(node.in(node.getMach())).isTrue();
                assertThat(node.in(10)).isTrue();
            }
            else
            {
                assertThat(node.in(node.getMach() - 0.01)).isFalse();
                assertThat(node.in(node.getMach())).isTrue();
                assertThat(node.in(next.getMach() - 0.01)).isTrue();
                assertThat(node.in(next.getMach())).isFalse();               
            }

            if (node.getMach() < 0.0001)
            {
                assertThat(node.getPrevious()).isNull();
                assertThat(node.in(0.000001)).isTrue();
                assertThat(node.calculateDrag(node.getMach() / 2)).isEqualTo(node.getDragCoefficient(), within(1e-7));
            }
        }
    }
}
