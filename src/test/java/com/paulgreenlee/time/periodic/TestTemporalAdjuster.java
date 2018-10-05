package com.paulgreenlee.time.periodic;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestTemporalAdjuster implements TemporalAdjuster {

	public static TemporalAdjuster identity() {
		return (temporal) -> temporal;
	}
	
	// special dates
	public static TemporalAdjuster thiryFirstOfMonth() {
		return t -> t.with(ChronoField.DAY_OF_MONTH, 31);
	}
	public static TemporalAdjuster fifthSunday() {
		return TemporalAdjusters.dayOfWeekInMonth(5, DayOfWeek.SUNDAY);
	}
	public static TemporalAdjuster leapDay() {
		return t -> t.with(ChronoField.DAY_OF_YEAR, 366);
	}
	
	public static TemporalAdjuster tomorrow() {
		return forward(ChronoField.EPOCH_DAY);
	}
	public static TemporalAdjuster yesterday() {
		return backward(ChronoField.EPOCH_DAY);
	}
	public static TemporalAdjuster forward(TemporalField field) {
		return (temporal) -> temporal.with(ChronoField.EPOCH_DAY, temporal.get(ChronoField.EPOCH_DAY) + 1);
	}
	public static TemporalAdjuster backward(TemporalField field) {
		return (temporal) -> temporal.with(ChronoField.EPOCH_DAY, temporal.get(ChronoField.EPOCH_DAY) - 1);
	}
	
	private List<TemporalAdjuster> sequence;
	private Iterator<TemporalAdjuster> iter;
	
	public TestTemporalAdjuster() {
		this.sequence = new ArrayList<>();
	}
	
	@Override
	public Temporal adjustInto(Temporal temporal) {
		if (sequence.isEmpty())
			throw new AssertionError("No adjuster set up. Bad test case set up");
		if (iter == null || !iter.hasNext())
			iter = sequence.iterator();
		return iter.next().adjustInto(temporal);
	}
	
	public TestTemporalAdjuster then(TemporalAdjuster nextAdjustment) {
		this.sequence.add(nextAdjustment);
		return this;
	}

}
