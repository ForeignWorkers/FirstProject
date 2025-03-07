package TESTDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Managers.DataManagers;
import TESTVO.TestContentVO;
import VO.ItemVO;

public class TestContentDAO {
	// list 내 컨텐츠 정보 vo 할당
	List<ItemVO> contentList;

	// TestContentVO 새로운 객체 생성
	public TestContentDAO() {
		contentList = new ArrayList<ItemVO>();
		testContentData();
	}

	public void testContentData() {
		int count = 10;

		for (int i = 0; i < count; i++) {
			contentList.add(DataManagers.getInstance().FindItemFromId(i));
		}
    }


	// 전체 컨텐츠 목록 반환
	public List<ItemVO> getAllContents() {
		return contentList;
	}
	
	// 특정 OTT 필터링 메서드 추가
    public List<ItemVO> getFilteredContents(String ottPlatform) {
        // 전체 보기 옵션 처리
        if (ottPlatform.equals("전체")) {
            return contentList;
        }

        // 특정 OTT에 해당하는 콘텐츠 필터링
        List<ItemVO> filteredList = new ArrayList<>();
        for (ItemVO content : contentList) {
        }
        return filteredList;
    }
	
	// 랜덤 컨텐츠 가져오기
	public ItemVO getRandomContent() {
		Random random = new Random();
		return contentList.get(random.nextInt(contentList.size()));
	}
}
