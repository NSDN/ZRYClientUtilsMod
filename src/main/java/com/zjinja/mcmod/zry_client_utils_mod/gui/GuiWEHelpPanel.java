package com.zjinja.mcmod.zry_client_utils_mod.gui;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class GuiWEHelpPanel extends Screen {
    public static class WEPanelFunctionItem {
        public String Name;
        public String Command;
        public int KeyCode;

        public WEPanelFunctionItem(String name, String command, int keycode) {
            this.Name = name;
            this.Command = command;
            this.KeyCode = keycode;
        }
    }

    private static Gson GSON = new Gson();

    private ArrayList<WEPanelFunctionItem> functionList = new ArrayList<>();
    private HashMap<Integer, String> functionShortcutKeys = new HashMap<>();
    private int eachFunctionButtonWidth = 54;
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
        this.loadFunctionListFromJson();
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
        AddFunctionButtonsLoop:
        for(WEPanelFunctionItem i : this.functionList) {
            if(wXPos + this.eachFunctionButtonWidth > this.functionAreaMaxX) {
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
            Component backText = Component.translatable(i.Name);
            addRenderableWidget(new Button(
                    wXPos,
                    wYPos,
                    this.eachFunctionButtonWidth,
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
            wXPos += this.eachFunctionButtonWidth;
        }
    }

    public void loadFunctionListFromJson() {
        var resLoc = new ResourceLocation(ZRYClientUtilsMod.MODID, this.funcListJsonUri);
        var fljson = Minecraft.getInstance().getResourceManager().getResource(resLoc);
        if(fljson.isPresent()) {
            try(var file = fljson.get().open()){
                try(var isr = new InputStreamReader(file, StandardCharsets.UTF_8)){
                    JsonArray jsonArray = GSON.fromJson(isr, JsonArray.class);
                    AddElemLoop:
                    for(JsonElement ji: jsonArray) {
                        var jo = ji.getAsJsonObject();
                        if(jo != null) {
                            String name = "";
                            String command = "";
                            int keyCode = 0;
                            var rawName = jo.getAsJsonPrimitive("name");
                            if(rawName != null) {
                                name = rawName.getAsString();
                                if(name.equals("")){
                                    continue AddElemLoop;
                                }
                            }else{
                                continue AddElemLoop;
                            }
                            var cmd = jo.getAsJsonPrimitive("command");
                            if (cmd != null) {
                                command = cmd.getAsString();
                                if(command.equals("")){
                                    continue AddElemLoop;
                                }
                            }else {
                                continue AddElemLoop;
                            }
                            var rawKeycode = jo.getAsJsonPrimitive("keybind");
                            if(rawKeycode != null){
                                keyCode = rawKeycode.getAsInt();
                                if(keyCode < 0) {
                                    keyCode = 0;
                                }
                            }
                            WEPanelFunctionItem ni = new WEPanelFunctionItem(name, command, keyCode);
                            this.functionList.add(ni);
                        }else{
                            ZLogUtil.log(
                                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                                    "gui/we-panel", "failed load function: unexpected type in json."
                            );
                        }
                    }
                }
            }catch (Exception e) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "gui/we-panel", "failed load resource.", e
                );
            }
        }else {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "gui/we-panel", "failed load resource: not found."
            );
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
