package com.example.ilgwon.alarmbomb.module_decibel_meter;

public class DecibelData {
    public static float dbCount = 40;
    public static float minDB = 100;
    public static float maxDB = 0;
    public static float lastDbCount = dbCount;
    private static float min = 0.5f;  //Set the minimum sound change
    private static float value = 0;   // Sound decibel value

    /**
     * Setting Decibel
     */
    public static void setDbCount(float dbValue) {
        // whether received dbValue is bigger than current one or not.
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        } else {
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        //  To prevent the sound from changing too fast
        dbCount = lastDbCount + value * 0.2f;

        // setting current decibel, min, max decibel.
        lastDbCount = dbCount;
        if (dbCount < minDB) minDB = dbCount;
        if (dbCount > maxDB) maxDB = dbCount;
    }

}