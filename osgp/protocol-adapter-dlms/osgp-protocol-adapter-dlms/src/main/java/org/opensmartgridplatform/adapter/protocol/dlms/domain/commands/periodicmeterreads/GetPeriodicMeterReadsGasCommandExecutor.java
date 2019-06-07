/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.periodicmeterreads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.openmuc.jdlms.AttributeAddress;
import org.openmuc.jdlms.GetResult;
import org.openmuc.jdlms.ObisCode;
import org.openmuc.jdlms.datatypes.DataObject;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.dlmsobjectconfig.AttributeAddressForProfile;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.dlmsobjectconfig.DlmsCaptureObject;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.dlmsobjectconfig.DlmsObjectConfigService;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.dlmsobjectconfig.DlmsObjectType;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.dlmsobjectconfig.model.Medium;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.AmrProfileStatusCodeHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.DlmsHelper;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.JdlmsObjectToStringUtil;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.BufferedDateTimeValidationException;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActionRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AmrProfileStatusCodeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AmrProfileStatusCodeFlagDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ChannelDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.CosemDateTimeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodTypeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadGasResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsGasRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsGasResponseItemDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
public class GetPeriodicMeterReadsGasCommandExecutor extends
        AbstractPeriodicMeterReadsCommandExecutor<PeriodicMeterReadsRequestDto, PeriodicMeterReadGasResponseDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPeriodicMeterReadsGasCommandExecutor.class);

    private static final String GAS_VALUE = "gasValue";
    private static final String UNEXPECTED_VALUE = "Unexpected null/unspecified value for Gas Capture Time";

    private final DlmsHelper dlmsHelper;
    private final AmrProfileStatusCodeHelper amrProfileStatusCodeHelper;
    private final DlmsObjectConfigService dlmsObjectConfigService;

    @Autowired
    public GetPeriodicMeterReadsGasCommandExecutor(final DlmsHelper dlmsHelper,
            final AmrProfileStatusCodeHelper amrProfileStatusCodeHelper,
            final DlmsObjectConfigService dlmsObjectConfigService) {
        super(PeriodicMeterReadsGasRequestDto.class);
        this.dlmsHelper = dlmsHelper;
        this.amrProfileStatusCodeHelper = amrProfileStatusCodeHelper;
        this.dlmsObjectConfigService = dlmsObjectConfigService;
    }

    @Override
    public PeriodicMeterReadsRequestDto fromBundleRequestInput(final ActionRequestDto bundleInput)
            throws ProtocolAdapterException {

        this.checkActionRequestType(bundleInput);
        final PeriodicMeterReadsGasRequestDto periodicMeterReadsGasRequestDto =
                (PeriodicMeterReadsGasRequestDto) bundleInput;

        return new PeriodicMeterReadsRequestDto(periodicMeterReadsGasRequestDto.getPeriodType(),
                periodicMeterReadsGasRequestDto.getBeginDate(), periodicMeterReadsGasRequestDto.getEndDate(),
                periodicMeterReadsGasRequestDto.getChannel());
    }

    @Override
    public PeriodicMeterReadGasResponseDto execute(final DlmsConnectionManager conn, final DlmsDevice device,
            final PeriodicMeterReadsRequestDto periodicMeterReadsQuery) throws ProtocolAdapterException {

        if (periodicMeterReadsQuery == null) {
            throw new IllegalArgumentException(
                    "PeriodicMeterReadsQuery should contain PeriodType, BeginDate and EndDate.");
        }

        final PeriodTypeDto queryPeriodType = periodicMeterReadsQuery.getPeriodType();
        final DateTime queryBeginDateTime = new DateTime(periodicMeterReadsQuery.getBeginDate());
        final DateTime queryEndDateTime = new DateTime(periodicMeterReadsQuery.getEndDate());

        final AttributeAddressForProfile profileBufferAddress = this.getProfileBufferAddress(queryPeriodType,
                periodicMeterReadsQuery.getChannel(), queryBeginDateTime, queryEndDateTime, device);

        final List<AttributeAddress> scalerUnitAddresses =
                this.getScalerUnitAddresses(periodicMeterReadsQuery.getChannel(), profileBufferAddress);

        LOGGER.info("Retrieving current billing period and profiles for gas for period type: {}, from: " + "{}, to: {}",
                queryPeriodType, queryBeginDateTime, queryEndDateTime);

        /*
         * workaround for a problem when using with_list and retrieving a profile
         * buffer, this will be returned erroneously.
         */
        final List<GetResult> getResultList = new ArrayList<>();

        final List<AttributeAddress> allAttributeAddresses = new ArrayList<>();
        allAttributeAddresses.add(profileBufferAddress.getAttributeAddress());
        allAttributeAddresses.addAll(scalerUnitAddresses);

        for (final AttributeAddress address : allAttributeAddresses) {

            conn.getDlmsMessageListener()
                    .setDescription(
                            "GetPeriodicMeterReadsGas for channel " + periodicMeterReadsQuery.getChannel() + ", "
                                    + queryPeriodType + " from " + queryBeginDateTime + " until " + queryEndDateTime
                                    + ", retrieve attribute: " + JdlmsObjectToStringUtil.describeAttributes(address));

            getResultList.addAll(this.dlmsHelper.getAndCheck(conn, device,
                    "retrieve periodic meter reads for " + queryPeriodType + ", channel "
                            + periodicMeterReadsQuery.getChannel(), address));
        }

        LOGGER.info("Received getResult: {} ", getResultList);

        final DataObject resultData = this.dlmsHelper.readDataObject(getResultList.get(0), "Periodic G-Meter Reads");
        final List<DataObject> bufferedObjectsList = resultData.getValue();

        final List<PeriodicMeterReadsGasResponseItemDto> periodicMeterReads = new ArrayList<>();
        for (final DataObject bufferedObject : bufferedObjectsList) {
            final List<DataObject> bufferedObjectValue = bufferedObject.getValue();

            try {
                periodicMeterReads.add(
                        this.convertToResponseItem(periodicMeterReadsQuery, bufferedObjectValue,
                                getResultList, profileBufferAddress, scalerUnitAddresses));
            } catch (final BufferedDateTimeValidationException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }

        LOGGER.info("Resulting periodicMeterReads: {} ", periodicMeterReads);

        return new PeriodicMeterReadGasResponseDto(queryPeriodType, periodicMeterReads);
    }

    private PeriodicMeterReadsGasResponseItemDto convertToResponseItem(
            final PeriodicMeterReadsRequestDto periodicMeterReadsQuery, final List<DataObject> bufferedObjects,
            final List<GetResult> getResultList, final AttributeAddressForProfile attributeAddressForProfile,
            final List<AttributeAddress> attributeAddresses)
            throws ProtocolAdapterException, BufferedDateTimeValidationException {

        final Date logTime = this.readClock(periodicMeterReadsQuery, bufferedObjects, attributeAddressForProfile);
        final AmrProfileStatusCodeDto status = this.readStatus(bufferedObjects, attributeAddressForProfile);
        final DataObject gasValue = this.readValue(bufferedObjects, attributeAddressForProfile);
        final DataObject scalerUnit = this.readScalerUnit(getResultList, attributeAddresses, attributeAddressForProfile,
                periodicMeterReadsQuery.getChannel().getChannelNumber());
        final Date captureTime = this.readCaptureTime(bufferedObjects, attributeAddressForProfile);

        LOGGER.info("Converting bufferObject with value: {} ", bufferedObjects);
        LOGGER.info("Resulting values: LogTime: {}, status: {}, gasValue {}, scalerUnit: {}, captureTime {} ", logTime,
                status, gasValue, scalerUnit, captureTime);

        return new PeriodicMeterReadsGasResponseItemDto(logTime,
                this.dlmsHelper.getScaledMeterValue(gasValue, scalerUnit, GAS_VALUE), captureTime, status);
    }

    private Date readClock(final PeriodicMeterReadsRequestDto periodicMeterReadsQuery,
            final List<DataObject> bufferedObjects, final AttributeAddressForProfile attributeAddressForProfile)
            throws ProtocolAdapterException, BufferedDateTimeValidationException {

        final PeriodTypeDto queryPeriodType = periodicMeterReadsQuery.getPeriodType();
        final DateTime queryBeginDateTime = new DateTime(periodicMeterReadsQuery.getBeginDate());
        final DateTime queryEndDateTime = new DateTime(periodicMeterReadsQuery.getEndDate());

        final Integer clockIndex = attributeAddressForProfile.getIndex(DlmsObjectType.CLOCK, null);

        CosemDateTimeDto cosemDateTime = null;

        if (clockIndex != null) {
            cosemDateTime = this.dlmsHelper.readDateTime(bufferedObjects.get(clockIndex),
                    "Clock from " + queryPeriodType + " buffer gas");
        }

        final DateTime bufferedDateTime = cosemDateTime == null ? null : cosemDateTime.asDateTime();

        this.dlmsHelper.validateBufferedDateTime(bufferedDateTime, cosemDateTime, queryBeginDateTime, queryEndDateTime);

        if (bufferedDateTime != null) {
            return bufferedDateTime.toDate();
        } else {
            return null;
        }
    }

    private AmrProfileStatusCodeDto readStatus(final List<DataObject> bufferedObjects,
            final AttributeAddressForProfile attributeAddressForProfile) throws ProtocolAdapterException {

        final Integer statusIndex = attributeAddressForProfile.getIndex(DlmsObjectType.AMR_STATUS, null);

        AmrProfileStatusCodeDto amrProfileStatusCode = null;

        if (statusIndex != null) {
            amrProfileStatusCode = this.readAmrProfileStatusCode(bufferedObjects.get(statusIndex));
        }

        return amrProfileStatusCode;
    }

    private DataObject readValue(final List<DataObject> bufferedObjects,
            final AttributeAddressForProfile attributeAddressForProfile) {

        final Integer valueIndex = attributeAddressForProfile.getIndex(DlmsObjectType.MBUS_MASTER_VALUE, 2);

        DataObject value = null;

        if (valueIndex != null) {
            value = bufferedObjects.get(valueIndex);
        }

        return value;
    }

    private DataObject readScalerUnit(final List<GetResult> getResultList,
            final List<AttributeAddress> attributeAddresses,
            final AttributeAddressForProfile attributeAddressForProfile, final Integer channel) {

        final DlmsCaptureObject captureObject =
                attributeAddressForProfile.getCaptureObject(DlmsObjectType.MBUS_MASTER_VALUE);

        int index = 0;
        Integer scalerUnitIndex = null;
        for (final AttributeAddress address : attributeAddresses) {
            final String obisCode = captureObject.getRelatedObject().getObisCode().replace("<c>", channel.toString());
            if (address.getInstanceId().equals(new ObisCode(obisCode))) {
                scalerUnitIndex = index;
            }
            index++;
        }

        if (scalerUnitIndex != null) {
            return getResultList.get(scalerUnitIndex).getResultData();
        }

        return null;
    }

    private Date readCaptureTime(final List<DataObject> bufferedObjects,
            final AttributeAddressForProfile attributeAddressForProfile)
            throws ProtocolAdapterException {

        final Integer captureTimeIndex = attributeAddressForProfile.getIndex(DlmsObjectType.MBUS_MASTER_VALUE, 5);

        if (captureTimeIndex != null) {
            final CosemDateTimeDto cosemDateTime = this.dlmsHelper.readDateTime(bufferedObjects.get(captureTimeIndex),
                    "Clock from mbus interval extended register");

            final Date captureTime;
            if (cosemDateTime.isDateTimeSpecified()) {
                captureTime = cosemDateTime.asDateTime().toDate();
            } else {
                throw new ProtocolAdapterException(UNEXPECTED_VALUE);
            }

            return captureTime;
        }

        return null;
    }

    private AttributeAddressForProfile getProfileBufferAddress(final PeriodTypeDto periodType,
            final ChannelDto channel, final DateTime beginDateTime, final DateTime endDateTime,
            final DlmsDevice device) throws ProtocolAdapterException {

        final DlmsObjectType type = DlmsObjectType.getTypeForPeriodType(periodType);

        // Add the attribute address for the profile
        final AttributeAddressForProfile attributeAddressProfile = this.dlmsObjectConfigService.findAttributeAddressForProfile(
                device, type, channel.getChannelNumber(), beginDateTime, endDateTime, Medium.GAS)
                .orElseThrow(() -> new ProtocolAdapterException("No address found for " + type));

        LOGGER.info("Dlms object config service returned profile buffer address {} ", attributeAddressProfile);

        return attributeAddressProfile;
    }

    private List<AttributeAddress> getScalerUnitAddresses(final ChannelDto channel,
            final AttributeAddressForProfile attributeAddressForProfile) {

        final List<AttributeAddress> attributeAddresses =
                this.dlmsObjectConfigService.getAttributeAddressesForScalerUnit(attributeAddressForProfile,
                        channel.getChannelNumber());

        LOGGER.info("Dlms object config service returned scaler unit addresses {} ", attributeAddresses);

        return attributeAddresses;
    }

    /**
     * Reads AmrProfileStatusCode from DataObject holding a bitvalue in a numeric
     * datatype.
     *
     * @param amrProfileStatusData
     *         AMR profile register value.
     *
     * @return AmrProfileStatusCode object holding status enum values.
     *
     * @throws ProtocolAdapterException
     *         on invalid register data.
     */
    private AmrProfileStatusCodeDto readAmrProfileStatusCode(final DataObject amrProfileStatusData)
            throws ProtocolAdapterException {

        if (!amrProfileStatusData.isNumber()) {
            throw new ProtocolAdapterException("Could not read AMR profile register data. Invalid data type.");
        }

        final Set<AmrProfileStatusCodeFlagDto> flags = this.amrProfileStatusCodeHelper.toAmrProfileStatusCodeFlags(
                amrProfileStatusData.getValue());
        return new AmrProfileStatusCodeDto(flags);
    }
}