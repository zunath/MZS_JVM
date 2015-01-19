package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerDying implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject oPC = NWScript.getLastPlayerDying();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);

		if (NWScript.getLocalInt(oDatabase, "zombified") == 1) return;
		NWScript.executeScript("bleed_on_dying", objSelf);

	}
}
