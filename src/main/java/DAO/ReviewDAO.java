package DAO;

import java.util.ArrayList;
import java.util.List;

import TESTVO.TestReviewVO;

public class ReviewDAO{

	private List<TestReviewVO> reviewList; // 리뷰 목록 저장

    public ReviewDAO() {
        reviewList = new ArrayList<>();
    }

    // 리뷰 추가
    public void addReview(String name, String content, double score) {
        TestReviewVO newReview = new TestReviewVO(name, content, score);
        reviewList.add(newReview);
    }

    // 저장된 리뷰 리스트 반환
    public List<TestReviewVO> getReviews() {
        return reviewList;
    }
	
}
