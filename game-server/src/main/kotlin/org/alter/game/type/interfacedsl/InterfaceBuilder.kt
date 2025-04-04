@file:Suppress("LocalVariableName", "unused")

package org.alter.game.type.interfacedsl

import net.rsprot.protocol.game.outgoing.interfaces.*
import net.rsprot.protocol.message.OutgoingGameMessage
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.rscm.RSCM.getRSCM
import java.awt.Color

object InterfaceBuilder {
    @Suppress("MemberVisibilityCanBePrivate")
    class Builder(val `interface`: Interface, var player: Player) {
        internal var destination: InterfaceDestination = InterfaceDestination.MAIN_SCREEN
        internal var components: Map<Int, InterfaceComponentBuilder> = emptyMap()
        internal var componentNameMap: Map<Int, String> = emptyMap()
        internal var onOpenAction: ((Player) -> Unit)? = null
        internal var onCloseAction: ((Player) -> Unit)? = null
        internal var dragActions: Map<Pair<String, String>, Player.(
            fromItemId: Int, toItemId: Int,
            fromSlot: Int, toSlot: Int,
            fromInterfaceId: Int, fromComponent: Int,
            toInterfaceId: Int, toComponent: Int
        ) -> Unit> = emptyMap()

        fun component(name: String, id: Int, clickAction: InterfaceComponentBuilder.(Player) -> Unit) {
            check(!components.containsKey(id)) { "${`interface`.entry} already has $id component assigned." }
            components += Pair(id, InterfaceComponentBuilder(name, id, player, `interface`).apply { clickAction(player) })
            componentNameMap += Pair(id, name)
        }
        fun component(name: String, entry: String, clickAction: InterfaceComponentBuilder.(Player) -> Unit) {
            check(entry.startsWith("component.")) { "${`interface`.entry}, Component entry should be from : `component.` right now it is: $entry" }
            val id = getRSCM(entry)
            check(!components.containsKey(id)) { "${`interface`.entry} already has $entry component assigned." }
            components += Pair(id, InterfaceComponentBuilder(name, id, player, `interface`).apply { clickAction(player) })
            componentNameMap += Pair(id, name)
        }
        fun onOpen(init: Player.() -> Unit) {
            check(onOpenAction == null) { "${`interface`.entry} onOpen was already assigned." }
            onOpenAction = { player -> init(player) }
        }
        fun onClose(init: Player.() -> Unit) {
            check(onCloseAction == null) { "${`interface`.entry} onClose was already assigned." }
            onCloseAction = { player -> init(player) }
        }
        fun drag(
            component1: String,
            component2: String,
            action: Player.(
                fromItemId: Int,
                toItemId: Int,
                fromSlot: Int,
                toSlot: Int,
                fromInterfaceId: Int,
                fromComponent: Int,
                toInterfaceId: Int,
                toComponent: Int) -> Unit) {
            componentNameMap.entries.find { it.value == component1 }?.key
                ?: throw IllegalArgumentException("Component '$component1' are not defined in ${`interface`.entry}.")
            componentNameMap.entries.find { it.value == component2 }?.key
                ?: throw IllegalArgumentException("Component '$component2' are not defined in ${`interface`.entry}.")
            val existingPair = listOf(component1 to component2, component2 to component1)
                .firstOrNull { dragActions.containsKey(it) }
            check(existingPair == null) {
                "Drag action between ${existingPair!!.first} and ${existingPair.second} already exists."
            }
            dragActions += Pair((component1 to component2), action)
        }
        fun destination(destination: InterfaceDestination) {
            this.destination = destination
        }
    }
}
class InterfaceComponentBuilder(val name: String, val id: Int, var player: Player, val Interface: Interface) {
    var interfaceID = getRSCM(Interface.entry)
    var outMessages = ArrayList<OutgoingGameMessage>()
    var clickAction: ((Player, Int, Int, Int) -> Unit)? = null
    var useOnNpcClick: ((Player, Npc, Int, Int) -> Unit)? = null

    var events: InterfaceEvent? = null
        set(value) {
            field = value ?: return
            val eventFlags = value.events.fold(0) { acc, event -> acc or event.flag }
            outMessages += IfSetEvents(
                interfaceId = interfaceID,
                componentId = id,
                start = value.startIndex,
                end = value.endIndex,
                events = eventFlags
            )
        }

