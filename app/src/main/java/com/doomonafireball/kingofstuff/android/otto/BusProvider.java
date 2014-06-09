package com.doomonafireball.kingofstuff.android.otto;

/**
 * Created by derek on 6/5/14.
 */
public class BusProvider {

    private static final KingOfStuffBus BUS = new KingOfStuffBus();

    public static KingOfStuffBus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
