/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is Content Registry 2.0.
 *
 * The Initial Owner of the Original Code is European Environment
 * Agency.  Portions created by Tieto Eesti are Copyright
 * (C) European Environment Agency.  All Rights Reserved.
 *
 * Contributor(s):
 * Jaanus Heinlaid, Tieto Eesti
 */
package eionet.doc.config;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author heinljab
 *
 */
public class GeneralConfig {

    /** */
    public static final String PROPERTIES_FILE_NAME = "doc.properties";

    /** */
    public static final String DB_DRV = "db.drv";
    public static final String DB_URL = "db.url";
    public static final String DB_USR = "db.usr";
    public static final String DB_PWD = "db.pwd";

    public static final String FILESTORE_PATH = "doc.files.folder";

    /** */
    private static Log logger = LogFactory.getLog(GeneralConfig.class);

    /** */
    private static Properties properties = null;

    /** */
    private static void init() {
        properties = new Properties();
        try {
            properties.load(GeneralConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));

            // trim all the values (i.e. we don't allow preceding or trailing
            // white space in property values)
            for (Entry<Object, Object> entry : properties.entrySet()) {
                entry.setValue(entry.getValue().toString().trim());
            }

        } catch (IOException e) {
            logger.fatal("Failed to load properties from " + PROPERTIES_FILE_NAME, e);
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static synchronized String getProperty(String key) {

        if (properties == null)
            init();

        return properties.getProperty(key);
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static synchronized String getProperty(String key, String defaultValue) {

        if (properties == null)
            init();

        return properties.getProperty(key, defaultValue);
    }

    /**
     * Returns integer property.
     *
     * @param key - property key in the properties file
     * @param defaultValue - default value that is returned if not specified or in incorrect format
     * @return property value or default if not specified correctly
     */
    public static synchronized int getIntProperty(final String key, final int defaultValue) {

        if (properties == null) {
            init();
        }
        String propValue = properties.getProperty(key);
        int value = defaultValue;
        if (propValue != null) {
            try {
                value = Integer.valueOf(propValue);
            } catch (Exception e) {
                logger.warn("Property " + key + " is defined incorrectly in props file: '" + propValue + "', returning default.");
            }
        }

        return value;
    }

    /**
     *
     * @param key
     * @return
     * @throws ConfigException
     */
    public static synchronized String getRequiredProperty(String key) throws Exception {

        String value = getProperty(key);
        if (value == null || value.trim().length() == 0)
            throw new Exception("Missing required property: " + key);
        else
            return value;
    }

    /**
     *
     * @return
     */
    public static synchronized Properties getProperties() {

        if (properties == null)
            init();

        return properties;
    }
}
