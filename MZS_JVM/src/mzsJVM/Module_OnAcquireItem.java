package mzsJVM;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Inventory;

public class Module_OnAcquireItem implements SchedulerListener {
	public void postFlushQueues(int remainingTokens) {}
	public void missedToken(NWObject objSelf, String token) {}
	public void context(NWObject objSelf) {}
	
	public void event(NWObject objSelf, String event) {
            
		NWObject oPC = NWScript.getModuleItemAcquiredBy();
	    NWObject oItem = NWScript.getModuleItemAcquired();
	    String sTag = NWScript.getTag(oItem);
	    NWObject oDataBase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
	    NWObject oRenown = NWScript.getItemPossessedBy(oPC, "shsaward");
	    int iRenown = NWScript.getLocalInt(oDataBase, "shs");
	    int iRenownAward = NWScript.getLocalInt(oRenown, "shs");

	    if (sTag == "_mdrn_ot_talkie")
	    {
	    	NWScript.createItemOnObject("_mdrn_ot_talk1", oPC, 1, "");
	    	NWScript.destroyObject(oItem, 0.1f);
	    }

	    //Update Bag of seeds items to new ones
	    if (sTag == "bagofseeds001")
	    {
	    	NWScript.createItemOnObject("bagofseeds001", oPC, 1, "");
	    	NWScript.destroyObject(oItem, 0.1f);
	        return;
	    }

	    if (sTag == "shsaward")  //Renown changed to SHS, please leave vars alone
	    {
	    	NWScript.setLocalInt(oDataBase, "shs", iRenown + iRenownAward);
	        int iRenownTotal = iRenown + iRenownAward;
	        NWScript.sendMessageToPC(oPC, "Current SHS points: " + iRenownTotal);
	        NWScript.sendMessageToAllDMs(NWScript.getName(oPC, false) + "'s current SHS Points: " + iRenownTotal);
	        NWScript.destroyObject(oItem, 0.1f);
	    }

	    if (sTag == "grasstuft")  //Useless grass tuft,for use only in "seed" lootables
	    {
	    	NWScript.sendMessageToPC(oPC, "You toss aside a tuft of grass while looking for seeds.");
	    	NWScript.destroyObject(oItem, 0.1f);
	    }

	    if (sTag == "mut_stats")//Mutational hook, equip stats once received - Skeet, 11/25/2014
	    	NWScript.actionEquipItem(oItem, Inventory.SLOT_RIGHTRING);

	    // Bioware default
	    NWScript.executeScript("x2_mod_def_aqu", objSelf);
		
	}
}
