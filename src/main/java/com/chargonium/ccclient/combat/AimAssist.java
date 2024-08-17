package com.chargonium.ccclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class AimAssist {

    private static KeyBinding keyBinding;
    private boolean aimAssistEnabled = false;
    private PlayerEntity closestPlayer;

    public static PlayerEntity getClosestPlayer(PlayerEntity clientPlayer) {
        // Get the current Minecraft client instance
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        // Initialize the closest player and the minimum distance
        PlayerEntity closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        // Iterate through all players in the world
        for (PlayerEntity player : minecraftClient.world.getPlayers()) {
            // Skip the client player (we don't want to compare the player to itself)
            if (player == clientPlayer) {
                continue;
            }

            // Calculate the distance between the client player and the current player
            double distance = clientPlayer.squaredDistanceTo(player);

            // Check if this is the closest player so far
            if (distance < minDistance) {
                minDistance = distance;
                closestPlayer = player;
            }
        }

        // Return the closest player (or null if no other players are found)
        return closestPlayer;
    }

    public void main() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (aimAssistEnabled) {
                closestPlayer = getClosestPlayer(client.player);

                if (client.player == null) return;
                if (closestPlayer == null) return;

                double dx = closestPlayer.getX() - client.player.getX();
                double dy = closestPlayer.getBodyY(0.5) - client.player.getEyeY();
                double dz = closestPlayer.getZ() - client.player.getZ();

                double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                client.player.setYaw((float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
                client.player.setPitch((float) -Math.toDegrees(Math.atan2(dy, horizontalDistance)));
            }
        });
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ccclient.toggle.aim.assist", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.ccclient.testing" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                aimAssistEnabled = !aimAssistEnabled;
            }
        });
    }

}
