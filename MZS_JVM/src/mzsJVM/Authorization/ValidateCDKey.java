package mzsJVM.Authorization;

import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ValidateCDKey implements IScriptEventHandler {
    @Override
    public void runScript(NWObject pc) {

        String sCDKey = NWScript.getPCPublicCDKey(pc, false);
        String sPlayerName = NWScript.getPCPlayerName(pc);
        String sIP = NWScript.getPCIPAddress(pc);

        if (NWScript.getIsDM(pc) || (NWScript.getIsDMPossessed(pc) && NWScript.getIsDM(NWScript.getMaster(pc))))
        {
            boolean isDM = false; // TODO: Database call to verify CD key is authorized to log in as a DM.

            if (isDM) {
                NWScript.sendMessageToAllDMs("<DM authorized to join server, " + sCDKey + ", " + sPlayerName + ">" );
            }
            else
            {
                NWScript.sendMessageToAllDMs("<WARNING, UNAUTHORIZED DM JOIN ATTEMPT BY " + sCDKey + ", " + sPlayerName + ", " + sIP + ">" );
                NWScript.bootPC(pc);
            }
        }
    }
}
