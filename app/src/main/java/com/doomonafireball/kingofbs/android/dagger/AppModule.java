package com.doomonafireball.kingofbs.android.dagger;

import android.app.Application;
import android.content.Context;

import com.doomonafireball.kingofbs.android.Datastore;
import com.doomonafireball.kingofbs.android.MainApp;
import com.doomonafireball.kingofbs.android.activity.MainActivity;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {
        Injector.class,
        MainApp.class,
        MainActivity.class
}, library = true, complete = true)
public class AppModule {
    private final Application application;
    private final Injector injector;

    public AppModule(Application application) {
        this.application = application;
        this.injector = new Injector(application);
    }

    /**
     * Allow the application context to be injected but require that it be annotated with {@link
     * ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Injector providesInjector() {
        return injector;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        return httpClient;
    }

    @Provides
    @Singleton
    Datastore providesDatastore() {
        return new Datastore(application);
    }

}

