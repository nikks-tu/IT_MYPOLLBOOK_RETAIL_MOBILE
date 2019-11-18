package com.contus.responsemodel;
import com.contus.app.ContactResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Contact.java
 * This class is used to load the contact user's wall data.
 *
 * @author Contus Team <developers@contus.in>
 * @version 1.0
 * @category Contus
 * @package com.poaap.chatpageview
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http ://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Created by user on 10/1/2015.
 */
public class Contact {

    @Expose
    private Boolean error;
    @SerializedName("response")
    @Expose
    private List<ContactResponse> contactResponse;

    /**
     *
     * @return
     * The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The response
     */
    public List<ContactResponse> getContactResponse() {
        return contactResponse;
    }

    /**
     *
     * @param contactResponse
     * The response
     */
    public void setContactResponse(List<ContactResponse> contactResponse) {
        this.contactResponse = contactResponse;
    }
}
