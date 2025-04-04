package org.alter.plugins.content.interfaces.gameframe

import org.alter.api.EquipmentType
import org.alter.api.cfg.Varbit
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.*
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.interfaces.attack.AttackTab.SPECIAL_ATTACK_VARP
import org.alter.plugins.content.mechanics.run.RunEnergy

class Orbs(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        Interface(Orbs) {
            component("XP Orb", "component.orbs_5") {
                click { _, option, _->
                    when (option) {
                        1 -> {
                            player.toggleVarbit(Varbit.XP_DROPS_VISIBLE_VARBIT)
                            if (player.getVarbit(Varbit.XP_DROPS_VISIBLE_VARBIT) == 1) {
                                player.openInterface(XpDropWindow)
                            } else {
                                player.closeInterface(XpDropWindow)
                            }
                        }
                        2 -> {
                            // @TODO Open XPSettingsInterface
                        }
                    }
                }
            } // Opt 1 => Hide/Show , 2 => Open XP Drop settings
            component("Prayer Orb", "component.orbs_19") {} // Opt 1 => Activate Quick Prayers, 2 => Setup Quick-Prayers
            component("Movement Orb", 27) {
                click { _, _, _ ->
                    RunEnergy.toggle(player)
                }
            }
            component("Special Attack Orb", 35) {
                click { _,_,_ ->
                    val weaponId = player.equipment[EquipmentType.WEAPON.id]!!.id
                    if (SpecialAttacks.executeOnEnable(weaponId)) {
                        if (!SpecialAttacks.execute(player, null, world)) {
                            player.message("You don't have enough power left.")
                        }
                    } else {
                        player.toggleVarp(SPECIAL_ATTACK_VARP)
                    }
                }
            }
            component("World Map Orb", 53) {} // Opt 2 => Floating , 3 => FullScreen , 4 => minimise
            // @TODO Add useComponentOnNpc / Player || component_50 on Npc = Wiki lookup

            component("Wiki lookup", 50) {

                useOnNpcClick { npc, p1,p2 ->
                    player.message("Component 50 used on ${npc.name} $p1 , $p2")
                }
                //useOnPlayerClick {}

            }
        }
    }
    object Orbs: OverlayInterface() {
        override val entry = "interface.orbs"
    }
}
    