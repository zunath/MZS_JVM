package mzsJVM;

import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.UUID;

@SuppressWarnings("unused")
public class Module_OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject objSelf) {
        NWObject oPC = NWScript.getEnteringObject();
        NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
        int iTimesLoggedIn = NWScript.getLocalInt(oDatabase, "times_logged_in");
        NWObject oDead = NWScript.getItemPossessedBy(oPC, "death_token");
        NWObject oSurvive = NWScript.getItemPossessedBy(oPC, "survivor_guide");
        NWObject oAfkTool = NWScript.getItemPossessedBy(oPC, "AFKPlayerTool");
        NWScript.setLocalInt(oPC, "PC_ENTERING_MOD", 1);
        NWObject oTarget = NWScript.getWaypointByTag("WP_death");
        final NWLocation lTarget = NWScript.getLocation(oTarget);

        if(NWScript.getIsPC(oPC) && NWScript.getXP(oPC) == 0 && !NWScript.getIsDM(oPC))      // character with zero xp
        {
            if (NWScript.getXP(oPC) == 0)
            {
                // First, wipe their inventory
                //Added by Drakaden, to attempt to avoid new characters to enter with hacked equipped items
                NWObject oInventory = NWScript.getItemInSlot(Inventory.SLOT_ARMS, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_ARROWS, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_BELT, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_BOLTS, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_BOOTS, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_BULLETS, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CARMOUR, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CHEST, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CLOAK, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_B, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_L, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_R, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_HEAD, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_LEFTHAND, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_LEFTRING, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_NECK, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_RIGHTHAND, oPC);
                NWScript.destroyObject(oInventory, 0.0f);
                oInventory = NWScript.getItemInSlot(Inventory.SLOT_RIGHTRING, oPC);
                NWScript.destroyObject(oInventory, 0.0f);


                for(NWObject item : NWScript.getItemsInInventory(oPC))
                {
                    NWScript.destroyObject(item, 0.0f);
                }
            }

            NWScript.sendMessageToPC(oPC, "Before firing simtools"); // debug

            // SimTools Chat System
            NWScript.executeScript("fky_chat_clenter", objSelf);

            // PC Authorization System
            NWScript.executeScript("auth_mod_enter", objSelf);

            // Utilized by d20_on_equip. If this variable is not detected
            // by that script, it does not destroy/reload ammo mags.
            // This is to prevent ammo-eating OnEnter.
            NWScript.setLocalInt(oPC, "RKP_PC_ENTERED", 1);

            //Radio is also on by default upon login
            NWScript.setLocalInt(oPC, "radio", 1);

            // EFFECT CUTSCENE GHOSTWALK TO PC'S - Skeet 7/4/10
            NWEffect eGhost = NWScript.effectCutsceneGhost();
            NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhost, oPC, 0.0f);

            //PlayerID + CDKEY + IP address Logging, reports to DM channel also,
            // This is used mainly for investigative and banning purposes.
            String sIP = NWScript.getPCIPAddress(oPC);
            String sPlayerName = NWScript.getPCPlayerName(oPC);
            String sCDKey = NWScript.getPCPublicCDKey(oPC, false);
            String sPostCD = NWScript.getPCPublicCDKey(oPC, false);
            NWScript.sendMessageToAllDMs("<Entering - Player GSID: " + sPlayerName + ", CD-Key: " + sPostCD + ", IP: " + sIP + ">");
            NWScript.printString(sPlayerName + sCDKey + sIP); // Added by Skeet, print physical log of entering players

            // DM ACCESS ONLY BY VERIFIED CDKEY
            NWScript.executeScript("dm_authorization", objSelf);

            //IF a hostile PC mutation, set appropriate faction reps
            if (NWScript.getLocalInt(oDatabase, "mutation") == 2)
            {
                Scheduler.assign(oPC, new Runnable() {
                    public void run() {
                        NWScript.clearAllActions(false);
                    }
                });
                NWScript.setStandardFactionReputation(StandardFaction.COMMONER, 0, oPC);
                NWScript.setStandardFactionReputation(StandardFaction.DEFENDER, 0, oPC);
                NWScript.setStandardFactionReputation(StandardFaction.HOSTILE, 100, oPC);
            }


            //If the PC has a death token, take them to the death area
            if(NWScript.getIsPC(oPC) && (NWScript.getIsObjectValid(oDead)))
            {
                Scheduler.assign(oPC, new Runnable() {
                    public void run() {
                        NWScript.clearAllActions(false);
                        NWScript.actionJumpToLocation(lTarget);
                    }
                });
            }

            NWScript.executeScript("php_mod_enter", objSelf);

            // Starting cash
            NWScript.takeGoldFromCreature(NWScript.getGold(oPC), oPC, true);
            NWScript.giveGoldToCreature(oPC, 10);

