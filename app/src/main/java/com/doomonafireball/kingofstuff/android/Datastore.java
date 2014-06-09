package com.doomonafireball.kingofstuff.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

@Singleton
public class Datastore {

    public static final int PROVIDER_WIKIPEDIA = 0x00;
    public static final int PROVIDER_WIKTIONARY = 0x01;
    private static final String DEVICE_VERSION = "DeviceVersion";
    private static final String CLUE_PROVIDER = "ClueProvider";
    EncryptedSharedPreferences encryptedSharedPreferences;

    public Datastore(Application app) {
        encryptedSharedPreferences = new EncryptedSharedPreferences(app,
                PreferenceManager.getDefaultSharedPreferences(app));
    }

    private SharedPreferences.Editor getEditor() {
        return encryptedSharedPreferences.edit();
    }

    private SharedPreferences getPrefs() {
        return encryptedSharedPreferences;
    }

    public int getVersion() {
        return getPrefs().getInt(DEVICE_VERSION, 0);
    }

    public void persistVersion(int version) {
        getEditor().putInt(DEVICE_VERSION, version).commit();
    }

    public int getProvider() {
        return getPrefs().getInt(CLUE_PROVIDER, PROVIDER_WIKIPEDIA);
    }

    public void persistProvider(int provider) {
        getEditor().putInt(CLUE_PROVIDER, provider).commit();
    }
}

