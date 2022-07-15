package org.cryptonews.main.ui.activities;

public class AppObject {

    boolean enabled;
    String url;
    AlertObject alert;

    public AppObject(){

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AlertObject getAlert() {
        return alert;
    }

    public void setAlert(AlertObject alert) {
        this.alert = alert;
    }
}
