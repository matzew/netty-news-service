/**
 * Copyright 2012 Matthias Wessendorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.wessendorf.netty;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Netty Handler that sends out messages to a connected TCP client
 * 
 */
public class BroadcastHandler extends SimpleChannelHandler {

    public BroadcastHandler() {
        // init things that are needed...
    }
    
    private ScheduledExecutorService ses;
    
    private static final Random random = new Random();

    private static final String[] BOGUS_NEWS = new String[] {
        "Ranking the top 25 players in baseball",
        "Spacecraft blasts off in search of 'Earths'", "Ancient marvels, sun-splashed islands of Greece",
        "Why we're sleeping less", "Unemployed place their bets on casino jobs", "Crews save homes from wildfire",
        "Spare us, say bowlers to planned tax", "Proof of ancient Malaysian civilization found",
        "Papua New Guinea gets first conservation area", "Nation's rappers down to last two samples"
    };

    private static String headline() {
        int headlineId;
        synchronized (random) {
            headlineId = random.nextInt(BOGUS_NEWS.length);
        }
        return BOGUS_NEWS[headlineId];
    }
    

    private void sendMessages(Channel channel) {
        final ChannelBuffer payload = ChannelBuffers.dynamicBuffer();
        payload.clear();
        payload.writeBytes((headline() + "\r\n").getBytes());
        channel.write(payload);     
    }
    
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        final Channel channel = e.getChannel();
        
        ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(
            new Runnable() {
                @Override
                public void run() {
                    sendMessages(channel);
                }
            },
            0L,   // wait before it starts
            100L, // time between the updates...
            TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ses.shutdownNow();
    }
}