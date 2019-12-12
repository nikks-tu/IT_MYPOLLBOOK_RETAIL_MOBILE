package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 10/6/2015.
 */
public class UserPollResponseModel {
    //Success string
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
            @SerializedName("id")
            private String id;
            //poll type
            @SerializedName("poll_type")
            private String pollType;

            //Poll question
            @SerializedName("poll_question")
            private String pollQuestion;
            @SerializedName("userCount")
            private String userCount;
            @SerializedName("adminCount")
            private String adminCount;

            public String getGroup_poll() {
                return group_poll;
            }

            public void setGroup_poll(String group_poll) {
                this.group_poll = group_poll;
            }

            @SerializedName("group_poll")
            private String group_poll;

            public String getUserCount() {
                return userCount;
            }

            public String getAdminCount() {
                return adminCount;
            }

            //poll image
            @SerializedName("poll_qimage")
            private String pollquestionImage;
            //Youttube url
            @SerializedName("youtube_url")
            private String youTubeUrl;

            //Poll question image1
            @SerializedName("polls_qimage2")
            private String pollquestionImage1;
            //time
            @SerializedName("updated_at")
            private String updatedAt;

            //user info details
            @SerializedName("user_infos")
            private UserInfo userInfo;

            //Poll likes counts
            @SerializedName("pollLikesCounts")
            private String pollLikesCounts;

            //userPollLikes
            @SerializedName("userPollLikes")
            private String userPollLikes;
            //Options
            @SerializedName("user_polls_ans")
            private List<UserPollAns> userPollsAns;
            //Poll COMMENTS counts
            @SerializedName("pollCommentsCounts")
            private String pollCommentsCounts;
            //User participate polls
            @SerializedName("user_participate_polls")
            private List<UserParticipatePolls> userParticipatePolls;
            @SerializedName("status")
            private String status;
            //Poll category
            private PollCategory pollcategory;
            /**
             * Getting the id
             *
             * @return id
             */
            public String getId() {
                return id;
            }

            /**
             * Get the status
             * @return
             */
            public String getStatus() {
                return status;
            }
            /**
             * Getting the poll type
             *
             * @return-poll type
             */
            public String getPollType() {
                return pollType;
            }

            /**
             * Getting the poll question
             *
             * @return-poll question
             */
            public String getPollQuestion() {
                return pollQuestion;
            }


            /**
             * getting Poll question image
             *
             * @return pollquestionImage
             */
            public String getPollquestionImage() {
                return pollquestionImage;
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
             * getting Poll question image
             *
             * @return pollquestionImage
             */
            public String getPollquestionImage1() {
                return pollquestionImage1;
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
                return pollcategory;
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
             * Getitng the user poll answer
             *
             * @return userPollsAns
             */
            public List<UserPollAns> getUserPollsAns() {
                return userPollsAns;
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



            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class PollCategory {
                //Category
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
