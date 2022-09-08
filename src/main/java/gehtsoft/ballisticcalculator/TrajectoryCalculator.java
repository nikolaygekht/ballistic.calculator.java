package gehtsoft.ballisticcalculator;

import javax.measure.*;
import javax.measure.quantity.*;

import gehtsoft.ballisticcalculator.Data.*;
import gehtsoft.ballisticcalculator.Drag.*;
import gehtsoft.ballisticcalculator.Units.*;
import si.uom.SI;
import systems.uom.common.Imperial;
import systems.uom.unicode.CLDR;
import tech.units.indriya.quantity.Quantities;

public class TrajectoryCalculator {
    /** The maximum step size of the calculation.
         * The default value is 10cm
        */
    private Quantity<Length> mMaximumCalculationStepSize = Quantities.getQuantity(0.1, SI.METRE);

    /** The maximum drop value to stop further calculation */
    private Quantity<Length> mMaximumDrop = Quantities.getQuantity(-1000, SI.METRE);

    /** The minimum velocity to stop the calculation */
    private Quantity<Speed> mMinimumVelocity = Quantities.getQuantity(1, SI.METRE_PER_SECOND);

    
    /** PIR = (PI/8)*(RHO0/144) */ 
    private static final double PIR = 2.08551e-04;

    /** 
         * Calculates the sight angle for the specified zero distance
        */
    public Quantity<Angle> calculateSightAngle(Projectile ammunition, Weapon rifle, Atmosphere atmosphere) {
        
        var rangeTo = rifle.getZeroingInformation().getZeroDistance().multiply(2);
        var step = rifle.getZeroingInformation().getZeroDistance().multiply(0.01);
        var calculationStep = getCalculationStep(step);

        var dragTable = ammunition.getBallisticCoefficient().getDragTable();

        if (atmosphere == null)
            atmosphere = new Atmosphere();

        var alt0 = atmosphere.getAltitude();
        var altDelta = Quantities.getQuantity(1, SI.METRE);

        double densityFactor = 0, drag;
        Quantity<Speed> mach = Quantities.getQuantity(1, SI.METRE_PER_SECOND);

        var sightAngle = Quantities.getQuantity(150, BCUnits.MOA);
        var barrelAzimuth = Quantities.getQuantity(150, SI.RADIAN);

        for (int approximation = 0; approximation < 100; approximation++)        {
            var barrelElevation = sightAngle;

            var velocity = ammunition.getMuzzleVelocity();
            double time = 0;

            //x - distance towards target,
            //y - drop and
            //z - windage
            var rangeVector = new QuantityVector<Length>(
                Quantities.getQuantity(0, SI.METRE),
                rifle.getZeroingInformation().getSightHeight().negate(),
                Quantities.getQuantity(0, SI.METRE));

            var velocityVector = new QuantityVector<Speed>(
                velocity.multiply(Math.cos(UnitUtils.in(barrelElevation, SI.RADIAN))).multiply(Math.cos(UnitUtils.in(barrelAzimuth, SI.RADIAN))),
                velocity.multiply(Math.sin(UnitUtils.in(barrelElevation, SI.RADIAN))),
                velocity.multiply(Math.cos(UnitUtils.in(barrelElevation, SI.RADIAN))).multiply(Math.sin(UnitUtils.in(barrelAzimuth, SI.RADIAN))));

            var maximumRange = rangeTo;
            Quantity<Length> lastAtAltitude = Quantities.getQuantity(-100000, SI.METRE);
            IDragTableNode dragTableNode = null;

            double adjustBallisticFactorForVelocityUnits = UnitUtils.in(Quantities.getQuantity(1, velocity.getUnit()), BCUnits.FEET_PER_SECOND);
            double ballisticFactor = 1.0 / ammunition.getBallisticCoefficientValue();
            var accumulatedFactor = PIR * adjustBallisticFactorForVelocityUnits * ballisticFactor;
            
            var earthGravity = Quantities.getQuantity(9.80665, SI.METRE_PER_SECOND).to(velocity.getUnit());
            
            //run all the way down the range
            while (UnitUtils.compare(rangeVector.getX(), maximumRange) <= 0)
            {
                var alt = alt0.add(rangeVector.getY());

                Quantity<Length> currentAltDelta = lastAtAltitude.subtract(alt);
                if (currentAltDelta.getValue().doubleValue() < 0)
                    currentAltDelta = currentAltDelta.negate();
                
                if (UnitUtils.compare(currentAltDelta, altDelta) > 0) {
                    var t = atmosphere.getTemperatureAtAltitude(alt);
                    densityFactor = atmosphere.getDensityFactorForTemperature(alt, t);
                    mach = atmosphere.getSpeedOfSound(t);
                    lastAtAltitude = alt;
                }

                if (UnitUtils.compare(velocity, mMinimumVelocity) <= 0 ||
                    UnitUtils.compare(rangeVector.getY(), mMaximumDrop) <= 0)
                    break;

                double deltaTime = calculateTravelTime(calculationStep, velocityVector.getX());

                double currentMach = UnitUtils.divide(velocity, mach);

                //find Mach node for the first time
                if (dragTableNode == null)
                    dragTableNode = dragTable.find(currentMach);

                //walk towards the beginning the table as velocity drops
                while (dragTableNode.getPrevious() != null && dragTableNode.getPrevious().getMach() > currentMach)
                    dragTableNode = dragTableNode.getPrevious();

                drag = accumulatedFactor * densityFactor * 
                       dragTableNode.calculateDrag(currentMach) * 
                       velocity.getValue().doubleValue();

                velocityVector = new QuantityVector<Speed>(
                    velocityVector.getX().subtract(velocityVector.getX().multiply(deltaTime * drag)),
                    velocityVector.getY().subtract(velocityVector.getY().multiply(deltaTime * drag))
                                         .subtract(earthGravity.multiply(deltaTime)),
                    velocityVector.getZ().subtract(velocityVector.getZ().multiply(deltaTime * drag)));


                var deltaRangeVector = new QuantityVector<Length>(calculationStep,
                        Quantities.getQuantity(UnitUtils.in(velocityVector.getY(), SI.METRE_PER_SECOND) * deltaTime, SI.METRE),
                        Quantities.getQuantity(UnitUtils.in(velocityVector.getZ(), SI.METRE_PER_SECOND) * deltaTime, SI.METRE));

                rangeVector = rangeVector.add(deltaRangeVector);

                if (UnitUtils.compare(rangeVector.getX(), rifle.getZeroingInformation().getZeroDistance()) >= 0) {
                    if (Math.abs(UnitUtils.in(rangeVector.getY(), SI.METRE)) < 0.0001)
                        return sightAngle;

                    sightAngle.subtract(Quantities.getQuantity(UnitUtils.in(rangeVector.getY(), SI.METRE) / 100 / UnitUtils.in(rifle.getZeroingInformation().getZeroDistance(), SI.METRE) * 100,
                                                               BCUnits.CENTIMETERS_PER_100METRES));                    
                    break;
                }

                velocity = velocityVector.getMagnitude();
                time += calculateTravelTime(deltaRangeVector.getMagnitude(), velocity);

            }
        }
        throw new IllegalArgumentException("Cannot find zero parameters for the specified zeroing information");
    }

