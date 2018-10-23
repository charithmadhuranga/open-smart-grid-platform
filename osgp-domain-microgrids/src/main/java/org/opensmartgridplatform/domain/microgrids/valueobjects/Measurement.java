/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.microgrids.valueobjects;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.joda.time.DateTime;

public class Measurement extends NodeIdentifier implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3315719218127525093L;

    private final int qualifier;
    private final DateTime time;
    private final double value;

    public Measurement(final int id, final String node, final int qualifier, final DateTime time, final double value) {
        super(id, node);
        this.qualifier = qualifier;
        this.time = time;
        this.value = this.roundValue(value);
    }

    public int getQualifier() {
        return this.qualifier;
    }

    public DateTime getTime() {
        return this.time;
    }

    public double getValue() {
        return this.value;
    }

    private double roundValue(final double value) {
        final DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(value));
    }
}
