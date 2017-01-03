/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.alliander.osgp.shared.application.config.AbstractConfig;

/**
 * Base class for the application configuration.
 */
@Configuration
@PropertySources({
    @PropertySource("classpath:cucumber-platform.properties"),
    @PropertySource(value = "file:/etc/osp/test/global-cucumber.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "file:/etc/osp/test/cucumber-platform.properties", ignoreResourceNotFound = true),
})
public abstract class ApplicationConfiguration extends AbstractConfig {

    @Value("${defaultTimeout}")
    public Integer defaultTimeout;

}