    /* 
    /// <summary>
    /// Calculates the trajectory for the specified parameters.
    /// </summary>
    /// <param name="ammunition"></param>
    /// <param name="rifle"></param>
    /// <param name="atmosphere"></param>
    /// <param name="shot"></param>
    /// <param name="wind"></param>
    /// <param name="dragTable">Custom drag table</param>
    /// <returns></returns>
    public TrajectoryPoint[] Calculate(Ammunition ammunition, Rifle rifle, Atmosphere atmosphere, ShotParameters shot, Wind[] wind = null, DragTable dragTable = null)
    {
        Measurement<DistanceUnit> rangeTo = shot.MaximumDistance;
        Measurement<DistanceUnit> step = shot.Step;
        Measurement<DistanceUnit> calculationStep = GetCalculationStep(step);

        atmosphere ??= new Atmosphere();

        dragTable = ValidateDragTable(ammunition, dragTable);

        Measurement<DistanceUnit> alt0 = atmosphere.Altitude;
        Measurement<DistanceUnit> altDelta = new Measurement<DistanceUnit>(1, DistanceUnit.Meter);
        double densityFactor = 0, drag;
        Measurement<VelocityUnit> mach = new Measurement<VelocityUnit>(0, VelocityUnit.MetersPerSecond);

        double stabilityCoefficient = 1;
        bool calculateDrift;

        if (rifle.Rifling != null && ammunition.BulletDiameter != null && ammunition.BulletLength != null)
        {
            stabilityCoefficient = CalculateStabilityCoefficient(ammunition, rifle, atmosphere);
            calculateDrift = true;
        }
        else
        {
            calculateDrift = false;
        }

        TrajectoryPoint[] trajectoryPoints = new TrajectoryPoint[(int)(Math.Floor(rangeTo / step)) + 1];

        var barrelAzimuth = shot.BarrelAzymuth ?? new Measurement<AngularUnit>(0.0, AngularUnit.Radian);
        var barrelElevation = shot.SightAngle;
        if (shot.ShotAngle != null)
            barrelElevation += shot.ShotAngle.Value;

        Measurement<VelocityUnit> velocity = ammunition.MuzzleVelocity;
        TimeSpan time = new TimeSpan(0);

        int currentWind = 0;
        Measurement<DistanceUnit> nextWindRange = new Measurement<DistanceUnit>(1e7, DistanceUnit.Meter);
        Vector<VelocityUnit> windVector;
        if (wind == null || wind.Length < 1)
        {
            windVector = new Vector<VelocityUnit>();
        }
        else
        {
            if (wind.Length > 1 && wind[0].MaximumRange != null)
                nextWindRange = wind[0].MaximumRange.Value;
            windVector = WindVector(shot, wind[0], velocity.Unit);
        }

        //x - distance towards target,
        //y - drop and
        //z - windage
        var rangeVector = new Vector<DistanceUnit>(new Measurement<DistanceUnit>(0, DistanceUnit.Meter),
            -rifle.Sight.SightHeight,
            new Measurement<DistanceUnit>(0, DistanceUnit.Meter));

        var velocityVector = new Vector<VelocityUnit>(velocity * barrelElevation.Cos() * barrelAzimuth.Cos(),
                                                        velocity * barrelElevation.Sin(),
                                                        velocity * barrelElevation.Cos() * barrelAzimuth.Sin());

        int currentItem = 0;
        Measurement<DistanceUnit> maximumRange = rangeTo + calculationStep;
        Measurement<DistanceUnit> nextRangeDistance = new Measurement<DistanceUnit>(0, DistanceUnit.Meter);

        Measurement<DistanceUnit> lastAtAltitude = new Measurement<DistanceUnit>(-1000000, DistanceUnit.Meter);
        DragTableNode dragTableNode = null;

        double adjustBallisticFactorForVelocityUnits = Measurement<VelocityUnit>.Convert(1, velocity.Unit, VelocityUnit.FeetPerSecond);
        double ballisicFactor = 1 / ammunition.GetBallisticCoefficient();
        var accumulatedFactor = PIR * adjustBallisticFactorForVelocityUnits * ballisicFactor;

        var earthGravity = (new Measurement<VelocityUnit>(Measurement<AccelerationUnit>.Convert(1, AccelerationUnit.EarthGravity, AccelerationUnit.MeterPerSecondSquare),
                                                                VelocityUnit.MetersPerSecond)).To(velocity.Unit);
        
        

        //run all the way down the range
        while (rangeVector.X <= maximumRange)
        {
            Measurement<DistanceUnit> alt = alt0 + rangeVector.Y;

            //update density and Mach velocity each 10 feet of altitude
            if (MeasurementMath.Abs(lastAtAltitude - alt) > altDelta)
            {
                atmosphere.AtAltitude(alt, out densityFactor, out mach);
                lastAtAltitude = alt;
            }

            if (velocity < MinimumVelocity || rangeVector.Y < -MaximumDrop)
                break;

            if (rangeVector.X >= nextWindRange)
            {
                currentWind++;
                windVector = WindVector(shot, wind[currentWind], velocity.Unit);

                if (currentWind == wind.Length - 1 || wind[currentWind].MaximumRange == null)
                    nextWindRange = new Measurement<DistanceUnit>(1e7, DistanceUnit.Meter);
                else
                    nextWindRange = wind[currentWind].MaximumRange.Value;
            }

            if (rangeVector.X >= nextRangeDistance)
            {
                var windage = rangeVector.Z;
                if (calculateDrift)
                    windage += new Measurement<DistanceUnit>(1.25 * (stabilityCoefficient + 1.2) * Math.Pow(time.TotalSeconds, 1.83) * (rifle.Rifling.Direction == TwistDirection.Right ? 1 : -1), DistanceUnit.Inch);

                trajectoryPoints[currentItem] = new TrajectoryPoint(
                    time: time,
                    weight: ammunition.Weight,
                    distance: rangeVector.X,
                    velocity: velocity,
                    mach: velocity / mach,
                    drop: rangeVector.Y,
                    windage: windage);
                nextRangeDistance += step;
                currentItem++;
                if (currentItem == trajectoryPoints.Length)
                    break;
            }

            TimeSpan deltaTime = BallisticMath.TravelTime(calculationStep, velocityVector.X);

            var velocityAdjusted = velocityVector - windVector;
            velocity = velocityAdjusted.Magnitude;
            double currentMach = velocity / mach;

            //find Mach node for the first time
            dragTableNode ??= dragTable.Find(currentMach);

            //walk towards the beginning the table as velocity drops
            while (dragTableNode.Mach > currentMach)
                dragTableNode = dragTableNode.Previous;

            drag = accumulatedFactor * densityFactor * dragTableNode.CalculateDrag(currentMach) * velocity.Value;

            velocityVector = new Vector<VelocityUnit>(
                velocityVector.X - deltaTime.TotalSeconds * drag * velocityAdjusted.X,
                velocityVector.Y - deltaTime.TotalSeconds * drag * velocityAdjusted.Y
                                    - earthGravity * deltaTime.TotalSeconds,
                velocityVector.Z - deltaTime.TotalSeconds * drag * velocityAdjusted.Z);

            var deltaRangeVector = new Vector<DistanceUnit>(calculationStep,
                    new Measurement<DistanceUnit>(velocityVector.Y.In(VelocityUnit.MetersPerSecond) * deltaTime.TotalSeconds, DistanceUnit.Meter),
                    new Measurement<DistanceUnit>(velocityVector.Z.In(VelocityUnit.MetersPerSecond) * deltaTime.TotalSeconds, DistanceUnit.Meter));

            rangeVector += deltaRangeVector;
            velocity = velocityVector.Magnitude;
            time = time.Add(BallisticMath.TravelTime(deltaRangeVector.Magnitude, velocity));
        }

        return trajectoryPoints;
    }
    */

