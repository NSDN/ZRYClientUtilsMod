package com.zjinja.mcmod.zry_client_utils_mod.keybinds;

import java.util.Map;

public class KeyCodeTranslate {
    public static String getKeyNameByKeyCode(int keyCode) {
        return keycodeNameMap.getOrDefault(keyCode, "");
    }

    private static final Map<Integer, String> keycodeNameMap = Map.ofEntries(
            Map.entry(256, "Esc"),
            Map.entry(257, "Enter"),
            Map.entry(258, "Tab"),
            Map.entry(259, "BS"),
            Map.entry(260, "Ins"),
            Map.entry(261, "Del"),
            Map.entry(262, "<-"),
            Map.entry(263, "->"),
            Map.entry(264, "Dn"),
            Map.entry(265, "Up"),
            Map.entry(266, "PgUp"),
            Map.entry(267, "PgDn"),
            Map.entry(268, "Home"),
            Map.entry(269, "End"),
            Map.entry(283, "PrtScr"),
            Map.entry(284, "Pause"),
            Map.entry(290, "F1"),
            Map.entry(291, "F2"),
            Map.entry(292, "F3"),
            Map.entry(293, "F4"),
            Map.entry(294, "F5"),
            Map.entry(295, "F6"),
            Map.entry(296, "F7"),
            Map.entry(297, "F8"),
            Map.entry(298, "F9"),
            Map.entry(299, "F10"),
            Map.entry(300, "F11"),
            Map.entry(301, "F12"),
            Map.entry(302, "F13"),
            Map.entry(303, "F14"),
            Map.entry(304, "F15"),
            Map.entry(305, "F16"),
            Map.entry(306, "F17"),
            Map.entry(307, "F18"),
            Map.entry(308, "F19"),
            Map.entry(309, "F20"),
            Map.entry(310, "F21"),
            Map.entry(311, "F22"),
            Map.entry(312, "F23"),
            Map.entry(313, "F24"),
            Map.entry(314, "F25"),
            Map.entry(320, "KP0"),
            Map.entry(321, "KP1"),
            Map.entry(322, "KP2"),
            Map.entry(323, "KP3"),
            Map.entry(324, "KP4"),
            Map.entry(325, "KP5"),
            Map.entry(326, "KP6"),
            Map.entry(327, "KP7"),
            Map.entry(328, "KP8"),
            Map.entry(329, "KP9"),
            Map.entry(331, "KP/"),
            Map.entry(332, "KP*"),
            Map.entry(333, "KP-"),
            Map.entry(334, "KP+"),
            Map.entry(335, "KPEnter"),
            Map.entry(340, "LShift"),
            Map.entry(341, "LCtrl"),
            Map.entry(342, "LAlt"),
            Map.entry(343, "LSup"),
            Map.entry(344, "RShift"),
            Map.entry(345, "RCtrl"),
            Map.entry(346, "RAlt"),
            Map.entry(347, "RSup"),
            Map.entry(348, "Menu"),
            Map.entry(48, "0"),
            Map.entry(49, "1"),
            Map.entry(50, "2"),
            Map.entry(51, "3"),
            Map.entry(52, "4"),
            Map.entry(53, "5"),
            Map.entry(54, "6"),
            Map.entry(55, "7"),
            Map.entry(56, "8"),
            Map.entry(57, "9"),
            Map.entry(65, "A"),
            Map.entry(66, "B"),
            Map.entry(67, "C"),
            Map.entry(68, "D"),
            Map.entry(69, "E"),
            Map.entry(70, "F"),
            Map.entry(71, "G"),
            Map.entry(72, "H"),
            Map.entry(73, "I"),
            Map.entry(74, "J"),
            Map.entry(75, "K"),
            Map.entry(76, "L"),
            Map.entry(77, "M"),
            Map.entry(78, "N"),
            Map.entry(79, "O"),
            Map.entry(80, "P"),
            Map.entry(81, "Q"),
            Map.entry(82, "R"),
            Map.entry(83, "S"),
            Map.entry(84, "T"),
            Map.entry(85, "U"),
            Map.entry(86, "V"),
            Map.entry(87, "W"),
            Map.entry(88, "X"),
            Map.entry(89, "Y"),
            Map.entry(90, "Z"),
            Map.entry(32, " "),
            Map.entry(39, "'"),
            Map.entry(44, ","),
            Map.entry(45, "-"),
            Map.entry(46, "."),
            Map.entry(47, "/"),
            Map.entry(59, ";"),
            Map.entry(61, "="),
            Map.entry(91, "["),
            Map.entry(92, "\\"),
            Map.entry(93, "]"),
            Map.entry(96, "`")
    );
}
