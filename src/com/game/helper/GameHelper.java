package com.game.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.game.bean.ContestantBean;
import com.game.url.URLConnectionReader;

public class GameHelper {
	public static final String ROUTE_GET_CUPS_URL="http://play.eslgaming.com/api/leagues/:id/ranking?limit=25";
	
	public List<String> getGameIdsList(JSONObject jsonGameObj){
		List<String> gameIds=new ArrayList<String>();
		Iterator<String> keys=jsonGameObj.keys();
		while (keys.hasNext()) {
			gameIds.add(keys.next());
			//System.out.println("Key::"+key);
			//System.out.println("Val::"+jsonObj.getString(key));
		}
		return gameIds;
	}
	
	
	public Map<String,ContestantBean> getContestantHashMap(List<String> gameIdList)throws Exception{
		Map<String,ContestantBean> contestantHashMap=new HashMap<String,ContestantBean>();
		for(String gameId:gameIdList){
			//Step 2) Prepare the URL as per gameId then reading Ranking jsonObj 
			String prepareGetCupURL=ROUTE_GET_CUPS_URL.replace(":id", gameId);
			StringBuilder rankingJsonStr=URLConnectionReader.getURLConnectionData(prepareGetCupURL);
			//Ranking JsonObject
			JSONObject rankingJsonObj=new JSONObject(rankingJsonStr.toString().trim());
			//Ranking JsonArray from RankingJsonObject
			JSONArray rankingArray=rankingJsonObj.getJSONArray("ranking");
			
			//rankingArray Iteration
			for(int i = 0; i < rankingArray.length(); i++){
			      JSONObject rankingJsonObject = rankingArray.getJSONObject(i);
			      //Read position from RankingObj
			      String position=rankingJsonObject.getString("position");
			      //System.out.println(position);
			      
			      if(position != null && ! position.trim().equals("")){
			    	  int intPosition=Integer.parseInt(position);
			    	  //Take the TeamJson from Ranking
			    	  JSONObject teamJsonObj = rankingJsonObject.getJSONObject("team");
				      //If contestBean is exists then
				      ContestantBean contestantBean=contestantHashMap.get(teamJsonObj.getString("id"));
				      if(contestantBean != null){
				    	  contestantBean.setCupsPlayed(contestantBean.getCupsPlayed()+1);
				    	  if(intPosition<contestantBean.getBestPosition()){
				    		  contestantBean.setBestPosition(intPosition);
				    	  }else if(intPosition>contestantBean.getWorstPosition()){
				    		  contestantBean.setWorstPosition(intPosition);
				    	  }
				      }
				      //If n't exists
				      else{
				    	  contestantBean=new ContestantBean();
				    	  contestantBean.setCupsPlayed(1);
				    	  contestantBean.setBestPosition(intPosition);
				    	  contestantBean.setWorstPosition(intPosition);
				      }
				      //put contestBean in HashMap
				      contestantHashMap.put(teamJsonObj.getString("id"),contestantBean);
			      }
			}
		}
		return contestantHashMap;
	}
	
	public JSONObject getJSONObjectResponse(Map<String, ContestantBean> contestantTreeMap)throws Exception{
		//JSONObject Response
		JSONObject resJSON=new JSONObject();
		//Iterate the hashMap as per keySet & set inside list.
		Set<String> keysCxtId=contestantTreeMap.keySet();
		Iterator<String> keySetIterator = keysCxtId.iterator();
		while(keySetIterator.hasNext()){
			String key = keySetIterator.next();
			JSONObject contHshMp=new JSONObject(contestantTreeMap.get(key));
			resJSON.put(key, contHshMp);
		}
		return resJSON;
	}
}
