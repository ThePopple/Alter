package org.alter.api.ext

import dev.openrune.cache.CacheManager
import gg.rsmod.util.BitManipulation
import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import net.rsprot.protocol.game.outgoing.inv.UpdateInvPartial
import net.rsprot.protocol.game.outgoing.misc.client.UrlOpen
import net.rsprot.protocol.game.outgoing.misc.player.*
import net.rsprot.protocol.game.outgoing.sound.MidiJingle
import net.rsprot.protocol.game.outgoing.sound.MidiSongV2
import net.rsprot.protocol.game.outgoing.sound.SynthSound
import net.rsprot.protocol.game.outgoing.varp.VarpLarge
import net.rsprot.protocol.game.outgoing.varp.VarpSmall
import org.alter.api.*
import org.alter.api.cfg.Varbit
import org.alter.api.cfg.Varp
import org.alter.game.model.attr.CHANGE_LOGGING
import org.alter.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import org.alter.game.model.bits.BitStorage
import org.alter.game.model.bits.StorageBits
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.type.interfacedsl.DisplayMode
import org.alter.game.model.item.Item
import org.alter.rscm.RSCM.getRSCM
import org.alter.game.model.timer.SKULL_ICON_DURATION_TIMER
import org.alter.game.rsprot.RsModIndexedObjectProvider
import org.alter.game.rsprot.RsModObjectProvider
import kotlin.math.floor

/**
 * The id of the script used to initialise the interface overlay options. The 'big' variant of this script
 * is used as it supports up to eight options rather than five.
 *
 * https://github.com/RuneStar/cs2-scripts/blob/master/scripts/[clientscript,interface_inv_init_big].cs2
 */
val INTERFACE_INV_INIT_BIG = ClientScript("interface_inv_init_big")


fun Player.message(
    message: String,
    type: ChatMessageType = ChatMessageType.CONSOLE,
    username: String? = null,
) {
    if (username != null) {
        write(MessageGame(type = type.id, message = message, name = username))
    } else {
        write(MessageGame(type = type.id, message = message))
    }
}
fun Player.filterableMessage(message: String) {
    write(MessageGame(type = ChatMessageType.SPAM.id, message = message))
}

fun Player.openUrl(url: String) {
    write(UrlOpen(url))
}

fun Player.runClientScript(script: CommonClientScripts, vararg args: Any) {
    write(RunClientScript(script.script.id, args.toList()))
}

fun Player.runClientScript(script: ClientScript, vararg args: Any) {
    write(RunClientScript(script.id, args.toList()))
}

fun Player.focusTab(tab: GameframeTab) {
    runClientScript(CommonClientScripts.FOCUS_TAB, tab.id)
}

fun Player.setInterfaceUnderlay(
    color: Int,
    transparency: Int,
) {
    runClientScript(CommonClientScripts.MAIN_MODAL_BACKGROUND, color, transparency)
    runClientScript(CommonClientScripts.MAIN_MODAL_OPEN, color, transparency)
}



fun Player.closeInputDialog() {
    write(TriggerOnDialogAbort)
}



fun Player.sendItemContainer(
    key: Int,
    items: Array<Item?>,
) {
    write(UpdateInvFull(inventoryId = key, capacity = items.size, provider = RsModObjectProvider(items)))
}

fun Player.sendItemContainer(
    interfaceId: Int,
    component: Int,
    items: Array<Item?>,
) {
    write(
        UpdateInvFull(
            interfaceId = interfaceId,
            componentId = component,
            inventoryId = 0,
            capacity = items.size,
            provider = RsModObjectProvider(items),
        ),
    )
}

fun Player.sendItemContainer(
    interfaceId: Int,
    component: Int,
    key: Int,
    items: Array<Item?>,
) {
    write(
        UpdateInvFull(
            interfaceId = interfaceId,
            componentId = component,
            inventoryId = key,
            capacity = items.size,
            provider = RsModObjectProvider(items),
        ),
    )
}

fun Player.sendItemContainer(
    key: Int,
    container: ItemContainer,
) = sendItemContainer(key, container.rawItems)

fun Player.sendItemContainer(
    interfaceId: Int,
    component: Int,
    container: ItemContainer,
) = sendItemContainer(interfaceId, component, container.rawItems)

