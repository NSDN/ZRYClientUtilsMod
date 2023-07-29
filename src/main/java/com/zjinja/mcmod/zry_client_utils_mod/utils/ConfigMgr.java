package com.zjinja.mcmod.zry_client_utils_mod.utils;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.NoFormatFoundException;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.*;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

public class ConfigMgr {
    public static ConfigMgr INSTANCE = new ConfigMgr();
    private final String ConfigDefaultResourceUri = "data/config.default.toml";

    public static class WEPanelFunctionItem {
        public String Name;
        public boolean WillTranslate;
        public String Command;
        public int KeyCode;
        public int Width;

        public WEPanelFunctionItem(String name, boolean willTranslate, String command, int keycode, int width) {
            this.Name = name;
            this.WillTranslate = willTranslate;
            this.Command = command;
            this.KeyCode = keycode;
            this.Width = width;
        }
    }

    private final ArrayList<WEPanelFunctionItem> WEPanelFunctionList = new ArrayList<>();
    private int WEPanelFunctionDefaultButtonWidth = 54;
    private File configFile;

    public static ConfigMgr getInstance() {
        return INSTANCE;
    }

    public ConfigMgr() {
    }

    public void init() {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                "cfg-mgr", "Initializing config manager..."
        );
        var mc = Minecraft.getInstance();
        var pathCfgRoot = new File(mc.gameDirectory, "config");
        if((!pathCfgRoot.exists()) || (!pathCfgRoot.isDirectory())) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "cfg-mgr", "minecraft config directory not exists."
            );
            return;
        }
        var pathModCfg = new File(pathCfgRoot, "zry_client_utils.toml");
        if (!pathModCfg.exists()) {
            if(!makeDefaultConfig(pathModCfg, ConfigDefaultResourceUri)) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "cfg-mgr", "config 'zry_client_utils.toml' not exists and failed create default one."
                );
                return;
            }else{
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.INFO,
                        "cfg-mgr", "config 'zry_client_utils.toml' not exists yet, so default one is created now."
                );
            }
        }
        this.configFile = pathModCfg;
        loadConfigFromFile();
    }

    private boolean makeDefaultConfig(File path, String resourceName) {
        var resLoc = new ResourceLocation(ZRYClientUtilsMod.MODID, resourceName);
        var fljson = Minecraft.getInstance().getResourceManager().getResource(resLoc);
        if(fljson.isEmpty()){
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "cfg-mgr/create-default", "failed load resource: '{}:{}': NotFound.",
                    ZRYClientUtilsMod.MODID, resourceName
            );
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(path)){
            try(var rfile = fljson.get().open()){
                var inCh = Channels.newChannel(rfile);
                fos.getChannel().transferFrom(inCh, 0, rfile.available());
                return true;
            }catch (Exception e){
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "cfg-mgr/create-default", "failed write file '{}': {}",
                        path.getPath(), e
                );
            }
        } catch (IOException e) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "cfg-mgr/create-default", "failed open file '{}': IOException: {}",
                    path.getPath(), e
            );
        }
        return false;
    }

    public WEPanelFunctionItem[] getWEPanelFunctionList() {
        WEPanelFunctionItem[] a = new WEPanelFunctionItem[this.WEPanelFunctionList.size()];
        this.WEPanelFunctionList.toArray(a);
        return a;
    }

    protected void loadConfigFromFile(){
        reloadConfig();
    }

    public boolean reloadConfig() {
        File fCfg = this.configFile;
        if(fCfg == null) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "cfg-mgr/load-cfg", "failed load config:  ConfigMgr not initialized."
            );
            return false;
        }
        boolean isOk = true;
        try(FileConfig cfg = FileConfig.of(fCfg)){
            cfg.load();
            List<Config> wePanelItemsCfg = cfg.get("we_panel.item");
            if(wePanelItemsCfg != null) {
                this.loadWEPanelFunctionList(wePanelItemsCfg);
            }else{
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "cfg-mgr/load-cfg", "failed load config: key 'we_panel.item' is null."
                );
                isOk = false;
            }
            this.WEPanelFunctionDefaultButtonWidth = cfg.getOrElse("we_panel.gui.defaultButtonWidth", 54);
            if(this.WEPanelFunctionDefaultButtonWidth <= 0){
                this.WEPanelFunctionDefaultButtonWidth = 54;
            }
            this.changeLogLevel(cfg.getOrElse("log.level", "info"));
        }catch (NoFormatFoundException e) {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                    "cfg-mgr/load-cfg", "failed load '{}': NoFormatFoundException: {}",
                    fCfg.getPath() ,e.getMessage()
            );
            isOk = false;
        }
        return isOk;
    }

    private void changeLogLevel(String lvname) {
        switch (lvname) {
            case "trace" -> ZLogUtil.modLogLevelFilter = ZLogUtil.Level.TRACE;
            case "debug" -> ZLogUtil.modLogLevelFilter = ZLogUtil.Level.DEBUG;
            case "info" -> ZLogUtil.modLogLevelFilter = ZLogUtil.Level.INFO;
            case "warn" -> ZLogUtil.modLogLevelFilter = ZLogUtil.Level.WARN;
            case "error" -> ZLogUtil.modLogLevelFilter = ZLogUtil.Level.ERROR;
            default -> {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.ERROR,
                        "cfg-mgr/load-cfg",
                        "unrecognized 'log.level': '{}'. will use 'info' as default",
                        lvname
                );
                ZLogUtil.modLogLevelFilter = ZLogUtil.Level.INFO;
            }
        }
    }

    private void loadWEPanelFunctionList(List<Config> cfg){
        this.WEPanelFunctionList.clear();
        WEPanelFunctionItemLoop:
        for(Config i : cfg){
            String name = i.getOrElse("name", "");
            if(name.equals("")) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "cfg-mgr/load-cfg", "we_panel.item[].name should not be empty. ignored."
                );
                continue WEPanelFunctionItemLoop;
            }
            Boolean willTranslate = i.getOrElse("willTranslate", false);
            String command = i.getOrElse("command", "");
            if(command.equals("")) {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "cfg-mgr/load-cfg", "we_panel.item[name='{}'].command should not be empty."
                );
                continue WEPanelFunctionItemLoop;
            }
            Integer keyBind = i.getOrElse("keybind", 0);
            if(keyBind < 0) {
                keyBind = 0;
            }
            Integer width = i.getOrElse("width", this.WEPanelFunctionDefaultButtonWidth);
            if(keyBind <= 0) {
                keyBind = this.WEPanelFunctionDefaultButtonWidth;
            }
            WEPanelFunctionItem pfi = new WEPanelFunctionItem(name, willTranslate, command, keyBind, width);
            this.WEPanelFunctionList.add(pfi);
        }
    }
}
