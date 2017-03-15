package com.lunametrics.gabq.jsplashgen;

import java.util.Comparator;
import org.metamesh.chub.proto.Message;

public class SignedMessageSorter {

    public static class ByTime implements Comparator<Message.SignedMessage> {
        @Override
        public int compare(Message.SignedMessage o1, Message.SignedMessage o2) {
            return (int) (Math.signum(o2.getTimestamp() - o1.getTimestamp()));
        }
    }
}
