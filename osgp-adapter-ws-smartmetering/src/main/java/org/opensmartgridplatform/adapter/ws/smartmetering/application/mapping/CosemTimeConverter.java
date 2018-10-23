package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.CosemTime;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class CosemTimeConverter extends CustomConverter<byte[], CosemTime> {

    @Override
    public CosemTime convert(final byte[] source, final Type<? extends CosemTime> destinationType,
            final MappingContext context) {
        final int hour = source[0] & 0xFF;
        final int minute = source[1] & 0xFF;
        final int second = source[2] & 0xFF;
        final int hundredths = source[3] & 0xFF;

        return new CosemTime(hour, minute, second, hundredths);
    }
}