    /**
     * @TODO Bug when it's not surrounded by `click`, click will execute all the non surrounded components.
     */
    fun click(init: InterfaceComponentBuilder.(slot: Int, option: Int, item: Int) -> Unit) {
        this.clickAction = { _, option, slot, item ->
            this.init(slot, option, item)
        }
    }
    fun setText(text: String) {
        outMessages += IfSetText(interfaceId = interfaceID, componentId = id, text = text)
    }
    fun setColor(color: Color) {
        outMessages += IfSetColour(interfaceId = interfaceID, componentId = id, color = color)
    }
    fun setAnim(anim: Int) {
        outMessages += IfSetAnim(interfaceId = interfaceID, componentId = id, anim = anim)
    }
    fun setHidden(hidden: Boolean) {
        outMessages += IfSetHide(interfaceId = interfaceID, componentId = id, hidden = hidden)
    }
    fun setObject(obj: Int, count: Int) {
        outMessages += IfSetObject(interfaceId = interfaceID, componentId = id, obj = obj, count = count)
    }
    fun setNpcHead(npc: Int) {
        outMessages += IfSetNpcHead(interfaceId = interfaceID, componentId = id, npc = npc)
    }
    fun setNpcHeadActive(index: Int) {
        /**
         * @TODO
         */
    }
    fun setPlayerHead() {
        outMessages += IfSetPlayerHead(interfaceId = interfaceID, componentId = id)
    }
    fun resync() {
        /**
         * IfResync()
         * topLevelInterface
         * subInterfaces
         * events
         * @TODO
         */
    }
    fun setAngle(angleX: Int, angleY: Int, zoom: Int) {
        outMessages += IfSetAngle(interfaceId = interfaceID, componentId = id, angleX = angleX, angleY = angleY, zoom = zoom)
    }
    fun setModel(model: Int) {
        outMessages += IfSetModel(interfaceId = interfaceID, componentId = id, model = model)
    }
    fun setPosition(x: Int, y: Int) {
        outMessages += IfSetPosition(
            interfaceId = interfaceID,
            componentId = id,
            x = x,
            y = y
        )
    }
    fun setRotateSpeed(xSpeed: Int, ySpeed: Int) {
        outMessages += IfSetRotateSpeed(interfaceId = interfaceID, componentId = id, xSpeed = xSpeed, ySpeed = ySpeed)
    }
    fun setScrollPos(scrollPos: Int) {
        outMessages += IfSetScrollPos(interfaceId = interfaceID, componentId = id, scrollPos = scrollPos)
    }
    fun useOnNpcClick(init: InterfaceComponentBuilder.(npc: Npc, selectedSub: Int, selectedObj: Int) -> Unit) {
        this.useOnNpcClick = { _, npc, selectedSub, selectedObj ->
            this.init(npc, selectedSub, selectedObj)
        }
    }
}
fun executeUseOnNpcClick(Interface: Interface, player: Player, componentId: Int, npc: Npc, selectedSub: Int, selectedObj: Int) {
    val inter = getInterfaceBuilder(Interface)
    val componentBuilder = InterfaceBuilder.Builder(Interface, player).apply(inter)
    val component = componentBuilder.components[componentId]
        ?: throw IllegalStateException("Component with id $componentId was not found within ${Interface.entry} during Use component on Npc.")
    component.useOnNpcClick?.invoke(player, npc, selectedSub, selectedObj)
}
@Deprecated("Needs to be moved into InterfaceHandler")
fun executeClick(Interface: Interface, player: Player, componentId: Int, optionId: Int, slot: Int, item: Int) {
    val inter = getInterfaceBuilder(Interface)
    val componentBuilder = InterfaceBuilder.Builder(Interface, player).apply(inter)
    val component = componentBuilder.components[componentId]
        ?: throw IllegalStateException("Component with id $componentId was not found within ${Interface.entry} during Click execution.")
    component.clickAction?.invoke(player, optionId, slot, item)
}
fun executeDrag(Interface: Interface, player: Player, fromItemId: Int,
                toItemId: Int, fromSlot: Int, toSlot: Int, fromInterfaceId: Int,
                fromComponent: Int, toInterfaceId: Int, toComponent: Int) {
    val inter = getInterfaceBuilder(Interface)
    val componentBuilder = InterfaceBuilder.Builder(Interface, player).apply(inter)
    val fromComponentName = componentBuilder.componentNameMap[fromComponent]
        ?: throw IllegalArgumentException("Component ID $fromComponent not found.")
    val toComponentName = componentBuilder.componentNameMap[toComponent]
        ?: throw IllegalArgumentException("Component ID $toComponent not found.")
    val dragAction = componentBuilder.dragActions[toComponentName to fromComponentName]
        ?: throw IllegalArgumentException("No drag action found for components $toComponentName and $fromComponentName.")
    dragAction(player, fromItemId, toItemId, fromSlot, toSlot, fromInterfaceId, fromComponent, toInterfaceId, toComponent)
}
var interfaceBuilderMap: Map<Interface, InterfaceBuilder.Builder.() -> Unit> = emptyMap()
fun Interface(Interface: Interface, init: InterfaceBuilder.Builder.() -> Unit) {
    interfaceBuilderMap = interfaceBuilderMap + (Interface to init)
}
fun getInterfaceBuilderByEntry(entry: String) : InterfaceBuilder.Builder.() -> Unit {
    return interfaceBuilderMap.entries.find { it.key.entry == entry }?.value
        ?: throw IllegalStateException("$entry is not handled.")
}
fun getInterfaceByEntry(entry: String) : Interface {
    return interfaceBuilderMap.keys.find { it.entry == entry }
    ?: throw IllegalStateException("$entry is not handled.")
}
fun getInterfaceByEntryOrNull(entry: String): Interface? {
    return interfaceBuilderMap.keys.find { it.entry == entry }
}
fun getInterfaceBuilder(`interface`: Interface): InterfaceBuilder.Builder.() -> Unit {
    return interfaceBuilderMap[`interface`] ?: {
        throw IllegalStateException("$`interface` is not handled. interfaceMap=$interfaceBuilderMap")
    }
}