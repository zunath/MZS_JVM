package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.RestEventtype;

@SuppressWarnings("unused")
public class Module_OnPlayerRest implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
		
		final NWObject oPC = NWScript.getLastPCRested();
		NWObject oItem = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
		NWObject oInfect = NWScript.getItemPossessedBy(oPC, "out_infection");
		NWObject oBook = NWScript.getItemPossessedBy(oPC, "badgebook");
		int iLog = NWScript.getLocalInt(oItem, "times_logged_in");

		String sLog = NWScript.intToString(iLog);
		String sSHS = NWScript.intToString(NWScript.getLocalInt(oItem, "shs"));
		String sInfection = NWScript.intToString(NWScript.getLocalInt(oInfect, "infection")) + "%";
		String sHunger = NWScript.intToString(NWScript.getLocalInt(oItem, "CURRENT_HUNGER")) + "%";
		String sThirst = NWScript.intToString(NWScript.getLocalInt(oItem, "CURRENT_THIRST")) + "%";
		String sBadges = NWScript.intToString(NWScript.getLocalInt(oBook, "badges"));
		String sZombieKills = NWScript.intToString(NWScript.getLocalInt(oItem, "zombie_kills"));
		String sStatus = NWScript.getLocalString(oItem, "badgestatus");

		NWScript.setCustomToken(2234, sLog);
		NWScript.setCustomToken(2235, sSHS); //Renown is being changed to SHS, 1 junk = 1 SHS
		NWScript.setCustomToken(2236, sInfection);
		NWScript.setCustomToken(2237, sHunger);
		NWScript.setCustomToken(2238, sThirst);
		NWScript.setCustomToken(2239, sBadges);
		NWScript.setCustomToken(2240, sStatus);
		NWScript.setCustomToken(2241, sZombieKills);


		if(NWScript.getLastRestEventType()== RestEventtype.REST_CANCELLED||NWScript.getLastRestEventType()==RestEventtype.REST_FINISHED)
		{
			NWScript.setLocalInt(oPC, "restmenu", 1);
		}


		if (NWScript.getLocalInt(oPC, "restmenu") != 1)
		{
			NWScript.setLocalInt(oPC, "restmenu", 1);

			Scheduler.assign(oPC, new Runnable() {
				@Override
				public void run() {
					NWScript.clearAllActions(false);
					NWScript.actionStartConversation(oPC, "tal_restmenu", true, false);
				}
			});
			Scheduler.flushQueues();

			NWScript.exportSingleCharacter(oPC);
			NWScript.sendMessageToPC(oPC, "Your character has been successfully saved!");

		}
		else
		{
			NWScript.setLocalInt(oPC, "restmenu", 0);
			NWScript.executeScript("tal_old_onrest", oPC);
		}
		
	}
}
