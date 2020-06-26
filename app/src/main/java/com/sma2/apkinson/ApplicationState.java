package com.sma2.apkinson;

/**
 * Retrieve the application state via ApplicationState.appUnderDevelopment()
 * Change app state here to switch between dev and release
 *
 * For more keys, add new static methods.
 *
 * MAKE SURE TO TURN OFF DEV-MODE FOR RELEASE VERSION!
 */
public class ApplicationState {
    private static boolean APP_STATE_DEVELOPMENT = false;

    public static boolean appUnderDevelopment(){return APP_STATE_DEVELOPMENT;}
}
