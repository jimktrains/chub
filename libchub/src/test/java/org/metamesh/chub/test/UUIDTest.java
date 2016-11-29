/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.test;

import java.util.UUID;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.metamesh.chub.util.UUIDHelper;

public class UUIDTest {
    @Test
    public void testUUID2bytes() {
        UUID u = UUID.randomUUID();
        byte[] bu = UUIDHelper.UUID2bytes(u);
        UUID u2 = UUIDHelper.bytes2UUID(bu);
        
        assertEquals(u, u2);
    }
}
