/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

public enum ExistStatus {
    BASE_EXIST_NOT_DIR(-1),
    BASE_CANT_CREATE(-2);

    public final int value;

    private ExistStatus(int v) {
        value = v;
    }
}
