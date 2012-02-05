A very simple (Netty based) TCP server that sends messages to its connected clients.

## Run the service:

In order to start the service, execute the following Maven command:

    mvn clean compile exec:java

This binds the server on port 7777 (on 0.0.0.0);

## Connect with netcat

The most simple client to this "news broadcast service" is using netcat:

    nc localhost 7777

You will see the message on the shell...

    <service>
