package com.paulgreenlee.time.periodic;

import java.time.temporal.*;
import java.util.*;

/**
 * This is an abstract class that represents a {@link TemporalAdjuster} that is
 * comprised of other TemporalAdjusters.
 * 
 * @author Paul Greenlee
 */
public abstract class CompoundTemporalAdjuster implements TemporalAdjuster {

	protected static final int ITERATION_LIMIT = 10 * 1000;

	protected final List<TemporalAdjuster> adjusters;
	
	/**
	 * <p>
	 * make a note about how the adjusters should be idempotent
	 * </p>
	 * @param adjusters
	 *            the set of TemporalAdjusters to be applied
	 * @param advancer
	 *            a TemporalAdjuster that will progress forward in time if an
	 *            attempt to align all the adjusters fails
	 */
	public CompoundTemporalAdjuster(List<TemporalAdjuster> adjusters) {
		this.adjusters = new ArrayList<>(adjusters);
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
		CompoundTemporalAdjuster other = (CompoundTemporalAdjuster) obj;
		return adjusters.equals(other.adjusters);
	}
	
}
