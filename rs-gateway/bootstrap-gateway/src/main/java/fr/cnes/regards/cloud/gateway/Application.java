/*
 * LICENSE_PLACEHOLDER
 */
package fr.cnes.regards.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import fr.cnes.regards.cloud.gateway.filters.ProxyLogFilter;
import fr.cnes.regards.framework.security.endpoint.DefaultAuthorityProvider;
import fr.cnes.regards.framework.security.endpoint.IAuthoritiesProvider;

/**
 *
 * Class GatewayApplication
 *
 * Spring boot starter class for Regards Gateway component
 *
 * @author Sébastien Binda
 * @since 1.0-SNAPSHOT
 */
@SpringBootApplication(scanBasePackages = "fr.cnes.regards.modules")
@EnableZuulProxy
@EnableAutoConfiguration
public class Application { // NOSONAR

    /**
     *
     * Starter method
     *
     * @param pArgs
     *            params
     * @since 1.0-SNAPSHOT
     */
    public static void main(final String[] pArgs) {
        SpringApplication.run(Application.class, pArgs); // NOSONAR
    }

    /**
     *
     * Create zuul proxy filter
     *
     * @return ProxyLogFilter
     * @since 1.0-SNAPSHOT
     */
    @Bean
    public ProxyLogFilter proxyLogFilter() {
        return new ProxyLogFilter();
    }

    /**
     * TODO : Replace with remote-tenant-resolver admin Authorities provider
     *
     * @return
     */
    @Bean
    public IAuthoritiesProvider authProvider() {
        return new DefaultAuthorityProvider();
    }

}
