/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

import java.util.EnumSet;
import java.util.Set;

// suppress warning for duplicate string at reserved (line 21,22,23) The duplicate part is a single string in the
// description of an enum value.
@SuppressWarnings("squid:S1192")
public enum ClockStatusBit {

    INVALID_VALUE("invalid value"),
    DOUBTFUL_VALUE("doubtful value"),
    DIFFERENT_CLOCK_BASE("different clock base"),
    INVALID_CLOCK_STATUS("invalid clock status"),
    RESERVED_1("reserved"),
    RESERVED_2("reserved"),
    RESERVED_3("reserved"),
    DAYLIGHT_SAVING_ACTIVE("daylight saving active");

    private final String description;

    ClockStatusBit(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isSet(final int clockStatus) {
        final int mask = 1 << this.ordinal();
        return mask == (mask & clockStatus);
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static Set<ClockStatusBit> forClockStatus(final byte clockStatus) {
        return forClockStatus(clockStatus & 0xFF);
    }

    // here returning null is independed of the actual ClockStatusBit that is later used to make the list. This meand
    // that returning an empty list would falsly suggest that there is an empty list, where in fact there is no valid
    // list to return
    @SuppressWarnings("squid:S1168")
    public static Set<ClockStatusBit> forClockStatus(final int clockStatus) {
        if (ClockStatus.STATUS_NOT_SPECIFIED == clockStatus) {
            return null;
        }
        if (clockStatus < 0 || clockStatus > 0xFF) {
            throw new IllegalArgumentException("clockStatus not in [0..255]");
        }
        final Set<ClockStatusBit> statusBits = EnumSet.noneOf(ClockStatusBit.class);
        for (final ClockStatusBit statusBit : values()) {
            if (statusBit.isSet(clockStatus)) {
                statusBits.add(statusBit);
            }
        }
        return statusBits;
    }

    public static int getStatus(final Set<ClockStatusBit> statusBits) {
        if (statusBits == null) {
            return ClockStatus.STATUS_NOT_SPECIFIED;
        }
        int status = 0;
        for (final ClockStatusBit statusBit : statusBits) {
            status |= (1 << statusBit.ordinal());
        }
        return status;
    }

}
