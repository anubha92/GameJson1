package com.game.main;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.game.bean.ContestantBean;
import com.game.helper.GameHelper;
import com.game.url.URLConnectionReader;

public class GameMainProgram {
	public static final String  LIST_CUPS_URL=" http://play.eslgaming.com/api/leagues?types=cup&states=finished&limit.total=25&path=%2Fplay%2Fworldoftanks%2Feurope%2F";
	
	public void gameAnalyzer()throws Exception{
		//Step 1)Read all game id's from this URL
		StringBuilder jsonListCups=URLConnectionReader.getURLConnectionData(GameMainProgram.LIST_CUPS_URL);
		
		//Then convert the jsonString to JSON object
		JSONObject jsonObj=new JSONObject(jsonListCups.toString().trim());
		
		//Pass the jsonObject to GameHelper then read game id's and add all those id's in list object.
		GameHelper gameHelper=new GameHelper();
		List<String> gameIdList=gameHelper.getGameIdsList(jsonObj);
		
		
		//getTheContest HashMap
		Map<String,ContestantBean>  contestantHashMap=gameHelper.getContestantHashMap(gameIdList);
		
		//JSONObject Response
		JSONObject resJSON=gameHelper.getJSONObjectResponse(contestantHashMap);
		
		System.out.println(resJSON.toString());
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		GameMainProgram gameMainProgram=new GameMainProgram();
		gameMainProgram.gameAnalyzer();
	}

}
