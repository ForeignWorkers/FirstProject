package VO;

import Managers.DBDataManagers;

public class RatingVO {
	private int ItemId;
	private double ratingPoint;
	private int ratingCount;

	public void updateRating(int contentId, double score) {
	    // DB에서 contentId에 해당하는 RatingVO 객체를 가져옵니다.
	    RatingVO rating = DBDataManagers.getInstance().getRatingData(contentId);

	    if (rating == null) {
	        // 해당 contentId에 대한 RatingVO가 없다면 새로 생성
	        rating = new RatingVO(contentId, score);
	        // 새로 생성된 RatingVO를 DB에 추가
	        DBDataManagers.getInstance().addRatingData(rating);
	    } else {
	        // 해당 RatingVO가 이미 존재한다면 기존 정보를 기반으로 평점 업데이트
	        int currentCount = rating.getRatingCount();
	        double currentScore = rating.getRatingPoint();

	        // 새로운 평점을 평균으로 계산 >> 이 구역 평점 얘예요 << 
	        double newScore = (currentScore * currentCount + score) / (currentCount + 1);

	        // 업데이트된 평점과 카운트를 설정
	        rating.setRatingPoint(newScore);
	        rating.RatingCountUp(contentId); // RatingCountUp 메서드로 count 증가
	    }

	    // DB에 업데이트된 RatingVO 저장
	    DBDataManagers.getInstance().updateRatingData(rating);
	}
	
	public int getRatingCount() {
		return ratingCount;
	}

	public void RatingCountUp(int itemId) {
		ratingCount++;
	}
	
	public RatingVO(int itemId, double ratingPoint) {
		ItemId = itemId;
		this.ratingPoint = ratingPoint;
	}

	public int getItemId() {
		return ItemId;
	}

	public void setItemId(int itemId) {
		ItemId = itemId;
	}

	public double getRatingPoint() {
		return ratingPoint;
	}

	public void setRatingPoint(double ratingPoint) {
		this.ratingPoint = ratingPoint;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}
}
