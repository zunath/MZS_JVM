package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.AttackBonus;
import org.nwnx.nwnx2.jvm.constants.Duration;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

@SuppressWarnings("unused")
public class Module_OnPlayerEquipItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {


		final NWObject oItem = NWScript.getPCItemLastEquipped();
		NWObject oPC   = NWScript.getPCItemLastEquippedBy();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

		NWObject oWeapon = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
		NWObject oOffHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);

		int iIdentification1;
		int iIdentification2;
		int iFireRateMode1 = NWScript.getLocalInt(oWeapon, "FireRateMode1");
		int iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
		int iAccuracyPenalty = 0;

		if (iFireRateMode1 <= 0)
		{
			NWScript.setLocalInt(oWeapon, "FireRateMode1", 1);
		}

		if (iFireRateMode1 == 1)
		{
			iAccuracyPenalty = 0;
			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
		}

		if (iFireRateMode1 == 2)
		{
			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate2");
			iAccuracyPenalty = 1;
		}

		if (iFireRateMode1 == 3)
		{
			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate3");
			iAccuracyPenalty = 2;
		}

		final NWEffect eExtraAttack1 = NWScript.effectModifyAttacks(iFireRate1 - 1);
		final NWEffect eDualGunAccuracyPenalty1 = NWScript.effectAttackDecrease(iAccuracyPenalty, AttackBonus.ONHAND);

		//Added by Drakaden, flashlights now use up a charge 1 minute after they have been equipped.
		//Added by Drakaden, if the flashlight only has 1 charge remaining, spawn a Flashlight (No Batteries) and destroy the flashlight.
		if (NWScript.getTag(oItem).equals("_mdrn_ot_light"))
		{
			Scheduler.delay(objSelf, 60000, new Runnable() {
				@Override
				public void run() {
					NWScript.setItemCharges(oItem, NWScript.getItemCharges(oItem) - 1);
				}
			});

			if (NWScript.getItemCharges(oItem) <= 1)
			{
				NWScript.createItemOnObject("flashlightnob001", oPC, 1, "");
				NWScript.destroyObject(oItem, 0.0f);
			}

		}

		//Added by Drakaden, single-weild firearms extra attack.
		iIdentification1 = NWScript.findSubString(NWScript.getTag(oOffHand), "off", 0);
		iIdentification2 = NWScript.findSubString(NWScript.getTag(oWeapon), "dual", 0);

		if (NWScript.getWeaponRanged(oItem) && iIdentification1 <= -1 && iIdentification2 <= 1)
		{
			if (NWScript.getLocalInt(oDatabase, "DualGunSEffect1") != 1)
			{
				Scheduler.assign(oPC, new Runnable() {
					@Override
					public void run() {
						NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eExtraAttack1, objSelf, 99999.0f);
						NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eDualGunAccuracyPenalty1, objSelf, 99999.0f);
					}
				});

				NWScript.setLocalInt(oDatabase, "DualGunsEffect1", 1);
			}
		}

		//Added by Drakaden, dual-weild firearms extra attack.
		iIdentification1 = NWScript.findSubString(NWScript.getTag(oOffHand), "off", 0);
		iIdentification2 = NWScript.findSubString(NWScript.getTag(oWeapon), "dual", 0);

		if (iIdentification1 >= 0 && iIdentification2 >= 0)
		{
			if (NWScript.getLocalInt(oDatabase, "DualGunSEffect1") != 1)
			{
				if (!NWScript.getLocalString(oOffHand, "AmmoType").equals(NWScript.getLocalString(oWeapon, "AmmoType")))
				{
					NWScript.sendMessageToPC(oPC, "The main hand and the offhand guns are ammunitions types incompatible, you can still equip them, but the offhand won't do anything.");
				}
				else
				{
					Scheduler.assign(oPC, new Runnable() {
						@Override
						public void run() {
							NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eExtraAttack1, objSelf, 99999.0f);
							NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eDualGunAccuracyPenalty1, objSelf, 99999.0f);

						}
					});

					NWScript.setLocalInt(oDatabase, "DualGunsEffect1", 1);
				}
			}
		}

		// d20
		NWScript.executeScript("d20_on_equip", objSelf);

		// Bioware Default
		NWScript.executeScript("x2_mod_def_equ", objSelf);

		Scheduler.flushQueues();
	}
}
