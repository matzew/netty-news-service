A very simple (Netty based) TCP server that sends messages to its connected clients.

## Run the service:

In order to start the service, execute the following Maven command:

    mvn clean compile exec:java

This binds the server on port 7777 (on 0.0.0.0);

## Connect with netcat

The most simple client to this "news broadcast service" is using netcat:

    nc localhost 7777

You will see the message on the shell...

## Extend the service to the browser with WebSocket!

With the help of the Kaazing WebSocket Gateway you can easily bring this "service" to the browser,
by using WebSocket (and/or Server-Sent-Events).

The following configuration is needed inside of Kaazing's `gateway-config.xml` file:

    ....
    <service>
        <!-- expose WebSocket and SSE endpoints -->
        <accept>ws://${gateway.hostname}:${gateway.extras.port}/nettyWS</accept>
        <accept>sse://${gateway.hostname}:${gateway.extras.port}/nettySSE</accept>

        <!-- PROXY the Netty-based TCP Service -->
        <type>proxy</type>
        <connect>tcp://${gateway.hostname}:7777</connect>
        <cross-site-constraint>
            <allow-origin>*</allow-origin>
        </cross-site-constraint>
    </service>
    ...

Once the Kaazing WebSocket Gateway is up and running you can connect against the Netty Service, with WebSocket or Server-Sent-Event APIs.