--
-- Rename GET_SPECIFIC_CONFIGURATION_OBJECT to GET_SPECIFIC_ATTRIBUTE_VALUE in device_function_mapping table
-- SLIM-675
--

update device_function_mapping set function = 'GET_SPECIFIC_ATTRIBUTE_VALUE' where function='GET_SPECIFIC_CONFIGURATION_OBJECT';
