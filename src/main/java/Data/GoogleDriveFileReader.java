package Data;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class GoogleDriveFileReader {
    public static void main(String[] args) {
        try {
            Drive service = GoogleDriveService.getDriveService();
            String fileName = "data.txt"; // 읽고 싶은 파일 이름

            // 특정 파일 찾기
            File file = findFile(service, fileName);
            if (file != null) {
                System.out.println("파일 찾음: " + file.getName());

                // 파일 내용 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        service.files().get(file.getId()).executeMediaAsInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
            } else {
                System.out.println("파일을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File findFile(Drive service, String fileName) throws IOException {
        FileList result = service.files().list()
                .setQ("name = '" + fileName + "'")
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        return files.isEmpty() ? null : files.get(0);
    }
}
