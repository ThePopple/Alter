package org.alter.plugins.content

import org.alter.api.*
import org.alter.api.CommonClientScripts
import org.alter.api.cfg.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.plugin.*
import org.alter.game.type.interfacedsl.InterfaceEvent
import org.alter.game.type.interfacedsl.InterfaceEventFlag
import org.alter.game.type.interfacedsl.openDefaultInterfaces
import org.alter.game.type.interfacedsl.setInterfaceEvents

class OSRSPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        setModalCloseLogic {
            val modal = player.interfaces.getModal()
            if (modal != "") {
                //player.closeInterface(modal)
                player.interfaces.setModal("")
            }
        }
        onLogin {
            with(player) {
                // Skill-related logic.
                calculateAndSetCombatLevel()
                if (getSkills().getBaseLevel(Skills.HITPOINTS) < 10) {
                    getSkills().setBaseLevel(Skills.HITPOINTS, 10)
                }
                calculateAndSetCombatLevel()
                sendWeaponComponentInformation()
                sendCombatLevelText()

                player.openDefaultInterfaces()
                setVarbit(Varbit.COMBAT_LEVEL_VARBIT, combatLevel)
                setVarbit(Varbit.CHATBOX_UNLOCKED, 1)
                runClientScript(CommonClientScripts.INTRO_MUSIC_RESTORE)
                if (getVarp(Varp.PLAYER_HAS_DISPLAY_NAME) == 0 && displayName.isNotBlank()) {
                    syncVarp(Varp.PLAYER_HAS_DISPLAY_NAME)
                }
                syncVarp(Varp.NPC_ATTACK_PRIORITY_VARP)
                syncVarp(Varp.PLAYER_ATTACK_PRIORITY_VARP)
                sendOption("Follow", 3)
                sendOption("Trade with", 4)
                sendOption("Report", 5)
                sendRunEnergy(player.runEnergy.toInt())
                message("Welcome to ${world.gameContext.name}.", ChatMessageType.GAME_MESSAGE)
                setVarbit(Varbit.ESC_CLOSES_CURRENT_INTERFACE, 1)
                /**
                 * @TODO
                 * As for now these varbit's disable Black bar on right side for Native client,
                 * The black bar is for loot tracker n whatnot
                 */
                setVarbit(13982, 1)
                setVarbit(13981, 1)
            }
        }

    }

}
