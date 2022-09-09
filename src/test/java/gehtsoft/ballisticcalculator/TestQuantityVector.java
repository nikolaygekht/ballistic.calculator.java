package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;

import gehtsoft.ballisticcalculator.Units.UnitUtils;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;
import javax.measure.*;
import javax.measure.quantity.*;

public class TestQuantityVector {
    @Test
    public void Constructor() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));

        assertThat(UnitUtils.compare(v.getX(), Metres(1))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getY(), Metres(2))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getZ(), Metres(3))).isEqualTo(0);
    }    

    @Test 
    public void getMagnitude() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        assertThat(UnitUtils.compare(v.getMagnitude(), Metres(3.7416573867739413))).isEqualTo(0);
    }

    @Test
    public void multiply() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        v = v.multiply(2);
        assertThat(UnitUtils.compare(v.getX(), Metres(2))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getY(), Metres(4))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getZ(), Metres(6))).isEqualTo(0);
    }

    @Test
    public void add_sameDimension() {
        var v1 = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        var v2 = new QuantityVector<Length>(Metres(2), 
                                           Metres(3), 
                                           Metres(4));
        var v = v1.add(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(3))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getY(), Metres(5))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getZ(), Metres(7))).isEqualTo(0);
    }

    @Test
    public void add_differentDimensions() {
        var v1 = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        var v2 = new QuantityVector<Length>(Inches(2), 
                                            Inches(3), 
                                            Inches(4));
        var v = v1.add(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(1 + 0.0254 * 2))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getY(), Metres(2 + 0.0254 * 3))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getZ(), Metres(3 + 0.0254 * 4))).isEqualTo(0);
    }

    @Test
    public void subtract() {
        var v1 = new QuantityVector<Length>(Metres(5), 
                                           Metres(6), 
                                           Metres(7));
        var v2 = new QuantityVector<Length>(Metres(2), 
                                           Metres(8), 
                                           Metres(5));
        var v = v1.subtract(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(3))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getY(), Metres(-2))).isEqualTo(0);
        assertThat(UnitUtils.compare(v.getZ(), Metres(2))).isEqualTo(0);
    }

    private static Quantity<Length> Metres(double value) {
        return Quantities.getQuantity(value, SI.METRE);
    }

    private static Quantity<Length> Inches(double value) {
        return Quantities.getQuantity(value, CLDR.INCH);
    }
   

}
