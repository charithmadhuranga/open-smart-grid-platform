/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.opensmartgridplatform.adapter.ws.schema.smartmetering.bundle.GetMbusEncryptionKeyStatusRequest;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.GetMbusEncryptionKeyStatusRequestData;

public class GetMbusEncryptionKeyStatusRequestMappingTest {

    private static final String MAPPED_OBJECT_NULL_MESSAGE = "Mapped object should not be null.";
    private static final String MAPPED_FIELD_VALUE_MESSAGE = "Mapped field should have the same value.";

    private static final String MBUS_DEVICE_IDENTIFICATION = "TestMbusDevice";

    private final ConfigurationMapper mapper = new ConfigurationMapper();

    @Test
    public void shouldConvertGetMbusEncryptionKeyStatusDataRequest() {
        final GetMbusEncryptionKeyStatusRequest source = this.makeRequest();
        final GetMbusEncryptionKeyStatusRequestData result = this.mapper.map(source,
                GetMbusEncryptionKeyStatusRequestData.class);
        assertNotNull(MAPPED_OBJECT_NULL_MESSAGE, result);
        assertEquals(MAPPED_FIELD_VALUE_MESSAGE, source.getMbusDeviceIdentification(),
                result.getMbusDeviceIdentification());

    }

    private GetMbusEncryptionKeyStatusRequest makeRequest() {

        final GetMbusEncryptionKeyStatusRequest result = new GetMbusEncryptionKeyStatusRequest();
        result.setMbusDeviceIdentification(MBUS_DEVICE_IDENTIFICATION);
        return result;
    }
}
