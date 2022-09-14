package gehtsoft.ballisticcalculator.Tools;

import java.io.*;
import java.util.ArrayList;

import javax.measure.*;
import javax.measure.quantity.*;

import gehtsoft.ballisticcalculator.*;
import gehtsoft.ballisticcalculator.Data.*;
import gehtsoft.ballisticcalculator.Drag.IDragTable;
import gehtsoft.ballisticcalculator.Drag.StandardDragTableFactory;
import gehtsoft.ballisticcalculator.Units.BCUnits;
import si.uom.SI;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

public class TrajectoryLoader {
    private Projectile mProjectile;
    private Atmosphere mAtmosphere;
    private Weapon mWeapon;
    private Wind mWind;
    private ArrayList<TrajectoryPoint> mTrajectory = new ArrayList<TrajectoryPoint>();

    public Projectile getProjectile() {
        return mProjectile;
    }

    public Atmosphere getAtmosphere() {
        return mAtmosphere;
    }

    public Weapon getWeapon() {
        return mWeapon;
    }

    public Wind getWind() {
        return mWind;
    }

    public ArrayList<TrajectoryPoint> getTrajectory() {
        return mTrajectory;
    }

    public void load(String name, IDragTable customDragTable) throws IOException {
        ClassLoader classLoader = TrajectoryLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            load(reader, customDragTable);
        }
    }

    public void load(BufferedReader reader, IDragTable customDragTable) throws IOException {
        String[] units = null;
        String[] values;
        String line;

        while((line = reader.readLine()) != null) {
            values = line.trim().split(";", 11);
            if (values.length < 1) 
                continue;
            if (values[0].equals("ammo")) {
                mProjectile = parseProjectile(values, customDragTable);
            }
            else if (values[0].equals("rifle")) {
                mWeapon = parseWeapon(values);
            }
            else if (values[0].equals("wind")) {
                mWind  = parseWind(values);
            }
            else if (values[0].equals("atmosphere")) {
                mAtmosphere  = parseAtmosphere(values);
            }
            else if (values[0].equals("shot")) {
                continue;
            }
            else {
                if (units == null)
                    units = values;
                else {
                    mTrajectory.add(parseTrajectoryPoint(values, units));
                }
            }
        }
    }

    private Unit<Speed> getSpeedUnit(String name) {
        if (name.equals("m/s"))
            return SI.METRE_PER_SECOND;
        if (name.equals("ft/s"))
            return BCUnits.FEET_PER_SECOND;
        if (name.equals("mph"))
            return CLDR.MILE_PER_HOUR;
        if (name.equals("km/h"))
        return SI.KILOMETRE_PER_HOUR;
        throw new IllegalArgumentException("Unknown speed unit: " + name);
    }

    private Unit<Angle> getAngleUnit(String name) {
        if (name.equals("rad"))
            return SI.RADIAN;
        if (name.equals("°") || name.equals("deg"))
            return CLDR.DEGREE;
        if (name.equals("mil"))
            return BCUnits.MIL;
        if (name.equals("moa"))
            return BCUnits.MOA;
        throw new IllegalArgumentException("Unknown angle unit: " + name);
    }

    private Unit<Length> getLengthUnit(String name) {
        if (name.equals("m"))
            return SI.METRE;
        if (name.equals("yd"))
            return CLDR.YARD;
        if (name.equals("ft") || name.equals("'"))
            return CLDR.FOOT;
        if (name.equals("in") || name.equals("\""))
            return CLDR.INCH;
        throw new IllegalArgumentException("Unknown length unit: " + name);
    }

    private Unit<Mass> getMassUnit(String name) {
        if (name.equals("g"))
            return SI.GRAM;
        if (name.equals("kg"))
            return SI.KILOGRAM;
        if (name.equals("lb"))
            return CLDR.POUND;
        if (name.equals("gr"))
            return BCUnits.GRAIN;
        throw new IllegalArgumentException("Unknown mass unit: " + name);
    }

    private Unit<Pressure> getPressureUnit(String name) {
        if (name.equals("pa"))
            return SI.PASCAL;
        if (name.equals("inHg"))
            return CLDR.INCH_HG;
        throw new IllegalArgumentException("Unknown pressure unit: " + name);
    }

    private Unit<Temperature> getTemperatureUnit(String name) {
        if (name.equals("°F"))
            return CLDR.FAHRENHEIT;
        if (name.equals("°C"))
            return CLDR.CELSIUS;
        if (name.equals("°K"))
            return CLDR.KELVIN;
        throw new IllegalArgumentException("Unknown temperature unit: " + name);
    }
    
    private Unit<Energy> getEnergyUnit(String name) {
        if (name.equals("J"))
            return CLDR.JOULE;
        if (name.equals("ft-lb"))
            return BCUnits.FOOT_POUND;
        throw new IllegalArgumentException("Unknown temperature unit: " + name);
    }

    private String[] splitValue(String value) {
        String[] result = new String[2];
        int i = 0;
        while (i < value.length()) { 
            char c = value.charAt(i);
            if (!Character.isDigit(c) && c != '.' && c != '-' && c != '+')
                break;
            i++;
        }
            
        result[0] = value.substring(0, i);
        result[1] = value.substring(i);
        return result;
    }

    private Quantity<Length> parseLength(String value) {
        var r = splitValue(value);
        return parseLength(r[0], r[1]);
    }

    private Quantity<Length> parseLength(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getLengthUnit(unit));
    }

    private Quantity<Angle> parseAngle(String value) {
        var r = splitValue(value);
        return parseAngle(r[0], r[1]);
    }

    private Quantity<Angle> parseAngle(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getAngleUnit(unit));
    }

    private Quantity<Speed> parseSpeed(String value) {
        var r = splitValue(value);
        return parseSpeed(r[0], r[1]);
    }

    private Quantity<Speed> parseSpeed(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getSpeedUnit(unit));
    }

    private Quantity<Energy> parseEnergy(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getEnergyUnit(unit));
    }

    private Quantity<Mass> parseMass(String value) {
        var r = splitValue(value);
        return parseMass(r[0], r[1]);
    }

    private Quantity<Mass> parseMass(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getMassUnit(unit));
    }

    private Quantity<Temperature> parseTemperature(String value) {
        var r = splitValue(value);
        return parseTemperature(r[0], r[1]);
    }

    private Quantity<Temperature> parseTemperature(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getTemperatureUnit(unit));
    }

    private Quantity<Pressure> parsePressure(String value) {
        var r = splitValue(value);
        return parsePressure(r[0], r[1]);
    }

    private Quantity<Pressure> parsePressure(String value, String unit) {
        return Quantities.getQuantity(Double.parseDouble(value), getPressureUnit(unit));
    }

    public BallisticCoefficient parseBallisticCoefficient(String value, IDragTable customTable) {
        BallisticCoefficientValueType type = BallisticCoefficientValueType.Coefficient;
        if (value.charAt(0) == 'F')
        {
            type = BallisticCoefficientValueType.FormFactor;
            value = value.substring(1);
        }
        var r = splitValue(value);
        IDragTable table = null;
        if (r[1].equals("G1"))
            table = StandardDragTableFactory.getInstance().G1();
        else if (r[1].equals("G2"))
            table = StandardDragTableFactory.getInstance().G2();
        else if (r[1].equals("G5"))
            table = StandardDragTableFactory.getInstance().G5();
        else if (r[1].equals("G6"))
            table = StandardDragTableFactory.getInstance().G6();
        else if (r[1].equals("G7"))
            table = StandardDragTableFactory.getInstance().G7();
        else if (r[1].equals("G8"))
            table = StandardDragTableFactory.getInstance().G8();
        else if (r[1].equals("GC"))
            table = customTable;
        else
            throw new IllegalArgumentException("Unknown drag table: " + r[1]);

        return new BallisticCoefficient(Double.parseDouble(r[0]), type, table);
    }

    private Projectile parseProjectile(String[] values, IDragTable customDragTable) {
        if (values.length >= 6) {
            return new Projectile(
                parseMass(values[2]),
                parseSpeed(values[3]),
                parseBallisticCoefficient(values[1], customDragTable),
                parseLength(values[4]),
                parseLength(values[5]));
        }
        else if (values.length >= 4) {
            return new Projectile(
                parseMass(values[2]),
                parseSpeed(values[3]),
                parseBallisticCoefficient(values[1], customDragTable));
        }
        return null;
    }

    private Weapon parseWeapon(String[] values) {
        Rifling rifling = null;
        if (values.length >= 5) {
            var direction = TwistDirection.Right;
            if (values[4] == "left")
                direction = TwistDirection.Left;
            rifling = new Rifling(parseLength(values[3]), direction);
        }
        if (values.length >= 3) {
            var zi = new ZeroingInformation(parseLength(values[1]), parseLength(values[2]));
            return new Weapon(zi, rifling);
        }
        return null;
    }

    private Atmosphere parseAtmosphere(String[] values) {
        if (values.length >= 5) {
            return new Atmosphere(
                parseLength(values[4]),
                parsePressure(values[3]),
                false,
                parseTemperature(values[1]),
                Double.parseDouble(values[2]) / 100.0
            );
        }
        return null;
    }

    private Wind parseWind(String[] values) {
        if (values.length >= 3) {
            return new Wind(
                parseSpeed(values[1]),
                parseAngle(values[2])
            );
        }
        return null;       
    }
    
    private TrajectoryPoint parseTrajectoryPoint(String[] values, String[] units) {
        if (values.length == units.length &&
            values.length >= 11) {
                return new TrajectoryPoint(
                    parseLength(values[0], units[0]),
                    parseSpeed(values[5], units[5]),
                    parseEnergy(values[7], units[7]),
                    Double.parseDouble(values[6]),
                    parseLength(values[1], units[1]),
                    parseAngle(values[2], units[2]),
                    parseLength(values[3], units[3]),
                    parseAngle(values[4], units[4]),
                    Quantities.getQuantity(Double.parseDouble(values[6]), CLDR.SECOND));
            }
        return null;
    }
}
