/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.inputparsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Parses the date of a cucumber input string to a java.util.Date instance.
 *
 * Use this class to ensure the same format is used over all scenarios, and
 * suppress any parsing exception to avoid clutter in the builders.
 *
 */
public class DateInputParser {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    
    //XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(YYYY, MM, DD));
    
    
    
    /**
     * Parses the date of a cucumber input string to a java.util.Date instance.
     *
     * @param input
     *            string with format "yyyy-MM-dd"
     * @return Date instance
     */
    public static XMLGregorianCalendar parse(final String input) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(input);
        } catch (final DatatypeConfigurationException e) {
            throw new AssertionError(String.format("Input date '%s' could not be parsed to a XMLGregorianCalendar object.", input));
    }
}
}
