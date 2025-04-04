package org.alter.plugins.content.areas.grandexchange

import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class GrandExchange(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        spawnNpc("npc.grand_exchange_clerk", 3164, 3488, direction = Direction.SOUTH)
        spawnNpc("npc.grand_exchange_clerk", 3165, 3488, direction = Direction.SOUTH)

        spawnNpc("npc.banker_1613", 3163, 3489, direction = Direction.WEST)
        spawnNpc("npc.banker_1613", 3163, 3490, direction = Direction.WEST)

        spawnNpc("npc.grand_exchange_clerk", 3164, 3491, direction = Direction.NORTH)
        spawnNpc("npc.grand_exchange_clerk", 3165, 3491, direction = Direction.NORTH)

        spawnNpc("npc.banker_1613", 3166, 3489, direction = Direction.EAST)
        spawnNpc("npc.banker_1613", 3166, 3490, direction = Direction.EAST)

        onObjOption("object.grand_exchange_booth", "Bank") {
//@@TODO            player.openBank()
        }
        arrayOf("Exchange", "Collect").forEach {
            onObjOption("object.grand_exchange_booth_10061", it) {
                player.message("GrandExchange is not Implemented.")
//@@TODO                player.openBank()
            }
        }
        onNpcOption("npc.banker_1613", "bank") {
//@@TODO            player.openBank()
        }
        /**
         * Examples
         */
        spawnNpc("npc.emma", 3163, 3496)
        spawnNpc("npc.thessalia", 3164, 3496)
        spawnNpc("npc.shop_assistant_2816", 3164, 3496)
    }
}
    