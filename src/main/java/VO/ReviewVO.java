package VO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewVO {
	private String reviewId; // 리뷰 ID
	private int contentId; // 작품 ID
	private String reviewerId; // 리뷰 작성자 ID
	private double reviewScore; // 리뷰 점수
	private double reviewTotal; // 리뷰 점수 총합
	private double avgReviewScore; // 리뷰 평균 점수
	private int iineCount; // 좋아요 수
	private int personNum; // 평점 등록자 수
	private String reviewContent;// 리뷰 내용
	private String reviewerName; // 리뷰 작성자명
	private String reviewDate; // 리뷰 작성일시.
	private String contentName; // 작품명

	//
	public ReviewVO(int contentId, String reviewerId, String reviewContent, double reviewScore) {
		this.contentId = contentId; // 작품 ID
		this.reviewerId = reviewerId; // 작성자ID
		this.reviewContent = reviewContent; // 리뷰 내용
		this.reviewScore = reviewScore; // 리뷰 평점
		this.reviewTotal = reviewScore; // 첫 리뷰 점수 그대로 저장
		this.personNum = 1; // 첫 리뷰어 1명
		this.iineCount = 0; // 좋아요 0개로 초기화
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.reviewDate = LocalDateTime.now().format(formatter);
	}
	
	public double getReviewAvgScore() {
		return Math.round((reviewTotal / personNum) * 10.0) / 10.0; // 소수 첫째 자리로 반올림
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public void setContentName(int contentId) {
		this.contentName = contentName;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewid(String reviewId) {
		this.reviewId = reviewId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public double getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(double reviewScore) {
		this.reviewScore = reviewScore;
	}

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public String getReviewName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public int getIineCount() {
		return iineCount;
	}

	public void addLike() {
		this.iineCount++;
	}

	public void removeLike() {
		if (this.iineCount > 0) {
			this.iineCount--;
		}
	}
}
