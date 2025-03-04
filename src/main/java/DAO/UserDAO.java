package DAO;

import Data.GoogleDriveFileReader;
import Managers.DBDataManagers;
import VO.UserVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private ArrayList<UserVO> users = new ArrayList<>();


    // JSON 변환을 위한 메서드
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static UserVO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserVO.class);
    }

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

    public void addUserToJson(UserVO newUser, String fileName, String folderId) throws IOException {
        TypeToken<List<UserVO>> typeToken = new TypeToken<>() {};
        List<UserVO> userList = GoogleDriveFileReader.getInstance().getListFromJson(fileName, folderId, typeToken);

        // 🆕 새 유저 추가
        userList.add(newUser);

        // 📤 기존 데이터를 유지하면서 새로운 JSON 업로드
        String updatedJson = new GsonBuilder().setPrettyPrinting().create().toJson(userList);
        GoogleDriveFileReader.getInstance().uploadJson(fileName, updatedJson, folderId);

        System.out.println("✅ 새로운 유저가 추가되었습니다: " + newUser.getId());

        //새로운 유저 로컬 디비 리스트에 추가
        DBDataManagers.getInstance().getDbUsersData().add(newUser);
    }
}
