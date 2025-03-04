package DAO;

import java.util.HashMap;
import java.util.HashSet;

public class SignUPDAO {
	private static HashSet<String> storedIDs = new HashSet<>();
	private static HashSet<String> storedNicknames = new HashSet<>();
	private static HashMap<String,String> userPasswords = new HashMap<>();
	
	// ✅ ID 중복 확인
	public static boolean isIDExists(String ID) {
		return storedIDs.contains(ID);
	}

	
	// ✅ 닉네임 중복 확인
		public static boolean isNicknameExists(String nickname) {
			return storedNicknames.contains(nickname);
		}
		
		
	// ✅ ID 저장
	public static boolean registerID(String ID) {
		if (isIDExists(ID)) {
			return false; // 이미 존재하는 ID
		}
		storedIDs.add(ID);
		return true; // 저장 성공
	}

	

	// ✅ 닉네임 저장
	public static boolean registerNickname(String nickname) {
		if (isNicknameExists(nickname)) {
			return false; // 이미 존재하는 닉네임
		}
		storedNicknames.add(nickname);
		return true; // 저장 성공
	}
	
	// ✅ 비밀번호 저장
	public static void storePassword(String ID, String password) {
        userPasswords.put(ID, password);
    }
	
	// ✅ 회원 가입 (ID & 닉네임 저장 추가)
	public static void registerUser(String ID, String password, String email, String mynum, String mynum7,
			String phoneNum, String nickname, String nationality) {
		// ID와 닉네임 저장
		registerID(ID);
		registerNickname(nickname);
		storePassword(ID, password); // 비밀번호 저장
		// 실제로 데이터를 저장하는 기능 (DB 사용 시 여기에 추가)
		System.out.println("회원가입 완료: " + ID + ", " + nickname);
	}
	
    // ✅ 로그인 기능 (ID와 비밀번호 검증 후 오류 메시지 리턴)
    public static String loginUser(String ID, String password) {
        if (!isIDExists(ID)) {
            return "잘못된 아이디를 입력하셨습니다."; // ID가 없으면
        }

        String findPw = userPasswords.get(ID);
        if (findPw != null && findPw.equals(password)) {
            return "로그인 성공"; // 비밀번호 일치 시 로그인 성공
        } else {
            return "잘못된 비밀번호를 입력하셨습니다."; // 비밀번호 틀리면
        }
    }
	  /*public static boolean loginUser(String ID, String password) {
		  boolean idchecking = false;
		  boolean pwchecking = false;
		  
	        for(String a : storedIDs) 
	        {
	        	a.equals(ID);
	        	idchecking = true;
	        }
	        
	        String findPw = userPasswords.get(ID);

	        if(!findPw.isEmpty()) 
	        {	       
	        	pwchecking = findPw.equals(password);
	        }
	        
	        System.out.println("B : " + pwchecking);
	        return idchecking && pwchecking;
	    }*/
}