fun Player.sendItemContainer(
    interfaceId: Int,
    component: Int,
    key: Int,
    container: ItemContainer,
) = sendItemContainer(interfaceId, component, key, container.rawItems)

fun Player.updateItemContainer(
    interfaceId: Int,
    component: Int,
    oldItems: Array<Item?>,
    newItems: Array<Item?>,
) {
    write(
        UpdateInvPartial(
            interfaceId = interfaceId,
            componentId = component,
            inventoryId = 0,
            provider = RsModIndexedObjectProvider(oldItems.asNonNullIterator, newItems),
        ),
    )
}

fun Player.updateItemContainer(
    interfaceId: Int,
    component: Int,
    key: Int,
    oldItems: Array<Item?>,
    newItems: Array<Item?>,
) {
    write(
        UpdateInvPartial(
            interfaceId = interfaceId,
            componentId = component,
            inventoryId = key,
            provider = RsModIndexedObjectProvider(oldItems.asNonNullIterator, newItems),
        ),
    )
}

fun Player.updateItemContainer(
    key: Int,
    oldItems: Array<Item?>,
    newItems: Array<Item?>,
) {
    write(
        UpdateInvPartial(
            inventoryId = key,
            provider = RsModIndexedObjectProvider(oldItems.asNonNullIterator, newItems),
        ),
    )
}

private val <T> Array<T>.asNonNullIterator: Iterator<Int>
    get() {
        return mapIndexedNotNull { index, item -> if (item != null) index else null }.iterator()
    }

/**
 * Sends a container type referred to as 'invother' in CS2, which is used for displaying a second container with
 * the same container key. An example of this is the trade accept screen, where the list of items being traded is stored
 * in container 90 for both the player's container, and the partner's container. A container becomes 'invother' when it's
 * component hash is less than -70000, which internally translates the container key to (key + 32768). We can achieve this by either
 * sending a component hash of less than -70000, or by setting the key ourselves. I feel like the latter makes more sense.
 *
 * Special thanks to Polar for explaining this concept to me.
 *
 * https://github.com/RuneStar/cs2-scripts/blob/a144f1dceb84c3efa2f9e90648419a11ee48e7a2/scripts/script768.cs2
 */
fun Player.sendItemContainerOther(
    key: Int,
    container: ItemContainer,
) {
    write(UpdateInvFull(inventoryId = key + 32768, capacity = container.rawItems.size, provider = RsModObjectProvider(container.rawItems)))
}

fun Player.sendRunEnergy(energy: Int) {
    write(UpdateRunEnergy(energy))
}

fun Player.playSound(
    id: Int,
    volume: Int = 1,
    delay: Int = 0,
) {

    write(SynthSound(id = id, loops = volume, delay = delay))
}
// @TODO
fun Player.playSong(id: Int) {
    write(MidiSongV2(
        id = 0,
        fadeOutDelay = 0,
        fadeOutSpeed = 0,
        fadeInDelay = 0,
        fadeInSpeed = 0
    ))
}

fun Player.playJingle(id: Int) {
    write(MidiJingle(id))
}

fun Player.getVarp(id: Int): Int = varps.getState(id)

fun Player.setVarp(
    id: Int,
    value: Int,
) {
    if (attr.has(CHANGE_LOGGING) && getVarp(id) != value) {
        message("Varp: $id was changed from: ${getVarp(id)} to $value")
    }
    varps.setState(id, value)
}

fun Player.toggleVarp(id: Int) {
    varps.setState(id, varps.getState(id) xor 1)
}

fun Player.syncVarp(id: Int) {
    setVarp(id, getVarp(id))
}

fun Player.getVarbit(id: Int): Int {
    val def = CacheManager.getVarbit(id)
    return varps.getBit(def.varp, def.startBit, def.endBit)
}

fun Player.incrementVarbit(
    id: Int,
    amount: Int = 1,
): Int {
    val inc = getVarbit(id) + amount
    setVarbit(id, inc)
    return inc
}

fun Player.decrementVarbit(
    id: Int,
    amount: Int = 1,
): Int {
    val dec = getVarbit(id) - amount
    setVarbit(id, dec)
    return dec
}

