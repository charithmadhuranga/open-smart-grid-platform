/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

import org.opensmartgridplatform.domain.core.valueobjects.DeviceModel;

public class AddSmartMeterRequest implements Serializable {

    private static final long serialVersionUID = -6363279003203263772L;

    final SmartMeteringDevice device;

    final DeviceModel deviceModel;

    public AddSmartMeterRequest(final SmartMeteringDevice device, final DeviceModel deviceModel) {
        this.device = device;
        this.deviceModel = deviceModel;
    }

    public SmartMeteringDevice getDevice() {
        return this.device;
    }

    public DeviceModel getDeviceModel() {
        return this.deviceModel;
    }

}
