package mzsJVM;
import mzsJVM.Bioware.*;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

public class Module_OnActivateItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject oPC = NWScript.getItemActivator();
		NWObject oItem = NWScript.getItemActivated();
		NWObject oItem2;
		NWObject oInfection = NWScript.getItemPossessedBy(oPC, "out_infection");
		NWObject oArea = NWScript.getArea(oPC);

		String sResRef = NWScript.getResRef(oItem);
		String sInfection = "infection";
		NWObject oTarget = NWScript.getItemActivatedTarget();
		String sItemTag = NWScript.getTag(oItem);
		String sTargetTag = NWScript.getTag(oTarget);

		String sCraft = NWScript.getStringLeft(sItemTag, 3);
		String sCure = NWScript.getStringLeft(sItemTag, 5);
		String sTag = NWScript.getTag(oArea);

		int iArmor1;
		int iArmor2;

		NWLocation lPCLocation = NWScript.getLocation(oPC);

		// PC Reload Widget [Hauntmaster - 11/6/2014]
		if (sItemTag.equals("_mdrn_it_reload_pc")) {
			NWScript.executeScript("_mdrn_rof_reload", oPC);
			return;
		}

		// SUBDUAL HANDLER - Skeet - 7/4/10
		if (sItemTag.equals("SubdualModeTog")) {
			NWScript.executeScript("subdualmodetog", oPC);
			return;
		}

		// Writing System - Skeet 7/12/10
		if (sItemTag.equals("td_it_quillpen")) {
			NWScript.executeScript("td_it_quillpen", oTarget);
			return;
		}

		// Infection Checker - Skeet 7/12/10
		if (sItemTag.equals("out_infchecker")) {
			NWScript.executeScript("out_infchecker", oTarget);
			return;
		}

		// Badge Book
		if (sItemTag.equals("badgebook")) {
			NWScript.executeScript("badgebook", oTarget);
			return;
		}

		// Books can now tear out pages
		// Edit by Drakaden, Notebooks can tear out pages too now
		if (sItemTag.equals("td_it_blankbook") || sItemTag.equals("notebook")) {
			NWScript.createItemOnObject("td_it_blankparch", oPC, 1, "");
			return;
		}

		// Added by Drakaden, Distraction Doll.
		if (sItemTag.equals("distractiondo001")) {
			oItem2 = NWScript.createObject(ObjectType.CREATURE,
					"distractiondo001", NWScript.getLocation(oPC), false, "");

			Scheduler.assign(oItem2, new Runnable() {
				public void run() {
					NWScript.speakString(
							"Hi! You want to be my friend? I want a hug.",
							Talkvolume.TALK);
				}
			});
			Scheduler.flushQueues();

			return;
		}

		// Added by Drakaden, empties perfume bottle.
		if (sItemTag.equals("perfumebottle001")) {
			NWScript.createItemOnObject("perfumebottle002", oPC, 1, "");
			NWScript.setPlotFlag(oItem, false);
			NWScript.destroyObject(oItem, 0.0f);
			return;
		}

		// Added by Drakaden, cigarettes packs can now spawn cigarettes, the
		// cigarette spawned is directly dependant on the user's sex for item
		// fashion purpose
		// Spawned cigs lasts 5 minutes then destroy themselves
		if (sItemTag.equals("_mdrn_ot_cigar")) {
			if (NWScript.getGender(oPC) == Gender.FEMALE) {
				oItem2 = NWScript.createItemOnObject("_mdrn_cigt_women", oPC,
						1, "");
				NWScript.destroyObject(oItem2, 300.0f);
			} else {
				oItem2 = NWScript.createItemOnObject("_mdrn_cigt_men", oPC, 1,
						"");
				NWScript.destroyObject(oItem2, 300.0f);
			}
			return;
		}

		// Added by Drakaden, Armor reinforcing Kit.
		if (sItemTag.equals("makeshiftarmo001")) {

			if (NWScript.getBaseItemType(oTarget) == BaseItem.ARMOR) {
				iArmor1 = NWScript.getItemACValue(oTarget);
				iArmor2 = 0;

				if (NWScript.getLocalInt(oTarget, "ArmorMakeshiftUpgraded1") == 1) {
					NWScript.sendMessageToPC(oPC,
							"This armor has already been upgraded.");
					return;
				}
				if (iArmor1 <= 3) // AC value limit, cannot upgrade more than up
									// to +4 if the number is 3.
				{

					NWItemProperty[] itemProperties = NWScript
							.getItemProperties(oTarget);

					for (NWItemProperty itemProperty : itemProperties) {
						if (NWScript.getLocalInt(oTarget, "ACPropertyFound1") != 1
								&& itemProperty.getItemPropertyId() == ItemProperty.AC_BONUS) {
							iArmor2 = NWScript
									.getItemPropertyCostTableValue(itemProperty);
							NWScript.setLocalInt(oTarget, "ACPropertyFound1", 1);

							break;
						}
					}

					NWScript.setLocalInt(oTarget, "ArmorMakeshiftUpgraded1", 1);
					XP3.IPSafeAddItemProperty(oTarget,
							NWScript.itemPropertyACBonus(iArmor2 + 1), 0.0f,
							AddItemPropertyPolicy.ReplaceExisting, false, false);
					NWScript.destroyObject(oItem, 0.0f);
				} else {
					NWScript.sendMessageToPC(oPC,
							"This armor cannot be upgraded further.");
				}
			}
			return;
		}

		// Added by Drakaden, hammer can dismantle specific things.
		if (sItemTag.equals("hammer001")) {
			if (sTargetTag.equals("MkwChair")) // Chairs made of wood. "MkwChair" is
											// the Chair Weapon.
			{
				NWScript.createItemOnObject("cr_nails", oPC, 1, "");
				NWScript.createItemOnObject("cr_wood", oPC, 1, "");
				if (NWScript.random(100) >= 71) {
					NWScript.createItemOnObject("cr_nails", oPC, 1, "");
				}
				if (NWScript.random(100) >= 71) {
					NWScript.createItemOnObject("cr_wood", oPC, 1, "");
				}
				NWScript.destroyObject(oTarget, 0.0f);
			}
		}

		// Added by Drakaden, scissors can salvage cloth and leather armors for
		// cloth/leather patches.
		if (sItemTag.equals("scissors001")) {
			// Added by Drakaden, allows to scissor cloth patches for bandages,
			// from 1 to 3.
			if (sTargetTag.equals("clothpatches001")) {
				NWScript.createItemOnObject("cr_bandages", oPC, 1, "");
				if (NWScript.random(100) >= 51) {
					NWScript.createItemOnObject("cr_bandages", oPC, 1, "");
				}
				if (NWScript.random(100) >= 51) {
					NWScript.createItemOnObject("cr_bandages", oPC, 1, "");
				}
				NWScript.destroyObject(oTarget, 0.0f);
			}

			if (NWScript.getBaseItemType(oTarget) == BaseItem.ARMOR) {
				XP3.IPRemoveAllItemProperties(oTarget, Duration.TYPE_PERMANENT);
				iArmor2 = NWScript.getItemACValue(oTarget);

				if (iArmor2 == 0) {
					NWScript.destroyObject(oTarget, 0.0f);
					NWScript.createItemOnObject("clothpatches001", oPC, 1, "");
				}
				if (iArmor2 >= 1 && iArmor2 <= 3) {
					NWScript.destroyObject(oTarget, 0.0f);
					NWScript.createItemOnObject("leatherpatche001", oPC, 1, "");
				}
			}
		}

		// Added by Drakaden, Zombie Perfume effects, be warned that this will
		// also affect Mutants, NPC humans on hostile faction.
		if (sItemTag.equals("zombieperfume001")) {

			NWScript.setStandardFactionReputation(StandardFaction.HOSTILE, 50,
					oPC);

			Scheduler.delay(oPC, 30, new Runnable() {
				public void run() {
					NWScript.setStandardFactionReputation(
							StandardFaction.HOSTILE, 0,
							NWScript.getItemActivator());
				}
			});
			Scheduler.flushQueues();

			// NWScript.delayCommand(30.0f,
			// NWScript.setStandardFactionReputation(StandardFaction.HOSTILE, 0,
			// oPC));
			NWScript.setLocalInt(
					oInfection,
					sInfection,
					NWScript.getLocalInt(oInfection, sInfection)
							+ (NWScript.random(5) + 1));
			NWScript.setName(
					oInfection,
					"Infection Rate: "
							+ NWScript.getLocalInt(oInfection, sInfection)
							+ "%");
			NWScript.sendMessageToPC(NWScript.getItemActivator(), "Infection: "
					+ NWScript.getLocalInt(oInfection, sInfection) + "%.");
		}

		// Added by Drakaden, recycle glass bottles from Bottles of wine, beer
		// or water.
		if (sResRef.equals("drink_waterbo002") || sResRef.equals("drink_waterbo001")
				|| sResRef.equals("drink_waterbottl")) {
			NWScript.createItemOnObject("cr_miscthin001", oPC, 1, "");
		}

		// DM Item Modifier
		if (sItemTag.equals("DM_Item_Manipulator")) {
			if (NWScript.getIsObjectValid(oTarget)
					&& NWScript.getObjectType(oTarget) == ObjectType.ITEM) {
				NWScript.setLocalObject(oPC, "TARGETED_ITEM", oTarget);

				Scheduler.assign(oPC, new Runnable() {
					public void run() {
						NWScript.actionStartConversation(
								NWScript.getItemActivator(), "dm_wand_item",
								true, false);
					}
				});
				Scheduler.flushQueues();

			} else {
				NWScript.sendMessageToPC(oPC, "That is not a valid item");
			}

			return;
		}

		// Mutational consumable item. Starts conversation - Skeet - 11/25/2014
		if (sItemTag.equals("pc_mutationsyn")) {
			Scheduler.assign(oPC, new Runnable() {
				public void run() {
					NWScript.actionStartConversation(
							NWScript.getItemActivator(), "mzs3_pcmutation",
							true, false);
				}
			});
			Scheduler.flushQueues();

			NWScript.destroyObject(oItem, 0.0f);
		}

		// Crafting: Any item with cr_ tag prefix is handled as a crafting item
		// and fired under mzs_crafting script - Skeet
		if (sCraft.equals("cr_")) {
			NWScript.executeScript("mzs_crafting", objSelf);
			return;
		}

		else if (sItemTag.equals("DyeKit")) {
			Scheduler.assign(oPC, new Runnable() {
				public void run() {
					NWScript.actionStartConversation(
							NWScript.getItemActivator(), "dye_dyekit", true,
							false);
				}
			});
			Scheduler.flushQueues();
		}

		else if (sCure.equals("cure_")) {
			NWScript.executeScript("mzs_curative", objSelf);
			return;
		}

		// Player AFK Tool
		if (sItemTag.equals("AFKPlayerTool")) {
			if (sTag.equals("OOCAFKLounge")) {
				NWScript.sendMessageToPC(oPC, "You're already in the lounge!");
				return;
			}

			if (NWScript.getIsInCombat(oPC)) {
				NWScript.sendMessageToPC(oPC, "This can't be used in combat!");
				return;
			}

			else {
				String sPCNAME = NWScript.getName(oPC, false);

				Scheduler.assign(oPC, new Runnable() {
					public void run() {
						NWObject waypoint = NWScript.getObjectByTag(
								"AFK_AREA_Spawn", 0);
						NWScript.jumpToObject(waypoint, false);
					}
				});
				Scheduler.flushQueues();

				NWScript.createObject(ObjectType.WAYPOINT, "playerwp_",
						lPCLocation, false, "Playerwp_ " + sPCNAME);
			}
			return;
		}

		// CNR Tra
		NWScript.executeScript("cnr_module_onact", objSelf);

		// d20
		NWScript.executeScript("d20_on_activate", objSelf);

		// Bioware Default
		NWScript.executeScript("x2_mod_def_act", objSelf);
	}
}
