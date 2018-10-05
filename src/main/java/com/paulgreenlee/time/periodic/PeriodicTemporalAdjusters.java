package com.paulgreenlee.time.periodic;

import java.time.DayOfWeek;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>A collection of utility methods for creating {@link PeriodicTemporalAdjuster}s.</p>
 * <p>These can easily be combined to form adjuster that will iterate through occurrences of date criteria. Examples:</p>
 * <ul>
 * 	<li>Every Valentine's Day: <code>forwardOnlyOf(annually(MonthDay.of(2, 14)))</code></li>
 *  <li>Every Monday, Wednesday, Friday: <code>forwardOnlyOf(weeklyOnDays(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))</code></li>
 * </ul>
 * 
 * @author Paul Greenlee
 *
 */
public class PeriodicTemporalAdjusters {

	private PeriodicTemporalAdjusters() { }
	
	/**
	 * Turn any {@link PeriodicTemporalAdjuster} into a {@link ForwardOnlyTemporalAdjuster}.
	 */
	public static PeriodicTemporalAdjuster forwardOnlyOf(PeriodicTemporalAdjuster adjuster) {
		return new ForwardOnlyTemporalAdjuster(adjuster, adjuster.getPeriod());
	}
	public static PeriodicTemporalAdjuster annually(TemporalAdjuster adjusters) {
		return new SimplePeriodicTemporalAdjuster(adjusters, Period.ofYears(1));
	}
	public static PeriodicTemporalAdjuster monthly(TemporalAdjuster adjusters) {
		return new SimplePeriodicTemporalAdjuster(adjusters, Period.ofMonths(1));
	}
	public static PeriodicTemporalAdjuster weekly(TemporalAdjuster adjuster) {
		return new SimplePeriodicTemporalAdjuster(adjuster, Period.ofWeeks(1));
	}
	public static PeriodicTemporalAdjuster everyNWeeks(TemporalAdjuster adjuster, int numberOfWeeks) {
		return new SimplePeriodicTemporalAdjuster(adjuster, Period.ofWeeks(numberOfWeeks));
	}

	public static PeriodicTemporalAdjuster weeklyOnDays(DayOfWeek dayOfWeek, DayOfWeek...otherDays) {
		List<TemporalAdjuster> allDays = new LinkedList<>();
		allDays.add(dayOfWeek);
		Arrays.stream(otherDays).forEach(allDays::add);
		return weekly(new NearestOfCompoundTemporalAdjuster(allDays));
	}
}
