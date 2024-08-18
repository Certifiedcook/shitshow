package com.chargonium.ccclient.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.MaceItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class Criticals {
    private KeyBinding keyBinding;
    private MinecraftClient client = MinecraftClient.getInstance();
    private boolean criticalsEnabled = false;

    private void doPacketJump()
    {
        sendFakeY(0.0625, true);
        sendFakeY(0, false);
        sendFakeY(1.1e-5, false);
        sendFakeY(0, false);
    }

    private void sendFakeY(double offset, boolean onGround)
    {

        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY() + offset, client.player.getZ(), onGround));
    }

    public void main() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ccclient.toggle.criticals", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_C, // The keycode of the key
                "category.ccclient.combat" // The translation ey of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                criticalsEnabled = !criticalsEnabled;
                client.player.sendMessage(Text.literal(criticalsEnabled ? "Criticals enabled" : "Criticals disabled"));
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.getMainHandStack().getItem() instanceof MaceItem) return ActionResult.PASS;
            if (criticalsEnabled) doPacketJump();
            return ActionResult.PASS;
        });
    }
}
