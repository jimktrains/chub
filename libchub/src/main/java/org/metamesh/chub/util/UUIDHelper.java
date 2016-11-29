/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 * @author jameskeener
 */
public class UUIDHelper {

    public static byte[] UUID2bytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
    
    public static UUID bytes2UUID(byte[] buuid) {   
        ByteBuffer bb = ByteBuffer.wrap(buuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid;
    }
    
    public static byte[] randomUUID() {
        return UUID2bytes(UUID.randomUUID());
    }
}
