/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.shared.usermanagement;

import java.util.Date;

public class ChangeOrganisationRequest {

    private String organisationIdentificationToChange;
    private String newOrganisationIdentification;
    private String newOrganisationName;
    private String functionGroup;
    private Date expiryDateContract;

    public ChangeOrganisationRequest() {

    }

    public ChangeOrganisationRequest(final String organisationIdentificationToChange,
            final String newOrganisationIdentification, final String newOrganisationName, final String functionGroup,
            final Date expiryDateContract) {
        this.organisationIdentificationToChange = organisationIdentificationToChange;
        this.newOrganisationIdentification = newOrganisationIdentification;
        this.newOrganisationName = newOrganisationName;
        this.functionGroup = functionGroup;
        this.expiryDateContract = expiryDateContract;
    }

    public String getOrganisationIdentificationToChange() {
        return this.organisationIdentificationToChange;
    }

    public String getNewOrganisationIdentification() {
        return this.newOrganisationIdentification;
    }

    public String getNewOrganisationName() {
        return this.newOrganisationName;
    }

    public String getFunctionGroup() {
        return this.functionGroup;
    }

    public Date getExpiryDateContract() {
        return this.expiryDateContract;
    }
}
