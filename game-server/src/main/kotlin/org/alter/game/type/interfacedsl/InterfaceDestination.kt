package org.alter.game.type.interfacedsl

enum class InterfaceDestination(
    val interfaceEntry: String,
    val fixedChildId: String,
    val resizeChildId: String,
    val resizeListChildId: String,
    val fullscreenChildId: String = "component.fullscreen_pane_-1",
    val clickThrough: Boolean = true,
) {
    CHAT_BOX(interfaceEntry = "interface.chat", fixedChildId = "component.fixed_pane_10", resizeChildId = "component.resizable_pane_96", resizeListChildId = "component.side_panels_resizable_pane_91", fullscreenChildId = "component.fullscreen_pane_1",),
    XP_COUNTER(interfaceEntry = "interface.experience_drops_window", fixedChildId = "component.fixed_pane_33", resizeChildId = "component.resizable_pane_9", resizeListChildId = "component.side_panels_resizable_pane_32", fullscreenChildId = "component.fullscreen_pane_11", clickThrough = true,),
    ATTACK(interfaceEntry = "interface.combat_tab", fixedChildId = "component.fixed_pane_80", resizeChildId = "component.resizable_pane_76", resizeListChildId = "component.side_panels_resizable_pane_72", fullscreenChildId = "component.fullscreen_pane_15"),
    SKILLS(interfaceEntry = "interface.skills_tab", fixedChildId = "component.fixed_pane_81", resizeChildId = "component.resizable_pane_77", resizeListChildId = "component.side_panels_resizable_pane_73", fullscreenChildId = "component.fullscreen_pane_16"),
    QUEST_ROOT(interfaceEntry = "interface.journal_header_tab", fixedChildId = "component.fixed_pane_82", resizeChildId = "component.resizable_pane_78", resizeListChildId = "component.side_panels_resizable_pane_74", fullscreenChildId = "component.fullscreen_pane_17"),
    INVENTORY(interfaceEntry = "interface.inventory_tab", fixedChildId = "component.fixed_pane_83", resizeChildId = "component.resizable_pane_79", resizeListChildId = "component.side_panels_resizable_pane_75", fullscreenChildId = "component.fullscreen_pane_18"),
    EQUIPMENT(interfaceEntry = "interface.equipment_tab", fixedChildId = "component.fixed_pane_84", resizeChildId = "component.resizable_pane_80", resizeListChildId = "component.side_panels_resizable_pane_76", fullscreenChildId = "component.fullscreen_pane_19"),
    PRAYER(interfaceEntry = "interface.prayer_tab", fixedChildId = "component.fixed_pane_85", resizeChildId = "component.resizable_pane_81", resizeListChildId = "component.side_panels_resizable_pane_77", fullscreenChildId = "component.fullscreen_pane_20"),
    MAGIC(interfaceEntry = "interface.spellbook_tab", fixedChildId = "component.fixed_pane_86", resizeChildId = "component.resizable_pane_82", resizeListChildId = "component.side_panels_resizable_pane_78", fullscreenChildId = "component.fullscreen_pane_21"),
    CLAN_CHAT(interfaceEntry = "interface.chat_header", fixedChildId = "component.fixed_pane_87", resizeChildId = "component.resizable_pane_83", resizeListChildId = "component.side_panels_resizable_pane_79", fullscreenChildId = "component.fullscreen_pane_22"),
    ACCOUNT_MANAGEMENT(interfaceEntry = "interface.account_management_tab", fixedChildId = "component.fixed_pane_88", resizeChildId = "component.resizable_pane_84", resizeListChildId = "component.side_panels_resizable_pane_80", fullscreenChildId = "component.fullscreen_pane_23"),
    SOCIAL(interfaceEntry = "interface.friend_list_tab", fixedChildId = "component.fixed_pane_89", resizeChildId = "component.resizable_pane_85", resizeListChildId = "component.side_panels_resizable_pane_81", fullscreenChildId = "component.fullscreen_pane_24"),
    LOG_OUT(interfaceEntry = "interface.logout_tab", fixedChildId = "component.fixed_pane_90", resizeChildId = "component.resizable_pane_86", resizeListChildId = "component.side_panels_resizable_pane_82", fullscreenChildId = "component.fullscreen_pane_25"),
    SETTINGS(interfaceEntry = "interface.settings_tab", fixedChildId = "component.fixed_pane_91", resizeChildId = "component.resizable_pane_87", resizeListChildId = "component.side_panels_resizable_pane_83", fullscreenChildId = "component.fullscreen_pane_26"),
    EMOTES(interfaceEntry = "interface.emote_tab", fixedChildId = "component.fixed_pane_92", resizeChildId = "component.resizable_pane_88", resizeListChildId = "component.side_panels_resizable_pane_84", fullscreenChildId = "component.fullscreen_pane_27"),
    MUSIC(interfaceEntry = "interface.music_tab", fixedChildId = "component.fixed_pane_93", resizeChildId = "component.resizable_pane_89", resizeListChildId = "component.side_panels_resizable_pane_85", fullscreenChildId = "component.fullscreen_pane_28"),
    PRIVATE_CHAT(interfaceEntry = "interface.private_chat", fixedChildId = "component.fixed_pane_35", resizeChildId = "component.resizable_pane_91", resizeListChildId = "component.side_panels_resizable_pane_88", fullscreenChildId = "component.fullscreen_pane_30"), // Fixed @TODO
    MINI_MAP(interfaceEntry = "interface.orbs", fixedChildId = "component.fixed_pane_24", resizeChildId = "component.resizable_pane_33", resizeListChildId = "component.side_panels_resizable_pane_90", fullscreenChildId = "component.fullscreen_pane_31"),
    MAIN_SCREEN(interfaceEntry = "interface.non_destination", fixedChildId = "component.fixed_pane_9", resizeChildId = "component.resizable_pane_16", resizeListChildId = "component.side_panels_resizable_pane_16", fullscreenChildId = "component.fullscreen_pane_13", clickThrough = false,),
    TAB_AREA(interfaceEntry = "interface.non_destination", fixedChildId = "component.fixed_pane_77", resizeChildId = "component.resizable_pane_74", resizeListChildId = "component.side_panels_resizable_pane_70", clickThrough = false), // @TODO
    WALKABLE(interfaceEntry = "interface.non_destination", fixedChildId = "component.fixed_pane_9", resizeChildId = "component.resizable_pane_3", resizeListChildId = "component.side_panels_resizable_pane_3"),
    WORLD_MAP(interfaceEntry = "interface.non_destination", fixedChildId = "component.fixed_pane_42", resizeChildId = "component.resizable_pane_18", resizeListChildId = "component.side_panels_resizable_pane_15", fullscreenChildId = "component.fullscreen_pane_36"),
    WORLD_MAP_FULL(interfaceEntry = "interface.non_destination", fixedChildId = "component.fixed_pane_27", resizeChildId = "component.resizable_pane_21", resizeListChildId = "component.side_panels_resizable_pane_37", fullscreenChildId = "component.fullscreen_pane_27", clickThrough = false), // @TODO
    OVERLAY(interfaceEntry = "interface.buff_bar_overlay", fixedChildId = "component.fixed_pane_43", resizeChildId = "component.resizable_pane_6", resizeListChildId = "component.side_panels_resizable_pane_43", fullscreenChildId = "component.fullscreen_pane_29"),
    ;

    fun isSwitchable(): Boolean =
        when (this) {
            CHAT_BOX, MAIN_SCREEN, WALKABLE, TAB_AREA,
            ATTACK, SKILLS, QUEST_ROOT, INVENTORY, EQUIPMENT,
            PRAYER, MAGIC, CLAN_CHAT, ACCOUNT_MANAGEMENT,
            SOCIAL, LOG_OUT, SETTINGS, EMOTES, MUSIC, OVERLAY,
            PRIVATE_CHAT, MINI_MAP, XP_COUNTER, WORLD_MAP,
                -> true
            else -> false
        }
    companion object {
        val values = enumValues<InterfaceDestination>()
        fun getModals() = values.filter { pane -> pane.interfaceEntry != "interface.non_destination" }
    }
}

fun getDisplayComponentId(displayMode: DisplayMode): String =
    when (displayMode) {
        DisplayMode.FIXED -> "interface.fixed_pane"
        DisplayMode.RESIZABLE_NORMAL -> "interface.resizable_pane"
        DisplayMode.RESIZABLE_LIST -> "interface.side_panels_resizable_pane"
        DisplayMode.FULLSCREEN -> "interface.fullscreen_pane"
        else -> throw RuntimeException("Unhandled display mode.")
    }

fun getChildId(
    pane: InterfaceDestination,
    displayMode: DisplayMode,
): String =
    when (displayMode) {
        DisplayMode.FIXED -> pane.fixedChildId
        DisplayMode.RESIZABLE_NORMAL -> pane.resizeChildId
        DisplayMode.RESIZABLE_LIST -> pane.resizeListChildId
        DisplayMode.FULLSCREEN -> pane.fullscreenChildId
        else -> throw RuntimeException("Unhandled display mode.")
    }
