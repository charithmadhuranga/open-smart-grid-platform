/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.microgrids.application.mapping;

import java.util.List;

import org.opensmartgridplatform.domain.microgrids.valueobjects.MeasurementFilter;
import org.opensmartgridplatform.domain.microgrids.valueobjects.ProfileFilter;
import org.opensmartgridplatform.domain.microgrids.valueobjects.SystemFilter;
import org.opensmartgridplatform.dto.valueobjects.microgrids.MeasurementFilterDto;
import org.opensmartgridplatform.dto.valueobjects.microgrids.ProfileFilterDto;
import org.opensmartgridplatform.dto.valueobjects.microgrids.SystemFilterDto;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class SystemFilterConverter extends BidirectionalConverter<SystemFilter, SystemFilterDto> {

    @Override
    public SystemFilterDto convertTo(final SystemFilter source, final Type<SystemFilterDto> destinationType,
            final MappingContext context) {
        final List<MeasurementFilterDto> measurementFilters = this.mapperFacade
                .mapAsList(source.getMeasurementFilters(), MeasurementFilterDto.class);
        final List<ProfileFilterDto> profileFilters = this.mapperFacade.mapAsList(source.getProfileFilters(),
                ProfileFilterDto.class);

        return new SystemFilterDto(source.getId(), source.getSystemType(), measurementFilters, profileFilters,
                source.isAll());
    }

    @Override
    public SystemFilter convertFrom(final SystemFilterDto source, final Type<SystemFilter> destinationType,
            final MappingContext context) {
        final List<MeasurementFilter> measurementFilters = this.mapperFacade.mapAsList(source.getMeasurementFilters(),
                MeasurementFilter.class);
        final List<ProfileFilter> profileFilters = this.mapperFacade.mapAsList(source.getProfileFilters(),
                ProfileFilter.class);

        return new SystemFilter(source.getId(), source.getSystemType(), measurementFilters, profileFilters,
                source.isAll());
    }

}
