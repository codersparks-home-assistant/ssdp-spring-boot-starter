package uk.codersparks.homeassistant.ssdp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to configure the ssdp listener
 */
@Configuration
@ConfigurationProperties(prefix = "home-assistant.ssdp")
@Data
public class HomeAssistantSSDPProperties {

    /**
     * Time between requests for update (milliseconds)
     */
    private long requestInterval = 300000L;

    /**
     * Max wait time to await response
     */
    private int maxWaitTime = 30;

    /**
     * Schema event that should be used to identfy box.
     *
     * This is used as part of the auto configuration to enable the listener
     */
    private String identifyingSchema;
}
