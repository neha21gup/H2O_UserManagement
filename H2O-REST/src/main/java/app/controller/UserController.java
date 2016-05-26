package app.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import app.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public Map<String, Object> createUser(@RequestBody Map<String, Object> userMap) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateString = userMap.get("dob").toString();
		Date date = null;
		try {
			date = sdf.parse(dateString);
			System.out.println(date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = new User(userMap.get("firstName").toString(), userMap.get("lastName").toString(),
				userMap.get("email").toString(), userMap.get("contact").toString(),
				Boolean.parseBoolean(userMap.get("isMale").toString()), date);

		userRepository.save(user);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("message", "User created successfully");
		response.put("User", user);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public User getUserDetails(@PathVariable("userId") String userId) {
		return userRepository.findOne(userId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public List<User> getAllUsersDetails() {
		return userRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/update")
	public Map<String, Object> updateUser(@RequestBody User user) {
		User user1 = userRepository.findOne(user.getId());
		user1 = user;
		userRepository.save(user1);
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("message", "User updated successfully");
		response.put("User", user1);
		return response;
	}

	// rest api for birthday sms
	/*
	 * get all users iterate over list and check if dob is equal to current
	 * system's date. If yes, trigger sms api
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/birthdayUsers")
	public Map<String, Object> checkBirthdayUsers() {
		List<User> userList = userRepository.findAll();
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		int currDate = cal.get(Calendar.DAY_OF_MONTH);
		int currMonth = cal.get(Calendar.MONTH);
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		ArrayList<User> birthdayList = new ArrayList<User>();
		for (User eachUser : userList) {

			if (eachUser.getDob() != null) {
				
				if (eachUser.getDob().getDate() == currDate && eachUser.getDob().getMonth() == currMonth) {
					// trigger sms api
					System.out.println(eachUser.getDob().getDate() + "  " + eachUser.getDob().getMonth());
					birthdayList.add(eachUser);

				}
			}
		}
		response.put("message", "Today's Birthday");
		response.put("User", birthdayList);
		
		for(User user : birthdayList){
			sendMsg(user);
		}

		return response;
	}
	
	public void sendMsg(User user){
		
		
		
		String url = "http://smsapi.24x7sms.com/api_2.0/SendSMS.aspx?APIKEY=IPuSuNWCi8n&MobileNo=91" +user.getContact()+ "&SenderID=SMSMsg&Message=Hi,HappyBirthday!!&ServiceName=PROMOTIONAL_HIGH";
		
		URL obj;
		try {
			obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		String responseError = con.getResponseMessage();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode + "Response Error" + responseError);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}