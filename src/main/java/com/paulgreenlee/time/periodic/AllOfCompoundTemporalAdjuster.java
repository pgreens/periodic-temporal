package com.paulgreenlee.time.periodic;

import java.time.DateTimeException;
import java.time.temporal.*;
import java.util.*;

/**
 * <p>
 * This adjuster attempts to find a Temporal such that all of the
 * conditions implied by the provided {@code TemporalAdjuster}s are true. For
 * example, it could adjust to the next January 1 that falls on a Monday by
 * providing {@code MonthDay.of(Month.JANUARY, 1)} and {@code DayOfWeek.MONDAY}.
 * </p>
 * <p>
 * It is possible to provide a combination of TemporalAdjusters that has no
 * solution (January 1 that is also the second Monday of the month). In this
 * case, an exception {@link DateTimeException} is thrown.
 * </p>
 * <p>
 * It is also possible that the next occurrence of the given criteria is in the
 * distant future. If all of the temporal adjusters are applied, but end up
 * conflicting with each other, the date is advanced using the {@code period}
 * provided. This should advance the date a sufficient amount to make another
 * attempt. This amount should be on the order of the scale of the largest
 * adjuster provided. For example, if the largest adjuster is a day of the week,
 * the advancer should advance the date to the next week. If the largest
 * adjuster adjusts to a specific month, the advancer should advance to the next
 * year.
 * </p>
 * 
 * @author Paul Greenlee
 * 
 */
public class AllOfCompoundTemporalAdjuster extends CompoundTemporalAdjuster implements PeriodicTemporalAdjuster {

	private final TemporalAmount period;
	
	public AllOfCompoundTemporalAdjuster(List<TemporalAdjuster> adjusters, TemporalAmount period) {
		super(adjusters);
		this.period = Objects.requireNonNull(period, "period is required");
	}
	
	@Override
	public Temporal adjustInto(final Temporal temporal) {
		Temporal beforeAttempt = Objects.requireNonNull(temporal);
		Temporal afterAttempt = applyAdjusters(beforeAttempt);
		int iterCount = 0;
		while (afterAttempt == null || !allFieldsMatch(afterAttempt)) {
			beforeAttempt = nextPeriod(beforeAttempt);
			afterAttempt = applyAdjusters(beforeAttempt);
			if (iterCount++ >= ITERATION_LIMIT)
				throw new DateTimeException("iteration reached its limit. No date found meeting criteria");
		}
		
		return afterAttempt;
	}

	private Temporal applyAdjusters(final Temporal temporal) {
		Temporal adjusted = temporal;
		for (TemporalAdjuster adjuster : adjusters) {
			try {
				adjusted = adjusted.with(adjuster);
			} catch (DateTimeException e) {
				// An invalid adjustment was attempted. Continue search.
				return null;
			}
		}
		return adjusted;
	}
	
	private boolean allFieldsMatch(Temporal temporal) {
		for (TemporalAdjuster adjuster : adjusters) {
			Temporal check = temporal.with(adjuster);
			if (!check.equals(temporal))
				return false;
		}
		return true;
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
		AllOfCompoundTemporalAdjuster other = (AllOfCompoundTemporalAdjuster) obj;
		return adjusters.equals(other.adjusters);
	}

	@Override
	public TemporalAmount getPeriod() {
		return period;
	}
	
}
