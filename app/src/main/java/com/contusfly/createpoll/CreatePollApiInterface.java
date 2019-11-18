package com.contusfly.createpoll;

import com.contus.app.Constants;
import com.contus.responsemodel.CreatePollResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by user on 9/30/2015.
 */
public interface CreatePollApiInterface {
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreatePoll(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_qimage2") TypedFile profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String pollGroup,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                        Callback<CreatePollResponseModel> callback);


    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreatePoll_with_url(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String pollGroup,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                        Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreatePollPollQuestion2(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type,  @Part("poll_qimage1") String profilemg,@Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String pollGroup,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                     Callback<CreatePollResponseModel> callback);



    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreatePollPollQuestion1(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg,@Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String pollGroup,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                     Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreatePollPollEmpty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg,@Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String pollGroup,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                 Callback<CreatePollResponseModel> callback);


    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultiplePoll(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultiplePollQuestion1(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                         Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultiplePollQuestion2(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                         Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultiplePollEmpty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img,@Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                     Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImages(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") String profilemg, @Part("poll_qimage2") String profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") String poll_answer1, @Part("poll_answer2") String poll_answer2, @Part("poll_answer3") String poll_answer3, @Part("poll_answer4") String poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                  Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateQuestion1Empty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_qimage2") TypedFile profile_img, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                  Callback<CreatePollResponseModel> callback);


    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion1(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer3") TypedFile poll_answer3, @Part("poll_answer4") TypedFile poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                           Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion1Answer3(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer3") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                  Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion1Answer4(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String userId, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer4") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                  Callback<CreatePollResponseModel> callback);


    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion1OptionsEmpty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage1") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                       Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesOption3(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage") TypedFile profilemg, @Part("poll_qimage2") TypedFile profilemg1, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer3") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                         Callback<CreatePollResponseModel> callback);


    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesOption4(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage") TypedFile profilemg, @Part("poll_qimage2") TypedFile profilemg1, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer4") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                         Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion2AnswerEmpty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage2") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("category_id") String category_id, @Part("poll_group") String tagsList,
                                                      Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion2Answer3(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage2") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer3") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                  Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion2Answer4(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage2") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer4") TypedFile poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                  Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestion2Empty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_qimage2") TypedFile profilemg, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestionAnswer4Empty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer4, @Part("poll_answer3") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                      Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestionAnswer(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                Callback<CreatePollResponseModel> callback);
    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestionAnswer3Empty(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer4, @Part("poll_answer4") TypedFile poll_answer3, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                      Callback<CreatePollResponseModel> callback);

    @Multipart
    @POST(Constants.SAVE_USER_POLLS_API)
    void postCreateMultipleImagesQuestionAnswer1(@Part(Constants.ACTION) String action, @Part(Constants.USER_ID) String user_id, @Part(Constants.USER_TYPE) String user_type, @Part("poll_type") String poll_type, @Part("poll_question") String poll_question, @Part("poll_answer1") TypedFile poll_answer1, @Part("poll_answer2") TypedFile poll_answer2, @Part("poll_answer3") TypedFile poll_answer3, @Part("poll_answer4") TypedFile poll_answer4, @Part("category_id") String category_id, @Part("poll_group") String tagsList,@Part("group_id") String groupId,@Part("contact_no") String contactNo,
                                                Callback<CreatePollResponseModel> callback);
}