    /** Gets a calculation step */
    private Quantity<Length> getCalculationStep(Quantity<Length> step) {
        step.divide(2);      //do it twice for increased accuracy of velocity calculation and 10 times per step

        var stepValue = UnitUtils.in(step, mMaximumCalculationStepSize.getUnit());
        if (stepValue > mMaximumCalculationStepSize.getValue().doubleValue())
        {
            int stepOrder = (int)Math.floor(Math.log10(stepValue));
            int maximumOrder = (int)Math.floor(Math.log10(mMaximumCalculationStepSize.getValue().doubleValue()));
            step.divide(Math.pow(10, stepOrder - maximumOrder + 1));
        }
        return step;
    }

    /** Calculates the vector of wind velocities */
    private static QuantityVector<Speed> calculateWindVector(ShotParameters shot, Wind wind, Unit<Speed> units) {
        double sightCosine = Math.cos(UnitUtils.in(shot.getSightAngle(), SI.RADIAN));
        double sightSine = Math.sin(UnitUtils.in(shot.getSightAngle(), SI.RADIAN));

        Quantity<Angle> cantAngle = shot.getCantAngle();
        if (cantAngle == null)
            cantAngle = Quantities.getQuantity(0, SI.RADIAN);
        double cantCosine = Math.cos(UnitUtils.in(cantAngle, SI.RADIAN));
        double cantSine = Math.sin(UnitUtils.in(cantAngle, SI.RADIAN));

        Quantity<Speed> rangeVelocity, crossComponent;

        if (wind != null)
        {
            rangeVelocity = Quantities.getQuantity(
                UnitUtils.in(wind.getSpeed(), units) * 
                Math.cos(UnitUtils.in(wind.getDirection(), SI.RADIAN)), units);

            crossComponent = Quantities.getQuantity(
                UnitUtils.in(wind.getSpeed(), units) * 
                Math.sin(UnitUtils.in(wind.getDirection(), SI.RADIAN)), units);
        }
        else
        {
            rangeVelocity = Quantities.getQuantity(0, units);
            crossComponent = Quantities.getQuantity(0, units);
        }

        Quantity<Speed> rangeFactor = rangeVelocity.negate().multiply(sightSine);

        return new QuantityVector<Speed>(
            rangeVelocity.multiply(sightCosine), 
            rangeFactor.multiply(cantCosine).add(crossComponent.multiply(cantSine)), 
            crossComponent.multiply(cantCosine).subtract(rangeFactor.multiply(cantSine)));
    }

