package net.wessendorf.netty;

import junit.framework.Assert;

import org.jboss.netty.channel.ChannelHandler;
import org.junit.Test;


/**
 * A lame Unit test...
 */
public class DummyTest {
    
    @Test
    public void createHandler() {
        ChannelHandler ch = new BroadcastHandler();
        Assert.assertNotNull(ch);
    }
}