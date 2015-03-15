package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("unused")
public class Module_OnPlayerDying implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		final NWObject oPC = NWScript.getLastPlayerDying();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

		if (NWScript.getLocalInt(oDatabase, "zombified") == 1) return;

        NWScript.setLocalInt(oPC, "iPCDCC", 0);
        Bleed(oPC, 1);
	}

	private void Bleed(final NWObject oPC, int iBleedAmt)
	{
		int iDCC = NWScript.getLocalInt(oPC, "iPCDCC");
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
		if (NWScript.getCurrentHitPoints(oPC) <= 0)
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

			NWScript.applyEffectToObject(Duration.TYPE_INSTANT, eBleedEff, oPC, 0.0f);

        /* -10 hit points is the death threshold, at or beyond it the character dies */
			if (NWScript.getCurrentHitPoints(oPC) <= -10)
			{
				NWScript.playVoiceChat(VoiceChat.DEATH, oPC); /* scream one last time */
				NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectVisualEffect(VfxImp.DEATH, false), oPC, 0.0f); /* make death dramatic */
				NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDeath(false, true), oPC, 0.0f); /* now kill them */
				return;
			}

			if (iBleedAmt > 0)  /* only check if character has not stablized */
			{
				int iConBonus = NWScript.getAbilityModifier(Ability.CONSTITUTION, oPC);
				int iStableRoll = NWScript.random(21) + iConBonus;
				if(iStableRoll >= iDC)
				{
					iBleedAmt = -iBleedAmt; /* reverse the bleeding process */
					NWScript.playVoiceChat(VoiceChat.LAUGH, oPC); /* laugh at death -- this time */
					NWScript.sendMessageToPC(oPC, "You have stabilized and have begun to heal.");
				}
				else
				{
					NWScript.sendMessageToPC(oPC, "You have failed to stabilize in round: " + NWScript.intToString(iDCC) + ".");
					switch (NWScript.random(6))
					{
						case 0: NWScript.playVoiceChat(VoiceChat.PAIN1, oPC); break;
						case 1: NWScript.playVoiceChat(VoiceChat.PAIN2, oPC); break;
						case 2: NWScript.playVoiceChat(VoiceChat.PAIN3, oPC); break;
						case 3: NWScript.playVoiceChat(VoiceChat.HEALME, oPC); break;
						case 4: NWScript.playVoiceChat(VoiceChat.NEARDEATH, oPC); break;
						case 5: NWScript.playVoiceChat(VoiceChat.HELP, oPC); break;
					}
				}
			}

			if (NWScript.getCurrentHitPoints(oPC) <= 0) {
				NWScript.setLocalInt(oPC, "iPCDCC", iDC);

				final int nextBleed = iBleedAmt;
				Scheduler.delay(oPC, 6000, new Runnable() {
					@Override
					public void run() {
						Bleed(oPC, nextBleed);
					}
				});
				Scheduler.flushQueues();
			}
		}
	}

}
