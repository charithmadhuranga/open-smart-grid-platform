/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

public interface ActionValueObject extends Serializable {
    /**
     * Validates the ActionValueObject.
     *
     * @throws FunctionalException
     *             is thrown when the validation is not ok.
     */
    void validate() throws FunctionalException;
}
