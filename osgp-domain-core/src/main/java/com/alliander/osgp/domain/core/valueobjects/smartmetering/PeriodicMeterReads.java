/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.util.Date;

public class PeriodicMeterReads extends MeterReads {

    private static final long serialVersionUID = -7981853503300669899L;

    private final AmrProfileStatusCode amrProfileStatusCode;

    /**
     * Constructor taking all data. Use for conversion purposes, when all fields
     * need to be copied.
     *
     * @param logTime
     * @param activeEnergyImport
     * @param activeEnergyExport
     * @param activeEnergyImportTariffOne
     * @param activeEnergyImportTariffTwo
     * @param activeEnergyExportTariffOne
     * @param activeEnergyExportTariffTwo
     * @param amrProfileStatusCode
     */
    public PeriodicMeterReads(final Date logTime, final Long activeEnergyImport, final Long activeEnergyExport,
            final Long activeEnergyImportTariffOne, final Long activeEnergyImportTariffTwo,
            final Long activeEnergyExportTariffOne, final Long activeEnergyExportTariffTwo,
            final AmrProfileStatusCode amrProfileStatusCode) {
        super(logTime, activeEnergyImport, activeEnergyExport, activeEnergyImportTariffOne,
                activeEnergyImportTariffTwo, activeEnergyExportTariffOne, activeEnergyExportTariffTwo);
        this.amrProfileStatusCode = amrProfileStatusCode;
    }

    /**
     * Constructor for interval reads.
     *
     * @param logTime
     * @param activeEnergyImport
     * @param activeEnergyExport
     * @param amrProfileStatusCode
     */
    public PeriodicMeterReads(final Date logTime, final long activeEnergyImport, final long activeEnergyExport,
            final AmrProfileStatusCode amrProfileStatusCode) {
        this(logTime, activeEnergyImport, activeEnergyExport, null, null, null, null, amrProfileStatusCode);
    }

    /**
     * Constructor for monthly reads. Does not hold a AMR profile status.
     *
     * @param logTime
     * @param activeEnergyImportTariffOne
     * @param activeEnergyImportTariffTwo
     * @param activeEnergyExportTariffOne
     * @param activeEnergyExportTariffTwo
     */
    public PeriodicMeterReads(final Date logTime, final long activeEnergyImportTariffOne,
            final long activeEnergyImportTariffTwo, final long activeEnergyExportTariffOne,
            final long activeEnergyExportTariffTwo) {
        this(logTime, activeEnergyImportTariffOne, activeEnergyImportTariffTwo, activeEnergyExportTariffOne,
                activeEnergyExportTariffTwo, null);
    }

    /**
     * Constructor for daily reads. Holds tariff values and AMR profile status.
     *
     * @param logTime
     * @param activeEnergyImportTariffOne
     * @param activeEnergyImportTariffTwo
     * @param activeEnergyExportTariffOne
     * @param activeEnergyExportTariffTwo
     * @param amrProfileStatusCode
     */
    public PeriodicMeterReads(final Date logTime, final long activeEnergyImportTariffOne,
            final long activeEnergyImportTariffTwo, final long activeEnergyExportTariffOne,
            final long activeEnergyExportTariffTwo, final AmrProfileStatusCode amrProfileStatusCode) {
        super(logTime, null, null, activeEnergyImportTariffOne, activeEnergyImportTariffTwo,
                activeEnergyExportTariffOne, activeEnergyExportTariffTwo);
        this.amrProfileStatusCode = amrProfileStatusCode;
    }

    public AmrProfileStatusCode getAmrProfileStatusCode() {
        return this.amrProfileStatusCode;
    }
}
