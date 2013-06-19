package ch.qos.logback.core.joran.v2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.core.joran.v2.io.stax.JoranStaxWriter;

public final class ConfigurationTestCase
{

    private Configuration expected;

    @Before
    public final void setUp()
    {
        expected = new Configuration.Builder()
                                    .setDebug( true )
                                    .setScan( true )
                                    .setScanPeriod( "30 seconds" )
                                    .addProperty( new Property.Builder()
                                                              .setName( "USER_HOME" )
                                                              .setValue( "/home/sebastien" )
                                                              .build() )
                                    .addProperty( new Property.Builder()
                                                              .setName( "nodeId" )
                                                              .setValue( "firstNode" )
                                                              .setScope( "context" )
                                                              .build() )
                                    .addProperty( new Property.Builder()
                                                              .setFile( "src/main/java/chapters/configuration/variables1.properties" )
                                                              .build() )
                                    .addProperty( new Property.Builder()
                                                              .setResource( "resource1.properties" )
                                                              .build() )
                                    .addAppender( new Appender.Builder()
                                                              .setName( "FILE" )
                                                              .setType( "ch.qos.logback.core.FileAppender" )
                                                              .setFile( "${USER_HOME}/${nodeId}/myApp.log" )
                                                              .setEncoder( Encoder.newEncoderInstance( "%msg%n" ) )
                                                              .build() )
                                    .setRoot( Root.newRootInstance( "info", AppenderRef.newAppenderRefInstance( "FILE" ) ) )
                                    .build();
    }

    @After
    public final void tearDown()
    {
        expected = null;
    }

    @Test
    public final void write()
        throws Exception
    {
        new JoranStaxWriter().write( System.out, expected );
    }

}
