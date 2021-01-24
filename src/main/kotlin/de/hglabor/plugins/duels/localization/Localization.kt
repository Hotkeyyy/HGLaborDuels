package de.hglabor.plugins.duels.localization

import net.axay.kspigot.chat.KColors

object Localization {
    val PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Duels ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val SOUPSIMULATOR_PREFIX = " ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}Soupsimulator ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val PARTY_PREFIX = " ${KColors.DARKGRAY}| ${KColors.MAGENTA}Party ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val NO_PERM_EN = "${PREFIX}${KColors.TOMATO}You do not have permissions to access that command."
    val NO_PERM_DE = "${PREFIX}${KColors.TOMATO}Du hast dazu keine Rechte."
    val PLAYER_NOT_ONLINE_EN = "${PREFIX}${KColors.RED}The player ${KColors.DARKRED}%playerName% ${KColors.RED}is not online."
    val PLAYER_NOT_ONLINE_DE = "${PREFIX}${KColors.RED}Der Spieler ${KColors.DARKRED}%playerName% ${KColors.RED}ist nicht online."
    val CANT_DO_THAT_RIGHT_NOW_EN = "${PREFIX}${KColors.RED}You can't do that right now."
    val CANT_DO_THAT_RIGHT_NOW_DE = "${PREFIX}${KColors.RED}Du kannst das gerade nicht tun."
    val COMMAND_WRONG_ARGUMENTS_EN = "${PREFIX}${KColors.TOMATO}Wrong arguments"
    val COMMAND_WRONG_ARGUMENTS_DE = "${PREFIX}${KColors.TOMATO}Falsche Argumente"

    // Command not found
    val UNKNOWN_COMMAND_EN = "${PREFIX}Command ${KColors.DARKGRAY}\"${KColors.DODGERBLUE}%command%${KColors.DARKGRAY}\" ${KColors.GRAY}not found."
    val UNKNOWN_COMMAND_DE = "${PREFIX}Der Command ${KColors.DARKGRAY}\"${KColors.DODGERBLUE}%command%${KColors.DARKGRAY}\" ${KColors.GRAY}wurde nicht gefunden."

    // CreateArena
    val ARENA_CREATION_SAVING_EN = "$PREFIX${KColors.CORNSILK}Saving arena..."
    val ARENA_CREATION_SAVING_DE = "$PREFIX${KColors.CORNSILK}Arena wird gespeichert..."
    val ARENA_CREATION_FAILED_VALUE_MISSING_EN = "$PREFIX${KColors.RED}Saving failed. There is a value missing."
    val ARENA_CREATION_FAILED_VALUE_MISSING_DE = "$PREFIX${KColors.RED}Speichern fehlgeschlagen. Ein wert wurde nicht gesetzt."
    val ARENA_CREATION_FAILED_FILE_EN = "$PREFIX${KColors.RED}Saving failed. Couldn't save arenas.yml."
    val ARENA_CREATION_FAILED_FILE_DE = "$PREFIX${KColors.RED}Speichern fehlgeschlagen. Konnte arenas.yml nicht speichern."
    val ARENA_CREATION_FAILED_ALREADY_EXISTING_EN = "$PREFIX${KColors.RED}Saving failed. Schematic %schematicName% is already existing."
    val ARENA_CREATION_FAILED_ALREADY_EXISTING_DE = "$PREFIX${KColors.RED}Speichern fehlgeschlagen. Schematic %schematicName% existiert bereits."
    val ARENA_CREATION_FAILED_SCHEMATIC_EN = "$PREFIX${KColors.RED}Saving failed. Couldn't save schematic."
    val ARENA_CREATION_FAILED_SCHEMATIC_DE = "$PREFIX${KColors.RED}Speichern fehlgeschlagen. Konnte Schematic nicht speichern."
    val ARENA_CREATION_SUCCESS_EN = "$PREFIX${KColors.GREEN}Arena was created successfully."
    val ARENA_CREATION_SUCCESS_DE = "$PREFIX${KColors.GREEN}Arena wurde erfolgreich gespiechert."

