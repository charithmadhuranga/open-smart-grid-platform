/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadContainer;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodType;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReads;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReadsContainer;

public class PeriodicMeterReadsResponseConverter
extends
BidirectionalConverter<com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReadsContainer, PeriodicMeterReadContainer> {

    @Override
    public PeriodicMeterReadContainer convertTo(final PeriodicMeterReadsContainer source,
            final Type<PeriodicMeterReadContainer> destinationType) {
        final List<com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReads> periodicMeterReads = new ArrayList<>(
                source.getMeterReads().size());
        for (final PeriodicMeterReads pmr : source.getMeterReads()) {
            periodicMeterReads.add(this.mapperFacade.map(pmr,
                    com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReads.class));
        }

        return new PeriodicMeterReadContainer(
                com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodType.valueOf(source.getPeriodType()
                        .name()), periodicMeterReads);
    }

    @Override
    public PeriodicMeterReadsContainer convertFrom(final PeriodicMeterReadContainer source,
            final Type<PeriodicMeterReadsContainer> destinationType) {
        final List<PeriodicMeterReads> periodicMeterReads = new ArrayList<>(source.getPeriodicMeterReads().size());
        for (final com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReads pmr : source
                .getPeriodicMeterReads()) {

            periodicMeterReads.add(this.mapperFacade.map(pmr, PeriodicMeterReads.class));
        }

        return new PeriodicMeterReadsContainer(PeriodType.valueOf(source.getPeriodType().name()), periodicMeterReads);
    }

}
