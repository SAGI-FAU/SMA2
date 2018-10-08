package com.sma2.sma2;

/**
 * Retrieve the application state via ApplicationState.appUnderDevelopment()
 * Change app state here to switch between dev and release
 *
 * For more keys, add new static methods.
 */
public class ApplicationState {
    private static boolean APP_STATE_DEVELOPMENT = true;

    public static boolean appUnderDevelopment(){return APP_STATE_DEVELOPMENT;}
}
