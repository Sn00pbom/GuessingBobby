package me.sn00pbom.bobby;


import java.util.ArrayList;
import java.util.Random;

public class DataUtil {



    @Deprecated
    public static boolean setContains(DataChunk[] dataChunks, DataChunk dataChunk) {
        for (DataChunk dc : dataChunks) {
            if (dc == null) {
                continue;
            } else if (dc.equals(dataChunk)) {
                return true;
            }
        }
        return false;
    }
}
