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

package org.omnifaces.facesconfigparser.digester.rules;

import org.apache.commons.digester.Rule;
import org.omnifaces.facesconfigparser.digester.beans.FacesConfigBean;
import org.omnifaces.facesconfigparser.digester.beans.LifecycleBean;
import org.xml.sax.Attributes;

/**
 * <p>
 * Digester rule for the <code>&lt;lifecycle&gt;</code> element.
 * </p>
 */

public class LifecycleRule extends Rule {

    private static final String CLASS_NAME = "org.omnifaces.facesconfigparser.digester.beans.LifecycleBean";

    // ------------------------------------------------------------ Rule Methods

    /**
     * <p>
     * Create or retrieve an instance of <code>LifecycleBean</code> and push it on to the object stack.
     * </p>
     *
     * @param namespace the namespace URI of the matching element, or an empty string if the parser is not namespace aware
     * or the element has no namespace
     * @param name the local name if the parser is namespace aware, or just the element name otherwise
     * @param attributes The attribute list of this element
     *
     * @exception IllegalStateException if the parent stack element is not of type FacesConfigBean
     */
    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {

        FacesConfigBean fcb = null;
        try {
            fcb = (FacesConfigBean) digester.peek();
        } catch (Exception e) {
            throw new IllegalStateException("No parent FacesConfigBean on object stack");
        }
        LifecycleBean lb = fcb.getLifecycle();
        if (lb == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[LifecycleRule]{" + digester.getMatch() + "} New " + CLASS_NAME);
            }
            Class clazz = digester.getClassLoader().loadClass(CLASS_NAME);
            lb = (LifecycleBean) clazz.newInstance();
            fcb.setLifecycle(lb);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[LifecycleRule]{" + digester.getMatch() + "} Old " + CLASS_NAME);
            }
        }
        digester.push(lb);

    }

    /**
     * <p>
     * No body processing is required.
     * </p>
     *
     * @param namespace the namespace URI of the matching element, or an empty string if the parser is not namespace aware
     * or the element has no namespace
     * @param name the local name if the parser is namespace aware, or just the element name otherwise
     * @param text The text of the body of this element
     */
    @Override
    public void body(String namespace, String name, String text) throws Exception {
    }

    /**
     * <p>
     * Pop the <code>LifecycleBean</code> off the top of the stack.
     * </p>
     *
     * @param namespace the namespace URI of the matching element, or an empty string if the parser is not namespace aware
     * or the element has no namespace
     * @param name the local name if the parser is namespace aware, or just the element name otherwise
     *
     * @exception IllegalStateException if the popped object is not of the correct type
     */
    @Override
    public void end(String namespace, String name) throws Exception {

        Object top = digester.pop();
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[LifecycleRule]{" + digester.getMatch() + "} Pop " + top.getClass());
        }
        if (!CLASS_NAME.equals(top.getClass().getName())) {
            throw new IllegalStateException("Popped object is not a " + CLASS_NAME + " instance");
        }

    }

    /**
     * <p>
     * No finish processing is required.
     * </p>
     *
     */
    @Override
    public void finish() throws Exception {
    }

    // ---------------------------------------------------------- Public Methods

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer("LifecycleRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }

}
