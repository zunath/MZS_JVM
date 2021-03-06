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
        NWScript.setLocalString(NWObject.MODULE, "NWNX!AREAS!DESTROY_AREA", Integer.toString(oArea.getObjectId()));
    }

    public static void SetAreaName(NWObject oArea, String sName)
    {
        NWScript.setLocalString(oArea, "NWNX!AREAS!SET_AREA_NAME", sName);
    }


}
