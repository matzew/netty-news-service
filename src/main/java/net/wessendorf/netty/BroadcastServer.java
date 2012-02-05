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

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Simple Server that creates a Netty Pipleline.
 * Server is sending out (bogus) news headlines..
 * 
 * <p>A very simple (command line) client is using netcat:
 * 
 * <p><code>nc localhost 7777</code>
 */
public class BroadcastServer {

    private final int port;

    public BroadcastServer(int port) {
        this.port = port;
    }

    public void run() {
        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()
        );

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        
        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                // chain/build the pipeline:
                return Channels.pipeline(
                        new BroadcastHandler());

            }
        });

        bootstrap.bind(new InetSocketAddress(port));
        System.out.println(bootstrap); 
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 7777;
        }
        new BroadcastServer(port).run();
    }
}