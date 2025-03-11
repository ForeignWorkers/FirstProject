package DAO;

import java.io.IOException;
import java.util.List;

import Managers.DataManagers;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Data.GoogleDriveFileReader;
import Managers.DBDataManagers;
import VO.FavoriteVO;
import VO.UserVO;

public class FavoriteDAO {
	
    public void addFavoriteToJson(FavoriteVO favoriteData, String fileName, String folderId) throws IOException {
        TypeToken<List<FavoriteVO>> typeToken = new TypeToken<>() {};
        List<FavoriteVO> favoriteDatas = GoogleDriveFileReader.getInstance().getListFromJson(fileName, folderId, typeToken);
        
        // 🆕 새 유저 추가
        favoriteDatas.add(favoriteData);
        
        // 📤 기존 데이터를 유지하면서 새로운 JSON 업로드
        String updatedJson = new GsonBuilder().setPrettyPrinting().create().toJson(favoriteDatas);
        GoogleDriveFileReader.getInstance().uploadJson(fileName, updatedJson, folderId);
        System.out.println("✅ 찜리스트 추가 되었습니다.: " + favoriteData.getUserId());
    }
    
    
    public void setLocalFavoriteData(FavoriteVO myFavoriteVO, int currentContentId) {
		if(DBDataManagers.getInstance().getDbFavoriteData().contains(myFavoriteVO))
    	{
            FavoriteVO tempVO = new FavoriteVO();
            for(FavoriteVO vo : DBDataManagers.getInstance().getDbFavoriteData())
            {
                if(vo == myFavoriteVO){
                    tempVO = vo;
                }
            }

            if(tempVO != null){
                DBDataManagers.getInstance().getDbFavoriteData().remove(tempVO);
                DBDataManagers.getInstance().getDbFavoriteData().add(myFavoriteVO);
            }
    	}
    	else {
    		//추가 되어야함
            DBDataManagers.getInstance().getDbFavoriteData().add(myFavoriteVO);
    	}
    }
	
}
