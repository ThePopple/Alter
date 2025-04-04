package org.alter.game.type.interfacedsl

import net.rsprot.protocol.game.outgoing.interfaces.*
import org.alter.game.model.entity.Player
import org.alter.rscm.RSCM.asRSCM
import org.alter.rscm.RSCM.getRSCM

fun Player.initInterfaces(displayMode: DisplayMode) {
    when (displayMode) {
        DisplayMode.FIXED -> {
            setInterfaceEvents(interfaceId = 548, component = 51, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 52, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 53, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 54, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 55, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 56, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 57, range = -1..-1, setting = 6)
            setInterfaceEvents(interfaceId = 548, component = 34, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 35, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 36, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 37, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 38, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 39, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 548, component = 40, range = -1..-1, setting = 2)
        }
        DisplayMode.RESIZABLE_NORMAL -> {
            setInterfaceEvents(interfaceId = 161, component = 54, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 55, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 56, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 57, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 58, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 59, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 60, range = -1..-1, setting = 6)
            setInterfaceEvents(interfaceId = 161, component = 38, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 39, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 40, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 41, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 42, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 43, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 161, component = 44, range = -1..-1, setting = 2)
        }
        DisplayMode.RESIZABLE_LIST -> {
            setInterfaceEvents(interfaceId = 164, component = 53, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 54, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 55, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 56, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 57, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 58, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 59, range = -1..-1, setting = 6)
            setInterfaceEvents(interfaceId = 164, component = 38, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 39, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 40, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 32, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 41, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 42, range = -1..-1, setting = 2)
            setInterfaceEvents(interfaceId = 164, component = 43, range = -1..-1, setting = 2)
        }
        else -> return
    }
}

@Deprecated("Will be removed, to prevent usage of Integer values.")
fun Player.setInterfaceEvents(
    interfaceId: Int,
    component: Int,
    range: IntRange,
    setting: Int,
) {
    write(IfSetEvents(
    interfaceId = interfaceId,
    componentId = component,
    start = range.first,
    end = range.last,
    events = setting
    ))
}
@Deprecated("Will be removed, to prevent usage of Integer values. ")
fun Player.setInterfaceEvents(
    interfaceId: Int,
    component: Int,
    event: InterfaceEvent
) {
    val list = arrayListOf<Int>()
    event.events.forEach {
        list.add(it.flag)
    }
    write(IfSetEvents(
    interfaceId = interfaceId,
    componentId = component,
    start = event.startIndex,
    end = event.endIndex,
    events = list.reduce(Int::or)
    ))
}


fun Player.openDefaultInterfaces() {
    openOverlayInterface(interfaces.displayMode)
    openModals(this)
    setInterfaceEvents(interfaceId = 239, component = 3, range = 0..665, setting = 6) // enable music buttons @TODO Why
    initInterfaces(interfaces.displayMode)
}
fun Player.openOverlayInterface(displayMode: DisplayMode) {
    if (displayMode != interfaces.displayMode) {
        interfaces.setVisible(
            parent = getDisplayComponentId(interfaces.displayMode),
            child = getChildId(InterfaceDestination.MAIN_SCREEN, interfaces.displayMode),
            visible = false,
        )
    }
    val component = getDisplayComponentId(displayMode)
    interfaces.setVisible(parent = getDisplayComponentId(displayMode), child = 0, visible = true)
    write(IfOpenTop(getRSCM(component)))
}

fun openModals(
    player: Player,
    fullscreen: Boolean = false,
) {
    InterfaceDestination.getModals().forEach { pane ->
        player.openInterface(pane.interfaceEntry, pane, fullscreen)
    }
}
// Since it parses new style interface we can assume its also configured and that we can launch `handleOpenInterface()`
fun Player.openInterface(`interface`: Interface) {
    val destination = getInterfaceBuilder(`interface`)
    val builder = InterfaceBuilder.Builder(`interface`, this).apply(destination)
    handleOpenInterface(`interface`)
    openInterface(`interface`.entry, builder.destination)
}
fun Player.openInterface(
    interfaceEntry: String,
    dest: InterfaceDestination,
    fullscreen: Boolean = false
) {
    val displayMode = if (!fullscreen || dest.fullscreenChildId == "component.fullscreen_pane_-1") interfaces.displayMode else DisplayMode.FULLSCREEN
    val child = getChildId(dest, displayMode)
    val parent = getDisplayComponentId(displayMode)
    openInterface(parent, child, interfaceEntry, type = if (dest.clickThrough) 1 else 0, isModal = dest == InterfaceDestination.MAIN_SCREEN)
}
/**
 * [type] should be classed
 */
fun Player.openInterface(
    parent: String,
    child: String,
    interfaceEntry: String,
    type: Int = 0,
    isModal: Boolean = false
) {
    if (isModal) {
        interfaces.openModal(parent, child, interfaceEntry)
    } else {
        interfaces.open(parent, child, interfaceEntry)
    }
    val isNewInterface = getInterfaceByEntryOrNull(interfaceEntry)
    if (isNewInterface != null) {
        handleOpenInterface(isNewInterface)
    } else {
        println("Legacy Interface detected: $interfaceEntry")
    }
    write(IfOpenSub(getRSCM(parent), getRSCM(child), getRSCM(interfaceEntry), type))
}
fun Player.handleOpenInterface(`interface`: Interface) {
    getInterfaceBuilder(`interface`).let {
        InterfaceBuilder.Builder(`interface`, this).apply(it)
    }.components.forEach { component ->
        component.value.outMessages.forEach {
            write(it)
        }
    }
}

fun Player.closeInterface(widget: Interface) {
    if (widget.entry == interfaces.getModal()) {
        interfaces.setModal("")
    }
    val hash = interfaces.close(widget)
    if (hash != -1) {
        val parent = hash shr 16
        val child = hash and 0xFFFF
        write(IfCloseSub(interfaceId = parent, componentId = child))
    }
}