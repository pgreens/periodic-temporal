package com.paulgreenlee.time.periodic;

import java.time.DateTimeException;
import java.time.temporal.*;
import java.util.*;

/**
 * This {@link CompoundTemporalAdjuster} uses only one of the adjusters in its
 * collection at a time. It adjusts the given Temporal using each adjuster
 * separately, and whichever resulting Temporal is closest to the original is
 * chosen as the result.
 * 
 * @author Paul Greenlee
 * 
 */
public class NearestOfCompoundTemporalAdjuster extends CompoundTemporalAdjuster {

	public NearestOfCompoundTemporalAdjuster(List<TemporalAdjuster> adjusters) {
		super(adjusters);
	}
	
	@Override
	public Temporal adjustInto(final Temporal temporal) {
		final Temporal beforeAttempt = Objects.requireNonNull(temporal);
		TemporalUnit precision = temporal.query(TemporalQueries.precision());
		
		Optional<Temporal> closest = adjusters.stream().map(beforeAttempt::with)
				.min(Comparator.comparingLong(adjusted -> Math.abs(precision.between(adjusted, temporal))));
		
		if (!closest.isPresent())
			throw new DateTimeException("Failed to adjust");
		
		return closest.get();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjusters == null) ? 0 : adjusters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NearestOfCompoundTemporalAdjuster other = (NearestOfCompoundTemporalAdjuster) obj;
		return adjusters.equals(other.adjusters);
	}
	
}
