package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import gehtsoft.ballisticcalculator.data.Atmosphere;
import gehtsoft.ballisticcalculator.units.UnitUtils;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

import static org.assertj.core.api.Assertions.*;

class TestAtmosphere {
    @ParameterizedTest
    @CsvSource(value = { "59.0,29.95,0,1.2261",
                         "59.0,29.95,0.78,1.2201",
                         "75,31.07,0.78,1.2237",
                         "10,28,0.3,1.2656"
                       })
    void density(double temperature, double pressure, double humidity, double density) {
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
    void speedOfSound(double temperature, double pressure, double speedOfSound) {
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
    void pressureShift(double temperature, double pressure, double altitude, double expectedPressure) {
        Atmosphere atm = new Atmosphere(Quantities.getQuantity(altitude, SI.METRE), 
                                        Quantities.getQuantity(pressure, CLDR.PASCAL), 
                                        true,
                                        Quantities.getQuantity(temperature, CLDR.CELSIUS), 0);
            assertThat(UnitUtils.in(atm.getPressure(), SI.PASCAL))
                      .isEqualTo(expectedPressure, within(5e-2));                   
    }

    @Test
    void densityCalculation() {
      Atmosphere atm = new Atmosphere();
      var t = atm.getTemperatureAtAltitude(atm.getAltitude());
      assertThat(UnitUtils.compare(atm.getTemperature(), 
                                   Quantities.getQuantity(t, SI.KELVIN))).isZero();

      t = atm.getTemperatureAtAltitude(Quantities.getQuantity(1000, SI.METRE));       
      var delta = t - UnitUtils.in(atm.getTemperature(), SI.KELVIN);
      assertThat(delta).isEqualTo(-6.5, within(0.1));

      var ss = atm.getSpeedOfSound(t);
      assertThat(UnitUtils.in(ss, SI.METRE_PER_SECOND))
              .isEqualTo(336.2, within(0.1));

      var df = atm.getDensityFactorForTemperature(Quantities.getQuantity(1000, SI.METRE), t);
      assertThat(df).isEqualTo(0.83370452858203414996288047512992, within(0.1));
    }

    @Test
    void speedOfSoundAt100Feet() {
        Atmosphere atm = new Atmosphere(Quantities.getQuantity(30.48, SI.METRE), 
                                        Quantities.getQuantity(29.92, CLDR.INCH_HG), 
                                        false,
                                        Quantities.getQuantity(59.0, CLDR.FAHRENHEIT), 0);
        assertThat(UnitUtils.in(atm.getSpeedOfSound(), SI.METRE_PER_SECOND))
                .isEqualTo(340.0, within(9e-1));
    }

}
