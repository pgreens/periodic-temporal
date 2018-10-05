package com.paulgreenlee.time.periodic;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;

/**
 * A PeriodicTemporalAdjuster adds the concept of a period to a
 * TemporalAdjuster. The period is a TemporalAmount that represents the amount
 * of time between occurrences of this adjustment. For instance, each
 * DayOfWeek.MONDAY is a week apart.
 *
 * @author Paul Greenlee
 * 
 */
public interface PeriodicTemporalAdjuster extends TemporalAdjuster {

	TemporalAmount getPeriod();
	
	default Temporal nextPeriod(Temporal temporal) {
		return getPeriod().addTo(temporal);
	}
	
}
