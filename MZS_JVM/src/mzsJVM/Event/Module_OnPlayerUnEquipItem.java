package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.AttackBonus;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

@SuppressWarnings("unused")
public class Module_OnPlayerUnEquipItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		final NWObject oPC   = NWScript.getPCItemLastUnequippedBy();
		NWObject oItem = NWScript.getPCItemLastUnequipped();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
		NWObject oWeapon = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
		NWObject oOffHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
        String sTag = NWScript.getTag(oItem);

		int iIdentification1;
		int iIdentification2;

		int iFireRateMode1 = NWScript.getLocalInt(oWeapon, "FireRateMode1");
		int iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
		int iAccuracyPenalty = 0;

		NWEffect eExtraAttack1;
		NWEffect eDualGunAccuracyPenalty1;

		if (iFireRateMode1 <= 0)
		{
			NWScript.setLocalInt(oWeapon, "FireRateMode1", 1);
		}

		if (iFireRateMode1 == 1)
		{
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

		eExtraAttack1 = NWScript.effectModifyAttacks(iFireRate1 - 1);
		eDualGunAccuracyPenalty1 = NWScript.effectAttackDecrease(iAccuracyPenalty, AttackBonus.ONHAND);

		// d20
		NWScript.executeScript("d20_on_unequip", objSelf);

		// Bioware Default
		NWScript.executeScript("x2_mod_def_unequ", objSelf);

		// Torches
		if (NWScript.getTag(oItem).equals("zn_torch"))
		{NWScript.destroyObject(oItem, 0.0f);}

		//If unequip mutational item, re-equip it.
		if (NWScript.getIsPC(oPC) && NWScript.getTag(oItem).equals("mut_stats"))
			NWScript.actionEquipItem(oItem, InventorySlot.RIGHTRING);

		//Added by Drakaden, single-weild firearms extra attack.
		iIdentification1 = NWScript.findSubString(NWScript.getTag(oOffHand), "off", 0);
		iIdentification2 = NWScript.findSubString(NWScript.getTag(oWeapon), "dual", 0);

		if (NWScript.getWeaponRanged(oItem) && iIdentification1 <= -1 && iIdentification2 <= 1)
		{
			NWEffect[] effects = NWScript.getEffects(oPC);

			for(NWEffect current : effects)
			{
				if (NWScript.getEffectType(current) == NWScript.getEffectType(eExtraAttack1) ||
						NWScript.getEffectType(current) == NWScript.getEffectType(eDualGunAccuracyPenalty1))
					NWScript.removeEffect(oPC, current);
			}

			NWScript.setLocalInt(oDatabase, "DualGunsEffect1", -1);
		}

		//Added by Drakaden, dual-weild firearms extra attack removal.
		iIdentification1 = NWScript.findSubString(NWScript.getTag(oItem), "off", 0);
		iIdentification2 = NWScript.findSubString(NWScript.getTag(oItem), "dual", 0);

		if (iIdentification1 >= 0 || iIdentification2 >= 0)
		{
			NWEffect[] effects = NWScript.getEffects(oPC);

			for(NWEffect current : effects)
			{
				if (NWScript.getEffectType(current) == NWScript.getEffectType(eExtraAttack1) ||
						NWScript.getEffectType(current) == NWScript.getEffectType(eDualGunAccuracyPenalty1))
				{
					NWScript.removeEffect(oPC, current);
				}
			}
			NWScript.setLocalInt(oDatabase, "DualGunsEffect1", -1);
		}


        if(sTag.equals("Injury"))
        {
            Scheduler.assign(oPC, new Runnable() {
                @Override
                public void run() {
                    NWScript.clearAllActions(false);
                    NWScript.actionEquipItem(oPC, InventorySlot.LEFTRING);
                }
            });
        }

        Scheduler.flushQueues();

	}
}
