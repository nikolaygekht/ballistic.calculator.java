package gehtsoft.ballisticcalculator.Data;

import javax.measure.Quantity;
import javax.measure.quantity.*;

import gehtsoft.ballisticcalculator.Units.UnitUtils;
import si.uom.SI;
import tech.units.indriya.quantity.Quantities;

/** The atmosphere data */
public class Atmosphere {
    private Quantity<Length> mAltitude;
    private Quantity<Pressure> mPressure;
    private Quantity<Temperature> mTemperature;
    private double mHumidity;

    private Quantity<Speed> mSpeedOfSound;
    private double mDensity; //in kb/m^3

    /** Get altitude */
    public Quantity<Length> getAltitude() {
        return mAltitude;
    }

    /** Gets pressure */
    public Quantity<Pressure> getPressure() {
        return mPressure;
    }

    /** Gets temperature */
    public Quantity<Temperature> getTemperature() {
        return mTemperature;
    }

    /** Gets humidity */
    public double getHumidity() {
        return mHumidity;
    }

    /** Gets speed of the sound for the atmosphere */
    public Quantity<Speed> getSpeedOfSound() {
        return mSpeedOfSound;
    }

    /** Gets density of the atmosphere
     * 
     * The value is in kg/m^3 
     */
    public double getDensity() {
        return mDensity;
    }

    /** Constructor 
     * @param altitude - altitude
     * @param pressure - pressure
     * @param pressureAtSeaLevel - the flag indicating whether the pressure value is set at the sea level
     * @param temperature - temperature
     * @param humidity - humidity (0 to 1 range)
    */
    public Atmosphere(Quantity<Length> altitude, Quantity<Pressure> pressure, Boolean pressureAtSeaLevel, Quantity<Temperature> temperature, double humidity) {
        mAltitude = altitude;
        if (pressureAtSeaLevel) {

            mPressure = Quantities.getQuantity(
                calculatePressure(UnitUtils.in(pressure, SI.PASCAL), 
                                  UnitUtils.in(temperature, SI.KELVIN), 
                                  0,  
                                  UnitUtils.in(altitude, SI.METRE)), SI.PASCAL);
        } else {
            mPressure = pressure;
        }
        mTemperature = temperature;
        mHumidity = humidity;

        mSpeedOfSound = getSpeedOfSound(UnitUtils.in(temperature, SI.KELVIN));
        mDensity = calculateDensity(UnitUtils.in(temperature, SI.KELVIN), UnitUtils.in(pressure, SI.PASCAL), humidity);
    }

    /** Standard atmosphere density.
     * 
     * The value is expressed in in kg/m^3 
     */
    public static final double STANDARD_DENSITY = 0.076474; 

    // https://www.omnicalculator.com/physics/air-density
    // http://www.emd.dk/files/windpro/WindPRO_AirDensity.pdf
    //base value for saturated vapor pressure
    private static final double ES0 = 6.1078;
    //Herman Wobus polynomial coefficient to calculate saturated vapor pressure
    private static final double SVP_C0 = 0.99999683;
    private static final double SVP_C1 = -0.90826951e-2;
    private static final double SVP_C2 = 0.78736169e-4;
    private static final double SVP_C3 = -0.61117958e-6;
    private static final double SVP_C4 = 0.43884187e-8;
    private static final double SVP_C5 = -0.29883885e-10;
    private static final double SVP_C6 = 0.21874425e-12;
    private static final double DRY_AIR_K = 287.058;
    private static final double VAPOR_K = 461.495;

    // Calculates 100% saturated vapor pressure for the specified temperature
    // @param t Temperature in degree of Celsius
    private static double calculateSaturatedVapourPressure(double t) {
        double pt = SVP_C0 + t * (SVP_C1 + t * (SVP_C2 + t * (SVP_C3 + t * (SVP_C4 + t * (SVP_C5 + t * SVP_C6)))));
        return ES0 / Math.pow(pt, 8);
    }

    // Calculate density of atmosphere in given conditions
    // @param temperature Temperature in Kelvin
    // @param pressure Pressure in pascals
    // @param humidity Relative humidity (0 to 1)
    // @returns The density in kg/m3
    private static double calculateDensity(double temperature, double pressure, double humidity) {
        double t = temperature - 273.15;
        double tk = temperature;

        double vaporSaturation = calculateSaturatedVapourPressure(t) * 100;
        double actualVapourPressure = vaporSaturation * humidity;
        double dryPressure = pressure - actualVapourPressure;

        double density = dryPressure / (DRY_AIR_K * tk) + actualVapourPressure / (VAPOR_K * tk);
        return density;
    }

    // https://www.grc.nasa.gov/www/BGH/isentrop.html
    // Calculates velocity of the sound for the given temperature
    // @param temperature The temperature in Kelvins
    // @returns The speed of sound in m/s
    private double calculateSoundVelocity(double temperature){
        return 331 * Math.sqrt(temperature / 273);
    }

    // https://www.mide.com/air-pressure-at-altitude-calculator
    private static final double TEMPERATURE_LAPSE = -0.0065;
    private static final double GAS_CONSTANT = 8.31432;
    private static final double G_CONSTANT = 9.80665;
    private static final double AIR_MOLAR_MASS = 0.0289644;

    //Calculate the pressure on the specified altitude with known pressure on the other altitude
    //
    // @param basePressure Pressure at base level (pascal)
    // @param baseTemperature Temperature at base level (kelvin)
    // @param baseAltitude Height of the base level (meter)
    // @param altitude Altitude to calculate (meter)
    // @return The pressure at the specified altitude in pascals
    private static double calculatePressure(double basePressure, double baseTemperature, double baseAltitude, double altitude) {
        final double exponent = -G_CONSTANT * AIR_MOLAR_MASS / (GAS_CONSTANT * TEMPERATURE_LAPSE);
        return basePressure * Math.pow(1 + TEMPERATURE_LAPSE / baseTemperature * (altitude - baseAltitude), exponent);
    }

    // Calculate temperature at different altitude
    //
    // @param baseTemperature Base temperature in kelvins
    // @param baseAltitude Base altitude
    // @param altitude Altitude to calculate
    // @return The temperature in kelvins
    private static double calculateTemperature(double baseTemperature, double baseAltitude, double altitude) {
        return baseTemperature + TEMPERATURE_LAPSE * (altitude - baseAltitude);
    }

    /** Returns temperature at altitude in Kelvins */
    public double getTemperatureAtAltitude(Quantity<Length> altitude) {
        double a = UnitUtils.in(altitude, SI.METRE);
        var t = calculateTemperature(UnitUtils.in(mTemperature, SI.KELVIN), UnitUtils.in(mAltitude, SI.METRE), a);
        return t;
    }

    /** Returns density factor for the specified altitude and temperature at altitude */
    public double getDensityFactorForTemperature(Quantity<Length> altitude, double t) {
        var p = calculatePressure(UnitUtils.in(mPressure, SI.PASCAL),
                                  UnitUtils.in(mTemperature, SI.KELVIN),
                                  UnitUtils.in(mAltitude, SI.METRE),
                                  UnitUtils.in(altitude, SI.METRE));
        var d = calculateDensity(t, p, mHumidity);
        return d / STANDARD_DENSITY;
    }

    /** Returns the speed of sound for the temperature */
    public Quantity<Speed> getSpeedOfSound(double t) {
        return Quantities.getQuantity(calculateSoundVelocity(t), SI.METRE_PER_SECOND);
    }
}
