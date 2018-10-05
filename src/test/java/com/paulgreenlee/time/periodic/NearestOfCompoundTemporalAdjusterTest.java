package com.paulgreenlee.time.periodic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class NearestOfCompoundTemporalAdjusterTest {

	private static final LocalDate SUNDAY = LocalDate.of(2018, 6, 24);
	private static final TemporalAdjuster START_OF_HOUR = (Temporal temporal) -> LocalDateTime.from(temporal).withMinute(0);
	private static final TemporalAdjuster HALF_HOUR = (Temporal temporal) -> LocalDateTime.from(temporal).withMinute(30);
	
	@Test
	public void shouldUseOnlyAdjusterWhenOneSupplied() {
		NearestOfCompoundTemporalAdjuster adjuster = new NearestOfCompoundTemporalAdjuster(Arrays.asList(TemporalAdjusters.firstDayOfMonth()));
		LocalDate date = LocalDate.of(2018, 6, 5);
		LocalDate actual = date.with(adjuster);
		assertThat(actual, equalTo(LocalDate.of(2018, 6, 1)));
	}

	@Test
	public void shouldAjustToNearestDayOfWeek() {
		NearestOfCompoundTemporalAdjuster adjuster = new NearestOfCompoundTemporalAdjuster(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
		LocalDate actual = SUNDAY.with(adjuster);
		assertThat(actual, equalTo(LocalDate.of(2018, 6, 22))); // will go back to Friday
	}
	
	@Test
	public void shouldTieBreakDayOfWeek() {
		NearestOfCompoundTemporalAdjuster adjuster = new NearestOfCompoundTemporalAdjuster(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
		LocalDate date = LocalDate.of(2018, 6, 19);
		LocalDate actual = date.with(adjuster);
		assertThat(actual, anyOf(equalTo(LocalDate.of(2018, 6, 18)), equalTo(LocalDate.of(2018, 6, 20))));
	}
	
	@Test
	public void shouldAdjustToNearestTime() {
		NearestOfCompoundTemporalAdjuster adjuster = new NearestOfCompoundTemporalAdjuster(Arrays.asList(START_OF_HOUR, HALF_HOUR));
		LocalDateTime start = LocalDateTime.of(SUNDAY, LocalTime.of(15, 10));
		LocalDateTime actual = start.with(adjuster);
		assertThat(actual, equalTo(LocalDateTime.of(SUNDAY, LocalTime.of(15, 0))));
	}
	
	@Test
	public void shouldAdjustToNearestAcrossMultipleTypesOfAdjusters() {
		NearestOfCompoundTemporalAdjuster adjuster = new NearestOfCompoundTemporalAdjuster(Arrays.asList(DayOfWeek.FRIDAY, HALF_HOUR));
		LocalDateTime start = LocalDateTime.of(SUNDAY, LocalTime.of(15, 10));
		LocalDateTime actual = start.with(adjuster);
		assertThat(actual, equalTo(LocalDateTime.of(SUNDAY, LocalTime.of(15, 30))));
	}
}
