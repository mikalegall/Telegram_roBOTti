package yoga.kulatantra.Services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

//import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.ObjectMapper;

import yoga.kulatantra.InitResponse;
import yoga.kulatantra.Classes.UserIdAndIndex;

/**
 * @author Lare
 *
 */
public class ReplyForPhoto {
//	private static final Logger log = Logger.getLogger(ReplyForPhoto.class);
	
    // Enviroment variables for Heroku to communicate with Azure
	static String subscriptionKey = System.getenv().get("COMPUTER_VISION_SUBSCRIPTION_KEY");
    static String endpoint = System.getenv().get("COMPUTER_VISION_ENDPOINT");
	
    static List<String> responseForPhoto = InitResponse.createRepliesForPhoto();
    static List<String> responseForGroupPhotoIndexViaAzureAndGoogle = InitResponse.createRepliesGroupPhotoIndexViaAzureAndGoogle();

	
	// This one is for group chats so it will send a photo to Azure AI
	public static String replyForPhoto(HashMap<Integer, UserIdAndIndex> userStatus, Update update, String uploadedFilePathURL) {

		String replieForPhoto = "";
		
		// Users display name on smartphone
		String firstName = update.getMessage().getFrom().getFirstName();


			// We want to reply via Azure AT only on Telegram public group 
			// 'title='null' means that this is private chat
			if (update.getMessage().getChat().getTitle() != null) {
							
				String pathToImage = "https://api.telegram.org/file/bot"
				+ System.getenv().get("GROUP_CHAT_BOT_TOKEN")
				+ "/"
				+ uploadedFilePathURL;
		

					String noun1;
					String noun2 = "";
					String noun3 = "";
					String accentColor;
					List<String> tagNounTranslatedAndAccentColor = AzurePhotoComputerVision.sendPhotoToAzure(subscriptionKey, endpoint, pathToImage);

					// If response has only one element then it means that error has occurred
					if (tagNounTranslatedAndAccentColor.size() == 1) {
						replieForPhoto = responseForGroupPhotoIndexViaAzureAndGoogle.get(0).replace("TELEGRAM_FirstName", firstName)
								+ "\r\n\r\n"
								+ tagNounTranslatedAndAccentColor.get(0);
						return replieForPhoto;
					} else {
						noun1 = tagNounTranslatedAndAccentColor.get(0);
						
						if (tagNounTranslatedAndAccentColor.size() >= 3) {
							noun2 = tagNounTranslatedAndAccentColor.get(1);
						}
							if (tagNounTranslatedAndAccentColor.size() == 4) {
								noun3 = tagNounTranslatedAndAccentColor.get(2);
							}

						accentColor = tagNounTranslatedAndAccentColor.get(tagNounTranslatedAndAccentColor.size()-1);
					}

				// TODO Map accent color hexadecimal to string
					
				int userId = update.getMessage().getFrom().getId();
		
				String serializePath = "exported_users/listOfUsers.json";
				
				UserIdAndIndex currentUser = userStatus.get(userId);
		
				int indexForGroupPhotosViaAzureAndGoogle  = currentUser.getGroupPhotoIndexViaAzureAndGoogle();
						if (indexForGroupPhotosViaAzureAndGoogle < responseForGroupPhotoIndexViaAzureAndGoogle.size()) {
							
							replieForPhoto = responseForGroupPhotoIndexViaAzureAndGoogle.get(indexForGroupPhotosViaAzureAndGoogle)
									.replace("TELEGRAM_FirstName", firstName)
									.replace("AZURE_Subs1", noun1)
									.replace("AZURE_Subs2", noun2)
									.replace("AZURE_Subs3", noun3)
									.replace("AZURE_AccentColor", accentColor);
				
							indexForGroupPhotosViaAzureAndGoogle++;
							currentUser.setGroupPhotoIndexViaAzureAndGoogle(indexForGroupPhotosViaAzureAndGoogle);
						} else {
				
							indexForGroupPhotosViaAzureAndGoogle = 1; // 0 has been reserved for Azure error message
				
							replieForPhoto = responseForGroupPhotoIndexViaAzureAndGoogle.get(indexForGroupPhotosViaAzureAndGoogle)
									.replace("TELEGRAM_FirstName", firstName)
									.replace("AZURE_Subs1", noun1)
									.replace("AZURE_Subs2", noun2)
									.replace("AZURE_Subs3", noun3)
									.replace("AZURE_AccentColor", accentColor);
				
							indexForGroupPhotosViaAzureAndGoogle++;
							currentUser.setGroupPhotoIndexViaAzureAndGoogle(indexForGroupPhotosViaAzureAndGoogle);
						}
						
					writeUserStatusIntoHardDrive(userStatus, serializePath);
				}
		
		return replieForPhoto;
	
	}
		
		
		
		// This one is for private chats
		public static String replyForPhoto(HashMap<Integer, UserIdAndIndex> userStatus, Update update) {

		String replieForPhoto;
		
		// Users display name on smartphone
		String firstName = update.getMessage().getFrom().getFirstName();
			
		int userId = update.getMessage().getFrom().getId();

		String serializePath = "exported_users/listOfUsers.json";
		
		UserIdAndIndex currentUser = userStatus.get(userId);

				int indexForPhoto = currentUser.getPhotoIndex();
				if (indexForPhoto < responseForPhoto.size()) {
		
					replieForPhoto = responseForPhoto.get(indexForPhoto).replace("TELEGRAM_FirstName", firstName);
		
					indexForPhoto++;
					currentUser.setPhotoIndex(indexForPhoto);
		
				} else {
		
					indexForPhoto = 0;
		
					replieForPhoto = responseForPhoto.get(indexForPhoto).replace("TELEGRAM_FirstName", firstName);
		
					indexForPhoto++;
					currentUser.setPhotoIndex(indexForPhoto);
		
				}
	
				writeUserStatusIntoHardDrive(userStatus, serializePath);
				
		return replieForPhoto;

	}
	
		public static void writeUserStatusIntoHardDrive(HashMap<Integer, UserIdAndIndex> userStatus, String serializePath) {
			
			// Serialize and continue from the backup snapshot where user was last time visited us (Shala Relax)
			try {
				// https://fasterxml.github.io/jackson-databind/javadoc/2.7/com/fasterxml/jackson/databind/ObjectMapper.html#writeValue(java.io.File,%20java.lang.Object)
				ObjectMapper objectMapperToJSON = new ObjectMapper();
				objectMapperToJSON.writeValue(new File(serializePath), userStatus.values());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

}