package Managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginConfrimManager {
  
	//text 파일 넣기
	private static final String fileName = "src/main/java/TESTVO/accounts.txt"; // 외부에서 넣을 파일
	
	//로그인 확인 매서드
    public static boolean loginConfrim(String userId, String passWord) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            //라인을 읽었을 때 TEXT가 없을때까지 실행
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                //id와 pw가 있으면 실행 
                if (data.length == 2) {
                    String storedId = data[0];
                    String storedPw = data[1];
                    // 저장되어 있는 ID,PW가 입력된 ID,PW와 같을 경우 성공 출력 
                    if (storedId.equals(userId) && storedPw.equals(passWord)) {
                        System.out.println("자동 로그인 성공");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
       
        }
        //ID,PW text를 찾지 못했을때
        System.out.println("비회원 접속");
        return false;
    }
    
/*
    public static void main(String[] args) {
        // 테스트용 실행
    	loginConfrim("ABC", "123");
    }
*/
}