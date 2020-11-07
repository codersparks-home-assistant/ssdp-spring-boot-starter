package uk.codersparks.homeassistant.ssdp.config;

import io.resourcepool.ssdp.client.SsdpClient;
import io.resourcepool.ssdp.model.DiscoveryListener;
import io.resourcepool.ssdp.model.SsdpService;
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class to declare beans
 */
@Configuration
public class BeanConfig {
    @Bean
    public SsdpClient ssdpClient() {
        return SsdpClient.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public DiscoveryListener noopDiscoveryListener() {
        return new DiscoveryListener() {
            @Override
            public void onServiceDiscovered(SsdpService service) {

            }

            @Override
            public void onServiceAnnouncement(SsdpServiceAnnouncement announcement) {

            }

            @Override
            public void onFailed(Exception ex) {

            }
        };
    }
}