fun Player.setVarbit(
    id: Int,
    value: Int,
) {
    if (attr.has(CHANGE_LOGGING) && getVarbit(id) != value) {
        message("Varbit: $id was changed from: ${getVarbit(id)} to $value")
    }
    val def = CacheManager.getVarbit(id)
    varps.setBit(def.varp, def.startBit, def.endBit, value)
}

/**
 * Write a varbit message to the player's client without actually modifying
 * its varp value in [Player.varps].
 */
fun Player.sendTempVarbit(
    id: Int,
    value: Int,
) {
    val def = CacheManager.getVarbit(id)
    val state = BitManipulation.setBit(varps.getState(def.varp), def.startBit, def.endBit, value)
    val message = if (state in -Byte.MAX_VALUE..Byte.MAX_VALUE) VarpSmall(def.varp, state) else VarpLarge(def.varp, state)
    write(message)
}

fun Player.toggleVarbit(id: Int) {
    if (attr.has(CHANGE_LOGGING)) {
        message("Varbit toggle: $id was changed from: ${getVarbit(id)} to ${getVarbit(id) xor 1}")
    }
    val def = CacheManager.getVarbit(id)
    varps.setBit(def.varp, def.startBit, def.endBit, getVarbit(id) xor 1)
}

fun Player.setMapFlag(
    x: Int,
    y: Int,
) {
    write(SetMapFlag(x, y))
}

fun Player.clearMapFlag() {
    setMapFlag(255, 255)
}

