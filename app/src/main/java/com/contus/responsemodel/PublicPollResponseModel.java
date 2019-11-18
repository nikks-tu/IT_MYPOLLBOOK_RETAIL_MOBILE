package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 9/8/2015.
 */
public class PublicPollResponseModel {
    //Success
    @SerializedName("success")
    private String mSuccess;
    //Results
    @SerializedName("results")
    private Results mResults;

    /**
     * Get the mSuccess
     *
     * @return
     */

    public String getmSuccess() {
        return mSuccess;
    }

    /**
     * Get the results
     *
     * @return
     */
    public Results getResults() {
        return mResults;
    }



    /**
     * Teh class results
     */
    public class Results {

        //campaignResults
        @SerializedName("camp_res")
        private CampaignResults campaignResults;
        /**
         * Get the campaignResults
         *
         * @return campaignResults
         */
        public CampaignResults getCampaignResults() {
            return campaignResults;
        }


    }

    /**
     * The class  CampaignResults
     */
    public class CampaignResults {
        //Data
        @SerializedName("data")
        List<Data> data;
        //Current PAGE
        @SerializedName("current_page")
        private String currentPage;
        //Get the last PAGE
        @SerializedName("last_page")
        private String lastPage;

        /**
         * Get the data
         *
         * @return data
         */
        public List<Data> getData() {
            return data;
        }


        /**
         * Get the current PAGE
         *
         * @return currentPage
         */
        public String getCurrentPage() {
            return currentPage;
        }



        /**
         * Get the last PAGE
         *
         * @return lastPage
         */
        public String getLastPage() {
            return lastPage;
        }


    }

    /**
     * The class data
     */
    public class Data {
        //Campaign image
        @SerializedName("camp_image")
        private String campImage;
        //Updated at
        @SerializedName("updated_at")
        private String updatedAt;
        //Camp description
        @SerializedName("camp_description")
        private String campDescription;
        //id
        @SerializedName("id")
        private String id;
        //Camp name
        @SerializedName("camp_name")
        private String campName;
        //Campaing likes counts
        @SerializedName("campaignLikesCounts")
        private String campaignLikesCounts;

        //campaignCommentsCounts
        @SerializedName("campaignCommentsCounts")
        private String campaignCommentsCounts;
        //campaignUserLikes
        @SerializedName("campaignUserLikes")
        private String campaignUserLikes;
        //Partner
        private Partner partner;
        //Category
        private Category category;


        /**
         * Get the campaign image
         *
         * @return
         */
        public String getCampImage() {
            return campImage;
        }



        /**
         * Get camp description
         *
         * @return
         */
        public String getCampDescription() {
            return campDescription;
        }

        /**
         * Get the updated at
         *
         * @return
         */
        public String getUpdatedAt() {
            return updatedAt;
        }


        /**
         * Get the partner
         *
         * @return partner
         */
        public Partner getPartner() {
            return partner;
        }



        /**
         * Get the id
         *
         * @return id
         */
        public String getId() {
            return id;
        }



        /**
         * Get the camp name
         *
         * @return
         */
        public String getCampName() {
            return campName;
        }



        /**
         * Get the campaign Likes counts
         *
         * @return
         */
        public String getCampaignLikesCounts() {
            return campaignLikesCounts;
        }


        /**
         * Get the campaignCommentsCounts
         *
         * @return campaignCommentsCounts
         */
        public String getCampaignCommentsCounts() {
            return campaignCommentsCounts;
        }


        /**
         * Get the campaignUserLikes
         *
         * @return campaignUserLikes
         */

        public String getCampaignUserLikes() {
            return campaignUserLikes;
        }



        /**
         * The class partner
         */
        public class Partner {
            //Partner logo
            @SerializedName("partner_logo")
            private String partnerLogo;

            //Partner tag line
            @SerializedName("partner_tagline")
            private String partnerTagLine;
            /**
             * Get the partner logo
             *
             * @return
             */
            public String getPartnerLogo() {
                return partnerLogo;
            }



            /**
             * Get the partner tag linne
             *
             * @return partnerTagLine
             */
            public String getPartnerTagLine() {
                return partnerTagLine;
            }
        }

        /**
         * Get the category
         *
         * @return category
         */
        public Category getCategory() {
            return category;
        }



        /**
         * The class category
         */
        public class Category {
            //Categorty Name
            @SerializedName("category_name")
            private String categoryName;
            /**
             * Get the categpory name
             *
             * @return
             */
            public String getCategoryName() {
                return categoryName;
            }


        }

    }
}
