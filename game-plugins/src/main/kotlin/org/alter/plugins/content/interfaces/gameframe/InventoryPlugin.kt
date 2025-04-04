package org.alter.plugins.content.interfaces.gameframe

import org.alter.api.ext.message
import org.alter.game.Server
import org.alter.game.action.EquipAction
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.GroundItem
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.Interface
import org.alter.game.type.interfacedsl.InterfaceEvent
import org.alter.game.type.interfacedsl.InterfaceEventFlag
import org.alter.game.type.interfacedsl.OverlayInterface

class InventoryPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    object Inventory : OverlayInterface() {
        override val entry = "interface.inventory_tab"
    }

    init {
        Interface(Inventory) {
            component("Inventory", 0) {
                events = InterfaceEvent(
                    startIndex = 0,
                    endIndex = 27,
                    events = listOf(
                        InterfaceEventFlag.ClickOp2,
                        InterfaceEventFlag.ClickOp3,
                        InterfaceEventFlag.ClickOp4,
                        InterfaceEventFlag.ClickOp6,
                        InterfaceEventFlag.ClickOp7,
                        InterfaceEventFlag.ClickOp10,
                        InterfaceEventFlag.UseOnGroundItem,
                        InterfaceEventFlag.UseOnNpc,
                        InterfaceEventFlag.UseOnObject,
                        InterfaceEventFlag.UseOnPlayer,
                        InterfaceEventFlag.UseOnInventory,
                        InterfaceEventFlag.UseOnComponent,
                        InterfaceEventFlag.DRAG_DEPTH1,
                        InterfaceEventFlag.DragTargetable,
                        InterfaceEventFlag.ComponentTargetable,
                    )
                )

                useOnNpcClick { npc, selectedSub, selectedObj ->
                    player.message("Uhhh: ${npc.name} : $selectedSub, $selectedObj")
                }

                click { slot, option, _ ->
                    if (slot < 0 || slot >= player.inventory.capacity) {
                        player.message("Something went wrong within Inventory $slot")
                        return@click
                    }
                    if (!player.lock.canItemInteract()) {
                        return@click
                    }
                    val item = player.inventory[slot] ?: return@click
                    when (option) {
                        7 -> {
                            if (world.plugins.canDropItem(player, item.id)) {
                                if (!world.plugins.executeItem(player, item.id, option)) {
                                    val remove =
                                        player.inventory.remove(item, assureFullRemoval = false, beginSlot = slot)
                                    if (remove.completed > 0) {
                                        val floor = GroundItem(item.id, remove.completed, player.tile, player)
                                        floor.timeUntilPublic = world.gameContext.gItemPublicDelay
                                        floor.timeUntilDespawn = world.gameContext.gItemDespawnDelay
                                        floor.ownerShipType = 1
                                        remove.firstOrNull()?.let { removed ->
                                            floor.copyAttr(removed.item.attr)
                                        }
                                        world.spawn(floor)
                                    }
                                }
                            }
                        }

                        3 -> {
                            val result = EquipAction.equip(player, item, slot)
                            if (result == EquipAction.Result.UNHANDLED && world.devContext.debugItemActions) {
                                player.message("Unhandled item action: [item=${item}, slot=$slot, option=$option]")
                            }
                        }

                        10 -> {
                            world.sendExamine(player, item.id, ExamineEntityType.ITEM)
                        }

                        else -> {
                            if (!world.plugins.executeItem(
                                    player,
                                    item.id,
                                    option
                                ) && world.devContext.debugItemActions
                            ) {
                                player.message("Unhandled item action: [item=${item.id}, slot=$slot, option=$option]")
                            }
                        }
                    }
                }
            }
            drag("Inventory", "Inventory") { fromItemId, toItemId, fromSlot, toSlot, fromInterfaceId, fromComponent, toInterfaceId, toComponent ->
                val container = player.inventory
                if (fromSlot in 0 until player.inventory.capacity && toSlot in 0 until player.inventory.capacity) {
                    container.swap(fromSlot, toSlot)
                } else {
                    container.dirty = true
                }
            }
        }
    }
}
