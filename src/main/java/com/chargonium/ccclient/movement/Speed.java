package com.chargonium.ccclient.movement;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Speed {
    private KeyBinding keyBinding;
    private boolean speedEnabled = false;

    public void main() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ccclient.toggle.speed", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_C, // The keycode of the key
                "category.ccclient.movement" // The translation ey of the keybinding's category.
        ));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                speedEnabled = !speedEnabled;
                client.player.sendMessage(Text.literal(speedEnabled ? "Speed enabled" : "Speed disabled"));
            }

            if (speedEnabled) {
                if (client.player == null) return;
                if (client.player.isSneaking() || client.player.forwardSpeed == 0 && client.player.sidewaysSpeed == 0) return;
                if (client.player.forwardSpeed > 0 && !client.player.horizontalCollision) client.player.setSprinting(true);
                if (!client.player.isOnGround()) return;

                Vec3d v = client.player.getVelocity();
                client.player.setVelocity(v.x * 1.8, v.y, v.z * 1.8);

                v = client.player.getVelocity();
                double currentSpeed = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.z, 2));
                double maxSpeed = 1;

                if(currentSpeed > maxSpeed) client.player.setVelocity(v.x / currentSpeed * maxSpeed, v.y, v.z / currentSpeed * maxSpeed);
            }
        });
    }
}
