package com.insider.configs;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:config.properties"})
public interface Configs extends Config {
    static Configs getConfigs() {
        return ConfigCache.getOrCreate(Configs.class);
    }

    @Key("env")
    String env();

    @Key("browser")
    String browser();

    @Key("${env}.insider.baseurl")
    String baseUrl();

    @Key("driver.options.headless")
    boolean isHeadless();

}
