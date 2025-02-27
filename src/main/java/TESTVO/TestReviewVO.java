package TESTVO;

import java.util.Objects;

public class TestReviewVO {

	private double reviewScore; // 리뷰 점수
	private double reviewTotal; // 리뷰 평점 누적 합
	private int iineCount; // 좋아요 수
	private int personNum; //평점 등록자 수
	private String reviewContent; // 리뷰 내용
	private String reviewName = ""; // 평점 등록자명
	
	
	public double getReviewScore() {
		return reviewScore;
	}
	public void setReviewScore(int reviewScore) {
		this.reviewScore = reviewScore;
	}
	public double getReviewTotal() {
		return reviewTotal;
	}
	public void setReviewTotal(int reviewTotal) {
		this.reviewTotal = reviewTotal;
	}
	public int getIineCount() {
		return iineCount;
	}
	public void setIineCount(int iineCount) {
		this.iineCount = iineCount;
	}
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum++;
	}
	
	// 리뷰 평점 ( 리뷰 점수 총합 / 리뷰어 수 )
	public void avgReview() { 
	    this.reviewTotal /= this.personNum;
	    System.out.printf("리뷰 평점 : %.1f\n", this.reviewTotal);
	}
	
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getReviewName() {
		return reviewName;
	}
	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}
		
	
	
	
}
