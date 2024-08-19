package com.chargonium.ccclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW; // (very cool source)

public class KillAura {

    private KeyBinding keyBinding;
    private boolean killAuraEnabled = false;
    private MinecraftClient client = MinecraftClient.getInstance();

    private boolean isWeapon(Item item) {
        return item instanceof SwordItem || item instanceof AxeItem || item instanceof MaceItem;
    }

    private boolean isWeaponReady() {
        assert client.player != null;
        float cooldownProgress = client.player.getAttackCooldownProgress(0.0F);
        return cooldownProgress >= 1.0F;
    }

    private PlayerEntity getClosestPlayer(PlayerEntity clientPlayer, double maxDistance) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        PlayerEntity closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        if (minecraftClient.world == null) return null;
        for (PlayerEntity player : minecraftClient.world.getPlayers()) {
            if (player == clientPlayer) {
                continue;
            }

            double distance = clientPlayer.distanceTo(player);
            if (distance > maxDistance) continue;
            if (distance < minDistance) {
                minDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }

    public void main() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (killAuraEnabled) {
                PlayerEntity closestPlayer = getClosestPlayer(client.player, 4);

                if (client.player == null) return;
                if (closestPlayer == null) return;

                ItemStack weapon = client.player.getMainHandStack();
                if (isWeapon(weapon.getItem()) && isWeaponReady()) {

                    AttackEntityCallback.EVENT.invoker().interact(client.player, client.world, client.player.getActiveHand(), closestPlayer, null);
                    client.player.networkHandler.sendPacket(
                            PlayerInteractEntityC2SPacket.attack(closestPlayer, client.player.isSneaking())
                    );
                    client.player.swingHand(Hand.MAIN_HAND);
                    client.player.attack(closestPlayer);
                }

            }
        });
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ccclient.toggle.kill.aura", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_K, // The keycode of the key
                "category.ccclient.combat" // The translation ey of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                killAuraEnabled = !killAuraEnabled;
                client.player.sendMessage(Text.literal(killAuraEnabled ? "KillAura enabled" : "KillAura disabled"));
            }
        });
    }
}
