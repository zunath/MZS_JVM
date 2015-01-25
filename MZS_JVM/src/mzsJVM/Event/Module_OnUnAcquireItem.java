package mzsJVM.Event;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

@SuppressWarnings("UnusedDeclaration")
public class Module_OnUnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {

		// Bioware Default
		NWScript.executeScript("x2_mod_def_unaqu", objSelf);

		NWObject iItemLost = NWScript.getModuleItemLost();
		NWObject oPC = NWScript.getModuleItemLostBy();
		String sTag = NWScript.getTag(iItemLost);

		if (sTag.equals("cr_c4explosive"))
		{
			NWScript.createObject(ObjectType.PLACEABLE, "c4bomb002", NWScript.getLocation(oPC),false, "");
			NWScript.destroyObject(iItemLost, 0.0f);
		}
	}
}
