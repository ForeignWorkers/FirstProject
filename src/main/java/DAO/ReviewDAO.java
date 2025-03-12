package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Data.GoogleDriveFileReader;
import Frame.FrameBase;
import Managers.DBDataManagers;
import VO.ReviewVO;
import VO.UserVO;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ReviewDAO{

	private List<ReviewVO> reviewList; // 리뷰 목록 저장

    public ReviewDAO() {
        reviewList = new ArrayList<>();
    }

    // 저장된 리뷰 리스트 반환
    public List<ReviewVO> getReviews() {
        return reviewList;
    }

    public void addReviewToJson(ReviewVO newReview, String fileName, String folderId) throws IOException {
        TypeToken<List<ReviewVO>> typeToken = new TypeToken<>() {};
        List<ReviewVO> reviewList = GoogleDriveFileReader.getInstance().getListFromJson(fileName, folderId, typeToken);

        //유저id에 해당하는 유저 닉네임으로 변경.
        newReview.setReviewerName(getReviewerNameById(newReview.getReviewerId()));
        
        // 🆕 리뷰 추가
        reviewList.add(newReview);

        // 📤 기존 데이터를 유지하면서 새로운 JSON 업로드
        String updatedJson = new GsonBuilder().setPrettyPrinting().create().toJson(reviewList);
        GoogleDriveFileReader.getInstance().uploadJsonWithLoading(fileName, updatedJson, folderId, FrameBase.getInstance());

        System.out.println("✅ 새로운 리뷰가 추가되었습니다: " + newReview.getReviewId());

        //새로운 리뷰 로컬 디비 리스트에 추가
        DBDataManagers.getInstance().getDbReviewsData().add(newReview);
    }
    

    // reviewerId에 해당하는 reviewerName을 반환
    private String getReviewerNameById(String reviewerId) {
        
        return DBDataManagers.getInstance().getReviewerNameById(reviewerId);
    }
    
}
