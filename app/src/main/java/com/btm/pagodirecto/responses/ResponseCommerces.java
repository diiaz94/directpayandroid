package com.btm.pagodirecto.responses;

import com.btm.pagodirecto.dto.Commerce;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Pedro on 22/11/2017.
 */

public class ResponseCommerces {

    @SerializedName("users")
    @Expose
    ArrayList<Commerce> commerces;

    public ArrayList<Commerce> getCommerces() {
        return commerces;
    }
}