    // CreateArenaGUI
    val ARENA_CREATION_GUI_NAME_EN = "${KColors.DODGERBLUE}Create Arena"
    val ARENA_CREATION_GUI_NAME_DE = "${KColors.DODGERBLUE}Arena Erstellen"
    val ARENA_CREATION_GUI_SETNAMEITEM_NAME_EN = "${KColors.DODGERBLUE}Set name of arena"
    val ARENA_CREATION_GUI_SETNAMEITEM_NAME_DE = "${KColors.DODGERBLUE}Name der Arena festlegen"
    val ARENA_CREATION_GUI_SETNAME_INCHAT_EN = "${KColors.DODGERBLUE}Set the name of the arena. Type \"Abort\" to abort."
    val ARENA_CREATION_GUI_SETNAME_INCHAT_DE = "${KColors.DODGERBLUE}Lege den Namen der Arena fest. Schreibe \"Abbrechen\", zum abbrechen."
    val ARENA_CREATION_GUI_CORNERITEM_NAME_EN = "${KColors.DODGERBLUE}Set corners of arena"
    val ARENA_CREATION_GUI_CORNERITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Ecken der Arena markieren"
    val ARENA_CREATION_GUI_LORE_GETTOOL_EN = "${KColors.CORNSILK}Click to get the tool"
    val ARENA_CREATION_GUI_LORE_GETTOOL_DE = "${KColors.CORNSILK}Klicke um das Tool zu erhalten"
    val ARENA_CREATION_GUI_LORE_POS1_EN = "${KColors.GRAY}Position 1: set"
    val ARENA_CREATION_GUI_LORE_POS1_DE = "${KColors.GRAY}Position 1: gesetzt"
    val ARENA_CREATION_GUI_LORE_POS2_EN = "${KColors.GRAY}Position 2: set"
    val ARENA_CREATION_GUI_LORE_POS2_DE = "${KColors.GRAY}Position 2: gesetzt"
    val ARENA_CREATION_GUI_SPAWNITEM_NAME_EN = "${KColors.DODGERBLUE}Set spawns of arena"
    val ARENA_CREATION_GUI_SPAWNITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Spawns der Arena markieren"
    val ARENA_CREATION_GUI_SETTAGITEM_NAME_EN = "${KColors.DODGERBLUE}Set tag of arena ${KColors.GRAY}(optional)"
    val ARENA_CREATION_GUI_SETTAGITEM_NAME_DE = "${KColors.DODGERBLUE}Tags der Arena festlegen ${KColors.GRAY}(optional)"
    val ARENA_CREATION_GUI_SAVEITEM_NAME_EN = "${KColors.SPRINGGREEN}Save arena"
    val ARENA_CREATION_GUI_SAVEITEM_NAME_DE = "${KColors.SPRINGGREEN}Arena speichern"
    val ARENA_CREATION_GUI_ABORTITEM_NAME_EN = "${KColors.TOMATO}Abort"
    val ARENA_CREATION_GUI_ABORTITEM_NAME_DE = "${KColors.TOMATO}Abbrechen"
    val ARENA_CREATION_GUI_ABORTITEM_LORE_EN = "${KColors.RED}Attention! ${KColors.GRAY}This will delete all saved values"
    val ARENA_CREATION_GUI_ABORTITEM_LORE_DE = "${KColors.RED}Achtung! ${KColors.GRAY}Löscht die gespeicherten Werte"
    val ARENA_CREATION_ABORTED_EN = "${PREFIX}You ${KColors.TOMATO}aborted ${KColors.GRAY}the creation."
    val ARENA_CREATION_ABORTED_DE = "${PREFIX}Du hast das Erstellen${KColors.TOMATO} abgebrochen${KColors.GRAY}."
    val ARENA_CREATION_CORNERITEM_LORE1_EN = "${KColors.GRAY}Left click: ${KColors.CORNSILK}Set position 1"
    val ARENA_CREATION_CORNERITEM_LORE1_DE = "${KColors.GRAY}Linksklick: ${KColors.CORNSILK}Position 1 setzen"
    val ARENA_CREATION_CORNERITEM_LORE2_EN = "${KColors.GRAY}Right click: ${KColors.CORNSILK}Set position 2"
    val ARENA_CREATION_CORNERITEM_LORE2_DE = "${KColors.GRAY}Rechtsklick: ${KColors.CORNSILK}Position 2 setzen"
    val ARENA_CREATION_SPAWNITEM_LORE1_EN = "${KColors.GRAY}Left click: ${KColors.CORNSILK}Set spawn 1"
    val ARENA_CREATION_SPAWNITEM_LORE1_DE = "${KColors.GRAY}Linksklick: ${KColors.CORNSILK}Spawn 1 setzen"
    val ARENA_CREATION_SPAWNITEM_LORE2_EN = "${KColors.GRAY}Right click: ${KColors.CORNSILK}Set spawn 2"
    val ARENA_CREATION_SPAWNITEM_LORE2_DE = "${KColors.GRAY}Rechtsklick: ${KColors.CORNSILK}Spawn 2 setzen"

    // ArenaTagsGUI
    val ARENA_CREATEION_TAGSGUI_NAME = "${KColors.DODGERBLUE}Arena Tags"
    val CHOOSE_TAG_EN = "${PREFIX}You choose the ${KColors.DODGERBLUE}%tag% ${KColors.GRAY}tag."
    val CHOOSE_TAG_DE = "${PREFIX}Du hast den ${KColors.DODGERBLUE}%tag% ${KColors.GRAY}Tag gewählt."

