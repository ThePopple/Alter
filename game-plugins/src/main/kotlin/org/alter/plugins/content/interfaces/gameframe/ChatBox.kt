package org.alter.plugins.content.interfaces.gameframe

import org.alter.api.cfg.Varbit
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.Interface
import org.alter.game.type.interfacedsl.OverlayInterface

class ChatBox(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        val GAME_BUTTON_COMPONENT = 7
        val PUBLIC_BUTTON_COMPONENT = 11
        val TRADE_BUTTON_COMPONENT = 27
        val REPORT_BUG_BUTTON_COMPONENT = 31

        onLogin {
            player.setVarbit(Varbit.CHATBOX_UNLOCKED, 1)
        }

        Interface(ChatBoxInterface) {
            component("Game Button", 7) {
                click { _, option, _ ->
                    when (option) {
                        1 -> {
                            player.toggleVarbit(26)
                        }
                        2 -> {
                            player.queue { dialog(player, this) }
                        }
                    }
                }
            }
            // OLD BLOCK
            /**
             *         listOf(PRIVATE_BUTTON_COMPONENT, CHANNEL_BUTTON_COMPONENT, CLAN_BUTTON_COMPONENT).forEach {
             *             onButton(PARENT_CHAT_BOX_INTERFACE, it) {
             *                 player.setVarbit(
             *                     when (it) {
             *                         PRIVATE_BUTTON_COMPONENT -> 13674
             *                         CHANNEL_BUTTON_COMPONENT -> 928
             *                         CLAN_BUTTON_COMPONENT -> 929
             *                         else -> {
             *                             return@onButton
             *                         }
             *                     },
             *                     when (player.getInteractingOption()) {
             *                         // Option : Varbit Value
             *                         4 -> 2
             *                         3 -> 1
             *                         2 -> 0
             *                         else -> {
             *                             println("[$PARENT_CHAT_BOX_INTERFACE : $it] ${player.getInteractingOption()} Interacting Option is unknown.")
             *                             return@onButton
             *                         }
             *                     },
             *                 )
             *             }
             *         }
             */
            /**
             *         listOf(PRIVATE_BUTTON_COMPONENT, CHANNEL_BUTTON_COMPONENT, CLAN_BUTTON_COMPONENT).forEach {
             *             onButton(PARENT_CHAT_BOX_INTERFACE, it) {
             *                 player.setVarbit(
             *                     when (it) {
             *                         PRIVATE_BUTTON_COMPONENT -> 13674
             *                         CHANNEL_BUTTON_COMPONENT -> 928
             *                         CLAN_BUTTON_COMPONENT -> 929
             *                         else -> {
             *                             return@onButton
             *                         }
             *                     },
             *                     when (player.getInteractingOption()) {
             *                         // Option : Varbit Value
             *                         4 -> 2
             *                         3 -> 1
             *                         2 -> 0
             *                         else -> {
             *                             println("[$PARENT_CHAT_BOX_INTERFACE : $it] ${player.getInteractingOption()} Interacting Option is unknown.")
             *                             return@onButton
             *                         }
             *                     },
             *                 )
             *             }
             *         }
             */
            component("Private button", 15) {
                player.message("Latter varbit 13674")
            }
            component("Channel button", 19) {
                player.message("Latter varbit 928")
            }
            component("Clan button", 23) {
                player.message("Latter varbit 929")
            }

        }
    }
    private suspend fun dialog(player: Player, it: QueueTask) {
        when (
            it.options(
                player, "Filter them." /* Filter or unfilter.*/,
                "Do not filter them.",
                title = "Boss kill-counts are not blocked by the spam filter.",
            )
        ) {
            1 -> {
                it.messageBox(player, "Boss kill-count messages that you receive in future will not be blocked by the spam filter.")
            }
            2 -> {
                it.messageBox(player, "CBA For now... Later.")
            }
        }
    }
}
object ChatBoxInterface: OverlayInterface() {
    override val entry = "interface.chat"
    // @TODO Either add hash check from here or during boot check for changes
}
