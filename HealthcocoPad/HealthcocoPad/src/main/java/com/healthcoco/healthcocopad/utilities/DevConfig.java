package com.healthcoco.healthcocopad.utilities;

import android.util.Log;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class DevConfig {
    /**
     * Builds
     * sets which server build it is
     */
    public static final BuildType buildType = BuildType.DEV;
    /**ss
     * Set it to true to enable forms in application pre-filled with data
     */
    public static final boolean PRE_FILLED_FORM = buildType.isPreFilledForm();

    /**
     * Set it to true to enable forms in application pre-filled with data
     */
//    public static final boolean PRE_FILLED_FORM = buildType.isPreFilledForm();

    /**
     * Set it to true to enable logging. If set to false logging will be decided
     * using {@link Log#isLoggable(String, int)}
     */
    public static final boolean LOGGING_ENABLED = buildType.isLoggingEnabled();

}
