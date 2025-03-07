package Managers;

import Data.AppConstants;
import Data.GoogleDriveFileReader;
import VO.FavoriteVO;
import VO.RatingVO;
import VO.ReviewVO;
import VO.UserVO;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBDataManagers {
    private boolean isInitialized = false;
    private static DBDataManagers dbDataManagers;
    private List<UserVO> dbUsersData = new ArrayList<>();
    private List<ReviewVO> dbReviewsData = new ArrayList<>();
    private List<RatingVO> dbRatingData = new ArrayList<>();
    private List<FavoriteVO> dbFavoriteData = new ArrayList<>();

    //싱글턴 생성 로직
    public static DBDataManagers getInstance() {
        if (dbDataManagers == null) {
            dbDataManagers = new DBDataManagers();
        }
        return dbDataManagers;
    }

    public List<UserVO> getDbUsersData() {
        return dbUsersData;
    }

    public List<ReviewVO> getDbReviewsData() {
        return dbReviewsData;
    }
    
    public List<RatingVO> getDbRatingData() {
        return dbRatingData;
    }
    
    public List<FavoriteVO> getDbFavoriteData() {
        return dbFavoriteData;
    }

    //드라이버 속도가 느려서 데이터는 처음 진입 시 초기화 써주기
    public DBDataManagers InitDBDataManagers() throws IOException {
        if(isInitialized)
        {
            System.out.println("이미 초기화가 되어있습니다.");
            return this;
        }

        // 유저 데이터 셋업
        TypeToken<List<UserVO>> userListToken = new TypeToken<List<UserVO>>() {};
        List<UserVO> userDatas = GoogleDriveFileReader.getInstance().getListFromJson(AppConstants.USER_FILE_NAME, AppConstants.FOLDER_ID,userListToken);
        if(userDatas != null)
        {
        	System.out.println("유저 데이터를 셋업했습니다.");
            dbUsersData.addAll(userDatas);
        }

        //리뷰 데이터 셋업
        TypeToken<List<ReviewVO>>  reviewListToken = new TypeToken<>() {};
        List<ReviewVO> reviewDatas = GoogleDriveFileReader.getInstance().getListFromJson(AppConstants.REVIEW_FILE_NAME, AppConstants.FOLDER_ID, reviewListToken);
        if(reviewDatas != null)
        {
        	System.out.println("리뷰 데이터를 셋업했습니다.");
            dbReviewsData.addAll(reviewDatas);
        }
        
        //평점 데이터 셋업
        TypeToken<List<RatingVO>> ratingListToken = new TypeToken<>() {};
        List<RatingVO> ratingDatas = GoogleDriveFileReader.getInstance().getListFromJson(AppConstants.RATING_FILE_NAME, AppConstants.FOLDER_ID, ratingListToken);
        if(ratingDatas != null)
        {
        	System.out.println("평점 데이터를 셋업했습니다.");
        	dbRatingData.addAll(ratingDatas);
        }
        
        //찜리스트 데이터 셋업
        TypeToken<List<FavoriteVO>> favoriteListToken = new TypeToken<>() {};
        List<FavoriteVO> favoriteDatas = GoogleDriveFileReader.getInstance().getListFromJson(AppConstants.FAVORITE_FILE_NAME, AppConstants.FOLDER_ID, favoriteListToken);
        if(favoriteDatas != null)
        {
        	System.out.println("평점 데이터를 셋업했습니다.");
        	dbFavoriteData.addAll(favoriteDatas);
        }

        isInitialized = true;
        return this;
    }
}
