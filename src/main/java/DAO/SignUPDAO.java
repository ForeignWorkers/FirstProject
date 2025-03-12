package DAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Frame.FrameBase;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Data.AppConstants;
import Data.GoogleDriveFileReader;
import Managers.DBDataManagers;
import Managers.DataManagers;
import VO.UserVO;

public class SignUPDAO {
	
	// ✅ ID 중복 확인
	public boolean isIDExists(String ID) {
		boolean isExist = false;
		for(UserVO von : DBDataManagers.getInstance().getDbUsersData())
		{
			if(von.getId().equals(ID))
				isExist = true;
		}
		return isExist;
	}
	
	// ✅ 닉네임 중복 확인
		public boolean isNicknameExists(String nickname) {
			boolean isExist = false;
			for(UserVO von : DBDataManagers.getInstance().getDbUsersData())
			{
				if(von.getNickName().equals(nickname))
					isExist = true;
			}
			return isExist;
		}
	
	// ✅ 회원 가입 (ID & 닉네임 저장 추가)
	public void registerUser(UserVO user) throws IOException {
		addUserToJson(user,AppConstants.USER_FILE_NAME, AppConstants.FOLDER_ID, false);
	}
	
	// ✅ 닉네임 수정
	public void updateUserNick(UserVO user, boolean isChangeNick) throws IOException {
		addUserToJson(user,AppConstants.USER_FILE_NAME, AppConstants.FOLDER_ID, isChangeNick);
	}
	
	
    // ✅ 로그인 기능 (ID와 비밀번호 검증 후 오류 메시지 리턴)
    public boolean loginUser(String ID, String password) {
        if (!isIDExists(ID)) {
    		System.out.println("존재 하지 않는 아이디입니다. 실패");
            return false;// ID가 없으면
        }

        for(UserVO vo : DBDataManagers.getInstance().getDbUsersData()) 
        {
        	if(vo.getId().equals(ID) && vo.getPassword().equals(password)) 
        	{
        		System.out.println("로그인 성공");
        		DataManagers.getInstance().setMyUser(vo);
        		return true;
        	}
        }
        
		System.out.println("로그인 실패 찾는 아이디나 비번이 없습니다.");
		return false;
    }
    
    private void addUserToJson(UserVO newUser, String fileName, String folderId, boolean isChangeNick) throws IOException {
    	TypeToken<List<UserVO>> typeToken = new TypeToken<>() {};
        List<UserVO> userList = GoogleDriveFileReader.getInstance().getListFromJson(fileName, folderId, typeToken);
        
        // 🆕 새 유저 추가
        System.out.println("BBB" +isChangeNick);
        if(isChangeNick) {
        	UserVO userVO = getExistUser(newUser, userList);
        	userVO.setNickName(newUser.getNickName());
        }
        else {
            userList.add(newUser);
        }
        
        // 📤 기존 데이터를 유지하면서 새로운 JSON 업로드
        String updatedJson = new GsonBuilder().setPrettyPrinting().create().toJson(userList);
        GoogleDriveFileReader.getInstance().uploadJsonWithLoading(fileName, updatedJson, folderId, FrameBase.getInstance());
        System.out.println("✅ 새로운 유저가 추가되었습니다: " + newUser.getId());

        //새로운 유저 로컬 디비 리스트에 추가
        DBDataManagers.getInstance().getDbUsersData().add(newUser);
    }
    
    private UserVO getExistUser(UserVO checkUser, List<UserVO> previousUser) {
    	for(UserVO vo : previousUser) 
    	{
    		if(vo.getId().equals(checkUser.getId())) return vo;
    	}
    	return null;
    }
}