/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology.order.web.search;

import java.util.Arrays;
import java.util.List;

import org.openmrs.Order.Urgency;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.order.RadiologyOrder;
import org.openmrs.module.radiology.order.RadiologyOrderSearchCriteria;
import org.openmrs.module.radiology.order.RadiologyOrderService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.PatientResource1_9;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Find RadiologyOrder's that match the specified search phrase.
 */
@Component
public class RadiologyOrderSearchHandler implements SearchHandler {
    
    
    public static final String REQUEST_PARAM_ACCESSION_NUMBER = "accessionNumber";
    
    public static final String REQUEST_PARAM_PATIENT = "patient";
    
    public static final String REQUEST_PARAM_URGENCY = "urgency";
    
    public static final String REQUEST_PARAM_TOTAL_COUNT = "totalCount";
    
    @Autowired
    RadiologyOrderService radiologyOrderService;
    
    SearchQuery searchQuery = new SearchQuery.Builder("Allows you to search for RadiologyOrder's by patient and urgency")
            .withOptionalParameters(REQUEST_PARAM_ACCESSION_NUMBER, REQUEST_PARAM_PATIENT, REQUEST_PARAM_URGENCY,
                REQUEST_PARAM_TOTAL_COUNT)
            .build();
    
    private final SearchConfig searchConfig =
            new SearchConfig("default", RestConstants.VERSION_1 + "/radiologyorder", Arrays.asList("2.0.*"), searchQuery);
    
    /**
     * @see org.openmrs.module.webservices.rest.web.resource.api.SearchHandler#getSearchConfig()
     */
    @Override
    public SearchConfig getSearchConfig() {
        
        return this.searchConfig;
    }
    
    /**
     * @see org.openmrs.module.webservices.rest.web.resource.api.SearchHandler#search()
     * @should return all radiology orders for given accession number
     * @should return empty search result if no radiology order exists for given accession number
     * @should return all radiology orders for given patient
     * @should return empty search result if patient cannot be found
     * @should return empty search result if patient has no radiology orders
     * @should return all radiology orders for given urgency
     * @should return empty search result if no radiology order exists for given urgency
     * @should throw illegal argument exception if urgency doesn't exist
     * @should return all radiology orders matching the search query and totalCount if
     *         requested
     */
    @Override
    public PageableResult search(RequestContext context) throws ResponseException {
        
        final String patientUuid = context.getRequest()
                .getParameter(REQUEST_PARAM_PATIENT);
        Patient patient = null;
        if (StringUtils.isNotBlank(patientUuid)) {
            patient = ((PatientResource1_9) Context.getService(RestService.class)
                    .getResourceBySupportedClass(Patient.class)).getByUniqueId(patientUuid);
            if (patient == null) {
                return new EmptySearchResult();
            }
        }
        
        final String urgencyString = context.getRequest()
                .getParameter(REQUEST_PARAM_URGENCY);
        Urgency urgency = null;
        if (StringUtils.isNotBlank(urgencyString)) {
            urgency = Urgency.valueOf(urgencyString);
        }
        
        final String accessionNumber = context.getRequest()
                .getParameter(REQUEST_PARAM_ACCESSION_NUMBER);
        
        final RadiologyOrderSearchCriteria radiologyOrderSearchCriteria =
                new RadiologyOrderSearchCriteria.Builder().withAccessionNumber(accessionNumber)
                        .withPatient(patient)
                        .withUrgency(urgency)
                        .build();
        
        final List<RadiologyOrder> result = radiologyOrderService.getRadiologyOrders(radiologyOrderSearchCriteria);
        
        if (result.isEmpty()) {
            return new EmptySearchResult();
        }
        return new NeedsPaging<RadiologyOrder>(result, context);
    }
}
