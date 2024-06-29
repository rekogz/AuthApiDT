package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/auth.properties"
})
public interface AuthConfig extends Config {

    @Config.Key("email")
    String userEmail();


    @Key("password")
    String userPassword();
}
