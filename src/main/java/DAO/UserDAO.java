package DAO;

import VO.UserVO;

import java.util.ArrayList;

public class UserDAO {
    private ArrayList<UserVO> users = new ArrayList<>();

    //생성자 진행 시 유저 데이터 추가
    public UserDAO(){

    }

    // ✅ ID 중복 확인
    public boolean isIDExists(String ID) {
        for (UserVO user : users) {
            return user.getId().equals(ID);
        }
        return false;
    }

    // ✅ ID 저장
    public boolean registerID(String ID) {
        //아이디 저장은 최종적으로 유저가 등록되면 클래스로 넣을 예정
        return false;
    }

    // ✅ 닉네임 중복 확인
    public boolean isNicknameExists(String nickname) {
        for (UserVO user : users) {
            return user.getNickName().equals(nickname);
        }
        return false;
    }

    // ✅ 닉네임 저장
    public boolean registerNickname(String nickname) {
        //닉네임 저장은 아이디 저장이랑 통합 가능할듯
        return false;
    }

    // ✅ 회원 가입 (ID & 닉네임 저장 추가)
    public void registerUser(String ID, String password, String email, String mynum, String mynum7,
                                    String phoneNum, String nickname, String nationality) {

        // 실제로 데이터를 저장하는 기능 (DB 사용 시 여기에 추가)
        // ID와 닉네임 저장 TO DO -> 최종 저장으로 변경 해야함
        // registerID(ID);
        // registerNickname(nickname);
    }
}
