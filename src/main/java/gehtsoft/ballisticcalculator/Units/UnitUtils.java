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
}

