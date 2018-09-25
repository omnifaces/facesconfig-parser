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

import org.omnifaces.facesconfigparser.digester.beans.ComponentBean;
import org.omnifaces.facesconfigparser.digester.beans.FacesConfigBean;
import org.xml.sax.Attributes;

/**
 * <p>
 * Digester rule for the <code>&lt;component&gt;</code> element.
 * </p>
 */

public class ComponentRule extends FeatureRule {

    private static final String CLASS_NAME = "org.omnifaces.facesconfigparser.digester.beans.ComponentBean";

    // ------------------------------------------------------------ Rule Methods

    /**
     * <p>
     * Create an empty instance of <code>ComponentBean</code> and push it on to the object stack.
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

        assert digester.peek() instanceof FacesConfigBean : "Assertion Error: Expected FacesConfigBean to be at the top of the stack";

        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ComponentRule]{" + digester.getMatch() + "} Push " + CLASS_NAME);
        }
        Class<?> clazz = digester.getClassLoader().loadClass(CLASS_NAME);
        ComponentBean cb = (ComponentBean) clazz.newInstance();
        digester.push(cb);
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
     * Pop the <code>ComponentBean</code> off the top of the stack, and either add or merge it with previous information.
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

        ComponentBean top = null;
        try {
            top = (ComponentBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " + CLASS_NAME + " instance");
        }
        FacesConfigBean fcb = (FacesConfigBean) digester.peek();
        ComponentBean old = fcb.getComponent(top.getComponentType());
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ComponentRule]{" + digester.getMatch() + "} New(" + top.getComponentType() + ")");
            }
            fcb.addComponent(top);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ComponentRule]{" + digester.getMatch() + "} Merge(" + top.getComponentType() + ")");
            }
            mergeComponent(top, old);
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

        StringBuffer sb = new StringBuffer("ComponentRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }

    // --------------------------------------------------------- Package Methods

    // Merge "top" into "old"
    static void mergeComponent(ComponentBean top, ComponentBean old) {

        // Merge singleton properties
        if (top.getComponentClass() != null) {
            old.setComponentClass(top.getComponentClass());
        }
        if (top.getBaseComponentType() != null) {
            old.setBaseComponentType(top.getBaseComponentType());
        }
        if (top.getComponentFamily() != null) {
            old.setComponentFamily(top.getComponentFamily());
        }
        if (top.getRendererType() != null) {
            old.setRendererType(top.getRendererType());
        }

        // Merge common collections
        AttributeRule.mergeAttributes(top, old);
        mergeFeatures(top, old);
        PropertyRule.mergeProperties(top, old);

        // Merge unique collections

    }

}
