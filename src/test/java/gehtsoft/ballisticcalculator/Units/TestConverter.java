package gehtsoft.ballisticcalculator.Units;

import org.junit.jupiter.api.Test;

import tech.units.indriya.function.AbstractConverter;

import static org.assertj.core.api.Assertions.*;

public class TestConverter {
    @Test
    public void tan() {
        TanConverter converter = new TanConverter(1);
        assertThat(converter.convert(0.6283185307179586476)).isEqualTo(0.7265425280053608858, within(1e-10));
    }

    @Test
    public void atan() {
        AtanConverter converter = new AtanConverter(1);
        assertThat(converter.convert(0.7265425280053608858)).isEqualTo(0.6283185307179586476, within(1e-10));
    }

    @Test
    public void tan1() {
        TanConverter converter = new TanConverter(100);
        assertThat(converter.convert(0.6283185307179586476)).isEqualTo(72.65425280053608858, within(1e-10));
    }

    @Test
    public void atan1() {
        AtanConverter converter = new AtanConverter(100);
        assertThat(converter.convert(72.65425280053608858)).isEqualTo(0.6283185307179586476, within(1e-10));
    }

    @Test 
    void tanInversion() {
        TanConverter converter = new TanConverter(2);
        AbstractConverter inverse = converter.inverse();
        assertThat(inverse).isInstanceOf(AtanConverter.class);
        assertThat(((AtanConverter) inverse).getBaseLeg()).isEqualTo(2);
    }

    @Test 
    void atanInversion() {
        AtanConverter converter = new AtanConverter(2);
        AbstractConverter inverse = converter.inverse();
        assertThat(inverse).isInstanceOf(TanConverter.class);
        assertThat(((TanConverter) inverse).getBaseLeg()).isEqualTo(2);
    }
    
}