            /// Provide starting widgets.
            // Deletes the mysterious second database.
            NWObject oItem = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
            if (NWScript.getIsObjectValid(oItem))
            {
                NWScript.setPlotFlag(oItem, false);
                NWScript.destroyObject(oItem, 0.0f);
            }

            oDatabase = NWScript.createItemOnObject(Constants.PCDatabaseTag, oPC, 1, "");
            // Set up their HP regen
            NWScript.setLocalInt(oDatabase, "HP_REGEN", 1);
            // Set up their max Hunger level
            NWScript.setLocalInt(oDatabase, "CURRENT_HUNGER", 100);
            NWScript.setLocalInt(oDatabase, "HUNGER_COUNT_DOWN", 30);
            // Set up their max thirst level
            NWScript.setLocalInt(oDatabase, "CURRENT_THIRST", 100);
            NWScript.setLocalInt(oDatabase, "THIRST_COUNT_DOWN", 20);
            // It's a new character, so don't make the skill system updates fire
            NWScript.setLocalInt(oDatabase, "SKILLS_UPDATED", 1);
            // Set their disease count down timer to max
            NWScript.setLocalInt(oDatabase, "DISEASE_COUNT_DOWN", 600);
            NWScript.setXP(oPC, 3001);    //LMA was 1001, but since level 3 is minimim now...
            NWScript.giveGoldToCreature(oPC, 100);
            NWScript.createItemOnObject("water_canteen", oPC, 1, "");
            // Starting Bread
            NWScript.createItemOnObject("food_bread", oPC, 2, "");

            NWScript.createItemOnObject("dmfi_pc_dicebag", oPC, 1, "");
            NWScript.createItemOnObject("dmfi_pc_follow", oPC, 1, "");
            NWScript.createItemOnObject("pcemotewidget", oPC, 1, "");
            NWScript.createItemOnObject("mzs2_itemdestroy", oPC, 1, "");

