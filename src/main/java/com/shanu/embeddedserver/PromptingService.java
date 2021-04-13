package com.shanu.embeddedserver;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PromptingService {
	static JSONObject promptJson = new JSONObject();

	public static void main(String[] args) throws JSONException {
		
		JSONObject jsonparams = new JSONObject();
		jsonparams.put("lsSCountry", "India");
		jsonparams.put("lsMCountry", "India;China;Australia");
		setpromptInfo(jsonparams);
		System.out.println(promptJson.toString());

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static void setpromptInfo(JSONObject json) throws JSONException {
		

		// String[] promptArray = new String[];
		List<JSONObject> promptArray = new ArrayList<JSONObject>();
		// promptArray.add(e)

		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			// String val = null;
			if (key.indexOf("lsI") != -1) {
				// Parsing keys for a BW prompt
				String promptName = key.substring(3, key.length());
				// check if prompt already exists in the prompt array
				int isPromptAlreadyParsed = 0;
				int a = 0;
				for (a = 0; a < promptArray.size(); a++) {
					try {
						if (java.net.URLDecoder
								.decode(((JSONObject) (promptArray.get(a))).getString("promptName"), "UTF-8")
								.equals(java.net.URLDecoder.decode(key.substring(3, key.length()), "UTF-8"))) {
							isPromptAlreadyParsed = 1;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (isPromptAlreadyParsed != 1) {
					// getting the prompt values and creating the object
					// before setting the keys if prompt has not been parsed
					// already i.e. the lsI parameter is before the main
					// prompt parameter
					String promptValues = null;
					String key1 = "";
					while (keys.hasNext()) {
						key1 = keys.next();
						if (key.indexOf(key1.substring(3)) != -1) {
							promptValues = json.getString(key1);
							break;
						}
					}
					try {
						promptArray.add(makePromptJsonObject(key1, promptValues));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (promptArray.size() > 0) {
					String keysString;
					try {
						keysString = URLDecoder.decode(json.getString(key), "UTF-8");

						// setting prompt keys
						String[] values;
						JSONObject promptObject = (JSONObject) promptArray.get(a);
						if (promptObject.getString("operator").equals("BETWEEN")) {
							keysString = keysString.substring(1, keysString.length() - 1);
							values = keysString.split("\\.\\.");
						} else {
							values = URLDecoder.decode(json.getString(key), "UTF-8").split(";");
						}
						ArrayList<String> listVal = new ArrayList<String>();
						for (int i = 0; i < values.length; i++) {
							listVal.add(values[i]);
						}
						promptObject.put("keys", listVal);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					promptArray.add(makePromptJsonObject(key, json.getString(key)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// reversing the prompt array to maintain the correct order

		if (promptArray.size() > 0) {
			if ((promptArray.get(0).get("promptName").toString().contains("(End)"))) {
				Collections.reverse(promptArray);
			}
		}
		// checking if there are between prompts
		// making the JSON syntax same for all range prompts
		String startString = "(Start)";
		String endString = "(End)";
		for (int x = 0; x < promptArray.size(); x++) {
			JSONObject startPromptObject = promptArray.get(x);
			if (startPromptObject.getString("promptName").indexOf("(Start)") != -1) {
				for (int y = x + 1; y < promptArray.size(); y++) {
					JSONObject endPromptObject = promptArray.get(y);
					// checking if a corresponding (End) prompt exists for
					// the above (Start) prompt.
					if (startPromptObject.getString("promptName")
							.substring(0, startPromptObject.getString("promptName").length() - startString.length())
							.equals(endPromptObject.getString("promptName").substring(0,
									endPromptObject.getString("promptName").length() - endString.length()))
							&& endPromptObject.getString("promptName")
									.substring(
											endPromptObject.getString("promptName").length() - endString.length(),
											endPromptObject.getString("promptName").length())
									.equals("(End)")) {
						startPromptObject.put("promptName", startPromptObject.getString("promptName").substring(0,
								startPromptObject.getString("promptName").length() - startString.length()));// removing
																											// (START)
																											// keyword
																											// from
																											// the
																											// prompt
																											// name
						startPromptObject.put("operator", "BETWEEN"); // changing
																		// prompt
																		// operator
																		// to
																		// between
						JSONArray valueList = new JSONArray();
						JSONArray valueList2 = new JSONArray();
						valueList = startPromptObject.getJSONArray("values");
						valueList2 = endPromptObject.getJSONArray("values");
						for (int aa = 0; aa < valueList2.length(); aa++) {// merging
																			// both
																			// the
																			// values
																			// from
																			// (Start)
																			// and
																			// (End)
																			// prompts
							valueList.put(valueList2.get(aa));
						}
						promptArray.remove(y);// removing the (End) prompt.
						break;
					}
				}
			}
		}
		
		
		if (promptArray != null) {
			//tempJson.put(promptArrayString, promptArray);
			for(JSONObject temp : promptArray){
				promptJson.append("PromptArray", temp);
		}
		}
		
	
		
	}

	public static JSONObject makePromptJsonObject(String a, String b) throws Exception {
		String promptName = URLDecoder.decode(a.substring(3, a.length()), "UTF-8");
		String[] values;
		String oprName = a.substring(0, 3);
		String op = getOperatorName(oprName);
		if (!oprName.equals("lsR")) {
			values = (URLDecoder.decode(b, "UTF-8")).split(";");
			// handling the scenario when a single prompt has multiple
			// values given in URL. We will take only the last value entered
			// for that prompt.
			if (oprName.equals("lsS") && values.length > 1)
				values = new String[] { values[values.length - 1] };
		} else {
			String data = URLDecoder.decode(b, "UTF-8");
			data = data.substring(1, data.length() - 1);
			values = data.split("\\.\\.");
		}

		ArrayList<String> listVal = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			listVal.add(values[i]);
		}
		JSONObject obj1 = new JSONObject();
		obj1.put("promptName", promptName);
		obj1.put("operator", op);
		obj1.put("values", listVal);
		obj1.put("keys", "");

		return obj1;
	}
	private static String getOperatorName(String opr) {
		if (opr.equals("lsS") || opr.equals("lsM"))
			return "EQUAL";
		else if (opr.equals("lsR"))
			return "BETWEEN";
		return "";
	}

}
