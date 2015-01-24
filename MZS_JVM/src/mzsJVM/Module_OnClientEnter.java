package mzsJVM;

import mzsJVM.Data.ItemDTO;
import mzsJVM.GameObjects.CreatureGO;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings("unused")
public class Module_OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject objSelf) {
        NWObject oPC = NWScript.getEnteringObject();

        // Prevent other script events from firing while logging in.
        NWScript.setLocalInt(oPC, "PC_ENTERING_MOD", 1);
        Scheduler.delay(oPC, 1000, new Runnable() {
            public void run() {

                NWScript.deleteLocalInt(objSelf, "PC_ENTERING_MOD");
            }
        });

        if (NWScript.getXP(oPC) == 0 && !NWScript.getIsDM(oPC))
        {
            InitializeNewCharacter(oPC);
        }

        SendPlayerJoinedMessage(oPC);
        ApplyPCEffects(oPC);
        ApplyMutationEffects(oPC);
        ApplyDeathEffects(oPC);
        ApplyPCVariables(oPC);
        AddJournalEntries(oPC);
        GiveSystemItems(oPC);
        ValidateCharacter(oPC);
        //LoadHitPoints(oPC);

        NWScript.floatingTextStringOnCreature("Welcome to Modern Zombie Survival 3!", oPC, false);
        NWScript.floatingTextStringOnCreature("Please read your journal and survival guide for module information!", oPC, false);

        Scheduler.flushQueues();

