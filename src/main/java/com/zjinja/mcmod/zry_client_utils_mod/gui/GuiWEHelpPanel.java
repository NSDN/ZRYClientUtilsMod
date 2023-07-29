package com.zjinja.mcmod.zry_client_utils_mod.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.keybinds.KeyCodeTranslate;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ConfigMgr;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.HashMap;

public class GuiWEHelpPanel extends Screen {
    private final HashMap<Integer, String> functionShortcutKeys = new HashMap<>();
    private int functionAreaMaxX;
    private int functionAreaMaxY;

    private final String funcListJsonUri = "data/we_panel_func.json";

    private int exitKeyCode = GLFW.GLFW_KEY_V;

    public GuiWEHelpPanel() {
        super(Component.translatable("gui.zry_client_utils.we_panel.title"));
    }
    @Override
    public void init() {
        super.init();
        for (KeyMapping kb : Minecraft.getInstance().options.keyMappings) {
            if (kb.getName().equals("key.zry_client_utils_mod.we_panel")) {
                this.exitKeyCode = kb.getKey().getValue();
            }
        }
        this.functionAreaMaxX = this.width - 24;
        this.functionAreaMaxY = this.height - 22;
        {
            Component backText = Component.translatable("gui.zry_client_utils.we_panel.back");
            addRenderableWidget(new Button(
                    4,
                    4,
                    36,
                    20,
                    backText,
                    button -> {
                        this.closePanel();
                    },
                    (button, poseStack, n1, n2) -> {

                    }
                )
            {});
        }
        int wXPos = 24, wYPos = 32;
        var cfgm = ConfigMgr.getInstance();
        ConfigMgr.WEPanelFunctionItem[] flist;
        if(cfgm != null){
            flist = cfgm.getWEPanelFunctionList();
        }else{
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "gui/we-panel", "Failed get ConfigMgr: is null."
            );
            flist = new ConfigMgr.WEPanelFunctionItem[0];
        }
        AddFunctionButtonsLoop:
        for(ConfigMgr.WEPanelFunctionItem i : flist) {
            if(wXPos + i.Width > this.functionAreaMaxX) {
                wYPos += 20;
                wXPos = 24;
            }
            if (wYPos + 20 > this.functionAreaMaxY) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "gui/we-panel", "Too many function items."
                );
                break AddFunctionButtonsLoop;
            }
            String keyTip;
            if(i.KeyCode > 0) {
                String rKeyTip = KeyCodeTranslate.getKeyNameByKeyCode(i.KeyCode);
                if(rKeyTip.equals("")){
                    keyTip = " (<?>)";
                }else{
                    keyTip = String.format(" (%s)", rKeyTip);
                }
            }else{
                keyTip = "";
            }
            Component backText;
            if(i.WillTranslate){
                if(keyTip.equals("")){
                    backText = Component.translatable(i.Name);
                }else{
                    backText = Component.translatable(i.Name).append(Component.literal(keyTip));
                }
            }else{
                if(keyTip.equals("")){
                    backText = Component.literal(i.Name);
                }else{
                    backText = Component.literal(i.Name).append(Component.literal(keyTip));
                }
            }
            addRenderableWidget(new Button(
                    wXPos,
                    wYPos,
                    i.Width,
                    20,
                    backText,
                    button -> {
                        issueCmd(i.Command);
                    },
                    (button, poseStack, n1, n2) -> {

                    }
            )
            {});
            if(i.KeyCode > 0) {
                this.functionShortcutKeys.put(i.KeyCode, i.Command);
            }
            wXPos += i.Width;
        }
    }

    public void issueCmd(String cmd) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.commandUnsigned(cmd);
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void closePanel() {
        KeyMapping.releaseAll();
        Minecraft.getInstance().setScreen(null);
        //Minecraft.getInstance().cursorEntered();
    }

    @Override
    public boolean keyPressed(int keycode, int param1, int modkey){
        if( modkey == 0) {
            if (keycode == this.exitKeyCode) {
                this.closePanel();
                return true;
            }
            if (this.functionShortcutKeys.containsKey(keycode)) {
                this.issueCmd(this.functionShortcutKeys.get(keycode));
            }
        }
        return super.keyPressed(keycode, param1, modkey);
    }
}
