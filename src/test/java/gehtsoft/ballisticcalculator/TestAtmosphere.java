package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import gehtsoft.ballisticcalculator.Data.Atmosphere;
import gehtsoft.ballisticcalculator.Units.UnitUtils;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;

public class TestAtmosphere {
    @ParameterizedTest
    @CsvSource(value = { "59.0,29.95,0,1.2261",
                         "59.0,29.95,0.78,1.2201",
                         "75,31.07,0.78,1.2237",
                         "10,28,0.3,1.2656"
                       })
    public void density(double temperature, double pressure, double humidity, double density) {
        Atmosphere atm = new Atmosphere(Quantities.getQuantity(0, SI.METRE), 
                                        Quantities.getQuantity(pressure, CLDR.INCH_HG), 
                                        false,
                                        Quantities.getQuantity(temperature, CLDR.FAHRENHEIT), humidity);
        assertThat(atm.getDensity()).isEqualTo(density, within(9e-5));
    }

    @ParameterizedTest
    @CsvSource(value = { "32.0,29.95,331",
                         "59.0,29.95,340",
                       })
    public void speedOfSound(double temperature, double pressure, double speedOfSound) {
        Atmosphere atm = new Atmosphere(Quantities.getQuantity(0, SI.METRE), 
                                        Quantities.getQuantity(pressure, CLDR.INCH_HG), 
                                        false,
                                        Quantities.getQuantity(temperature, CLDR.FAHRENHEIT), 0);

        assertThat(UnitUtils.in(atm.getSpeedOfSound(), SI.METRE_PER_SECOND))
                .isEqualTo(speedOfSound, within(9e-1));
    }

    @ParameterizedTest
    @CsvSource(value = { "15, 101325, 1000, 89874.57",
                         "25, 101325, 200, 99024.40",
                       })
    public void pressureShift(double temperature, double pressure, double altitude, double expectedPressure) {
        Atmosphere atm = new Atmosphere(Quantities.getQuantity(altitude, SI.METRE), 
                                        Quantities.getQuantity(pressure, CLDR.PASCAL), 
                                        true,
                                        Quantities.getQuantity(temperature, CLDR.CELSIUS), 0);
            assertThat(UnitUtils.in(atm.getPressure(), SI.PASCAL))
                      .isEqualTo(expectedPressure, within(5e-2));                   
    }
}