package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 3/2/16.
 */
public class PrivatePollResponseModel {   //Success string
    @SerializedName("success")
    private String success;
    //Subclass results
    private Results results;
    /**
     * Success Value
     *
     * @return -returns the value from the response
     */
    public String getSuccess() {
        return success;
    }
    /**
     * Results
     *
     * @return-return the details in sub class
     */
    public Results getResults() {
        return results;
    }
    /**
     * Contains the variable to retrive the details from the resposne
     */
    public class Results {
        //Perpage count
        @SerializedName("per_page")
        private String perPage;
        //Current PAGE
        @SerializedName("current_page")
        private String currentPage;
        //Last PAGE
        @SerializedName("last_page")
        private String lastPage;
        //Array list
        private List<Data> data;
        /**
         * Return the per PAGE count
         *
         * @return-per PAGE
         */
        public String getPerPage() {
            return perPage;
        }

        /**
         * Current count
         *
         * @return returns the current count
         */
        public String getCurrentPage() {
            return currentPage;
        }

        /**
         * Last PAGE count
         *
         * @return-last PAGE
         */
        public String getLastPage() {
            return lastPage;
        }
        /**
         * Get the list details
         *
         * @return-return the array list details
         */
        public List<Data> getData() {
            return data;
        }

        /**
         * Contains the variable to retrive the details from the resposne
         */
        public class Data {
            //id

            //Poll likes counts
            @SerializedName("pollLikesCounts")
            private String pollLikesCounts;

            //userPollLikes
            @SerializedName("userPollLikes")
            private String userPollLikes;

            //Poll COMMENTS counts
            @SerializedName("pollCommentsCounts")
            private String pollCommentsCounts;
            //User participate polls
            @SerializedName("user_participate_polls")
            private List<UserParticipatePolls> userParticipatePolls;
            @SerializedName("status")
            private String status;
            //user info details
            @SerializedName("user_infos")
            private UserInfo userInfo;
            //Poll category
            private PollCategory pollcategories;
            //time
            @SerializedName("updated_at")
            private String updatedAt;

            @SerializedName("user_polls_question")
            private UserPollQuestion userPollsQuestion;

            public UserPollQuestion getUserPollsQuestion() {
                return userPollsQuestion;
            }



            /**
             * Get the status
             * @return
             */
            public String getStatus() {
                return status;
            }


            /**
             * Getting the time
             *
             * @return updatedAt
             */
            public String getUpdatedAt() {
                return updatedAt;
            }





            /**
             * Getting the  poll category details
             *
             * @return pollcategory
             */
            public PollCategory getCategory() {
                return pollcategories;
            }



            /**
             * Getting the user info details
             *
             * @return userInfo
             */
            public UserInfo getUserInfo() {
                return userInfo;
            }


            /**
             * Getting the poll ikes counts
             *
             * @return pollLikesCounts
             */
            public String getCampaignLikesCounts() {
                return pollLikesCounts;
            }


            /**
             * Poll COMMENTS counts
             *
             * @return pollCommentsCounts
             */
            public String getCampaignCommentsCounts() {
                return pollCommentsCounts;
            }



            /**
             * Getting the poll participate count
             *
             * @return pollParticipateCount
             */
            public String getPollParticipateCount() {
                return pollParticipateCount;
            }

            //pollParticipateCount
            private String pollParticipateCount;

            /**
             * Getting the user poll likes
             *
             * @return userPollLikes
             */
            public String getCampaignUserLikes() {
                return userPollLikes;
            }



            public class UserPollQuestion{

                @SerializedName("poll_question")
                private String pollQuestion;
                @SerializedName("poll_qimage")
                private String pollQuestionImage;
                @SerializedName("polls_qimage2")
                private String pollQuestionImage1;
                @SerializedName("id")
                private String id;
                //poll type
                @SerializedName("poll_type")
                private String pollType;
                //Youttube url
                @SerializedName("youtube_url")
                private String youTubeUrl;


