package DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Managers.DataManagers;
import VO.ItemVO;

public class ContentDAO {
	
	// list 내 컨텐츠 정보 vo 할당
	List<ItemVO> contentList;

	// ContentVO 새로운 객체 생성
	public ContentDAO() {
		contentList = new ArrayList<ItemVO>();
		ContentData();
	}

	// 0부터 count개수만큼 순차적으로 가져오는 메소드
	public void ContentData() {
		int count = 49; // 원하는 개수(반드시 썸네일 요구하는 데이터 이상으로 넣어줘야함)

		for (int i = 0; i < count; i++) { // 0부터 count까지 순차적으로 들어오기 때문에 중복 발생 불가

			ItemVO item = DataManagers.getInstance().FindItemFromId(i);

			if (item != null) { // 데이터가 존재할 때만 추가
				contentList.add(item);
			}
		}
	}

	// 전체 컨텐츠 목록 반환(중복 제거된 데이터)
	public List<ItemVO> getAllContents() {
		return contentList;
	}

	// 랜덤 컨텐츠 반환
	public ItemVO getRandomContent() {
		if (contentList.isEmpty()) {
			return null; // 남은 컨텐츠 없을 경우 null 반환
		}
		Random random = new Random();
		int index = random.nextInt(contentList.size());
		return contentList.remove(index); // 뽑은 컨텐츠 삭제 후 반환 다시 뽑힐 수 없게 함
	}
}
