package Managers;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.gson.reflect.TypeToken;

import Data.AppConstants;
import Data.GoogleDriveFileReader;
import Panel.ReviewPanel;
import VO.FavoriteVO;
import VO.RatingVO;
import VO.ReviewVO;
import VO.UserVO;

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

    // 더미 리뷰 데이터를 반환하는 메서드
    public List<ReviewVO> getReviewerReviewsData(String reviewerId) {
        List<ReviewVO> reviews = new ArrayList<>();
        
        // 더미 데이터 추가
        if ("5555".equals(reviewerId)) { // 테스트용 reviewerId
            reviews.add(new ReviewVO(1234, "5555", "정말 멋진 영화입니다!", 4.5)); // 첫 번째 리뷰
            reviews.add(new ReviewVO(1235, "5555", "조금 아쉬웠던 점이 있지만 전반적으로 좋았어요.", 3.5)); // 두 번째 리뷰
        }
        
        return reviews;
    }
    
    // RatingVO 객체 가져오기 (contentId에 해당하는)
    public RatingVO getRatingData(int contentId) {
        // DB나 JSON에서 contentId에 맞는 RatingVO를 찾아서 반환
        return findRatingInDB(contentId);
    }

    // RatingVO 추가
    public void addRatingData(RatingVO ratingVO) {
        // DB나 JSON에 RatingVO를 추가
        saveRatingToDB(ratingVO);
    }

    // RatingVO 갱신
    public void updateRatingData(RatingVO ratingVO) {
        // DB나 JSON에서 RatingVO를 갱신
        updateRatingInDB(ratingVO);
    }

    // DB에서 RatingVO 찾기
    private RatingVO findRatingInDB(int contentId) {
        // DB나 JSON에서 contentId에 맞는 RatingVO를 찾는 코드 작성
        return null; // 예시
    }

    // DB에 RatingVO 추가
    private void saveRatingToDB(RatingVO ratingVO) {
        // DB나 JSON에 RatingVO를 저장하는 코드 작성
    }

    // DB에서 RatingVO 갱신
    private void updateRatingInDB(RatingVO ratingVO) {
        // DB나 JSON에서 RatingVO를 업데이트하는 코드 작성
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

    
    
    
    //컨텐츠 ID로 분류해서 리뷰 목록 생성
    public List<ReviewVO> getContentReviewsData(int contentId) {
        List<ReviewVO> allReviews = getDbReviewsData(); // 전체 리뷰 가져오기
        List<ReviewVO> filteredReviews = new ArrayList<>();
        for (ReviewVO review : allReviews) {
            if (review.getContentId() == contentId) { // 콘텐츠명 비교
                filteredReviews.add(review);
            }
        }
        return filteredReviews;
    }
   /* //리뷰어명으로 분류해서 리뷰 목록 생성
    public List<ReviewVO> getReviewerReviewsData(String reviewerId) {
        List<ReviewVO> allReviews = getDbReviewsData(); // 전체 리뷰 가져오기
        List<ReviewVO> filteredReviews = new ArrayList<>();

        for (ReviewVO review : allReviews) {
            if (review.getReviewerId().equals(reviewerId)) { // 작성자명 비교
                filteredReviews.add(review);
            }
        }
        return filteredReviews;
    }
    */
    // 리뷰어 ID로 리뷰어명 조회
    public String getReviewerNameById(String reviewerId) {
        for (UserVO user : dbUsersData) {
            if (user.getId() == reviewerId) {
                return user.getNickName();
            }
        }
        return "알 수 없는 사용자"; // 리뷰어id가 db에 없을 경우
    }
    
    // 컨텐츠ID로 컨텐츠명 조회
    public List<ReviewVO> getViewDataById(int itemId) {
    	List<ReviewVO> findReviews = new ArrayList<>();
    	
    	for(ReviewVO vo : dbReviewsData) {
    		if(vo.getContentId() == itemId) {
    			findReviews.add(vo);
    		}
    	}
    	return findReviews;
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
