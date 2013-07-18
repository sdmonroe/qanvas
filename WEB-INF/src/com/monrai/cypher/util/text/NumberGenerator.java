/*
 * Created on Feb 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.monrai.cypher.util.text;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated partOfSpeech comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NumberGenerator {

    public static int sequence = 1000000;

    public static void main(String[] args) {
    }

    public static String getUniqueNumber() {
            String txnID = "";
    
            if (NumberGenerator.sequence >= 9999999) {
                NumberGenerator.sequence = 1000000;
    //            System.out.println("Re-initializing sequence");
            }
    
            NumberGenerator.sequence = NumberGenerator.sequence + 1;
    
            Integer IntSeq = new Integer(NumberGenerator.sequence);
            String stringSeq = IntSeq.toString();
    
            Long longTime = new Long(System.currentTimeMillis());
            String stringLongTime = longTime.toString();
    
            txnID = stringSeq.concat(stringLongTime);
    //        Long LongUID = new Long(UID);
    
    //        txnID = LongUID.longValue();
            return txnID;
        }
}
