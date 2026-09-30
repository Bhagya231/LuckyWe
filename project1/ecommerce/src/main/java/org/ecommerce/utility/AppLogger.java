package org.ecommerce.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger
{
    private AppLogger() {

    }

    public static Logger getLogger(Class<?> clazz)
    {
        return LoggerFactory.getLogger(clazz);
    }
}