    // CreateArenaListener
    val ARENA_CREATION_LISTENER_ABORTED_SET_NAME_EN = "${PREFIX}You ${KColors.TOMATO}didn't set a name ${KColors.GRAY}for the arena."
    val ARENA_CREATION_LISTENER_ABORTED_SET_NAME_DE = "${PREFIX}Du ${KColors.TOMATO}keinen Namen ${KColors.GRAY}für die Arena ${KColors.TOMATO}festgelegt${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_NAME_EN = "${PREFIX}You named the arena ${KColors.DEEPSKYBLUE}%arenaName%${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_NAME_DE = "${PREFIX}Du hast die Arena ${KColors.DEEPSKYBLUE}%arenaName% ${KColors.GRAY}genannt."
    val ARENA_CREATION_LISTENER_SET_CORNER1_EN = "${PREFIX}You set the ${KColors.DODGERBLUE}first Position ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY})${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_CORNER1_DE = "${PREFIX}Du hast die ${KColors.DODGERBLUE}erste Position ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY}) ${KColors.GRAY}gesetzt."
    val ARENA_CREATION_LISTENER_SET_SPAWN1_EN = "${PREFIX}You set the ${KColors.DODGERBLUE}first Spawn ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY})${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_SPAWN1_DE = "${PREFIX}Du hast den ${KColors.DODGERBLUE}ersten Spawn ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY}) ${KColors.GRAY}gesetzt."
    val ARENA_CREATION_LISTENER_SET_CORNER2_EN = "${PREFIX}You set the ${KColors.DODGERBLUE}second Position ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY})${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_CORNER2_DE = "${PREFIX}Du hast die ${KColors.DODGERBLUE}zweite Position ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY}) ${KColors.GRAY}gesetzt."
    val ARENA_CREATION_LISTENER_SET_SPAWN2_EN = "${PREFIX}You set the ${KColors.DODGERBLUE}second Spawn ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY})${KColors.GRAY}."
    val ARENA_CREATION_LISTENER_SET_SPAWN2_DE = "${PREFIX}Du hast den ${KColors.DODGERBLUE}zweiten Spawn ${KColors.DARKGRAY}(${KColors.DEEPSKYBLUE}%x% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%y% ${KColors.DARKGRAY}| ${KColors.DEEPSKYBLUE}%z%${KColors.DARKGRAY}) ${KColors.GRAY}gesetzt."

    // ArenaCommand
    val ARENA_COMMAND_HELP1_EN = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena create ${KColors.DARKGRAY}- ${KColors.GRAY}Create an arena"
    val ARENA_COMMAND_HELP1_DE = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena create ${KColors.DARKGRAY}- ${KColors.GRAY}Erstelle eine Arena"
    val ARENA_COMMAND_HELP2_EN = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena buildworld ${KColors.DARKGRAY}- ${KColors.GRAY}Teleports you into the buildworld"
    val ARENA_COMMAND_HELP2_DE = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena buildworld ${KColors.DARKGRAY}- ${KColors.GRAY}Teleportiere dich in die buildworld"
    val ARENA_COMMAND_HELP3_EN = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena fightworld ${KColors.DARKGRAY}- ${KColors.GRAY}Teleports you into the fightworld"
    val ARENA_COMMAND_HELP3_DE = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena fightworld ${KColors.DARKGRAY}- ${KColors.GRAY}Teleportiere dich in die fightworld"
    val ARENA_COMMAND_HELP4_EN = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena list ${KColors.DARKGRAY}- ${KColors.GRAY}List all arenas"
    val ARENA_COMMAND_HELP4_DE = " ${KColors.DARKGRAY}| ${KColors.DODGERBLUE}/arena list ${KColors.DARKGRAY}- ${KColors.GRAY}Zeige alle Arenen an"

    // LeaveCommand
    val LEAVE_COMMAND_TO_LEAVE_EN = "${KColors.DODGERBLUE}/leave ${KColors.GRAY}to leave"
    val LEAVE_COMMAND_TO_LEAVE_DE = "${KColors.DODGERBLUE}/leave ${KColors.GRAY}zum Verlassen"

    // SpecCommand
    val SPEC_COMMAND_PLAYER_NOT_FIGHTING_EN = "${PREFIX}${KColors.TOMATO}%playerName% is not fighting."
    val SPEC_COMMAND_PLAYER_NOT_FIGHTING_DE = "${PREFIX}${KColors.TOMATO}%playerName% ist nicht am Kämpfen."
    val SPEC_COMMAND_HELP_EN = "${PREFIX}${KColors.GRAY}Please use ${KColors.DODGERBLUE}/spec ${KColors.DARKGRAY}<${KColors.DEEPSKYBLUE}Player${KColors.DARKGRAY}>"
    val SPEC_COMMAND_HELP_DE = "${PREFIX}${KColors.GRAY}Bitte benutze ${KColors.DODGERBLUE}/spec ${KColors.DARKGRAY}<${KColors.DEEPSKYBLUE}Spieler${KColors.DARKGRAY}>"

    // StatsCommand
    val STATS_COMMAND_PLAYER_NOT_FOUND_EN = "${PREFIX}${KColors.TOMATO}Player %playerName% not found."
    val STATS_COMMAND_PLAYER_NOT_FOUND_DE = "${PREFIX}${KColors.TOMATO}Spieler %playerName% wurde nicht gefunden."

    // ChallengeCommand
    val CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_EN = "${PREFIX}${KColors.TOMATO}%playerName% is already in fight."
    val CHALLENGE_COMMAND_ACCEPT_PLAYER_IN_FIGHT_DE = "${PREFIX}${KColors.TOMATO}%playerName% ist bereits im Kampf."
    val CHALLENGE_COMMAND_CANT_DUEL_SELF_EN = "${PREFIX}${KColors.TOMATO}You can't challenge yourself."
    val CHALLENGE_COMMAND_CANT_DUEL_SELF_DE = "${PREFIX}${KColors.TOMATO}Du kannst dich nicht selbst herrausfordern."
    val CHALLENGE_COMMAND_HELP = "${PREFIX}${KColors.TOMATO}/Challenge ${KColors.DARKGRAY}[${KColors.RED}${KColors.DARKGRAY}]"

    // DuelOverviewCommand
    val DUELOVERVIEW_COMMAND_GAME_NOT_FOUND_EN = "${PREFIX}${KColors.TOMATO}Game with ID %gameID% not found."
    val DUELOVERVIEW_COMMAND_GAME_NOT_FOUND_DE = "${PREFIX}${KColors.TOMATO}Spiel mit der ID %gameID% wurde nicht gefunden."
    val DUELOVERVIEW_HELP = "${PREFIX}${KColors.TOMATO}/dueloverview gameID player"

    // Party
    val PARTY_CREATED_EN = "${PARTY_PREFIX}Your party has been ${KColors.GREEN}created${KColors.GRAY}."
    val PARTY_CREATED_DE = "${PARTY_PREFIX}Deine Party wurde ${KColors.GREEN}erstellt${KColors.GRAY}."
    val PARTY_YOU_INVITED_EN = "${PARTY_PREFIX}You invited ${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}to your party."
    val PARTY_YOU_INVITED_DE = "${PARTY_PREFIX}Du hast ${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}zu deiner Party eingealden."
    val PARTY_YOU_WERE_INVITED_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}invited you to their party."
    val PARTY_YOU_WERE_INVITED_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}hat dich in seine Party eingeladen."
    val PARTY_PLAYER_JOINED_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}joined the party."
    val PARTY_PLAYER_JOINED_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}ist der Party beigetreten."
    val PARTY_YOU_JOINED_EN = "${PARTY_PREFIX}You joined ${KColors.MEDIUMPURPLE}%playerName%'s ${KColors.GRAY}party."
    val PARTY_YOU_JOINED_DE = "${PARTY_PREFIX}Du bist ${KColors.MEDIUMPURPLE}%playerName%'s ${KColors.GRAY}party."
    val PARTY_PLAYER_LEFT_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}left ${KColors.GRAY}left the party."
    val PARTY_PLAYER_LEFT_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}hat die Party ${KColors.TOMATO}verlassen${KColors.GRAY}."
    val PARTY_YOU_LEFT_EN = "${PARTY_PREFIX}${KColors.GRAY}You ${KColors.TOMATO}left ${KColors.GRAY}the party."
    val PARTY_YOU_LEFT_DE = "${PARTY_PREFIX}${KColors.GRAY}Du hast die Party ${KColors.TOMATO}verlassen${KColors.GRAY}."
    val PARTY_PLAYER_WAS_KICKED_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}was ${KColors.TOMATO}kicked ${KColors.GRAY}from the party."
    val PARTY_PLAYER_WAS_KICKED_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}wurde aus der Party ${KColors.TOMATO}geworfen${KColors.GRAY}."
    val PARTY_YOU_WERE_KICKED_EN = "${PARTY_PREFIX}${KColors.GRAY}You were ${KColors.TOMATO}kicked ${KColors.GRAY}from the party."
    val PARTY_YOU_WERE_KICKED_DE = "${PARTY_PREFIX}${KColors.GRAY}Du wurdest aus der Party ${KColors.TOMATO}gekickt${KColors.GRAY}."
    val PARTY_DELETED_EN = "${PARTY_PREFIX}${KColors.GRAY}The party was ${KColors.TOMATO}deleted${KColors.GRAY}."
    val PARTY_DELETED_DE = "${PARTY_PREFIX}${KColors.GRAY}Die Party ${KColors.TOMATO}gelöscht${KColors.GRAY}."
    val PARTY_NOW_PUBLIC_EN = "${PARTY_PREFIX}${KColors.GRAY}The party is now ${KColors.MEDIUMPURPLE}public${KColors.GRAY}."
    val PARTY_NOW_PUBLIC_DE = "${PARTY_PREFIX}${KColors.GRAY}Die Party ist nun ${KColors.MEDIUMPURPLE}öffentlich${KColors.GRAY}."
    val PARTY_NOW_PRIVAT_EN = "${PARTY_PREFIX}${KColors.GRAY}The party is now ${KColors.MEDIUMPURPLE}privat${KColors.GRAY}."
    val PARTY_NOW_PRIVAT_DE = "${PARTY_PREFIX}${KColors.GRAY}Die Party ist nun ${KColors.MEDIUMPURPLE}privat${KColors.GRAY}."

    // Party Command
    val PARTY_COMMAND_ALREADY_IN_PARTY_EN = "${PARTY_PREFIX}${KColors.TOMATO}You're already in a party."
    val PARTY_COMMAND_ALREADY_IN_PARTY_DE = "${PARTY_PREFIX}${KColors.TOMATO}Du bist bereits in einer Party"
    val PARTY_COMMAND_NOT_IN_PARTY_DE = "${PARTY_PREFIX}${KColors.TOMATO}You're not in a party."
    val PARTY_COMMAND_NOT_IN_PARTY_EN = "${PARTY_PREFIX}${KColors.TOMATO}Du bist in keiner Party."
    val PARTY_COMMAND_PLAYER_ALREADY_IN_PARTY_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}is already in a party."
    val PARTY_COMMAND_PLAYER_ALREADY_IN_PARTY_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}ist bereits in einer Party."
    val PARTY_COMMAND_PLAYER_ALREADY_INVITED_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}ist bereits in einer Party."
    val PARTY_COMMAND_CANT_JOIN_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}doesn't have a public party."
    val PARTY_COMMAND_CANT_JOIN_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}hat keine öffentliche Party."
    val PARTY_COMMAND_PLAYER_HAS_NO_PARTY_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}has no party."
    val PARTY_COMMAND_PLAYER_HAS_NO_PARTY_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}hat keine Party."
    val PARTY_COMMAND_PLAYER_NOT_IN_PARTY_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}isn't in a party."
    val PARTY_COMMAND_PLAYER_NOT_IN_PARTY_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}ist in keiner Party."
    val PARTY_COMMAND_NOT_LEADER_EN = "${PARTY_PREFIX}${KColors.TOMATO}You aren't the party leader."
    val PARTY_COMMAND_NOT_LEADER_DE = "${PARTY_PREFIX}${KColors.TOMATO}Du bist nicht der Partyleiter."
    val PARTY_COMMAND_PLAYER_NOT_IN_OWN_EN = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}isn't in your party."
    val PARTY_COMMAND_PLAYER_NOT_IN_OWN_DE = "${PARTY_PREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.TOMATO}ist nicht in deiner Party."

    // Duel
    val DUEL_STARTING_TITLE_EN = "${KColors.DEEPSKYBLUE}Good luck!"
    val DUEL_STARTING_TITLE_DE = "${KColors.DEEPSKYBLUE}Viel Glück!"
    val DUEL_WINNER_EN = " ${KColors.DARKGRAY}| ${KColors.GRAY}Winner ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}"
    val DUEL_WINNER_DE = " ${KColors.DARKGRAY}| ${KColors.GRAY}Gewinner ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}"
    val DUEL_LOSER_EN = " ${KColors.DARKGRAY}| ${KColors.GRAY}Loser ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}"
    val DUEL_LOSER_DE = " ${KColors.DARKGRAY}| ${KColors.GRAY}Verlierer ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}"
    val CLICK_NAME_TO_OPEN_INV_EN = " ${KColors.DARKGRAY}| ${KColors.GRAY}${KColors.ITALIC}Click name to open players inventory"
    val CLICK_NAME_TO_OPEN_INV_DE = " ${KColors.DARKGRAY}| ${KColors.GRAY}${KColors.ITALIC}Klicke auf einen Namen umd das Inventar zu öffnen"

    // Kit
    val CAN_USE_KIT_AGAIN_EN = "${PREFIX}You can use your kit again."
    val CAN_USE_KIT_AGAIN_DE = "${PREFIX}Du kannst dein Kit wieder benutzen."

    // CreateArenaInventory
    val ARENA_INVENTORY_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Open Arena GUI ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val ARENA_INVENTORY_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Arena GUI öffnen ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"

    // MainInventory
    val MAIN_INVENTORY_PARTY_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Create Party ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val MAIN_INVENTORY_PARTY_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Party Erstellen ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val MAIN_INVENTORY_DUEL_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Duel Player"
    val MAIN_INVENTORY_DUEL_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Spieler herrausfordern"
    val MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Soupsimulator ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val MAIN_INVENTORY_SOUPSIMULATOR_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Soupsimulator ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val MAIN_INVENTORY_QUEUE_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Queue ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val MAIN_INVENTORY_QUEUE_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Queue ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val MAIN_INVENTORY_SETTINGS_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Settings ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val MAIN_INVENTORY_SETTINGS_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Einstellungen ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"

    // PartyInventory
    val PARTY_INVENTORY_PARTY_GAME_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Party Games ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val PARTY_INVENTORY_PARTY_GAME_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Party Spiele ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val PARTY_INVENTORY_INFO_ITEM_NAME_EN = "${KColors.DEEPSKYBLUE}Party Info ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val PARTY_INVENTORY_INFO_ITEM_NAME_DE = "${KColors.DEEPSKYBLUE}Party Info ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"

    // SoupsimulatorGUI
    val SOUPSIMULATOR_GUI_EASY_NAME_EN = "${KColors.LIMEGREEN}EASY"
    val SOUPSIMULATOR_GUI_EASY_NAME_DE = "${KColors.LIMEGREEN}EINFACH"
    val SOUPSIMULATOR_GUI_EASY_LORE1_EN = "${KColors.CORNSILK}Endless Soupsimulator"
    val SOUPSIMULATOR_GUI_EASY_LORE1_DE = "${KColors.CORNSILK}Endloser Soupsimulator"
    val SOUPSIMULATOR_GUI_EASY_LORE2_EN = "${KColors.CORNSILK}Practice hotkeying and quickdropping"
    val SOUPSIMULATOR_GUI_EASY_LORE2_DE = "${KColors.CORNSILK}Übe das Hotkeyn und Quickdroppen"
    val SOUPSIMULATOR_GUI_MEDIUM_NAME_EN = "${KColors.LIMEGREEN}MEDIUM"
    val SOUPSIMULATOR_GUI_MEDIUM_NAME_DE = "${KColors.LIMEGREEN}MITTEL"
    val SOUPSIMULATOR_GUI_MEDIUM_LORE1_EN = "${KColors.CORNSILK}Damage when failing"
    val SOUPSIMULATOR_GUI_MEDIUM_LORE1_DE = "${KColors.CORNSILK}Schaden bei Fehlern"
    val SOUPSIMULATOR_GUI_HARD_NAME_EN = "${KColors.DARKRED}HARD"
    val SOUPSIMULATOR_GUI_HARD_NAME_DE = "${KColors.DARKRED}SCHWER"
    val SOUPSIMULATOR_GUI_HARD_LORE1_EN = "${KColors.CORNSILK}Damage when failing + time limit"
    val SOUPSIMULATOR_GUI_HARD_LORE1_DE = "${KColors.CORNSILK}Schaden bei Fehlern + Zeitbegrenzung"
    val SOUPSIMULATOR_GUI_BONUS_NAME = "${KColors.PURPLE}BONUS"
    val SOUPSIMULATOR_GUI_BONUS_LORE1_EN = "${KColors.CORNSILK}Damage when failing + time limit"
    val SOUPSIMULATOR_GUI_BONUS_LORE1_DE = "${KColors.CORNSILK}Schaden bei Fehlern + Zeitbegrenzung"
    val SOUPSIMULATOR_GUI_BONUS_LORE2_EN = "${KColors.CORNSILK}+ Refill and Recraft"
    val SOUPSIMULATOR_GUI_BONUS_LORE2_DE = "${KColors.CORNSILK}+ Refill und Recraft"

    // Soupsimulator
    val SOUPSIMULATOR_SURVIVED_EN = "${SOUPSIMULATOR_PREFIX}You ${KColors.GREEN}survived ${KColors.GRAY}the simulator."
    val SOUPSIMULATOR_SURVIVED_DE = "${SOUPSIMULATOR_PREFIX}Du hast den Simulator ${KColors.GREEN}überlebt${KColors.GRAY}."
    val SOUPSIMULATOR_END_SCORE_EN = " ${KColors.DARKGRAY}| ${KColors.GRAY}Score ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}%score%"
    val SOUPSIMULATOR_END_SCORE_DE = " ${KColors.DARKGRAY}| ${KColors.GRAY}Ergebnis ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}%score%"
    val SOUPSIMULATOR_END_REFILLS = " ${KColors.DARKGRAY}| ${KColors.GRAY}Refills ${KColors.DARKGRAY}» ${KColors.BLUEVIOLET}%refills%"
    val SOUPSIMULATOR_END_RECRAFTS = " ${KColors.DARKGRAY}| ${KColors.GRAY}Recrafts ${KColors.DARKGRAY}» ${KColors.DARKVIOLET}%recrafts%"
    val SOUPSIMULATOR_END_WRONGHOTKEYS_EN = " ${KColors.DARKGRAY}| ${KColors.GRAY}Wrong Hotkeys ${KColors.DARKGRAY}» ${KColors.RED}%wrongHotkeys%"
    val SOUPSIMULATOR_END_WRONGHOTKEYS_DE = " ${KColors.DARKGRAY}| ${KColors.GRAY}Falsche Hotkeys ${KColors.DARKGRAY}» ${KColors.RED}%wrongHotkeys%"
    val SOUPSIMULATOR_NEW_RECORD_EN = "${SOUPSIMULATOR_PREFIX}${KColors.GREEN}You broke your previous record${KColors.GRAY}."
    val SOUPSIMULATOR_NEW_RECORD_DE = "${SOUPSIMULATOR_PREFIX}${KColors.GREEN}Du hast deinen bisherigen Rekord gebrochen${KColors.GRAY}."

    // SoupsimulatorEvents
    val SOUPSIMULATOR_END_DIED_EN ="${SOUPSIMULATOR_PREFIX}You ${KColors.RED}did not survive ${KColors.GRAY}the simulator."
    val SOUPSIMULATOR_END_DIED_DE = "${SOUPSIMULATOR_PREFIX}Du hast den Simulator ${KColors.RED}nicht überlebt${KColors.GRAY}."

    // SpawnUtils
    val SETSPAWN_EN = "${PREFIX}${KColors.GREEN}You set the spawnpoint"
    val SETSPAWN_DE = "${PREFIX}${KColors.GREEN}Du hast den Spawn gesetzt"

    //Playerfunction
    val NO_ARENA_FOUND_EN = "${PREFIX}${KColors.TOMATO}No arena with tag \"${KColors.DARKRED}%tag%${KColors.TOMATO}\" found."
    val NO_ARENA_FOUND_DE = "${PREFIX}${KColors.TOMATO}Es wurde keien Arena mit dem Tag \"${KColors.DARKRED}%tag%${KColors.TOMATO}\" gefunden."
    val YOU_DUELED_EN = "${PREFIX}You challenged ${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}with the kit ${KColors.DODGERBLUE}%kit%${KColors.GRAY}."
    val YOU_DUELED_DE = "${PREFIX}Du hast ${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}mit dem Kit ${KColors.DODGERBLUE}%kit% ${KColors.GRAY}herrausgefordert."
    val YOU_WERE_DUELED_EN = "${PREFIX}${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}challenged you with the kit ${KColors.DODGERBLUE}%kit%${KColors.GRAY}."
    val YOU_WERE_DUELED_DE = "${PREFIX}Du wurdest von ${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}mit dem Kit ${KColors.DODGERBLUE}%kit% ${KColors.GRAY}herrausgefordert."
    val ACCEPT_BUTTON_EN = "Accept the challenge"
    val ACCEPT_BUTTON_DE = "Nehme die Herrausforderung an"
    val PLAYER_STARTED_SPECTATING_EN = "$PREFIX${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}is now spectating the fight."
    val PLAYER_STARTED_SPECTATING_DE = "$PREFIX${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}sieht dem Kampf nun zu."
    val PLAYER_STOPPED_SPECTATING_EN = "$PREFIX${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}is no longer spectating the fight."
    val PLAYER_STOPPED_SPECTATING_DE = "$PREFIX${KColors.DODGERBLUE}%playerName% ${KColors.GRAY}hat aufgehört dem Kampf zu zusehen."
    val STOP_SPECTATING_ITEM_NAME_EN = "${KColors.DODGERBLUE}Stop spectating ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val STOP_SPECTATING_ITEM_NAME_DE = "${KColors.DODGERBLUE}Aufhören zu spectaten ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"


    // STAFF
    val STAFFPREFIX = " ${KColors.DARKGRAY}| ${KColors.DARKPURPLE}Staff ${KColors.DARKGRAY}» ${KColors.GRAY}"
    val HAVE_TO_BE_IN_STAFFMODE_EN = "${STAFFPREFIX}${KColors.TOMATO}You have to be in staff mode to do that."
    val HAVE_TO_BE_IN_STAFFMODE_DE = "${STAFFPREFIX}${KColors.TOMATO}Du musst im Staffmode sein, um das zu tun."
    val STAFF_PLAYER_NOT_FOUND_EN = "${STAFFPREFIX}${KColors.TOMATO}Player %playerName% not found."
    val STAFF_PLAYER_NOT_FOUND_DE = "${STAFFPREFIX}${KColors.TOMATO}Spieler %playerName% wurde nicht gefunden."

    // StaffmodeCommand
    val STAFFMODE_ENABLED_EN = "${STAFFPREFIX}You have ${KColors.GREEN}entered ${KColors.DARKPURPLE}staffmode${KColors.GRAY}."
    val STAFFMODE_ENABLED_DE = "${STAFFPREFIX}Du hast den ${KColors.DARKPURPLE}Staffmode ${KColors.GREEN}betreten${KColors.GRAY}."
    val STAFFMODE_DISABLED_EN = "${STAFFPREFIX}You have ${KColors.RED}left ${KColors.DARKPURPLE}staffmode${KColors.GRAY}."
    val STAFFMODE_DISABLED_DE = "${STAFFPREFIX}Du hast den ${KColors.DARKPURPLE}Staffmode ${KColors.RED}verlassen${KColors.GRAY}."

    // StaffInventory
    val STAFFINVENTORY_TOGGLE_INVISIBILITY_EN = "${KColors.RED}Toggle Invisibility ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val STAFFINVENTORY_TOGGLE_INVISIBILITY_DE = "${KColors.RED}Unsichtbarkeit umschalten ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val STAFFINVENTORY_TOGGLE_VISIBILITY_EN = "${KColors.GREEN}Toggle Visibility ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val STAFFINVENTORY_TOGGLE_VISIBILITY_DE = "${KColors.RED}Sichtbarkeit umschalten ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"
    val STAFFINVENTORY_INVSEE_EN = "${KColors.DODGERBLUE}Invsee ${KColors.DARKGRAY}* ${KColors.GRAY}Right click"
    val STAFFINVENTORY_INVSEE_DE = "${KColors.DODGERBLUE}Invsee ${KColors.DARKGRAY}* ${KColors.GRAY}Rechtsklick"

    // FollowCommand
    val STAFF_PLAYER_IS_AT_SPAWN_EN = "${STAFFPREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}is at spawn.\n${KColors.GRAY}You will be teleported when their next fight begins."
    val STAFF_PLAYER_IS_AT_SPAWN_DE = "${STAFFPREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}ist am Spawn.\n${KColors.GRAY}Du wirst teleportiert, wenn sein/ihr nächster Kampf beginnt."
    val FOLLOW_COMMAND_PLAYER_STARTED_FIGHTING_EN = "${STAFFPREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}started fighting.\n${KColors.GRAY}You have been teleported."
    val FOLLOW_COMMAND_PLAYER_STARTED_FIGHTING_DE = "${STAFFPREFIX}${KColors.MEDIUMPURPLE}%playerName% ${KColors.GRAY}hat angefangen zu kämpfen.\n${KColors.GRAY}Du wurdest teleportiert."
    val FOLLOW_COMMAND_HELP_EN = "${STAFFPREFIX}${KColors.GRAY}Please use ${KColors.MEDIUMPURPLE}/follow ${KColors.DARKGRAY}<${KColors.MEDIUMPURPLE}Player${KColors.DARKGRAY}>"
    val FOLLOW_COMMAND_HELP_DE = "${STAFFPREFIX}${KColors.GRAY}Bitte benutze ${KColors.MEDIUMPURPLE}/follow ${KColors.DARKGRAY}<${KColors.MEDIUMPURPLE}Spieler${KColors.DARKGRAY}>"

    // SettingsGUI
    val SETTINGSGUI_KNOCKBACK_NAME_EN = "${KColors.DODGERBLUE}Knockback"
    val SETTINGSGUI_KNOCKBACK_NAME_DE = "${KColors.DODGERBLUE}Rückstoß"
    val SETTINGSGUI_DAMAGESOUND_NAME_EN = "${KColors.DODGERBLUE}Knockback Attack sound"
    val SETTINGSGUI_DAMAGESOUND_NAME_DE = "${KColors.DODGERBLUE}Rückstoß Angriff Sound"
    val SETTINGSGUI_ALLOWSPECTATORS_NAME = "${KColors.DODGERBLUE}Spectators"
    val SETTINGSGUI_CHATINFIGHT_NAME_EN = "${KColors.DODGERBLUE}Chat in fight"
    val SETTINGSGUI_CHATINFIGHT_NAME_DE = "${KColors.DODGERBLUE}Chat im Kampf"
    val SETTINGSGUI_CHAT_NAME = "${KColors.DODGERBLUE}Chat"

    // Tournament

    // Queue
    val QUEUE_JOINED_EN = "${PREFIX}Entered queue for ${KColors.MEDIUMPURPLE}%kit%"
    val QUEUE_JOINED_DE = "${PREFIX}Queue für ${KColors.MEDIUMPURPLE}%kit% ${KColors.GRAY}betreten"
    val QUEUE_LEFT_EN = "${PREFIX}Left queue for ${KColors.MEDIUMPURPLE}%kit%"
    val QUEUE_LEFT_DE = "${PREFIX}Queue für ${KColors.MEDIUMPURPLE}%kit% ${KColors.GRAY}verlassen."

}
