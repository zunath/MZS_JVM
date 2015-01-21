package mzsJVM;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("UnusedDeclaration")
public class Module_OnPlayerChat implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
		final NWObject oPC = NWScript.getPCChatSpeaker();
		NWObject oRadio = NWScript.getItemPossessedBy(oPC, "_mdrn_ot_talk1");
		NWObject oDataBase = NWScript.getItemPossessedBy(oPC, "database");
		NWObject oDataBase2;
		NWObject oWeapon = NWScript.getItemInSlot(Inventory.SLOT_RIGHTHAND, oPC);
		NWObject oOffHand = NWScript.getItemInSlot(Inventory.SLOT_LEFTHAND, oPC);

		final String sName = NWScript.getName(oPC, false);
		String sTag;
		String sSpoken = NWScript.getPCChatMessage();

		int iVolume = NWScript.getPCChatVolume();
		int iFireRateMode1 = NWScript.getLocalInt(oWeapon, "FireRateMode1");
		int iFireRate1;

		float fNumber1;

		if (NWScript.getLocalFloat(oDataBase, "RadioChannel1") <= 0.0)
		{
			NWScript.setLocalFloat(oDataBase, "RadioChannel1", 105.5f);
		}

		if (iVolume == Talkvolume.TALK || iVolume == Talkvolume.WHISPER)
		{
			if ((NWScript.getStringLeft(sSpoken, 5).equals("name-")) || (NWScript.getStringLeft(sSpoken, 6).equals("write-")))
				// if message was a talk then delet previous string and save new one
				// save last chat on PC.
				NWScript.deleteLocalString(oPC, "TD_QUILLCHAT");
			final String tempSpoken = sSpoken;

			Scheduler.delay(oPC, 500, new Runnable() {
				@Override
				public void run() {
					NWScript.setLocalString(oPC, "TD_QUILLCHAT", tempSpoken);
				}
			});
		}







		//Added by Skeet, RP XP System
		NWObject oNearbyPCHeard = NWScript.getNearestCreature(CreatureType.PLAYER_CHAR, 1, oPC, 1, CreatureType.PERCEPTION, Perception.HEARD, -1, -1);


		if (NWScript.getLocalInt(NWScript.getArea(oPC), "IS_OOC") == 0)   //Only works in non-ooc areas
		{
			if (iVolume == Talkvolume.TALK || iVolume == Talkvolume.WHISPER)
			{

				if (NWScript.getIsPC(oPC) && oNearbyPCHeard != NWObject.INVALID)
				{
					int iLength = NWScript.getStringLength(sSpoken);
					NWObject oDB = NWScript.getItemPossessedBy(oPC, "database");
					int iBuffer = NWScript.getLocalInt(oDB, "xpbuffer");
					NWScript.setLocalInt(oDB, "xpbuffer", iBuffer + iLength);
					iBuffer = NWScript.getLocalInt(oDB, "xpbuffer");


					if (iBuffer > 1500)
					{
						NWScript.setLocalInt(oDB, "xpbuffer", 0);
						NWScript.sendMessageToPC(oPC, "You gained experience by roleplaying with others.");
						NWScript.giveXPToCreature(oPC, 1);
						NWScript.writeTimestampedLogEntry(NWScript.getName(oPC, false) + " gained RP XP.");
					}
				}

			}
		}




		//Added by Drakaden, gun rate of fire switches
		if(NWScript.getStringLeft(sSpoken, 4).equals("#g1") && NWScript.getWeaponRanged(oWeapon))
		{

			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
			if (iFireRate1 >= -99)
			{
				iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
				NWScript.setLocalInt(oWeapon, "FireRateMode1", 1);
				RefreshFireRate(objSelf);
				NWScript.sendMessageToPC(oPC, "Weapon is set to single shot mode.");
				NWScript.setPCChatMessage(sSpoken = "");
			}
			else
			{
				NWScript.sendMessageToPC(oPC, "Invalid mode for this weapon.");
				NWScript.setPCChatMessage(sSpoken = "");
			}
		}

		if(NWScript.getStringLeft(sSpoken, 4).equals("#g2") && NWScript.getWeaponRanged(oWeapon))
		{
			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate2");
			if (iFireRate1 >= 1)
			{
				NWScript.setLocalInt(oWeapon, "FireRateMode1", 2);
				RefreshFireRate(objSelf);
				NWScript.sendMessageToPC(oPC, "Weapon is set to semi/burst shot mode.");
				NWScript.setPCChatMessage(sSpoken = "");
			}
			else
			{
				NWScript.sendMessageToPC(oPC, "Invalid mode for this weapon.");
				NWScript.setPCChatMessage(sSpoken = "");
			}

		}

		if(NWScript.getStringLeft(sSpoken, 4).equals("#g3") && NWScript.getWeaponRanged(oWeapon))
		{
			iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate3");
			if (iFireRate1 >= 1)
			{
				NWScript.setLocalInt(oWeapon, "FireRateMode1", 3);
				RefreshFireRate(objSelf);
				NWScript.sendMessageToPC(oPC, "Weapon is set to full/auto shot mode.");
				NWScript.setPCChatMessage(sSpoken = "");
			}
			else
			{
				NWScript.sendMessageToPC(oPC, "Invalid mode for this weapon.");
				NWScript.setPCChatMessage(sSpoken = "");
			}

		}

		// Radio Script, By Skeet!'
		if(NWScript.getStringLeft(sSpoken, 4).equals("#ron"))
		{
			NWScript.setLocalInt(oPC, "radio", 1);
			NWScript.sendMessageToPC(oPC, "Radio is -ON-");
		}

		if(NWScript.getStringLeft(sSpoken, 5).equals("#roff"))
		{
			NWScript.setLocalInt(oPC, "radio", 0);
			NWScript.sendMessageToPC(oPC, "Radio is -OFF-");
		}
		//Handsfree mode
		if(NWScript.getStringLeft(sSpoken, 5).equals("#hfon"))
		{
			NWScript.setLocalInt(oPC, "handsfree", 1);
			NWScript.sendMessageToPC(oPC, "Handsfree Mode -ON-");
		}

		if(NWScript.getStringLeft(sSpoken, 6).equals("#hfoff"))
		{
			NWScript.setLocalInt(oPC, "handsfree", 0);
			NWScript.sendMessageToPC(oPC, "Handsfree Mode -OFF-");
		}

