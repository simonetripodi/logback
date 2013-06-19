package ch.qos.logback.core.joran.v2;

import static org.junit.Assert.assertTrue;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.core.joran.v2.io.stax.JoranStaxReader;
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
    public final void read()
        throws Exception
    {
        Configuration actual = new JoranStaxReader().read( getClass().getResourceAsStream( "logback.xml" ) );
        assertReflectionEquals( expected, actual );
    }

    @Test
    public final void write()
        throws Exception
    {
        String expectedDocument = FileUtils.readFileToString( new File( System.getProperty( "user.dir" ), "src/test/resources/ch/qos/logback/core/joran/v2/logback.xml" ) );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new JoranStaxWriter().write( baos, expected );
        String actualDocument = new String(baos.toByteArray());

        Diff diff = new Diff( expectedDocument, actualDocument );
        assertTrue( "Joran write didn't work as expected " + diff, diff.identical() );
    }

}
