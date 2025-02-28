package TESTDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import TESTVO.TestContentVO;

public class TestContentDAO {
	//list 내 컨텐츠 정보 vo 할당
	List<TestContentVO> contentList;
	
	//TestContentVO 새로운 객체 생성
	public TestContentDAO() {
	  contentList = new ArrayList<TestContentVO>();
	  testContentData(); 
	}

	// 컨텐츠 데이터 저장
	public void testContentData() {
		contentList.add(new TestContentVO("라라랜드", 9.0, true, "src/main/java/TESTVO/라라랜드.png"));
		contentList.add(new TestContentVO("당신거기있어줄래요", 9.9, true,"src/main/java/TESTVO/당신거기있어줄래요.png"));
		contentList.add(new TestContentVO("겨울왕국", 10.0, true, "src/main/java/TESTVO/겨울왕국.png"));
		contentList.add(new TestContentVO("부산행", 6.0, true, "src/main/java/TESTVO/부산행.png"));
		contentList.add(new TestContentVO("스파이더맨", 8.0, true, "src/main/java/TESTVO/스파이더맨.png"));
		contentList.add(new TestContentVO("어벤져스", 7.9, true, "src/main/java/TESTVO/어벤져스.png"));
		contentList.add(new TestContentVO("인사이드아웃", 8.0, true, "src/main/java/TESTVO/인사이드아웃.png"));	
	}
	
	//전체 컨텐츠 목록 반환
	 public List<TestContentVO> getAllContents() {
	        return contentList;
	    }
	 
	 //랜덤 컨텐츠 가져오기
	 public TestContentVO getRandomContent() {
	        Random random = new Random();
	        return contentList.get(random.nextInt(contentList.size()));
	    }
	
}