//Added by Drakaden, chooses custom radio channels
//Modified by Drakaden to only use 1 #chan- command, and use a float instead of 2 integers.
		if(NWScript.getStringLeft(sSpoken, 6).equals("#chan-"))
		{
			int iText = (NWScript.getStringLength(sSpoken)-6);
			String sText = NWScript.getStringRight(sSpoken, iText);
			fNumber1= NWScript.stringToFloat(sText);
			if (fNumber1 >= 1000.0 || fNumber1 <= -0.0)
			{
				NWScript.sendMessageToPC(oPC, "Invalid number, put a number between 0.1 and 999.9.");
				return;
			}
			NWScript.setLocalFloat(oDataBase, "RadioChannel1", fNumber1);
			NWScript.sendMessageToPC(oPC, "Radio Channel is now set to: " + NWScript.floatToString(NWScript.getLocalFloat(oDataBase, "RadioChannel1"), 3, 1) + "");

		}


		if(NWScript.getStringLeft(sSpoken, 3).equals("#r "))
		{
			//Modified by Drakaden, allows DMs and possessed NPCS to radio without a physical radio item.
			if (!NWScript.getIsObjectValid(oRadio) && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC) || (NWScript.getLocalInt(oPC, "radio")) == 0 && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC))
			{
				NWScript.setPCChatMessage(sSpoken = "");
				return;
			}
			final String sIdentity = NWScript.getStringLeft(sSpoken, 3);
			NWScript.deleteLocalString(oPC, sIdentity);

			final String tempSpoken = sSpoken;
			Scheduler.delay(oPC, 500, new Runnable() {
				@Override
				public void run() {
					NWScript.setLocalString(objSelf, sIdentity, tempSpoken);
				}
			});
			Scheduler.flushQueues();

			int iLength = NWScript.getStringLength(sSpoken);
			final String sSub = NWScript.getSubString(sSpoken, 3, iLength);
			NWScript.setPCChatMessage(sSpoken = "");

			NWObject[] pcs = NWScript.getPCs();

			for(NWObject pc : pcs)
			{
				//Corrected by Drakaden, detect if each PC has a radio or not
				oRadio = NWScript.getItemPossessedBy(pc, "_mdrn_ot_talk1");
				//Added by Drakaden, to detect radio channels
				//Modified by Drakaden, made so DMs and possessed NPCs don't need a radio, only the database.
				//Added by Drakaden, if within 0.5 of the frequency, players can receive hints of messages.
				oDataBase2 = NWScript.getItemPossessedBy(pc, "database");
				if (NWScript.getIsPC(pc) && NWScript.getIsObjectValid(oRadio) &&
						NWScript.getLocalFloat(oDataBase, "RadioChannel1") != NWScript.getLocalFloat(oDataBase2, "RadioChannel1") &&
						//NWScript.getLocalInt(oDataBase, "RadioChannel2") != NWScript.getLocalInt(oDataBase2, "RadioChannel2") &&
						NWScript.getLocalFloat(oDataBase, "RadioChannel1") >= (NWScript.getLocalFloat(oDataBase2, "RadioChannel1") - 0.5) &&
						NWScript.getLocalFloat(oDataBase, "RadioChannel1") <= (NWScript.getLocalFloat(oDataBase2, "RadioChannel1") + 0.5))
				{
					NWScript.sendMessageToPC(pc,"*Radio, " + NWScript.floatToString(NWScript.getLocalFloat(oDataBase, "RadioChannel1"), 3, 1) + "*, " + "*Fuzzy voice noises*");
				}

				if (NWScript.getIsPC(pc) && NWScript.getIsObjectValid(oRadio) &&
						NWScript.getLocalFloat(oDataBase, "RadioChannel1") == NWScript.getLocalFloat(oDataBase2, "RadioChannel1") ||
						NWScript.getIsDM(pc) &&
								NWScript.getLocalFloat(oDataBase, "RadioChannel1") == NWScript.getLocalFloat(oDataBase2, "RadioChannel1") ||
						NWScript.getIsDMPossessed(pc) &&
								NWScript.getLocalFloat(oDataBase, "RadioChannel1") == NWScript.getLocalFloat(oDataBase2, "RadioChannel1"))
				{
					if (!NWScript.getIsDM(pc) && (NWScript.getLocalInt(pc, "radio") == 1) && (NWScript.getLocalInt(pc, "handsfree")) == 0)
					{
						Scheduler.assign(pc, new Runnable() {
							@Override
							public void run() {
								NWObject database = NWScript.getItemPossessedBy(objSelf, Constants.PCDatabaseTag);
								float channel = NWScript.getLocalFloat(database, "RadioChannel1");
								String channelString = NWScript.floatToString(channel, 3, 1);

								NWScript.actionSpeakString("*Radio, " + channelString + "*, " + sName + " - " + "'" + sSub + "'", Talkvolume.TALK);
								NWScript.playSound("sonarbeep");
							}
						});
					}

					else
					{

						NWScript.sendMessageToPC(pc, "*Radio, " + NWScript.floatToString(NWScript.getLocalFloat(oDataBase, "RadioChannel1"), 3, 1) + "*, " + sName + " - " + "'" +  sSub + "'");
					}

				}
			}
			NWScript.sendMessageToAllDMs("*Radio, " + NWScript.floatToString(NWScript.getLocalFloat(oDataBase, "RadioChannel1"), 3, 1) + "*, " + sName + " - " + "'" + sSub + "'");

		}
		sSpoken = "";
	}


	private void RefreshFireRate(final NWObject objSelf)
	{
		NWObject oPC = NWScript.getPCChatSpeaker();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
		NWObject oWeapon = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
		NWObject oOffHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);

		int iFireRateMode1 = NWScript.getLocalInt(oWeapon, "FireRateMode1");
		int iFireRate1 = NWScript.getLocalInt(oWeapon, "FireRate1");
		int iAccuracyPenalty = 0;

		NWScript.setLocalInt(oDatabase, "DualGunsEffect1", -1);

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

		Scheduler.assign(oPC, new Runnable() {
			@Override
			public void run() {
				NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eExtraAttack1, objSelf, 99999.0f);
				NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eDualGunAccuracyPenalty1, objSelf, 99999.0f);
			}
		});

		NWScript.setLocalInt(oDatabase, "DualGunsEffect1", 1);

		Scheduler.flushQueues();
	}



}