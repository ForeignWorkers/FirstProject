package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Data.GoogleDriveFileReader;
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

    // 리뷰 추가- TO DO -> 리뷰 추가 할때 리뷰 ID와 어떤 작품에 대한 리뷰인지 추가적으로 받아주세요.
    public void addReview(String name, String content, double score) {
        ReviewVO newReview = new ReviewVO(name, content, score);
        reviewList.add(newReview);
    }

    // 저장된 리뷰 리스트 반환
    public List<ReviewVO> getReviews() {
        return reviewList;
    }

    public void addReviewToJson(ReviewVO newReview, String fileName, String folderId) throws IOException {
        TypeToken<List<ReviewVO>> typeToken = new TypeToken<>() {};
        List<ReviewVO> reviewList = GoogleDriveFileReader.getInstance().getListFromJson(fileName, folderId, typeToken);

        // 🆕 새 유저 추가
        reviewList.add(newReview);

        // 📤 기존 데이터를 유지하면서 새로운 JSON 업로드
        String updatedJson = new GsonBuilder().setPrettyPrinting().create().toJson(reviewList);
        GoogleDriveFileReader.getInstance().uploadJson(fileName, updatedJson, folderId);

        System.out.println("✅ 새로운 리뷰가 추가되었습니다: " + newReview.getReviewid());

        //새로운 리뷰 로컬 디비 리스트에 추가
        DBDataManagers.getInstance().getDbReviewsData().add(newReview);
    }
}
