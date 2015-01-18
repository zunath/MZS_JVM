package mzsJVM.NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class NWNX_Areas {

    public static NWObject LoadArea(String resref)
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!AREAS!CREATE_AREA", resref);
        return NWScript.getLocalObject(NWObject.MODULE, "NWNX!AREAS!GET_LAST_AREA_ID");
    }

    public static void DestroyArea(NWObject oArea)
    {
        NWObject[] pcs = NWScript.getPCs();

        if(pcs.length > 0)
        {
            NWScript.sendMessageToPC(NWScript.getPCs()[0], "ObjectID = " + oArea.getObjectId() + ", ToString() = " + oArea.toString()); // DEBUG
        }
        else
        {

        }

        //NWScript.setLocalString(NWObject.MODULE, "NWNX!AREAS!DESTROY_AREA", oArea.getObjectId());
    }

    public static void SetAreaName(NWObject oArea, String sName)
    {
        NWScript.setLocalString(oArea, "NWNX!AREAS!SET_AREA_NAME", sName);
    }


}
