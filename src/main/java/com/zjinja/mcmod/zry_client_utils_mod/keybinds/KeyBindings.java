package com.zjinja.mcmod.zry_client_utils_mod.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY = "key.category.zry_client_utils_mod";
    public static final String KEY_WE_PANEL = "key.zry_client_utils_mod.we_panel";

    public static final KeyMapping KEY_MAPPING_WE_PANEL =
            new KeyMapping(KEY_WE_PANEL, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY);
}
