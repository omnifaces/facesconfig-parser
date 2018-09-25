/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.omnifaces.facesconfigparser.digester.beans;

/**
 * <p>
 * Configuration bean for <code>&lt;referenced-bean&gt;</code> element.
 * </p>
 */

public class ReferencedBeanBean extends FeatureBean {

    // -------------------------------------------------------------- Properties

    private String referencedBeanClass;

    public String getReferencedBeanClass() {
        return referencedBeanClass;
    }

    public void setReferencedBeanClass(String referencedBeanClass) {
        this.referencedBeanClass = referencedBeanClass;
    }

    private String referencedBeanName;

    public String getReferencedBeanName() {
        return referencedBeanName;
    }

    public void setReferencedBeanName(String referencedBeanName) {
        this.referencedBeanName = referencedBeanName;
    }

    // -------------------------------------------------------------- Extensions

    // ----------------------------------------------------------------- Methods

}
