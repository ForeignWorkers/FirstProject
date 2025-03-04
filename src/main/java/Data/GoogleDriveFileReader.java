package Data;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GoogleDriveFileReader {
    private static GoogleDriveFileReader driveFileReader;
    private static Drive service;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //싱글턴 생성 로직
    public static GoogleDriveFileReader getInstance() {
        if (driveFileReader == null && service == null) {
            driveFileReader = new GoogleDriveFileReader();

            try {
                service = GoogleDriveService.getDriveService();
            }
            catch (Exception e) {
                System.out.println("GoogleDriveService.getDriveService를 실패하였습니다.");
            }
        }
        return driveFileReader;
    }

    public void uploadJson(String fileName, String jsonData, String folderId) throws IOException {
        String fileId = findFileId(fileName, folderId);
        ByteArrayContent content = new ByteArrayContent("application/json", jsonData.getBytes());

        //파일 공유 받기 - 메일추가히믄 됩니다.
        shareFileWithMyGoogleAccount(fileId,"minquu@gmail.com");

        if (fileId == null) {
            // 📤 새 파일 업로드 (폴더에 저장)
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setMimeType("application/json");

            // ✅ 특정 폴더에 저장
            if (folderId != null && !folderId.isEmpty()) {
                fileMetadata.setParents(Collections.singletonList(folderId));
            } else {
                System.out.println("⚠️ 폴더 ID가 제공되지 않음! 루트 디렉토리에 저장됨.");
            }

            service.files().create(fileMetadata, content)
                    .setFields("id, parents")
                    .execute();
            System.out.println("📂 JSON 파일이 폴더(" + folderId + ")에 업로드됨: " + fileName);
        } else {
            // 🔄 기존 JSON 데이터 유지하면서 업데이트
            service.files().update(fileId, null, content)
                    .setFields("id")
                    .execute();
            System.out.println("✅ JSON 파일이 업데이트됨: " + fileName);
        }
    }

    public <T> List<T> getListFromJson(String fileName, String folderId, TypeToken<List<T>> typeToken) throws IOException {
        String fileId = GoogleDriveFileReader.getInstance().findFileId(fileName, folderId);
        List<T> list = new ArrayList<>();

        if (fileId != null) {
            String existingJson = GoogleDriveFileReader.getInstance().downloadJson(fileName, folderId);
            if (existingJson != null && !existingJson.isEmpty()) {
                try {
                    JsonElement jsonElement = JsonParser.parseString(existingJson);
                    if (jsonElement.isJsonArray()) {
                        // 🎯 이미 리스트라면 그대로 변환
                        list = gson.fromJson(existingJson, typeToken.getType());
                    } else if (jsonElement.isJsonObject()) {
                        // 🎯 객체라면 리스트로 변환 후 저장
                        T singleObject = (T) gson.fromJson(existingJson, typeToken.getRawType());
                        list.add(singleObject);
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("🚨 JSON 파싱 오류: " + e.getMessage());
                }
            }
        }

        return list;
    }

    private String findFileId(String fileName, String folderId) throws IOException {
        String query = "name = '" + fileName + "' and mimeType = 'application/json'";

        // 특정 폴더 내에서만 검색
        if (folderId != null) {
            query += " and '" + folderId + "' in parents";
        }

        FileList result = service.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        return (files.isEmpty()) ? null : files.get(0).getId();
    }

    // 📌 Google Drive에서 JSON 파일 다운로드
    private String downloadJson(String fileName, String folderID) throws IOException {
        String fileId = findFileId(fileName,folderID);

        // 만약 파일이 없으면 .txt 확장자로 검색
        if (fileId == null && fileName.endsWith(".json")) {
            fileId = findFileId(fileName.replace(".json", ".txt"),folderID);
        }

        if (fileId == null) {
            System.out.println("파일을 찾을 수 없습니다: " + fileName);
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        return outputStream.toString();
    }

    public void shareFileWithMyGoogleAccount(String fileId, String myEmail) throws IOException {
        Permission permission = new Permission()
                .setType("user")
                .setRole("writer")  // "reader"로 하면 읽기 전용
                .setEmailAddress(myEmail);

        service.permissions().create(fileId, permission)
                .setFields("id")
                .execute();

        System.out.println("✅ 파일이 " + myEmail + " 계정에 공유됨.");
    }
}