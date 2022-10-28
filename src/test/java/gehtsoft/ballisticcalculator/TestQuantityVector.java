package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;

import gehtsoft.ballisticcalculator.units.UnitUtils;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;
import javax.measure.*;
import javax.measure.quantity.*;

class TestQuantityVector {
    @Test
    void Constructor() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));

        assertThat(UnitUtils.compare(v.getX(), Metres(1))).isZero();
        assertThat(UnitUtils.compare(v.getY(), Metres(2))).isZero();
        assertThat(UnitUtils.compare(v.getZ(), Metres(3))).isZero();
    }    

    @Test 
    void getMagnitude() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        assertThat(UnitUtils.compare(v.getMagnitude(), Metres(3.7416573867739413))).isZero();
    }

    @Test
    void multiply() {
        var v = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        v = v.multiply(2);
        assertThat(UnitUtils.compare(v.getX(), Metres(2))).isZero();
        assertThat(UnitUtils.compare(v.getY(), Metres(4))).isZero();
        assertThat(UnitUtils.compare(v.getZ(), Metres(6))).isZero();
    }

    @Test
    void add_sameDimension() {
        var v1 = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        var v2 = new QuantityVector<Length>(Metres(2), 
                                           Metres(3), 
                                           Metres(4));
        var v = v1.add(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(3))).isZero();
        assertThat(UnitUtils.compare(v.getY(), Metres(5))).isZero();
        assertThat(UnitUtils.compare(v.getZ(), Metres(7))).isZero();
    }

    @Test
    void add_differentDimensions() {
        var v1 = new QuantityVector<Length>(Metres(1), 
                                           Metres(2), 
                                           Metres(3));
        var v2 = new QuantityVector<Length>(Inches(2), 
                                            Inches(3), 
                                            Inches(4));
        var v = v1.add(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(1 + 0.0254 * 2))).isZero();
        assertThat(UnitUtils.compare(v.getY(), Metres(2 + 0.0254 * 3))).isZero();
        assertThat(UnitUtils.compare(v.getZ(), Metres(3 + 0.0254 * 4))).isZero();
    }

    @Test
    void subtract() {
        var v1 = new QuantityVector<Length>(Metres(5), 
                                           Metres(6), 
                                           Metres(7));
        var v2 = new QuantityVector<Length>(Metres(2), 
                                           Metres(8), 
                                           Metres(5));
        var v = v1.subtract(v2);
        assertThat(UnitUtils.compare(v.getX(), Metres(3))).isZero();
        assertThat(UnitUtils.compare(v.getY(), Metres(-2))).isZero();
        assertThat(UnitUtils.compare(v.getZ(), Metres(2))).isZero();
    }

    private static Quantity<Length> Metres(double value) {
        return Quantities.getQuantity(value, SI.METRE);
    }

    private static Quantity<Length> Inches(double value) {
        return Quantities.getQuantity(value, CLDR.INCH);
    }
   

}
