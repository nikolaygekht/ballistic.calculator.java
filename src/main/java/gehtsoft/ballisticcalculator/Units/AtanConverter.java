package gehtsoft.ballisticcalculator.units;

import java.util.Objects;
import javax.measure.UnitConverter;

import tech.units.indriya.function.AbstractConverter;
import tech.uom.lib.common.function.ValueSupplier;

final class AtanConverter extends AbstractConverter implements ValueSupplier<String> { 
	private static final long serialVersionUID = -445644556745L;

	private double mBaseLeg;

	public double getBaseLeg() {
		return mBaseLeg;
	}

    public AtanConverter(double baseLeg) {
		mBaseLeg = baseLeg;
	}

	@Override
	public boolean isIdentity() {
		return false;
	}

	@Override
	protected boolean canReduceWith(AbstractConverter that) {
		return that instanceof TanConverter tanConverter && mBaseLeg == tanConverter.getBaseLeg();
	}

	@Override
	protected AbstractConverter reduce(AbstractConverter that) {
		return AbstractConverter.IDENTITY;
	}

	@Override
	public AbstractConverter inverseWhenNotIdentity() {
		return new TanConverter(mBaseLeg);
	}

	@Override
	public final String transformationLiteral() {
		return String.format("x -> atan(x / %f)", mBaseLeg);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof AtanConverter atanConverter && mBaseLeg == atanConverter.getBaseLeg();
	}

	@Override
	public int hashCode() {
		return Objects.hash(mBaseLeg);

	}

    @Override
    protected Number convertWhenNotIdentity(Number value) {
        return Math.atan(value.doubleValue() / mBaseLeg);
    }

	@Override
	public boolean isLinear() {
		return false;
	}

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public int compareTo(UnitConverter o) {
		if (this == o) {
			return 0;
		}
		if (o instanceof ValueSupplier) {
			return getValue().compareTo(String.valueOf(((ValueSupplier<?>) o).getValue()));
		}
		return -1;
	}
}