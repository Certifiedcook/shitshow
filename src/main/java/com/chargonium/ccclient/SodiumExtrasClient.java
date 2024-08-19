package com.chargonium.ccclient;

import com.chargonium.ccclient.combat.AimAssist;
import com.chargonium.ccclient.combat.Criticals;
import com.chargonium.ccclient.combat.KillAura;
import com.chargonium.ccclient.combat.TriggerBot;
import com.chargonium.ccclient.movement.Speed;
import com.chargonium.ccclient.utils.ModuleLoader;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


public class SodiumExtrasClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModuleLoader moduleLoader = new ModuleLoader();

        // Combat Modules
        moduleLoader.registerModule(new TriggerBot(), "initialize");
        moduleLoader.registerModule(new AimAssist());
        moduleLoader.registerModule(new KillAura());
        moduleLoader.registerModule(new Criticals());

        //Movement Modules
        moduleLoader.registerModule(new Speed());

    }
}