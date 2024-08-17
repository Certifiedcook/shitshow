package com.chargonium.ccclient;

import com.chargonium.ccclient.combat.AimAssist;
import net.fabricmc.api.ClientModInitializer;
import com.chargonium.ccclient.utils.ModuleLoader;


public class SodiumExtrasClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModuleLoader moduleLoader = new ModuleLoader();

        moduleLoader.registerModule(new AimAssist());
    }
}