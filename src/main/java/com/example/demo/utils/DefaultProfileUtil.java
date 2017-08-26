package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jerry on 2017/8/27.
 */
@Slf4j
public class DefaultProfileUtil {
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    private static final long serialVersionUID = 1L;

    private static final String SPRING_PROFILE_ACTIVE = "spring.profiles.active";
    private static final Properties BUILD_PROPERTIES = readProperties();

    /**
     * Load application.yml from classpath.
     *
     * @return {@link Properties}
     */
    private static Properties readProperties() {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(new ClassPathResource("config/application.yml"));
            return factory.getObject();

        } catch (Exception e) {
            log.error("Failed to reader application.yml to get default profile");
        }
        return null;
    }

    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
        * The default profile to use when no other profiles are defined
        * This cannot be set in the <code>application.yml</code> file.
        * See https://github.com/spring-projects/spring-boot/issues/1219
        */
        defProperties.put(SPRING_PROFILE_ACTIVE, getDefaultActiveProfiles());
        app.setDefaultProperties(defProperties);
    }

    public static String getDefaultActiveProfiles() {
        if (BUILD_PROPERTIES != null) {
            String activeProfile = BUILD_PROPERTIES.getProperty(SPRING_PROFILE_ACTIVE);
            if (activeProfile != null && !activeProfile.isEmpty() &&
                    (activeProfile.contains(SPRING_PROFILE_DEVELOPMENT) ||
                            activeProfile.contains(SPRING_PROFILE_PRODUCTION))) {
                return activeProfile;
            }
        }
        log.warn("No Spring profile configured, running with default profile: {}", SPRING_PROFILE_DEVELOPMENT);
        return SPRING_PROFILE_DEVELOPMENT;
    }
}
