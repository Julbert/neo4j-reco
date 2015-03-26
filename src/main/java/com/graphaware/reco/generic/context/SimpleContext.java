/*
 * Copyright (c) 2015 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.reco.generic.context;

import com.graphaware.reco.generic.stats.DefaultStatistics;
import com.graphaware.reco.generic.stats.Statistics;

import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * The simplest possible context, allowing all recommendations.
 */
public class SimpleContext<OUT, IN> implements Context<OUT, IN> {

    private final Mode mode;
    private final int limit;
    private final Statistics statistics;

    /**
     * Construct a new context.
     *
     * @param input for which recommendations are being computed.
     * @param mode  in which recommendations are being computed. Must not be <code>null</code>.
     * @param limit the maximum number of desired recommendations. Must be positive.
     */
    public SimpleContext(IN input, Mode mode, int limit) {
        notNull(input);
        notNull(mode);
        isTrue(limit > 0);

        this.mode = mode;
        this.limit = limit;
        this.statistics = createStatistics(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Mode mode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int limit() {
        return limit;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * always returns true by default.
     */
    @Override
    public boolean allow(OUT recommendation, IN input, String task) {
        notNull(recommendation);
        notNull(input);
        hasLength(task);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.lang.UnsupportedOperationException at all times, unless overridden.
     */
    @Override
    public void disallow(OUT recommendation) {
        throw new UnsupportedOperationException("SimpleContext does not support blacklisting items. Please use FilteringContext");
    }

    /**
     * Create a new statistics object. To be overridden by subclasses if needed.
     *
     * @param input for which recommendations are being computed.
     * @return {@link com.graphaware.reco.generic.stats.DefaultStatistics} by default.
     */
    protected Statistics createStatistics(IN input) {
        return new DefaultStatistics<>(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statistics statistics() {
        return statistics;
    }
}

