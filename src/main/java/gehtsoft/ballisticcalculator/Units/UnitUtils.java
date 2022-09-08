package gehtsoft.ballisticcalculator.Units;

import javax.measure.*;

import tech.units.indriya.quantity.Quantities;

public final class UnitUtils {
     public static <Q extends Quantity<Q>> double convert(Unit<Q> from, Unit<Q> to, double value) {
        return from.getConverterTo(to).convert(value);
     }

    public static <Q extends Quantity<Q>> double in(Quantity<Q> from, Unit<Q> to) {
            return convert(from.getUnit(), to, from.getValue().doubleValue());
    }

    public static <Q extends Quantity<Q>> Quantity<Q> convert(Quantity<Q> from, Unit<Q> to) {
        return Quantities.getQuantity(convert(from.getUnit(), to, from.getValue().doubleValue()), to);
    }

    public static <Q extends Quantity<Q>> int compare(Quantity<Q> a, Quantity<Q> b) {
        double v1, v2;
        v1 = a.getValue().doubleValue();

        if (a.getUnit() == b.getUnit())
            v2 = b.getValue().doubleValue();
        else
            v2 = in(b, a.getUnit());

        if (v1 < v2)
            return -1;
        else if (v1 > v2)
            return 1;
        return 0;
    }

    public static <Q extends Quantity<Q>> double divide(Quantity<Q> a, Quantity<Q> b) {
        double v1, v2;
        v1 = a.getValue().doubleValue();

        if (a.getUnit() == b.getUnit())
            v2 = b.getValue().doubleValue();
        else
            v2 = in(b, a.getUnit());

        return v1 / v2;
    }
}

