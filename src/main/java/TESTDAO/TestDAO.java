package TESTDAO;

import TESTVO.TestVO;

import java.util.ArrayList;
import java.util.List;

public class TestDAO {
    private List<TestVO> testVOList = new ArrayList<>();

    public TestDAO() {

    }

    //체이닝을 위한 자신 리턴
    public TestDAO addTestVO(TestVO test){
        testVOList.add(test);
        return this;
    }
}
