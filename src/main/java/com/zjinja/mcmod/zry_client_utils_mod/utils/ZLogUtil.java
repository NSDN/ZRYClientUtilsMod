package com.zjinja.mcmod.zry_client_utils_mod.utils;

import com.zjinja.mcmod.zry_client_utils_mod.ZRYClientUtilsMod;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

public class ZLogUtil {
    public enum Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }

    public static Level modLogLevelFilter = Level.INFO;

    public static boolean checkLevel(Logger logger, Level level) {
        return level.compareTo(modLogLevelFilter) >= 0;
    }

    private static void doLog(Logger logger, Level level, String module, String msg, Throwable throwable) {
        var emsg = MessageFormatter.arrayFormat(
                "[{}|{}/{}]: {}",
                new Object[]{ level.toString(), ZRYClientUtilsMod.MODID, module, msg},
                throwable
        ).getMessage();
        switch (level){
            case TRACE -> logger.info(emsg);
            case DEBUG -> logger.info(emsg);
            case INFO -> logger.info(emsg);
            case WARN -> logger.warn(emsg);
            case ERROR -> logger.error(emsg);
        }
    }

    public static void log(Logger logger, Level level, String module, String msg) {
        if(!checkLevel(logger, level)) return;
        doLog(logger, level, module, msg, null);
    }

    public static void log(Logger logger, Level level, String module, String msg, Object... argArray) {
        if(!checkLevel(logger, level)) return;
        var m = MessageFormatter.arrayFormat(msg, argArray).getMessage();
        doLog(logger, level, module, m, null);
    }

    public static void log(Logger logger, Level level, String module, String msg, Throwable throwable) {
        if(!checkLevel(logger, level)) return;
        doLog(logger, level, module, msg, throwable);
    }
}
