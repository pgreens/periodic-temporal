package com.paulgreenlee.time.periodic;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.Objects;

/**
 * This implementation of PeriodicTemporalAdjuster does nothing more than store
 * the associated TemporalAmount (representing the period) along with the
 * related TemporalAdjuster. The adjustment is not affected by the period. The
 * period is simply stored and made available to other classes.
 * 
 * @author Paul Greenlee
 *  
 */
public class SimplePeriodicTemporalAdjuster implements PeriodicTemporalAdjuster {

	private final TemporalAdjuster adjuster;
	private final TemporalAmount period;
	
	public SimplePeriodicTemporalAdjuster(TemporalAdjuster adjuster, TemporalAmount period) {
		this.adjuster = Objects.requireNonNull(adjuster, "adjuster is required");
		this.period = Objects.requireNonNull(period, "period is required");
	}
	
	@Override
	public Temporal adjustInto(Temporal temporal) {
		return adjuster.adjustInto(temporal);
	}

	@Override
	public TemporalAmount getPeriod() {
		return period;
	}

}
