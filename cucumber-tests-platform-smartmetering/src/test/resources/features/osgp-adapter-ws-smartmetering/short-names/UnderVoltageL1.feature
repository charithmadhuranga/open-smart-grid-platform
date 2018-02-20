# Needs a DlmsDevice simulator with e650 profile on port 1026
@SmartMetering @Platform @SN
Feature: SmartMetering short names - UnderVoltageL1

  Scenario: Get dedicated event log UnderVoltageL1 capture objects from L+G E650
    Given a dlms device
      | DeviceIdentification | TEST1024000000005 |
      | DeviceType           | SMART_METER_E     |
      | UseSn                | true              |
      | UseHdlc              | true              |
      | Port                 |              1026 |
      | Hls5active           | false             |
      | Lls1active           | true              |
    When the get specific attribute value request is received
      | DeviceIdentification | TEST1024000000005 |
      | ClassId              |                 7 |
      | ObisCodeA            |                 1 |
      | ObisCodeB            |                 1 |
      | ObisCodeC            |                99 |
      | ObisCodeD            |                98 |
      | ObisCodeE            |               152 |
      | ObisCodeF            |               255 |
      | Attribute            |                 3 |
    Then a get specific attribute value response should be returned
      | DeviceIdentification | TEST1024000000005 |
      | Result               | OK                |
      | ResponsePart         | 1-1:32.17.0.255   |

  Scenario: Get dedicated event log UnderVoltageL1 buffer from L+G E650
    Given a dlms device
      | DeviceIdentification | TEST1024000000005 |
      | DeviceType           | SMART_METER_E     |
      | UseSn                | true              |
      | UseHdlc              | true              |
      | Port                 |              1026 |
      | Hls5active           | false             |
      | Lls1active           | true              |
    When the get profile generic data request is received
      | DeviceIdentification | TEST1024000000005 |
      | ClassId              |                 7 |
      | ObisCodeA            |                 1 |
      | ObisCodeB            |                 1 |
      | ObisCodeC            |                99 |
      | ObisCodeD            |                98 |
      | ObisCodeE            |               152 |
      | ObisCodeF            |               255 |
      | Attribute            |                 2 |
      | BeginDate            | 2015-09-06T23:59  |
      | EndDate              | 2015-09-08T00:01  |
    Then the profile generic data result should be returned
      | DeviceIdentification           | TEST1024000000005 |
      | Result                         | OK                |
      | NumberOfCaptureObjects         |                 4 |
      | CaptureObject_ClassId_1        |                 8 |
      | CaptureObject_LogicalName_1    | 0.0.1.0.0.255     |
      | CaptureObject_AttributeIndex_1 |                 2 |
      | CaptureObject_DataIndex_1      |                 0 |
      | CaptureObject_Unit_1           | UNDEFINED         |
      | CaptureObject_ClassId_2        |                 7 |
      | CaptureObject_LogicalName_2    | 1.1.99.98.152.255 |
      | CaptureObject_AttributeIndex_2 |                29 |
      | CaptureObject_DataIndex_2      |                 0 |
      | CaptureObject_Unit_2           | UNDEFINED         |
      | CaptureObject_ClassId_3        |                 7 |
      | CaptureObject_LogicalName_3    | 1.1.99.98.152.255 |
      | CaptureObject_AttributeIndex_3 |                30 |
      | CaptureObject_DataIndex_3      |                 0 |
      | CaptureObject_Unit_3           | UNDEFINED         |
      | CaptureObject_ClassId_4        |                 4 |
      | CaptureObject_LogicalName_4    | 1.1.32.17.0.255   |
      | CaptureObject_AttributeIndex_4 |                 2 |
      | CaptureObject_DataIndex_4      |                 0 |
      | CaptureObject_Unit_4           | V                 |
