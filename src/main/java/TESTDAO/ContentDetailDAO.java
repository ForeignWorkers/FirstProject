package TESTDAO;

import TESTVO.TestContentDetailPanelVO;

public interface ContentDetailDAO {
    TestContentDetailPanelVO getContentDetail(int id);  // 콘텐츠 상세 정보를 조회
}