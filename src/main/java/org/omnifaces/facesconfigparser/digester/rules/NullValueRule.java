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
import org.omnifaces.facesconfigparser.digester.beans.NullValueHolder;
import org.xml.sax.Attributes;

/**
 * <p>
 * Digester rule for the <code>&lt;null-value&gt;</code> element.
 * </p>
 */

public class NullValueRule extends Rule {

    // ------------------------------------------------------------ Rule Methods

    /**
     * <p>
     * Set the <code>nullValue</code> property to null on the <code>NullValueHolder</code> at the top of the stack.
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

        NullValueHolder nvh = null;
        try {
            nvh = (NullValueHolder) digester.peek();
        } catch (Exception e) {
            throw new IllegalStateException("No parent NullValueHolder on object stack");
        }
        nvh.setNullValue(true);
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[NullValueRule]{" + digester.getMatch() + "} Set");
        }

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
     * No end processing is required.
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

        StringBuffer sb = new StringBuffer("NullValueRule[]");
        return (sb.toString());

    }

}
