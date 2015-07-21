/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.shared.exceptionhandling;

/**
 * Enum having list of ExceptionsCodes
 */

public enum ComponentType {
    WS_ADMIN("Osgp Web Service Adapter Admin"),
    WS_CORE("Osgp Web Service Adapter Core"),
    WS_PUBLIC_LIGHTING("Osgp Web Service Adapter Public Lighting"),
    WS_TARIFF_SWITCHING("Osgp Web Service Adapter Tariff Switching"),
    WS_SMART_METERING("Osgp Web Service Adapter Smart Metering"),
    DOMAIN_ADMIN("Osgp Domain Adapter Admin"),
    DOMAIN_CORE("Osgp Domain Adapter Core"),
    DOMAIN_PUBLIC_LIGHTING("Osgp Domain Adapter Public Lighting"),
    DOMAIN_TARIFF_SWITCHING("Osgp Domain Adapter Tariff Switching"),
    DOMAIN_SMART_METERING("Osgp Domain Adapter Smart Metering"),
    OSGP_CORE("Osgp Core"),
    PROTOCOL_OSLP("Osgp Protocol Adapter OSLP"),

    UNKNOWN("Unknown");

    private ComponentType(final String componentName) {

    }
}
