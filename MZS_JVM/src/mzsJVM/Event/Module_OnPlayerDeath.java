package mzsJVM.Event;
import mzsJVM.Constants;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class Module_OnPlayerDeath implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject oPC = NWScript.getLastPlayerDied();
		NWObject oDatabase = NWScript.getItemPossessedBy(oPC, Constants.PCDatabaseTag);
		int iIDNumber = NWScript.getLocalInt(oDatabase, "ID_NUMBER");
		NWObject oDead = NWScript.getItemPossessedBy(oPC, "death_token");

		// ROTD death system
		NWScript.executeScript("perma_death", objSelf);

		// d20
		NWScript.executeScript("d20_on_death", objSelf);

		if (!NWScript.getIsObjectValid(oDead))
		{
			NWScript.createItemOnObject("death_token", oPC, 1, "");
		}

		NWScript.exportSingleCharacter(oPC);
	}
}