            NWScript.createItemOnObject("out_infection", oPC, 1, "");
        }

        // NOT NEW CHARS
        //LMA Added xp to old characters to keep up with new players
        int iXP = NWScript.getXP(oPC);
        if (iXP < 3000)
            NWScript.setXP(oPC, iXP + 2000);

        NWScript.executeScript("update_journal", objSelf);
        // Structure system - Disabled by Zunath 6/21/2011
        //ExecuteScript("struc_mod_enter", OBJECT_SELF);


        Scheduler.delay(oPC, 1, new Runnable() {
            public void run() {

                NWScript.deleteLocalInt(objSelf, "PC_ENTERING_MOD");
            }
        });

        // Zunath 2015-01-18: The old PC ID numbers were not being used, so I'm assigning
        // users with a UUID instead so we don't need to make a DB call.
        if(NWScript.getLocalString(oPC, Constants.PCIDNumberVariable).equals(""))
        {
            NWScript.setLocalString(oPC, Constants.PCIDNumberVariable, UUID.randomUUID().toString());
        }

        // Add individual starting items here
        if(NWScript.getIsPC(oPC) && (!NWScript.getIsObjectValid(oSurvive)))
            NWScript.createItemOnObject("survivor_guide", oPC, 1, "");

        if(NWScript.getIsPC(oPC) && (NWScript.getIsObjectValid(oAfkTool)))
        {
            NWScript.createItemOnObject("afkplayertool", oPC, 1, "");
        }

        // Gives the character a PC Reload Widget if they don't have one.
        if (!NWScript.getIsObjectValid(NWScript.getItemPossessedBy(oPC, "_mdrn_it_reload_pc")))
            NWScript.createItemOnObject("_mdrn_it_reload", oPC, 1, "");

        NWObject oDye = NWScript.getItemPossessedBy(oPC, "DyeKit");
        if(NWScript.getIsPC(oPC) && (!NWScript.getIsObjectValid(oDye)))
            oDye = NWScript.createItemOnObject("mil_dyekit001", oPC, 1, "");
        NWScript.setDroppableFlag(oDye, false);
        NWScript.setItemCursedFlag(oDye, true);

        //Writing System
        NWObject oPen = NWScript.getItemPossessedBy(oPC, "td_it_quillpen");
        if(NWScript.getIsPC(oPC) && !NWScript.getIsObjectValid(oPen))
        {
            oPen = NWScript.createItemOnObject("td_it_quillpen", oPC, 1, "");
            NWScript.setLocalInt(oPen, "ModuleUpdatedVersion", 5);
        }
        else
        if(NWScript.getIsPC(oPC) && NWScript.getIsObjectValid(oPen) && NWScript.getLocalInt(oPen, "ModuleUpdatedVersion") != 5)
        {
            NWScript.destroyObject(oPen, 0.0f);
            oPen = NWScript.createItemOnObject("td_it_quillpen", oPC, 1, "");
            NWScript.setLocalInt(oPen, "ModuleUpdatedVersion", 5);
        }

        //Infection Checker Item
        NWObject oInfChecker = NWScript.getItemPossessedBy(oPC, "out_infchecker");
        if(NWScript.getIsPC(oPC) && (!NWScript.getIsObjectValid(oInfChecker)))
            NWScript.createItemOnObject("out_infchecker", oPC, 1, "");

        //Updated Radio Items
        NWObject oRadioItem = NWScript.getItemPossessedBy(oPC, "_mdrn_ot_talkie");
        if (NWScript.getIsPC(oPC) && (NWScript.getIsObjectValid(oRadioItem)))
        {
            NWScript.destroyObject(oRadioItem, 0.1f);
            NWScript.createItemOnObject("_mdrn_ot_talk1", oPC, 1, "");
        }

        //Added by Drakaden, updates the old radio with the new one 14/06/11 Update Version: 2.
        oRadioItem = NWScript.getItemPossessedBy(oPC, "_mdrn_ot_talk1");
        if(NWScript.getIsPC(oPC) && NWScript.getIsObjectValid(oRadioItem) && NWScript.getLocalInt(oRadioItem, "ModuleUpdatedVersion") < 2)
        {
            NWScript.destroyObject(oRadioItem, 0.0f);
            oRadioItem = NWScript.createItemOnObject("_mdrn_ot_talk1", oPC, 1, "");
            NWScript.setLocalInt(oRadioItem, "ModuleUpdatedVersion", 2);
        }

        // Give a "Break Down Item" to players that don't have it already.
        NWObject oBreakDownItem = NWScript.getItemPossessedBy(oPC, "mzs2_itemdestroy");
        if (!NWScript.getIsObjectValid(oBreakDownItem))
        {
            NWScript.createItemOnObject("mzs2_itemdestroy", oPC, 1, "");
        }

        //Badge Book
        NWObject oBadgeBook = NWScript.getItemPossessedBy(oPC, "badgebook");
        String sPCName = NWScript.getName(oPC, false);
        if(NWScript.getIsPC(oPC) && (!NWScript.getIsObjectValid(oBadgeBook)))
        {
            NWScript.createItemOnObject("badgebook", oPC, 1, "");
            NWScript.setName(NWScript.getItemPossessedBy(oPC, "badgebook"), sPCName + "'s " + "Journal of Badges and Achievements");
            NWScript.setDroppableFlag(NWScript.getItemPossessedBy(oPC, "badgebook"), false);
            NWScript.setItemCursedFlag(NWScript.getItemPossessedBy(oPC, "badgebook"), true);
        }
        else
        {
            NWScript.setName(NWScript.getItemPossessedBy(oPC, "badgebook"), sPCName + "'s " + "Journal of Badges and Achievements");
            NWScript.setDroppableFlag(NWScript.getItemPossessedBy(oPC, "badgebook"), false);
            NWScript.setItemCursedFlag(NWScript.getItemPossessedBy(oPC, "badgebook"), true);
        }

        //CNR Refinery Tradeskill System, OnEnter
        NWScript.executeScript("cnr_module_oce", objSelf);

        //SUBDUAL HANDLER - Skeet 7/4/10
        NWScript.executeScript("subdual_clenter", NWScript.getEnteringObject());

        iTimesLoggedIn = iTimesLoggedIn + 1;
        NWScript.setLocalInt(oDatabase, "times_logged_in", iTimesLoggedIn);

        // Check if it's a invalid character.
        if (NWScript.getLevelByClass(ClassType.SORCERER, oPC) > 0 || NWScript.getLevelByClass(ClassType.WIZARD, oPC) > 0
                || NWScript.getLevelByClass(ClassType.CLERIC, oPC) > 0 || NWScript.getLevelByClass(ClassType.PALADIN, oPC) > 0
                || NWScript.getLevelByClass(ClassType.BARD, oPC) > 0 || NWScript.getLevelByClass(ClassType.DRUID, oPC) > 0
                || NWScript.getRacialType(oPC)!= RacialType.HUMAN)
        {
            NWScript.sendMessageToPC(oPC, "Invalid Character: No wizards, sorcerers, clerics, paladins, bards, or druids. Humans only. Please leave and try again.");


            Scheduler.assign(oPC, new Runnable() {
                public void run() {
                    NWScript.clearAllActions(false);
                    NWScript.actionJumpToLocation(lTarget);
                }
            });
        }

        // Set jump allowances
        //noinspection ConstantConditions,PointlessBooleanExpression
        if (!Constants.AllowJumpingByDefault)
            NWScript.setLocalInt(oPC, "JUMP_INVALID", 1);

        NWScript.floatingTextStringOnCreature("Welcome to Modern Zombie Survival 3!", oPC, false);
        NWScript.floatingTextStringOnCreature("Please, read your journal and survival guide for module information!", oPC, false);

        Scheduler.flushQueues();
    }
}
