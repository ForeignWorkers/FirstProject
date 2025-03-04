package Managers;

import Data.AppConstants;
import Data.GoogleDriveFileReader;
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
            dbUsersData.addAll(userDatas);
        }

        //리뷰 데이터 셋업
        TypeToken<List<ReviewVO>>  reviewListToken = new TypeToken<>() {};
        List<ReviewVO> reviewDatas = GoogleDriveFileReader.getInstance().getListFromJson(AppConstants.REVIEW_FILE_NAME, AppConstants.FOLDER_ID, reviewListToken);
        if(userDatas != null)
        {
            dbReviewsData.addAll(reviewDatas);
        }

        isInitialized = true;
        return this;
    }
}
