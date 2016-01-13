package org.osgp.adapter.protocol.dlms.application.config;

public class JwccWSConfig {

    private String sms_uri;
    private String licenseKey;
    private String username;
    private String password;
    private String api_version;

    public JwccWSConfig(final String sms_uri, final String licenseKey, final String username, final String password,
            final String api_version) {
        this.sms_uri = sms_uri;
        this.licenseKey = licenseKey;
        this.username = username;
        this.password = password;
        this.api_version = api_version;
    }

    public String getSms_uri() {
        return this.sms_uri;
    }

    public String getLicenseKey() {
        return this.licenseKey;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getApi_version() {
        return this.api_version;
    }
}
