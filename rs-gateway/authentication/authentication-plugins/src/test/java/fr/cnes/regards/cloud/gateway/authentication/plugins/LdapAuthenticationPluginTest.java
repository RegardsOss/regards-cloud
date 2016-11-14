/*
 * LICENSE_PLACEHOLDER
 */
package fr.cnes.regards.cloud.gateway.authentication.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import fr.cnes.regards.cloud.gateway.authentication.plugins.domain.AuthenticationPluginResponse;
import fr.cnes.regards.cloud.gateway.authentication.plugins.domain.AuthenticationStatus;
import fr.cnes.regards.cloud.gateway.authentication.plugins.impl.LdapAuthenticationPlugin;
import fr.cnes.regards.framework.test.report.annotation.Purpose;
import fr.cnes.regards.modules.plugins.domain.PluginParameter;
import fr.cnes.regards.modules.plugins.domain.PluginParametersFactory;
import fr.cnes.regards.plugins.utils.PluginUtils;
import fr.cnes.regards.plugins.utils.PluginUtilsException;

/**
 *
 * Class LdapAuthenticationPluginTest
 *
 * Test authentication through LDAP server
 *
 * @author Sébastien Binda
 * @since 1.0-SNAPSHOT
 */
public class LdapAuthenticationPluginTest {

    /**
     * Test email to return by ldap mock
     */
    private final static String EMAIL = "test@regards.fr";

    /**
     * LDAP Authentication plugin to test
     */
    private static LdapAuthenticationPlugin plugin;

    /**
     *
     * Initialize LDAP Authentication plugin thought plugin utilities.
     *
     * @since 1.0-SNAPSHOT
     */
    @BeforeClass
    public static void init() {

        /*
         * Set all parameters
         */
        final List<PluginParameter> parameters = PluginParametersFactory.build()
                .addParameter(LdapAuthenticationPlugin.PARAM_LDAP_HOST, "test")
                .addParameter(LdapAuthenticationPlugin.PARAM_LDAP_PORT, "8080")
                .addParameter(LdapAuthenticationPlugin.PARAM_LDAP_CN, "ou=people,ou=commun")
                .addParameter(LdapAuthenticationPlugin.PARAM_LDAP_EMAIL_ATTTRIBUTE, "email").getParameters();
        try {
            // instantiate plugin
            plugin = PluginUtils.getPlugin(parameters, LdapAuthenticationPlugin.class);
            Assert.assertNotNull(plugin);
        } catch (final PluginUtilsException e) {
            Assert.fail();
        }

    }

    /**
     *
     * Test valid authentication throught LDAP plugin
     *
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test valid authentication throught LDAP plugin")
    @Test
    public void testLdapAuthentication() {

        Mockito.mock(LdapNetworkConnection.class);

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);
        final LdapConnection mockedConnection = getMockedLdapConnection(true);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("Error authentication. Access should be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_GRANTED));
        Assert.assertTrue("Error authentication. Email is not valid", response.getEmail().equals(EMAIL));

    }

    /**
     *
     * Test error authentication throught LDAP plugin
     *
     * @throws LdapException
     *             test error.
     * @throws IOException
     *             test error.
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test error authentication throught LDAP plugin")
    @Test
    public void testErrorConnection() throws LdapException, IOException {
        final LdapConnection mockedConnection = Mockito.mock(LdapConnection.class);
        Mockito.when(mockedConnection.connect()).thenReturn(false);

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("The authentication shoul not be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_DENIED));
    }

    /**
     *
     * Test error authentication throught LDAP plugin
     *
     * @throws LdapException
     *             test error.
     * @throws IOException
     *             test error.
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test error authentication throught LDAP plugin")
    @Test
    public void testErrorAuthentication() throws LdapException, IOException {
        final LdapConnection mockedConnection = Mockito.mock(LdapConnection.class);
        Mockito.when(mockedConnection.connect()).thenReturn(true);
        Mockito.when(mockedConnection.isAuthenticated()).thenReturn(false);

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("The authentication shoul not be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_DENIED));
    }

    /**
     *
     * Test error authentication throught LDAP plugin
     *
     * @throws LdapException
     *             test error.
     * @throws IOException
     *             test error.
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test error authentication throught LDAP plugin")
    @Test
    public void testErrorInvalidEmail() throws LdapException, IOException {
        Mockito.mock(LdapNetworkConnection.class);

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);
        final LdapConnection mockedConnection = getMockedLdapConnection(false);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("The authentication shoul not be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_DENIED));
    }

    /**
     *
     * Test error authentication throught LDAP plugin
     *
     * @throws LdapException
     *             test error.
     * @throws IOException
     *             test error.
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test error authentication throught LDAP plugin")
    @Test
    public void testErrorLdapException() throws LdapException, IOException {
        final LdapConnection mockedConnection = Mockito.mock(LdapConnection.class);
        Mockito.when(mockedConnection.connect()).thenThrow(new LdapException("ldap exception test"));

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("The authentication shoul not be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_DENIED));

    }

    /**
     *
     * Test error authentication throught LDAP plugin
     *
     * @throws LdapException
     *             test error.
     * @throws IOException
     *             test error.
     * @since 1.0-SNAPSHOT
     */
    @Purpose("Test error authentication throught LDAP plugin")
    @Test
    public void testErrorLdapException2() throws LdapException, IOException {
        final LdapConnection mockedConnection = Mockito.mock(LdapConnection.class);
        Mockito.when(mockedConnection.connect()).thenThrow(new LdapAuthenticationException("ldap exception test"));

        final LdapAuthenticationPlugin spy = Mockito.spy(plugin);

        Mockito.doReturn(mockedConnection).when(spy).getLdapConnection(Mockito.anyString(), Mockito.anyInt());

        final AuthenticationPluginResponse response = spy.authenticate("login", "password", "project");
        Assert.assertNotNull("Response should not be null", response);
        Assert.assertTrue("The authentication shoul not be granted.",
                          response.getStatus().equals(AuthenticationStatus.ACCESS_DENIED));

    }

    /**
     *
     * Create LDAP connection mock for test
     *
     * @param pValidEmail
     *            does the LDAP mock connection return a valid email or not
     * @return LdapConnection
     * @since 1.0-SNAPSHOT
     */
    private LdapConnection getMockedLdapConnection(final boolean pValidEmail) {

        final LdapConnection mockedConnection = Mockito.mock(LdapConnection.class);

        try {
            Mockito.when(mockedConnection.connect()).thenReturn(true);
            Mockito.when(mockedConnection.isAuthenticated()).thenReturn(true);

            final List<Entry> entries = new ArrayList<Entry>();

            final Attribute mockedAttribute = Mockito.mock(Attribute.class);
            if (pValidEmail) {
                Mockito.when(mockedAttribute.getString()).thenReturn(EMAIL);
            }

            final Entry mockedEntry = Mockito.mock(Entry.class);
            Mockito.when(mockedEntry.get(Mockito.anyString())).thenReturn(mockedAttribute);

            entries.add(mockedEntry);
            final EntryCursorStub entry = new EntryCursorStub();
            entry.setEntries(entries);

            Mockito.when(mockedConnection.search(Mockito.anyString(), Mockito.anyString(),
                                                 Mockito.any(SearchScope.class), Mockito.anyString()))
                    .thenReturn(entry);
        } catch (LdapException | IOException e) {
            Assert.fail(e.getMessage());
        }

        return mockedConnection;

    }

}