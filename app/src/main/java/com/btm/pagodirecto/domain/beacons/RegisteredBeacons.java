package com.btm.pagodirecto.domain.beacons;

import java.util.List;

/**
 * Created by ealvarado on 05/06/17.
 */

public class RegisteredBeacons {

    String id;
    String beaconId;
    String ownerId;
    String location;
    String providerId;
    String providerName;
    List<String> tags;

    public RegisteredBeacons(String id, String beaconId, String ownerId, String location, String providerId, String providerName){
        this.id = id;
        this.beaconId = beaconId;
        this.ownerId = ownerId;
        this.location = location;
        this.providerId = providerId;
        this.providerName = providerName;
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getLocation() {
        return location;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getBeaconId() {
        return beaconId;
    }
}

