package com.lunametrics.gabq.jsplashgen;

import java.util.Comparator;

/**
 * 
 * Format:
 * filename,objecttime,objecttype,valid
 */
public class ManifestEntry {
    public String filename;
    public Long object_time;
    public String object_type;
    public Boolean valid;
    public ManifestEntry(String filename, Long object_time, String object_type, Boolean valid){
        this.filename = filename;
        this.object_time = object_time;
        this.object_type = object_type;
        this.valid = valid;
    }
    
    public static class SortByTime implements Comparator<ManifestEntry> {
        @Override
        public int compare(ManifestEntry o1, ManifestEntry o2) {
            return (int) (o2.object_time - o1.object_time);
        }
    }
}
