/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.dlms.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.alliander.osgp.logging.domain.entities.DeviceLogItem;
import com.alliander.osgp.logging.domain.repositories.DeviceLogItemRepository;

@Deprecated
@Component
@Configuration
public class ResponseNotifierImpl implements ResponseNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseNotifierImpl.class);

    private static final int FIRST_WAIT_TIME = 1000;

    private Connection connection;

    @Autowired
    private DeviceLogItemRepository deviceLogItemRepository;

    @Value("${osgpadapterwssmartmeteringdbs.url}")
    private String jdbcUrlOsgpAdapterWsSmartmetering;

    @Value("${osgploggingdbs.url}")
    private String jdbcUrlOsgpLogging;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Override
    public boolean waitForResponse(final String correlid, final int timeout, final int maxtime) {
        Statement statement = null;
        try {
            statement = this.conn(this.jdbcUrlOsgpAdapterWsSmartmetering).createStatement();

            // check if we have (almost) immediate response
            Thread.sleep(FIRST_WAIT_TIME);
            PollResult pollres = this.pollDatabase(statement, correlid);
            if (pollres.equals(PollResult.OK)) {
                return true;
            }

            int delayedtime = 0;
            while (true) {
                Thread.sleep(timeout);
                if ((delayedtime += timeout) < maxtime) {
                    pollres = this.pollDatabase(statement, correlid);
                    if (pollres.equals(PollResult.OK)) {
                        return true;
                    } else if (pollres.equals(PollResult.ERROR)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (final SQLException se) {
            LOGGER.error(se.getMessage());
            return false;
        } catch (final InterruptedException intex) {
            LOGGER.error(intex.getMessage());
            return false;
        } finally {
            this.closeStatement(statement);
        }
    }

    @Override
    public boolean waitForLog(final String deviceId, final int timeout, final int maxtime) {
        Statement statement = null;
        try {
            statement = this.conn(this.jdbcUrlOsgpLogging).createStatement();

            // check if we have (almost) immediate response
            Thread.sleep(FIRST_WAIT_TIME);
            PollResult pollres = this.pollLogDatabase(statement, deviceId);
            if (pollres.equals(PollResult.OK)) {
                return true;
            }

            int delayedtime = 0;
            while (true) {
                Thread.sleep(timeout);
                if ((delayedtime += timeout) < maxtime) {
                    pollres = this.pollLogDatabase(statement, deviceId);
                    if (pollres.equals(PollResult.OK)) {
                        return true;
                    } else if (pollres.equals(PollResult.ERROR)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (final SQLException se) {
            LOGGER.error(se.getMessage());
            return false;
        } catch (final InterruptedException intex) {
            LOGGER.error(intex.getMessage());
            return false;
        } finally {
            this.closeStatement(statement);
        }
    }

    private PollResult pollDatabase(final Statement statement, final String correlid) {
        ResultSet rs = null;
        PollResult result = PollResult.NOT_OK;
        try {
            rs = statement.executeQuery(
                    "SELECT count(*) FROM meter_response_data WHERE correlation_uid = '" + correlid + "'");
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    result = PollResult.OK;
                }
            }
            return result;
        } catch (final SQLException se) {
            LOGGER.error(se.getMessage());
            return PollResult.ERROR;
        } finally {
            this.closeResultSet(rs);
        }
    }

    private PollResult pollLogDatabase(final Statement statement, final String deviceId) {
        final ResultSet rs = null;
        PollResult result = PollResult.NOT_OK;

        final List<DeviceLogItem> deviceLogItems = this.deviceLogItemRepository
                .findByDeviceIdentification(deviceId, new PageRequest(0, 2)).getContent();

        if (deviceLogItems != null && deviceLogItems.size() > 1) {
            result = PollResult.OK;
        }
        return result;

    }

    private void closeStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void closeResultSet(final ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Connection conn(final String dbUrl) {
        if (this.connection == null) {
            this.connection = this.connectToDatabaseOrDie(dbUrl);
        }
        return this.connection;
    }

    private Connection connectToDatabaseOrDie(final String dbUrl) {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(dbUrl, this.username, this.password);
        } catch (final ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage());
            System.exit(2);
        }
        return this.connection;
    }

    private enum PollResult {
        OK,
        NOT_OK,
        ERROR;
    }
}
