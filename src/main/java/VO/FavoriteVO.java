package VO;

import java.util.ArrayList;
import java.util.List;

public class FavoriteVO {
	private String userId;
	private List<Integer> myFavoriteList = new ArrayList<>();
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Integer> getMyFavoriteList() {
		return myFavoriteList;
	}
	public void setMyFavoriteList(List<Integer> myFavoriteList) {
		this.myFavoriteList = myFavoriteList;
	}
}