fun Player.sendOption(
    option: String,
    id: Int,
    leftClick: Boolean = false,
) {
    check(id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    val index = id - 1
    options[index] = option
    write(SetPlayerOp(id = index, priority = leftClick, op = option))
}

/**
 * Checks if the player has an option with the name [option] (case-sensitive).
 */
fun Player.hasOption(
    option: String,
    id: Int = -1,
): Boolean {
    check(id == -1 || id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    return if (id != -1) options.any { it == option } else options[id - 1] == option
}

/**
 * Removes the option with [id] from this player.
 */
fun Player.removeOption(id: Int) {
    check(id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    val index = id - 1
    write(SetPlayerOp(id = index, priority = false, op = "null"))
    options[index] = null
}

fun Player.getStorageBit(
    storage: BitStorage,
    bits: StorageBits,
): Int = storage.get(this, bits)

fun Player.hasStorageBit(
    storage: BitStorage,
    bits: StorageBits,
): Boolean = storage.get(this, bits) != 0

fun Player.setStorageBit(
    storage: BitStorage,
    bits: StorageBits,
    value: Int,
) {
    storage.set(this, bits, value)
}

fun Player.toggleStorageBit(
    storage: BitStorage,
    bits: StorageBits,
) {
    storage.set(this, bits, storage.get(this, bits) xor 1)
}

fun Player.heal(
    amount: Int,
    capValue: Int = 0,
) {
    getSkills().alterCurrentLevel(skill = Skills.HITPOINTS, value = amount, capValue = capValue)
}

fun Player.getTarget(): Pawn? = attr[COMBAT_TARGET_FOCUS_ATTR]?.get()

fun Player.hasSpellbook(book: Spellbook): Boolean = getVarbit(Varbit.PLAYER_SPELL_BOOK) == book.id

fun Player.getSpellbook(): Spellbook = Spellbook.values.first { getVarbit(Varbit.PLAYER_SPELL_BOOK) == it.id }

fun Player.setSpellbook(book: Spellbook) = setVarbit(Varbit.PLAYER_SPELL_BOOK, book.id)

fun Player.getWeaponType(): Int = getVarbit(Varbit.WEAPON_TYPE_VARBIT)

fun Player.getAttackStyle(): Int = getVarp(Varp.WEAPON_ATTACK_STYLE)

fun Player.hasWeaponType(
    type: WeaponType,
    vararg others: WeaponType,
): Boolean =
    getWeaponType() == type.id ||
        others.isNotEmpty() &&
        getWeaponType() in
        others.map {
            it.id
        }
fun Player.hasEquipped(
    slot: EquipmentType,
    vararg items: Int,
): Boolean {
    check(items.isNotEmpty()) { "Items shouldn't be empty." }
    return items.any { equipment.hasAt(slot.id, it) }
}
fun Player.hasEquipped(
    slot: EquipmentType,
    vararg items: String,
): Boolean {
    check(items.isNotEmpty()) { "Items shouldn't be empty." }
    val scanned = items.map { getRSCM(it) }.toTypedArray()
    return scanned.any { equipment.hasAt(slot.id, it) }
}
fun Player.hasEquipped(items: IntArray) = items.all { equipment.contains(it) }
fun Player.hasEquipped(items: Array<String>) = items.map {getRSCM(it)}.all { equipment.contains(it) }
fun Player.getEquipment(slot: EquipmentType): Item? = equipment[slot.id]
fun Player.setSkullIcon(icon: SkullIcon) {
    avatar.extendedInfo.setSkullIcon(icon.id)
}
fun Player.skull(
    icon: SkullIcon,
    durationCycles: Int,
) {
    check(icon != SkullIcon.NONE)
    setSkullIcon(icon)
    timers[SKULL_ICON_DURATION_TIMER] = durationCycles
}
// @TODO UPDATE2
fun Player.hasSkullIcon(icon: SkullIcon): Boolean = skullIcon == icon.id

fun Player.isClientResizable(): Boolean =
    interfaces.displayMode == DisplayMode.RESIZABLE_NORMAL || interfaces.displayMode == DisplayMode.RESIZABLE_LIST

fun Player.sendWorldMapTile() {
    runClientScript(CommonClientScripts.WORLD_MAP_TILE, tile.as30BitInteger)
}

fun Player.sendCombatLevelText() {
    setVarbit(13027, combatLevel)
}

fun Player.sendWeaponComponentInformation() {
    println("TODO : sendWeaponComponentInformation")
    /**
     *
    val weapon = getEquipment(EquipmentType.WEAPON)
    val name: String
    val panel: Int
    if (weapon != null) {
        val definition = getItem(weapon.id)
        name = definition.name
        panel = Math.max(0, definition.weaponType)
        setComponentText(593, 3, "Category: " + WeaponCategory.get(definition.category))
    } else {
        name = "Unarmed"
        panel = 0
        setComponentText(593, 3, "Category: Unarmed")
    }
    setComponentText(593, 2, name)
    setVarbit(357, panel)
    TODO("Rewip")
     */
}

fun Player.calculateAndSetCombatLevel(): Boolean {
    val old = combatLevel

    val attack = getSkills().getBaseLevel(Skills.ATTACK)
    val defence = getSkills().getBaseLevel(Skills.DEFENCE)
    val strength = getSkills().getBaseLevel(Skills.STRENGTH)
    val hitpoints = getSkills().getBaseLevel(Skills.HITPOINTS)
    val prayer = getSkills().getBaseLevel(Skills.PRAYER)
    val ranged = getSkills().getBaseLevel(Skills.RANGED)
    val magic = getSkills().getBaseLevel(Skills.MAGIC)

    val melee = (strength + attack).toDouble()
    val mage = (floor((magic * 0.50)) + magic)
    val range = (floor((ranged * 0.50)) + ranged)

    val style =
        when {
            melee >= range && melee >= mage -> melee
            range >= melee && range >= mage -> range
            else -> mage
        }

    this.appearance.combatLevel = floor(0.25 * (defence + hitpoints + floor((prayer * 0.50))) + 0.325 * style).toInt()
    val changed = combatLevel != old

    runClientScript(CommonClientScripts.COMABT_LEVEL_SUMMARY, 46661634, 46661635, combatLevel)
    if (changed) {
        sendCombatLevelText()
        avatar.extendedInfo.setCombatLevel(combatLevel)
        return true
    }

    return false
}

// Note: this does not take ground items, that may belong to the player, into account.
fun Player.hasItem(
    item: Int,
    amount: Int = 1,
): Boolean = containers.values.firstOrNull { container -> container.getItemCount(item) >= amount } != null

fun Player.isPrivilegeEligible(to: String): Boolean = world.privileges.isEligible(privilege, to)

fun Player.getStrengthBonus(): Int = equipmentBonuses[10]

fun Player.getRangedStrengthBonus(): Int = equipmentBonuses[11]

fun Player.getMagicDamageBonus(): Int = equipmentBonuses[12]

fun Player.getPrayerBonus(): Int = equipmentBonuses[13]

