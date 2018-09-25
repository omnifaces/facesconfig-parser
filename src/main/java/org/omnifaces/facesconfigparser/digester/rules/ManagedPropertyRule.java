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

import org.omnifaces.facesconfigparser.digester.beans.ManagedBeanBean;
import org.omnifaces.facesconfigparser.digester.beans.ManagedPropertyBean;
import org.omnifaces.facesconfigparser.util.ToolsUtil;
import org.xml.sax.Attributes;

/**
 * <p>
 * Digester rule for the <code>&lt;managed-property&gt;</code> element.
 * </p>
 */

public class ManagedPropertyRule extends FeatureRule {

    private static final String CLASS_NAME = "org.omnifaces.facesconfigparser.digester.beans.ManagedPropertyBean";

    // ------------------------------------------------------------ Rule Methods

    /**
     * <p>
     * Create an empty instance of <code>ManagedPropertyBean</code> and push it on to the object stack.
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

        assert digester.peek() instanceof ManagedBeanBean : "Assertion Error: Expected ManagedBeanBean to be at the top of the stack";

        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ManagedPropertyRule]{" + digester.getMatch() + "} Push " + CLASS_NAME);
        }
        Class clazz = digester.getClassLoader().loadClass(CLASS_NAME);
        ManagedPropertyBean mpb = (ManagedPropertyBean) clazz.newInstance();
        digester.push(mpb);

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
     * Pop the <code>ManagedPropertyBean</code> off the top of the stack, and either add or merge it with previous
     * information.
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

        ManagedPropertyBean top;
        try {
            top = (ManagedPropertyBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " + CLASS_NAME + " instance");
        }
        ManagedBeanBean mbb = (ManagedBeanBean) digester.peek();

        validate(mbb.getManagedBeanName(), top);

        ManagedPropertyBean old = mbb.getManagedProperty(top.getPropertyName());
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ManagedPropertyRule]{" + digester.getMatch() + "} New(" + top.getPropertyName() + ")");
            }
            mbb.addManagedProperty(top);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ManagedPropertyRule]{" + digester.getMatch() + "} Merge(" + top.getPropertyName() + ")");
            }
            mergeManagedProperty(top, old);
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

        StringBuffer sb = new StringBuffer("ManagedPropertyRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }

    // --------------------------------------------------------- Package Methods

    // Merge "top" into "old"
    static void mergeManagedProperty(ManagedPropertyBean top, ManagedPropertyBean old) {

        // Merge singleton properties
        if (top.getPropertyClass() != null) {
            old.setPropertyClass(top.getPropertyClass());
        }
        if (top.isNullValue()) {
            old.setNullValue(true);
        }
        if (top.getValue() != null) {
            old.setValue(top.getValue());
        }

        // Merge common collections
        mergeFeatures(top, old);

        // Merge unique collections
        ListEntriesRule.mergeListEntries(top, old);
        MapEntriesRule.mergeMapEntries(top, old);

    }

    // Merge "top" into "old"
    static void mergeManagedProperties(ManagedBeanBean top, ManagedBeanBean old) {

        ManagedPropertyBean[] mpb = top.getManagedProperties();
        for (int i = 0; i < mpb.length; i++) {
            ManagedPropertyBean mpbo = old.getManagedProperty(mpb[i].getPropertyName());
            if (mpbo == null) {
                old.addManagedProperty(mpb[i]);
            } else {
                mergeManagedProperty(mpb[i], mpbo);
            }
        }

    }

    // --------------------------------------------------------- Private Methods

    private void validate(String managedBeanName, ManagedPropertyBean property) {

        String managedPropertyName = property.getPropertyName();
        if (managedPropertyName == null || managedPropertyName.length() == 0) {
            throw new IllegalStateException(ToolsUtil.getMessage(ToolsUtil.MANAGED_BEAN_NO_MANAGED_PROPERTY_NAME_ID, new Object[] { managedBeanName }));
        }

        // managed-property instances that have list-entries must
        // not have value or map-entries. It is a configuration
        // error if they do.
        if (property.getListEntries() != null) {
            if (property.getMapEntries() != null || property.getValue() != null || property.isNullValue()) {

                throw new IllegalStateException(
                        ToolsUtil.getMessage(ToolsUtil.MANAGED_BEAN_LIST_PROPERTY_CONFIG_ERROR_ID, new Object[] { managedBeanName, managedPropertyName }));
            }
        }

        // managed-property instances that have map-entries, must
        // not have value or list-entries. It is a configuration
        // error if they do.
        if (property.getMapEntries() != null) {
            if (property.getValue() != null || property.isNullValue()) {
                throw new IllegalStateException(
                        ToolsUtil.getMessage(ToolsUtil.MANAGED_BEAN_MAP_PROPERTY_CONFIG_ERROR_ID, new Object[] { managedBeanName, managedPropertyName }));
            }
        }

        // If the managed property has no list or map entries, nor
        // any defined value or configured as a null value, then an error
        // will be raised
        if (property.getListEntries() == null && property.getMapEntries() == null && property.getValue() == null && !property.isNullValue()) {
            throw new IllegalStateException(
                    ToolsUtil.getMessage(ToolsUtil.MANAGED_BEAN_PROPERTY_CONFIG_ERROR_ID, new Object[] { managedBeanName, managedPropertyName }));
        }

    } // END validate

}
