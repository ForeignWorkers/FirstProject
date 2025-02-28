package DAO;

import java.util.HashSet;

public class SignUPDAO {
	private static HashSet<String> storedIDs = new HashSet<>();
	private static HashSet<String> storedNicknames = new HashSet<>();

	// ✅ ID 중복 확인
	public static boolean isIDExists(String ID) {
		return storedIDs.contains(ID);
	}

	// ✅ ID 저장
	public static boolean registerID(String ID) {
		if (isIDExists(ID)) {
			return false; // 이미 존재하는 ID
		}
		storedIDs.add(ID);
		return true; // 저장 성공
	}

	// ✅ 닉네임 중복 확인
	public static boolean isNicknameExists(String nickname) {
		return storedNicknames.contains(nickname);
	}

	// ✅ 닉네임 저장
	public static boolean registerNickname(String nickname) {
		if (isNicknameExists(nickname)) {
			return false; // 이미 존재하는 닉네임
		}
		storedNicknames.add(nickname);
		return true; // 저장 성공
	}

	// ✅ 회원 가입 (ID & 닉네임 저장 추가)
	public static void registerUser(String ID, String password, String email, String mynum, String mynum7,
			String phoneNum, String nickname, String nationality) {
		// ID와 닉네임 저장
		registerID(ID);
		registerNickname(nickname);

		// 실제로 데이터를 저장하는 기능 (DB 사용 시 여기에 추가)
		System.out.println("회원가입 완료: " + ID + ", " + nickname);
	}
}