package com.paulgreenlee.time.periodic;

import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQueries;
import java.util.Objects;

/**
 * A {@link PeriodicTemporalAdjuster} that only goes forward in time. If an
 * adjustment results in a time before (or equal to) the Temporal that was
 * adjusted, the original Temporal is moved forward by periods until an
 * occurrence is found that is after the original time.
 * 
 * @author Paul Greenlee
 *
 */
public class ForwardOnlyTemporalAdjuster implements PeriodicTemporalAdjuster {

	private static final int SAFETY_CUTOFF = 10000;
	private final TemporalAdjuster adjuster;
	private final TemporalAmount period;

	public ForwardOnlyTemporalAdjuster(TemporalAdjuster adjuster, TemporalAmount period) {
		this.adjuster = Objects.requireNonNull(adjuster, "a TemporalAdjuster is required");
		this.period = Objects.requireNonNull(period);
	}

	public static ForwardOnlyTemporalAdjuster of(PeriodicTemporalAdjuster periodic) {
		return new ForwardOnlyTemporalAdjuster(periodic, periodic.getPeriod());
	}

	@Override
	public Temporal adjustInto(Temporal temporal) {
		Temporal beforeAttempt = temporal;
		Temporal attempt = beforeAttempt.with(adjuster);
		int count = 0;
		while (isBeforeOriginalDate(temporal, attempt)) {
			beforeAttempt = nextPeriod(beforeAttempt);
			attempt = beforeAttempt.with(adjuster);
			if (count++ >= SAFETY_CUTOFF)
				throw new DateTimeException("Potentially inifinite loop encountered in temporal adjusters");
		}
		return attempt;
	}

	private boolean isBeforeOriginalDate(Temporal temporal, Temporal attempt) {
		// Using the smallest unit supported, compare the two temporals.
		return temporal.query(TemporalQueries.precision()).between(temporal, attempt) <= 0;
	}

	@Override
	public TemporalAmount getPeriod() {
		return period;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + adjuster.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForwardOnlyTemporalAdjuster other = (ForwardOnlyTemporalAdjuster) obj;
		return adjuster.equals(other.adjuster);
	}

}
