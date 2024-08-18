package com.chargonium.ccclient;

import com.chargonium.ccclient.combat.AimAssist;
import com.chargonium.ccclient.combat.Criticals;
import com.chargonium.ccclient.combat.TriggerBot;
import com.chargonium.ccclient.utils.ModuleLoader;
import net.fabricmc.api.ClientModInitializer;


public class SodiumExtrasClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModuleLoader moduleLoader = new ModuleLoader();
        moduleLoader.registerModule(new TriggerBot(), "initialize");
        moduleLoader.registerModule(new AimAssist());
        moduleLoader.registerModule(new Criticals());

    }
}