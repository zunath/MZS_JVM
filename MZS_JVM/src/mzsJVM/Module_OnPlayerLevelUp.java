package mzsJVM;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.ClassType;

public class Module_OnPlayerLevelUp implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		// SIMTools
		NWScript.executeScript("fky_chat_levelup", objSelf);

		NWObject oPC = NWScript.getPCLevellingUp();

		if (NWScript.getLevelByClass(ClassType.SORCERER, oPC)>0 ||
				NWScript.getLevelByClass(ClassType.WIZARD, oPC)>0 ||
				NWScript.getLevelByClass(ClassType.CLERIC, oPC)>0 ||
				NWScript.getLevelByClass(ClassType.PALADIN, oPC)>0 ||
				NWScript.getLevelByClass(ClassType.BARD, oPC)>0 ||
				NWScript.getLevelByClass(ClassType.DRUID, oPC)>0)
		{
			NWScript.sendMessageToPC(oPC, "No Wizards, Sorcerers, Clerics, Paladins, Bards, or Druids allowed on this mod.  Please choose a different class.");
			NWScript.setXP(oPC, 999);
			NWScript.setXP(oPC, 1001);
			NWScript.exportSingleCharacter(oPC);


		}
	}
}
