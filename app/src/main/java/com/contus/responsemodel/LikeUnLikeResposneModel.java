/**
 * @category TeasorTrailor
 * @package com.contus.responsemodel
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.responsemodel;

/**
 * Created by user on 9/28/2015.
 */
public class LikeUnLikeResposneModel {
    //Success
    private String success;
    //Message
    private String msg;
    //Results
    private String results;
    //Count
    private String count;
    /**
     * Get theh success
     *
     * @return
     */
    public String getSuccess() {
        return success;
    }
    /**
     * Get the msg
     *
     * @return msg
     */
    public String getMsg() {
        return msg;
    }
    /**
     * Get the results
     *
     * @return results
     */
    public String getResults() {
        return results;
    }
    /**
     * Get the count
     *
     * @return count
     */
    public String getCount() {
        return count;
    }


}
