package com.nekolaboratory.Lilium.network;

/**
 * @author Yusuke Arakawa
 */

public interface LiliumRequest {
    String endpoint();

    String serialize();
}
