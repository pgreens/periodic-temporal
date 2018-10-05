package com.paulgreenlee.time.periodic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.*;
import java.time.temporal.*;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AllOfCompoundTemporalAdjusterTest {

	@Test
	public void startingOnDateThatMeetsCriteriaShouldReturnThatDay() {
		AllOfCompoundTemporalAdjuster cta = new AllOfCompoundTemporalAdjuster(
				Arrays.asList(Month.NOVEMBER, TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.THURSDAY)), 
				Period.ofYears(1));
		
		ZonedDateTime actual = ZonedDateTime.of(2017, 11, 16, 0, 0, 0, 0, ZoneId.of("Z")).with(cta);
		ZonedDateTime expected = ZonedDateTime.of(2017, 11, 16, 0, 0, 0, 0, ZoneId.of("Z"));
		
		assertThat(actual, equalTo(expected));
	}
	
	public static Stream<Arguments> thirtyFirstData() {
		return Stream.of(
				Arguments.of(LocalDate.of(2018, 3, 31), LocalDate.of(2018, 3, 31), "Identity"),
				Arguments.of(LocalDate.of(2018, 3, 1), LocalDate.of(2018, 3, 31), "Start of same month"),
				Arguments.of(LocalDate.of(2018, 2, 1), LocalDate.of(2018, 3, 31), "Start of previous month"),
				Arguments.of(LocalDate.of(2018, 4, 1), LocalDate.of(2018, 5, 31), "Start of next month")
			);
	}
	
	@ParameterizedTest(name="{index}: {2} - from {0}, expect {1}")
	@MethodSource(value = {"thirtyFirstData"})
	public void findThiryFirsts(LocalDate from, LocalDate expected, String testDesc) {
		AllOfCompoundTemporalAdjuster thrityFirst = new AllOfCompoundTemporalAdjuster(Arrays.asList(TestTemporalAdjuster.thiryFirstOfMonth()), Period.ofMonths(1));
		LocalDate actual = from.with(thrityFirst);
		assertThat(actual, equalTo(expected));
	}
	
	public static Stream<Arguments> fifthSundayData() {
		return Stream.of(
				Arguments.of(LocalDate.of(2018, 4, 29), LocalDate.of(2018, 4, 29), "Identity"),
				Arguments.of(LocalDate.of(2018, 4, 1), LocalDate.of(2018, 4, 29), "Start of same month"),
				Arguments.of(LocalDate.of(2018, 3, 1), LocalDate.of(2018, 4, 29), "Start of previous month"),
				Arguments.of(LocalDate.of(2018, 5, 1), LocalDate.of(2018, 7, 29), "Start of next month")
			);
	}
	
	@ParameterizedTest(name="{index}: {2} - from {0}, expect {1}")
	@MethodSource(value = {"fifthSundayData"})
	public void findFifthSundays(LocalDate from, LocalDate expected, String testDesc) {
		AllOfCompoundTemporalAdjuster fifthSunday = new AllOfCompoundTemporalAdjuster(Arrays.asList(TestTemporalAdjuster.fifthSunday()), Period.ofMonths(1));
		LocalDate actual = from.with(fifthSunday);
		assertThat(actual, equalTo(expected));
	}
	
	public static Stream<Arguments> fifthSundayThirtyFirstData() {
		return Stream.of(
				Arguments.of(LocalDate.of(2019, 3, 31), LocalDate.of(2019, 3, 31), "Identity"),
				Arguments.of(LocalDate.of(2019, 3, 1), LocalDate.of(2019, 3, 31), "Start of same month"),
				Arguments.of(LocalDate.of(2019, 2, 1), LocalDate.of(2019, 3, 31), "Start of previous month"),
				Arguments.of(LocalDate.of(2018, 3, 1), LocalDate.of(2019, 3, 31), "A year ago"),
				Arguments.of(LocalDate.of(2019, 4, 1), LocalDate.of(2020, 5, 31), "Start of next month")
			);
	}

	@ParameterizedTest(name="{index}: {2} - from {0}, expect {1}")
	@MethodSource(value = {"fifthSundayThirtyFirstData"})
	public void findFifthSundaysThatAre31sts(LocalDate from, LocalDate expected, String testDesc) {
		AllOfCompoundTemporalAdjuster fifthSunday = new AllOfCompoundTemporalAdjuster(Arrays.asList(TestTemporalAdjuster.fifthSunday(), TestTemporalAdjuster.thiryFirstOfMonth()), Period.ofMonths(1));
		LocalDate actual = from.with(fifthSunday);
		assertThat(actual, equalTo(expected));
	}
	
	@Test
	public void testImpossibleDate() {
		AllOfCompoundTemporalAdjuster fifthSunday = new AllOfCompoundTemporalAdjuster(Arrays.asList(TemporalAdjusters.firstDayOfMonth(), TemporalAdjusters.lastDayOfMonth()), Period.ofMonths(1));
		assertThrows(DateTimeException.class, () -> LocalDate.now().with(fifthSunday));
	}
	
}
