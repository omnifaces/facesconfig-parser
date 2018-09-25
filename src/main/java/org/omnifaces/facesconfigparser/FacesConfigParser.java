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
package org.omnifaces.facesconfigparser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.digester.Digester;
import org.omnifaces.facesconfigparser.digester.DigesterFactory;
import org.omnifaces.facesconfigparser.digester.beans.FacesConfigBean;
import org.omnifaces.facesconfigparser.digester.rules.FacesConfigRuleSet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parser a faces config file into a tree of Java objects.
 *
 * <p>
 * This is based on the Mojarra JSF Tools sub-project.
 *
 */
public class FacesConfigParser {


    // ---------------------------------------------------------- Public Methods


    /**
     * Starts the parsing of a faces config file into a tree of Java objects.
     *
     * @param facesConfig the faces config filename to parse, relatively to the current directory or as an absolute path.
     * @param schemaDirectory directory that stores the .dtd and .xsd files referenced by faces-config.xml. When null the classpath is
     * tried. When a referenced schema file is not on the classpath standard entity resolution will be attempted. See
     * {@link DigesterFactory} and its inner class <code>JsfEntityResolver</code>
     *
     * @return the faces-config file as a tree of Java objects rooted in the returned <code>FacesConfigBean</code>
     */
    public static FacesConfigBean parseFacesConfig(String facesConfig, String schemaDirectory) {
        try (InputStream stream = newInputStream(facesConfig)) {
            return (FacesConfigBean)
                    createDigester(true, false, true, schemaDirectory)
                        .parse(newInputSource(stream, facesConfig));
        } catch (IOException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     * Configure and return a <code>Digester</code> instance suitable for use in the
     * environment specified by our parameter flags.
     * </p>
     *
     * @param design Include rules suitable for design time use in a tool
     * @param generate Include rules suitable for generating component, renderer,
     * and tag classes
     * @param runtime Include rules suitable for runtime execution
     */
    private static Digester createDigester(boolean design, boolean generate, boolean runtime, String schemaDirectory) {

        Digester digester = DigesterFactory.newInstance(true, schemaDirectory).createDigester();

        // Configure parsing rules
        digester.addRuleSet(new FacesConfigRuleSet(design, generate, runtime));

        return digester;
    }

    private static InputStream newInputStream(String fileName) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(fileName));
    }

    private static InputSource newInputSource(InputStream stream, String fileName) throws MalformedURLException {
        InputSource source = new InputSource(new File(fileName).toURI().toURL().toString());
        source.setByteStream(stream);

        return source;
    }

}