    /** Returns a bullet stability coefficient */
    private static double calculateStabilityCoefficient(Projectile ammunitionInfo, Weapon rifleInfo, Atmosphere atmosphere) {
        double weight = UnitUtils.in(ammunitionInfo.getBulletWeight(), BCUnits.GRAIN);
        double diameter = UnitUtils.in(ammunitionInfo.getBulletDiameter(), Imperial.INCH);
        double length = UnitUtils.in(ammunitionInfo.getBulletLength(), Imperial.INCH);
        double twist = UnitUtils.in(rifleInfo.getRifling().getRiflingStep(), Imperial.INCH);
        
        double sd = 30 * weight / (Math.pow(twist, 2) * Math.pow(diameter, 3) * length * (1 + Math.pow(length, 2)));
        double fv = Math.pow(UnitUtils.in(ammunitionInfo.getMuzzleVelocity(), BCUnits.FEET_PER_SECOND) / 2800, 1.0 / 3.0);
        double ftp = 1;

        if (atmosphere != null)
        {
            double ft = UnitUtils.in(atmosphere.getTemperature(), CLDR.FAHRENHEIT);
            double pt = UnitUtils.in(atmosphere.getPressure(), CLDR.INCH_HG);
            ftp = ((ft + 460.0) / (59.0 + 460.0)) * (29.92 / pt);
        }
        return sd * fv * ftp;
    }

    private double calculateTravelTime(Quantity<Length> range, Quantity<Speed> velocity) {
        return UnitUtils.in(range, SI.METRE) / UnitUtils.in(velocity, SI.METRE_PER_SECOND);
    }
}