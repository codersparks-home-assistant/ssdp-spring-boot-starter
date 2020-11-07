package uk.codersparks.homeassistant.ssdp.autoconfig;

import io.resourcepool.ssdp.client.SsdpClient;
import io.resourcepool.ssdp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import uk.codersparks.homeassistant.ssdp.config.HomeAssistantSSDPProperties;

import javax.annotation.PreDestroy;

/**
 * Auto configuration class for the ssdp-spring-boot-starter
 */
@Configuration
@ConditionalOnBean(DiscoveryListener.class)
@ConditionalOnProperty("home-assistant.ssdp.identifyingSchema")
public class SsdpAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SsdpAutoConfiguration.class);

    private final SsdpClient ssdpClient;
    private final HomeAssistantSSDPProperties properties;
    private final DiscoveryListener discoveryListener;


    public SsdpAutoConfiguration(SsdpClient ssdpClient, HomeAssistantSSDPProperties properties, DiscoveryListener discoveryListener) {
        this.ssdpClient = ssdpClient;
        this.properties = properties;
        this.discoveryListener = discoveryListener;

        logger.info("SsdpAutoConfiguration Loaded");
    }

    /**
     * Function that reacts to application started event and starts the ssdp client scanning
     * @param event {@link ApplicationStartedEvent} to react to
     */
    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("Starting SSDP Client");

        DiscoveryOptions options = DiscoveryOptions.builder()
                .intervalBetweenRequests(properties.getRequestInterval())
                .maxWaitTimeSeconds(properties.getMaxWaitTime())
                .build();

        DiscoveryRequest discoveryRequest = SsdpRequest.builder()
                .serviceType(properties.getIdentifyingSchema())
                .discoveryOptions(options)
                .build();

        ssdpClient.discoverServices(discoveryRequest, discoveryListener);
    }

    @PreDestroy
    public void preDestroy() {
        ssdpClient.stopDiscovery();
    }


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
