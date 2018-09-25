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
import org.omnifaces.facesconfigparser.digester.beans.MapEntriesBean;
import org.omnifaces.facesconfigparser.digester.beans.MapEntriesHolder;
import org.omnifaces.facesconfigparser.digester.beans.MapEntryBean;
import org.xml.sax.Attributes;

/**
 * <p>
 * Digester rule for the <code>&lt;map-entries&gt;</code> element.
 * </p>
 */

public class MapEntriesRule extends Rule {

    private static final String CLASS_NAME = "org.omnifaces.facesconfigparser.digester.beans.MapEntriesBean";

    // ------------------------------------------------------------ Rule Methods

    /**
     * <p>
     * Create an empty instance of <code>MapEntriesBean</code> and push it on to the object stack.
     * </p>
     *
     * @param namespace the namespace URI of the matching element, or an empty string if the parser is not namespace aware
     * or the element has no namespace
     * @param name the local name if the parser is namespace aware, or just the element name otherwise
     * @param attributes The attribute map of this element
     *
     * @exception IllegalStateException if the parent stack element is not of type MapEntriesHolder
     */
    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {

        assert digester.peek() instanceof MapEntriesHolder : "Assertion Error: Expected MapEntriesHolder to be at the top of the stack";

        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[MapEntriesRule]{" + digester.getMatch() + "} Push " + CLASS_NAME);
        }
        Class clazz = digester.getClassLoader().loadClass(CLASS_NAME);
        MapEntriesBean meb = (MapEntriesBean) clazz.newInstance();
        digester.push(meb);

    }

    /**
     * <p>
     * No body processing is requlred.
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
     * Pop the <code>MapEntriesBean</code> off the top of the stack, and either add or merge it with previous information.
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

        MapEntriesBean top = null;
        try {
            top = (MapEntriesBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " + CLASS_NAME + " instance");
        }
        MapEntriesHolder meh = (MapEntriesHolder) digester.peek();
        MapEntriesBean old = meh.getMapEntries();
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[MapEntriesRule]{" + digester.getMatch() + "} New");
            }
            meh.setMapEntries(top);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ManagedBeanRule]{" + digester.getMatch() + "} Merge");
            }
            mergeMapEntries(top, old);
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

        StringBuffer sb = new StringBuffer("MapEntriesRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }

    // --------------------------------------------------------- Package Methods

    // Merge "top" into "old"
    static void mergeMapEntries(MapEntriesBean top, MapEntriesBean old) {

        // Merge singleton properties
        if (top.getKeyClass() != null) {
            old.setKeyClass(top.getKeyClass());
        }
        if (top.getValueClass() != null) {
            old.setValueClass(top.getValueClass());
        }

        // Merge common collections

        // Merge unique collections
        MapEntryBean mapEntries[] = top.getMapEntries();
        for (int i = 0; i < mapEntries.length; i++) {
            old.addMapEntry(mapEntries[i]);
        }

    }

    // Merge "top" into "old"
    static void mergeMapEntries(MapEntriesHolder top, MapEntriesHolder old) {

        MapEntriesBean mebt = top.getMapEntries();
        if (mebt != null) {
            MapEntriesBean mebo = old.getMapEntries();
            if (mebo != null) {
                mergeMapEntries(mebt, mebo);
            } else {
                old.setMapEntries(mebt);
            }
        }

    }

}
