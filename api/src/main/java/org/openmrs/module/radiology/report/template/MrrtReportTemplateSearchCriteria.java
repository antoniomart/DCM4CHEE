/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology.report.template;

/**
 * The search parameter object for {@code MrrtReportTemplate}.
 */
public class MrrtReportTemplateSearchCriteria {
    
    
    private final String title;
    
    /**
     * @return the title of the mrrt report template
     */
    public String getTitle() {
        
        return title;
    }
    
    public static class Builder {
        
        
        private String title;
        
        /**
         * @param title the title of the mrrt report template
         * @return this builder instance
         */
        public Builder withTitle(String title) {
            
            this.title = title;
            return this;
        }
        
        /**
         * Creates an {@code MrrtReportTemplateSearchCriteria} with properties of this builder instance.
         * 
         * @return a new search criteria instance
         * @should create an mrrt report template search criteria instance with title if title is set
         */
        public MrrtReportTemplateSearchCriteria build() {
            
            return new MrrtReportTemplateSearchCriteria(this);
        }
    }
    
    private MrrtReportTemplateSearchCriteria(Builder builder) {
        
        this.title = builder.title;
    }
}
