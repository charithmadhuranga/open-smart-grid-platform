/**
 * Copyright 2020 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.UpdatedDevice;
import org.opensmartgridplatform.domain.core.entities.DeviceOutputSetting;
import org.opensmartgridplatform.domain.core.entities.Ssld;
import org.opensmartgridplatform.domain.core.valueobjects.RelayType;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

/**
 * Maps the device output settings, because those can not be mapped
 * automatically by Orika. Orika initially tries to clear the outputSettings
 * field, which is a List, of the Ssld. This fails, because Ssld.outputSettings
 * is an unmodifiable list.
 */
public class DeviceOutputSettingsMapper extends CustomMapper<UpdatedDevice, Ssld> {

    @Override
    public void mapAtoB(final UpdatedDevice source, final Ssld destination, final MappingContext context) {

        destination.updateOutputSettings(this.mapOutputSettings(source.getOutputSettings()));
    }

    private List<DeviceOutputSetting> mapOutputSettings(
            final List<org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceOutputSetting> outputSettings) {

        if (outputSettings == null) {
            return new ArrayList<>();
        } else {
            return outputSettings.stream().map(this::mapOutputSetting).collect(Collectors.toList());
        }

    }

    private DeviceOutputSetting mapOutputSetting(
            final org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.DeviceOutputSetting dos) {

        final RelayType relayType = RelayType.valueOf(dos.getRelayType().toString());

        return new DeviceOutputSetting(dos.getInternalId(), dos.getExternalId(), relayType, dos.getAlias());
    }

}