        FireScripts(objSelf);
    }

    private void InitializeNewCharacter(final NWObject oPC)
    {
        CreatureGO pcGO = new CreatureGO(oPC);
        pcGO.destroyAllEquippedItems();
        pcGO.destroyAllInventoryItems();

        Scheduler.assign(oPC, new Runnable() {
            @Override
            public void run() {
                NWScript.takeGoldFromCreature(NWScript.getGold(oPC), oPC, true);
                NWScript.giveGoldToCreature(oPC, 10);
            }
        });

        NWObject oDatabase = NWScript.createItemOnObject(Constants.PCDatabaseTag, oPC, 1, "");
        NWScript.setLocalInt(oDatabase, "HP_REGEN", 1);
        NWScript.setLocalInt(oDatabase, "CURRENT_HUNGER", 100);
        NWScript.setLocalInt(oDatabase, "HUNGER_COUNT_DOWN", 30);
        NWScript.setLocalInt(oDatabase, "CURRENT_THIRST", 100);
        NWScript.setLocalInt(oDatabase, "THIRST_COUNT_DOWN", 20);
        NWScript.setLocalInt(oDatabase, "SKILLS_UPDATED", 1);
        NWScript.setLocalInt(oDatabase, "DISEASE_COUNT_DOWN", 600);
        NWScript.setXP(oPC, 3001); // Set to level 3
        NWScript.createItemOnObject("food_bread", oPC, 2, "");
    }

    private void SendPlayerJoinedMessage(NWObject oPC)
    {
        String sIP = NWScript.getPCIPAddress(oPC);
        String sPlayerName = NWScript.getPCPlayerName(oPC);
        String sCDKey = NWScript.getPCPublicCDKey(oPC, false);
        String sPostCD = NWScript.getPCPublicCDKey(oPC, false);
        NWScript.sendMessageToAllDMs("<Entering - Player GSID: " + sPlayerName + ", CD-Key: " + sPostCD + ", IP: " + sIP + ">");
        NWScript.printString(sPlayerName + sCDKey + sIP);
    }

    private void ApplyPCEffects(NWObject oPC)
    {
        NWEffect eGhost = NWScript.effectCutsceneGhost();
        NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhost, oPC, 0.0f);
    }

    private void ApplyMutationEffects(NWObject oPC)
    {
        NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

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
    }

    private void ApplyDeathEffects(NWObject oPC)
    {
        NWObject oDeathToken = NWScript.getItemPossessedBy(oPC, "death_token");
        final String deathWaypoint = "WP_death";
        final NWLocation lTarget = NWScript.getLocation(NWScript.getWaypointByTag(deathWaypoint));

        if(NWScript.getIsPC(oPC) && (NWScript.getIsObjectValid(oDeathToken)))
        {
            Scheduler.assign(oPC, new Runnable() {
                public void run() {
                    NWScript.clearAllActions(false);
                    NWScript.actionJumpToLocation(lTarget);
                }
            });
        }
    }

    private void ApplyPCVariables(NWObject oPC)
    {
        NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

        // Uniquely generated ID
        if(NWScript.getLocalString(oDatabase, Constants.PCIDNumberVariable).equals(""))
        {
            NWScript.setLocalString(oDatabase, Constants.PCIDNumberVariable, UUID.randomUUID().toString());
        }

        // Utilized by d20_on_equip. If this variable is not detected
        // by that script, it does not destroy/reload ammo mags.
        // This is to prevent ammo-eating OnEnter.
        NWScript.setLocalInt(oPC, "RKP_PC_ENTERED", 1);

        NWScript.setLocalInt(oPC, "radio", 1);

        // Set jump allowances
        //noinspection ConstantConditions,PointlessBooleanExpression
        if (!Constants.AllowJumpingByDefault) {
            NWScript.setLocalInt(oPC, "JUMP_INVALID", 1);
        }

        NWScript.setLocalInt(oDatabase, "times_logged_in", NWScript.getLocalInt(oDatabase, "times_logged_in") + 1);
    }

    private void ValidateCharacter(NWObject oPC)
    {
        Integer[] validClasses = { ClassType.FIGHTER, ClassType.ROGUE, ClassType.RANGER, ClassType.MONK, ClassType.BARBARIAN, ClassType.INVALID };
        Integer[] validRaces = { RacialType.HUMAN };

        Integer race = NWScript.getRacialType(oPC);
        Integer classType1 = NWScript.getClassByPosition(1, oPC);
        Integer classType2 = NWScript.getClassByPosition(2, oPC);
        Integer classType3 = NWScript.getClassByPosition(3, oPC);

        if(!Arrays.asList(validRaces).contains(race) ||
                !Arrays.asList(validClasses).contains(classType1) ||
                !Arrays.asList(validClasses).contains(classType2) ||
                !Arrays.asList(validClasses).contains(classType3))
        {
            NWScript.sendMessageToPC(oPC, "Invalid Character: No wizards, sorcerers, clerics, paladins, bards, or druids. Humans only. Please leave and try again.");

            final String waypointTag = "";
            final NWLocation lTarget = NWScript.getLocation(NWScript.getWaypointByTag(waypointTag));

            Scheduler.assign(oPC, new Runnable() {
                @Override
                public void run() {
                    NWScript.clearAllActions(false);
                    NWScript.actionJumpToLocation(lTarget);
                }
            });
        }

    }


    // Creates system items on the player if they don't already exist or if the PC's version is older than the current one.
    private void GiveSystemItems(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC)) return;
        String pcName = NWScript.getName(oPC, false);

        ArrayList<ItemDTO> items = new ArrayList<ItemDTO>();
        items.add(new ItemDTO("dmfi_pc_dicebag", "dmfi_pc_dicebag", 1, ""));
        items.add(new ItemDTO("dmfi_pc_follow", "dmfi_pc_follow", 1, ""));
        items.add(new ItemDTO("pcemotewidget", "dmfi_pc_emote", 1, ""));
        items.add(new ItemDTO("mzs2_itemdestroy", "mzs2_itemdestroy", 1, ""));
        items.add(new ItemDTO("out_infection", "out_infection", 1, ""));
        items.add(new ItemDTO("out_infchecker", "out_infchecker", 1, ""));
        items.add(new ItemDTO("td_it_quillpen", "td_it_quillpen", 5, ""));
        items.add(new ItemDTO("badgebook", "badgebook", 1, "%s's Journal of Badges and Achievements"));
        items.add(new ItemDTO("mil_dyekit001", "DyeKit", 1, ""));
        items.add(new ItemDTO("_mdrn_it_reload", "_mdrn_it_reload_pc", 1, ""));
        items.add(new ItemDTO("afkplayertool", "AFKPlayerTool", 1, ""));
        items.add(new ItemDTO("survivor_guide", "survivor_guide", 1, ""));
        items.add(new ItemDTO("water_canteen", "water_canteen", 1, ""));

        for(ItemDTO item : items)
        {
            NWObject oItem = NWScript.getItemPossessedBy(oPC, item.GetResref());

            if(!NWScript.getIsObjectValid(oItem))
            {
                oItem = NWScript.getItemPossessedBy(oPC, item.GetTag());
            }

            int version = NWScript.getLocalInt(oItem, "MZS2_ITEM_VERSION");

            if(!NWScript.getIsObjectValid(oItem) || version < item.GetVersion())
            {
                NWScript.destroyObject(oItem, 0.0f);
                oItem = NWScript.createItemOnObject(item.GetResref(), oPC, 1, "");

                NWScript.setDroppableFlag(oItem, false);
                NWScript.setItemCursedFlag(oItem, true);

                NWScript.setLocalInt(oItem, "MZS2_ITEM_VERSION", item.GetVersion());

                // Rename the item only if a format has been specified.
                if(!item.GetNameFormat().equals(""))
                {
                    NWScript.setName(oItem, String.format(item.GetNameFormat(), pcName));
                }
            }
        }
    }
    private void AddJournalEntries(NWObject oPC)
    {
        NWScript.addJournalQuestEntry("info_1", 1, oPC, false, false, false);
        NWScript.addJournalQuestEntry("info_2", 1, oPC, false, false, false);
        NWScript.addJournalQuestEntry("rules_1", 1, oPC, false, false, false);
        NWScript.addJournalQuestEntry("write_1", 1, oPC, false, false, false);
    }

    private void LoadHitPoints(NWObject oPC)
    {
        NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
        int iFirstRun = NWScript.getLocalInt(oDatabase, "PHP_FIRSTRUN");
        int iHP = NWScript.getCurrentHitPoints(oPC);

        // Prevents new characters from being damaged
        if(iFirstRun < 2)
        {
            NWScript.setLocalInt(oDatabase, "PHP_FIRSTRUN", 2);
            return;
        }

        int iStoredHP = NWScript.getLocalInt(oDatabase, "PHP_STORED_HP");
        int iDamage;

        // If the stored HP is in the negatives, we need to bring the PC down to 0 HP, and then apply the HP
        if(iStoredHP < 0)
        {
            iDamage = iHP + Math.abs(iStoredHP);
        }
        // Otherwise the stored HP is in the positives, so we simply need to take current HP and subtract it by the stored HP
        else
        {
            iDamage = iHP - iStoredHP;
        }

        // Deal damage only if the HP of oPC needs to be lowered.
        if(iDamage != 0) {
            NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDamage(iDamage, DamageType.MAGICAL, DamagePower.NORMAL), oPC, 0.0f);
        }
    }

    private void FireScripts(final NWObject objSelf)
    {
        NWScript.executeScript("fky_chat_clenter", objSelf); // SIMTools
        NWScript.executeScript("auth_mod_enter", objSelf);   // PC Authorization System
        NWScript.executeScript("dm_authorization", objSelf); // DM Authorization System
        NWScript.executeScript("cnr_module_oce", objSelf);   // CNR Refinery System
    }

}
