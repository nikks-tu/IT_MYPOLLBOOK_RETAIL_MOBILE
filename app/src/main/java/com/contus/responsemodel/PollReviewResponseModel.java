package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 10/26/2015.
 */
public class PollReviewResponseModel {

    //Success
    private String success;
    //results
    private Results results;

    /**
     * Get the success
     *
     * @return success
     */
    public String getSuccess() {
        return success;
    }


    /**
     * Get the results
     *
     * @return results
     */
    public Results getResults() {
        return results;
    }



    /**
     * The class Results
     */
    public class Results {
        //POll review
        private List<Results.PollReview> pollreview;

        //Total count
        private String pollTotalCount;
        /**
         * Get the poll review
         *
         * @return
         */
        public List<PollReview> getPollreview() {
            return pollreview;
        }



        /**
         * Get the toatal count
         *
         * @return pollTotalCount
         */
        public String getPollTotalCount() {
            return pollTotalCount;
        }


        /**
         * The class Poll review
         */
        public class PollReview {
            //Poll question
            @SerializedName("poll_question")
            private String pollQuestion;
            //Poll  type
            @SerializedName("poll_type")
            private String pollType;
            //Poll question image
            @SerializedName("poll_qimage")
            private String pollQuestionimage;
            @SerializedName("polls_qimage2")
            private String pollQuestionimage2;
            //GEt the answer
            @SerializedName("user_polls_ans")
            private List<Results.PollReview.PollAnswer> pollAnswer;
            //youtubeUrl
            @SerializedName("youtube_url")
            private String youtubeUrl;
            //Participate poll
            private List<Results.PollReview.ParticipatePoll> participatePoll;
            /**
             * Get the poll question
             *
             * @return
             */
            public String getPollQuestion() {
                return pollQuestion;
            }



            /**
             * Get the poll question image
             *
             * @return pollQuestionimage
             */
            public String getPollQuestionimage() {
                return pollQuestionimage;
            }

            /**
             * Get the pol type
             *
             * @return pollType
             */

            public String getPollType() {
                return pollType;
            }



            //Poll question image 2
            public String getPollQuestionimage2() {
                return pollQuestionimage2;
            }



            /**
             * GEt the you tube url
             *
             * @return youtubeUrl
             */
            public String getYoutubeUrl() {
                return youtubeUrl;
            }


            /**
             * Get the poll answer
             *
             * @return pollAnswer
             */
            public List<PollAnswer> getPollAnswer() {
                return pollAnswer;
            }



            /**
             * Get the participate poll
             *
             * @return
             */
            public List<ParticipatePoll> getParticipatePoll() {
                return participatePoll;
            }



            /**
             * The class participate poll
             */
            public class ParticipatePoll {
                //Poll answer option
                @SerializedName("poll_answer_option")
                private String pollAnswerOption;
                //Poll counts
                @SerializedName("pollCounts")
                private String pollCounts;
                /**
                 * GEt the poll answser option
                 *
                 * @return
                 */
                public String getPollAnswerOption() {
                    return pollAnswerOption;
                }



                /**
                 * Get the poll counts
                 *
                 * @return pollCounts
                 */
                public String getPollCounts() {
                    return pollCounts;
                }



            }

            /**
             * The class poll answer
             */
            public class PollAnswer {


                //Poll answer
                @SerializedName("poll_answer1")
                private String pollAnswer1PollReview;
                //Poll answer
                @SerializedName("poll_answer2")
                private String pollAnswer2PollReview;

                //Poll answer
                @SerializedName("poll_answer4")
                private String pollAnswer4PollReview;
                //Poll answer
                @SerializedName("poll_answer3")
                private String pollAnswer3PollReview;
                /**
                 * Get the poll answer
                 *
                 * @return pollAnswer1PollReview
                 */
                public String getPollAnswer1() {
                    return pollAnswer1PollReview;
                }



                /**
                 * Get the poll answer
                 *
                 * @return pollAnswer1PollReview
                 */
                public String getPollAnswer2() {
                    return pollAnswer2PollReview;
                }



                /**
                 * Get the poll answer
                 *
                 * @return pollAnswer1PollReview
                 */
                public String getPollAnswer3() {
                    return pollAnswer3PollReview;
                }



                /**
                 * Get the poll answer
                 *
                 * @return pollAnswer1PollReview
                 */
                public String getPollAnswer4() {
                    return pollAnswer4PollReview;
                }


            }
        }
    }
}
