package gehtsoft.ballisticcalculator.Units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.*;


import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

public class TestUnitUtils {
    @Test
    public void convert() {
        var v = Quantities.getQuantity(1, CLDR.INCH);
        var v2 = UnitUtils.convert(v, SI.METRE);
        assertThat(v2.getValue().doubleValue()).isEqualTo(0.0254);
        assertThat(v2.getUnit()).isEqualTo(SI.METRE);
    }

    @ParameterizedTest
    @CsvSource(value = { "1.1, 1.1, 0",
                         "1.0, 1.1, -1",
                         "1.1, 1.0, 1",
                       })
    public void CompareSameDimension(double n1, double n2, int expectedResult) {
        var v1 = Quantities.getQuantity(n1, CLDR.INCH);
        var v2 = Quantities.getQuantity(n2, CLDR.INCH);
        assertThat(UnitUtils.compare(v1, v2)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource(value = { "1, 0.0254, 0",
                         "2, 1, -1",
                         "2, 0.0254, 1",
                       })
    public void CompareDifferentDimensions(double n1, double n2, int expectedResult) {
        var v1 = Quantities.getQuantity(n1, CLDR.INCH);
        var v2 = Quantities.getQuantity(n2, CLDR.METER);
        assertThat(UnitUtils.compare(v1, v2)).isEqualTo(expectedResult);
    }
}