                public String getPollQuestionImage1() {
                    return pollQuestionImage1;
                }
                public String getPollQuestionImage() {
                    return pollQuestionImage;
                }

                public String getPollQuestion() {
                    return pollQuestion;
                }
                //Options
                @SerializedName("poll_answers")
                private UserPollAns userPollsAns;
                /**
                 * Getting the poll type
                 *
                 * @return-poll type
                 */
                public String getPollType() {
                    return pollType;
                }
                /**
                 * Getting the youtube url
                 *
                 * @return url
                 */
                public String getYouTubeUrl() {
                    return youTubeUrl;
                }




                /**
                 * Getting the id
                 *
                 * @return id
                 */
                public String getId() {
                    return id;
                }
                /**
                 * Getitng the user poll answer
                 *
                 * @return userPollsAns
                 */
                public UserPollAns getUserPollsAns() {
                    return userPollsAns;
                }


            }
            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class PollCategory {

                //Category
                //Category name
                @SerializedName("categories")
                private Category category;
                /**
                 * Getting the category details from the resposne
                 */
                public Category getCategory() {
                    return category;
                }



                /**
                 * Contains the variable to retrive the details from the resposne
                 */
                public class Category {
                    //Category name
                    @SerializedName("category_name")
                    private String categoryName;
                    /**
                     * Getting the category name
                     *
                     * @return
                     */
                    public String getCategoryName() {
                        return categoryName;
                    }


                }


            }

            /**
             * Getting the user participate details
             *
             * @return userParticipatePolls
             */
            public List<UserParticipatePolls> getUserParticipatePolls() {
                return userParticipatePolls;
            }



            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class UserParticipatePolls {
                //poll id
                @SerializedName("poll_id")
                private String pollId;
                //Poll answer
                @SerializedName("poll_answer")
                private String pollAnswer;
                //user id
                @SerializedName("user_id")
                private String userId;
                /**
                 * Getting the poll id
                 *
                 * @return pollId
                 */
                public String getPollId() {
                    return pollId;
                }



                /**
                 * Getting the poll answer
                 *
                 * @return pollAnswer
                 */
                public String getPollAnswer() {
                    return pollAnswer;
                }



                /**
                 * Getting the userId
                 *
                 * @return userId
                 */
                public String getUserId() {
                    return userId;
                }


            }

            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class UserInfo {

                //user name
                @SerializedName("name")
                private String userName;
                //user profile image
                @SerializedName("image")
                private String userProfileImg;
                /**
                 * Getting the user name
                 *
                 * @return user name
                 */
                public String getUserName() {
                    return userName;
                }


                /**
                 * Getting the user profile image
                 *
                 * @return userProfileImg
                 */
                public String getUserProfileImg() {
                    return userProfileImg;
                }



            }

            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class UserPollAns {
                //id
                @SerializedName("id")
                private String id;
                //poll answer1
                @SerializedName("poll_answer1")
                private String pollAnswer1;
                //poll answer2
                @SerializedName("poll_answer2")
                private String pollAnswer2;
                //poll answer3
                @SerializedName("poll_answer3")
                private String pollAnswer3;

                //poll answer4
                @SerializedName("poll_answer4")
                private String pollAnswer4;

                /**
                 * Getting the id
                 *
                 * @return id
                 */
                public String getId() {
                    return id;
                }



                /**
                 * Getting the poll answer1
                 *
                 * @return pollAnswer1
                 */
                public String getPollAnswer1() {
                    return pollAnswer1;
                }



                /**
                 * Getting the poll answer1
                 *
                 * @return pollAnswer1
                 */
                public String getPollAnswer2() {
                    return pollAnswer2;
                }



                /**
                 * Getting the poll answer1
                 *
                 * @return pollAnswer1
                 */
                public String getPollAnswer3() {
                    return pollAnswer3;
                }


                /**
                 * Getting the poll answer1
                 *
                 * @return pollAnswer1
                 */
                public String getPollAnswer4() {
                    return pollAnswer4;
                }


            }

        }
    }


}
