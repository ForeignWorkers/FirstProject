package TESTDAO;

import java.util.ArrayList;
import java.util.List;

import TESTVO.TestContentDetailPanelVO;

public class TestContentDetailPanelDAO {

	private List<TestContentDetailPanelVO> detailDatas = new ArrayList<>();
	
	//작품 생성 메서드(리스트에 추가하는)
	public void addDetailDatas(TestContentDetailPanelVO vo)	{
		detailDatas.add(vo);
	}
	
	//패널에 생성하는 메서드.(검증)
	public TestContentDetailPanelVO getItemUseID(int id) {
		for(var item : detailDatas)
		{
			if(item.getId() == id) {
				return item;
			};
		}
		
		return null;
	}
}//class
