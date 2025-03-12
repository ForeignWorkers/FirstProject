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
		int count = 50; // 원하는 개수(반드시 썸네일 요구하는 데이터 이상으로 넣어줘야함)

		for (int i = 0; i < count; i++) { // 0부터 count까지 순차적으로 들어오기 때문에 중복 발생 불가
			
			if (i == 17) {
				continue; // id가 17번일 경우 추가하지 않고 건너뜀
			}
			
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
	public ItemVO getRandomContent(List<Integer> excludeIds) {
		// 제외되지 않은 리스트 생성
	    List<ItemVO> filteredList = new ArrayList<>();
	    for (ItemVO item : contentList) {
	        if (!excludeIds.contains(item.getId())) {
	            filteredList.add(item);
	        }
	    }

	    // 사용할 수 있는 게 없으면 null
	    if (filteredList.isEmpty()) {
	        return null;
	    }

	    // 랜덤 반환
	    Random random = new Random();
	    int index = random.nextInt(filteredList.size());
	    ItemVO selectedItem = filteredList.get(index);
	    contentList.remove(selectedItem); // 사용한 데이터 제거 (중복 방지)
	    return selectedItem;
	}
}
