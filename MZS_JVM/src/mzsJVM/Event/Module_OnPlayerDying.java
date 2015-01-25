package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("unused")
public class Module_OnPlayerDying implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject oPC = NWScript.getLastPlayerDying();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

		if (NWScript.getLocalInt(oDatabase, "zombified") == 1) return;
		BleedProcess(objSelf);

	}

	private void BleedProcess(final NWObject objSelf) {
		NWObject oDying = NWScript.getLastPlayerDying();
		NWScript.setLocalObject(oDying, "PCName", oDying);
		NWScript.setLocalInt(oDying, "iPCDCC", 0);

		Scheduler.assign(oDying, new Runnable() {
			@Override
			public void run() {
				NWScript.clearAllActions(false);
				Bleed(objSelf, 1);
			}
		});
		Scheduler.flushQueues();
	}


	private void Bleed(final NWObject objSelf, int iBleedAmt)
	{
		NWObject oDying = NWScript.getLocalObject(objSelf, "PCName");
		//SendMessageToPC(oDying, "Death status: " + IntToString(GetLocalInt(GetItemPossessedBy(oDying, "database"), "DEAD"))); // DEBUG
		int iDCC = NWScript.getLocalInt(oDying, "iPCDCC");
		iDCC = iDCC + 1;
		int iDC;
		int iDCBase = 16; /* Decrease/increase this value to lower/raise the base DC. */
		iDC = iDCBase;

		if(iDCC > iDCBase)
		{
			iDC = iDCC; /* This is used to decrease the PC's chance to stabilize
                        should they not stabilize after iDCBase turns.
                        Is really only effective when iDCBase < 10*/
		}

		NWEffect eBleedEff;

    /* keep executing recursively until character is dead or at +1 hit points */
		if (NWScript.getCurrentHitPoints(objSelf) <= 0)
		{

        /* a positive bleeding amount means damage, otherwise heal the character */
			if (iBleedAmt > 0)
			{
				eBleedEff = NWScript.effectDamage(iBleedAmt, DamageType.MAGICAL, DamagePower.NORMAL);
			}
			else
			{
				eBleedEff = NWScript.effectHeal(-iBleedAmt);  /* note the negative sign */
			}

			NWScript.applyEffectToObject(Duration.TYPE_INSTANT, eBleedEff, oDying, 0.0f);

        /* -10 hit points is the death threshold, at or beyond it the character dies */
			if (NWScript.getCurrentHitPoints(objSelf) <= -10)
			{
				NWScript.playVoiceChat(VoiceChat.DEATH, objSelf); /* scream one last time */
				NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectVisualEffect(VfxImp.DEATH, false), objSelf, 0.0f); /* make death dramatic */
				NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDeath(false, true), oDying, 0.0f); /* now kill them */
				return;
			}

			if (iBleedAmt > 0)  /* only check if character has not stablized */
			{
				int iConBonus = NWScript.getAbilityModifier(Ability.CONSTITUTION, oDying);
				int iStableRoll = NWScript.random(21) + iConBonus;
				if(iStableRoll >= iDC)
				{
					iBleedAmt = -iBleedAmt; /* reverse the bleeding process */
					NWScript.playVoiceChat(VoiceChat.LAUGH, objSelf); /* laugh at death -- this time */
					NWScript.sendMessageToPC(oDying, "You have stabilized and have begun to heal.");
				}
				else
				{
					NWScript.sendMessageToPC(oDying, "You have failed to stabilize in round: " + NWScript.intToString(iDCC) + ".");
					switch (NWScript.random(6))
					{
						case 0: NWScript.playVoiceChat(VoiceChat.PAIN1, objSelf); break;
						case 1: NWScript.playVoiceChat(VoiceChat.PAIN2, objSelf); break;
						case 2: NWScript.playVoiceChat(VoiceChat.PAIN3, objSelf); break;
						case 3: NWScript.playVoiceChat(VoiceChat.HEALME, objSelf); break;
						case 4: NWScript.playVoiceChat(VoiceChat.NEARDEATH, objSelf); break;
						case 5: NWScript.playVoiceChat(VoiceChat.HELP, objSelf);
					}
				}
			}

			if (NWScript.getCurrentHitPoints(objSelf) <= 0) {
				NWScript.setLocalInt(oDying, "iPCDCC", iDC);

				final int nextBleed = iBleedAmt;
				Scheduler.delay(objSelf, 6000, new Runnable() {
					@Override
					public void run() {
						Bleed(objSelf, nextBleed);
					}
				});
				Scheduler.flushQueues();
			}
		}
	}

}
