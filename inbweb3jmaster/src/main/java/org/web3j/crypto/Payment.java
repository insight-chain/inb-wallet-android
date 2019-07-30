package org.web3j.crypto;

import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">
 * yellow paper</a>.
 */
public class Payment {

    private String resourcePayer;
    private final byte vp;
    private final byte[] rp;
    private final byte[] sp;

    public Payment(){
        this(null,new Byte("0"),new byte[]{},new byte[]{});
    }

    public Payment(String resourcePayer, byte vp, byte[] rp, byte[] sp) {
        this.resourcePayer = resourcePayer;
        this.vp = vp;
        this.rp = rp;
        this.sp = sp;
    }

    public String getResourcePayer() {
        return resourcePayer;
    }

    public byte getVp() {
        return vp;
    }

    public byte[] getRp() {
        return rp;
    }

    public byte[] getSp() {
        return sp;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "resourcePayer='" + resourcePayer + '\'' +
                ", vp=" + vp +
                ", rp=" + Arrays.toString(rp) +
                ", sp=" + Arrays.toString(sp) +
                '}';
    }
}
