package com.chargonium.ccclient;

import com.chargonium.ccclient.combat.AimAssist;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


public class SodiumExtrasClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AimAssist aimAssist = new AimAssist();
        aimAssist.main();
    }
}