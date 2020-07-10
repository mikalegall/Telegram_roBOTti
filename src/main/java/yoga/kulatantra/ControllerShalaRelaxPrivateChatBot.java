package yoga.kulatantra;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.databind.ObjectMapper;

import yoga.kulatantra.Classes.UserIdAndIndex;
import yoga.kulatantra.Services.ReplyForDocument;
import yoga.kulatantra.Services.ReplyForPhoto;
import yoga.kulatantra.Services.ReplyForSticker;
import yoga.kulatantra.Services.ReplyForString;
import yoga.kulatantra.Services.ReplyForVideo;

/**
 * @author Lare
 *
 */
public class ControllerShalaRelaxPrivateChatBot extends TelegramLongPollingBot {
//	private static final Logger LOGGER = LogManager.getLogger(ControllerShalaRelaxBot.class);

//	StringBuilder serializePath = new StringBuilder("exported_users/" + userId + ".json");
	String serializePath = "exported_users/listOfUsers.json";
	String publicGroupConversationPath = "logs/public_group_conversation.log";
	String privateConversationPath = "logs/private_conversation.log";

	HashMap<Integer, UserIdAndIndex> userStatus = InitResponse.importUserStatus(serializePath);

	@Override
	public void onUpdateReceived(Update update) {
//		LOGGER.info(update.toString());

		User temVariableforRunaways = update.getMessage().getLeftChatMember();
		
		// Save conversation (&& dont save if it is just information that someone has left the group)
		if (update.getMessage().getChat().getTitle() != null && temVariableforRunaways == null) {
			// This is public group conversation
			Boolean bot = false; // Message is from human user
			writeConversationLog(update.toString(), publicGroupConversationPath, bot); 
		} else {
			// This is private conversation (when title is null)
			Boolean bot = false; // Message is from human user
			writeConversationLog(update.toString(), privateConversationPath, bot);
		}

		String replieForString;
		String replieForPhoto;
		String replieForVideo;
		String replieForDocument;
		String replieForSticker;

		SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields

		int userId = update.getMessage().getFrom().getId();
		// Unknown users will be created and serialised out into hard drive
		if (!userStatus.containsKey(userId)) {

			UserIdAndIndex newUser = new UserIdAndIndex();
			newUser.setUserId(userId);
			newUser.setFirstName(update.getMessage().getFrom().getFirstName());
			newUser.setStringIndex(0);
			newUser.setPhotoIndex(0);
			newUser.setVideoIndex(0);
			newUser.setDocumentIndex(0);
			newUser.setStickerIndex(0);
			userStatus.put(newUser.getUserId(), newUser);
			try {
				// https://fasterxml.github.io/jackson-databind/javadoc/2.7/com/fasterxml/jackson/databind/ObjectMapper.html#writeValue(java.io.File,%20java.lang.Object)
				ObjectMapper objectMapperToJSON = new ObjectMapper();
				objectMapperToJSON.writeValue(new File(serializePath.toString()), userStatus.values());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}




		// In this case we dont want to reply publicly on Telegram group (only private chats)
		// 'title='null' means that this is private chat
		if (update.getMessage().getChat().getTitle() == null) {

			// We check if the update has a message and the message has PHOTO
			if (update.hasMessage() && update.getMessage().hasPhoto()) {

				// The service which will be called should handle request and reply with
				// appropriate response for TelegramServerAPI
				replieForPhoto = ReplyForPhoto.replyForPhoto(userStatus, update);

				message.setChatId(update.getMessage().getChatId()).setText(replieForPhoto);

				Boolean bot = true; // Message is from Bot
				writeConversationLog(message.toString(), privateConversationPath, bot);

				try {
					execute(message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}



		// In this case we dont want to reply publicly on Telegram group (only private chats)
		// 'title='null' means that this is private chat
		if (update.getMessage().getChat().getTitle() == null) {

			// We check if the update has a message and the message has VIDEO
			if (update.hasMessage() && update.getMessage().hasVideo()) {

				// The service which will be called should handle request and reply with
				// appropriate response for TelegramServerAPI
				replieForVideo = ReplyForVideo.replyForVideo(userStatus, update);

				message.setChatId(update.getMessage().getChatId()).setText(replieForVideo);

				Boolean bot = true; // Message is from Bot
				writeConversationLog(message.toString(), privateConversationPath, bot);

				try {
					execute(message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}

		}



		// In this case we dont want to reply publicly on Telegram group (only private chats)
		// 'title='null' means that this is private chat
		if (update.getMessage().getChat().getTitle() == null) {

			// We check if the update has a message and the message has DOCUMENT
			if (update.hasMessage() && update.getMessage().hasDocument()) {

				// The service which will be called should handle request and reply with
				// appropriate response for TelegramServerAPI
				replieForDocument = ReplyForDocument.replyForDocument(userStatus, update);

				message.setChatId(update.getMessage().getChatId()).setText(replieForDocument);

				Boolean bot = true; // Message is from Bot
				writeConversationLog(message.toString(), privateConversationPath, bot);

				try {
					execute(message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}



		// In this case we dont want to reply publicly on Telegram group (only private chats)
		// 'title='null' means that this is private chat
		if (update.getMessage().getChat().getTitle() == null) {

			// We check if the update has a message and the message has STICKER
			if (update.hasMessage() && update.getMessage().hasSticker()) {

				// The service which will be called should handle request and reply with
				// appropriate response for TelegramServerAPI
				replieForSticker = ReplyForSticker.replyForSticker(userStatus, update);

				message.setChatId(update.getMessage().getChatId()).setText(replieForSticker);

				Boolean bot = true; // Message is from Bot
				writeConversationLog(message.toString(), privateConversationPath, bot);

				try {
					execute(message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}



		// In this case we dont want to reply publicly on Telegram group (only private chats)
		// 'title='null' means that this is private chat
		if (update.getMessage().getChat().getTitle() == null) {

			// We check if the update has a message and the message has TEXT
			if (update.hasMessage() && update.getMessage().hasText()) {

				String tempVariable = update.getMessage().getText();
				// If this is the very first opening message for discussion then session starts
				// like this
				if (tempVariable.equals("/start")) {

					// Users display name on smartphone
					String firstName = update.getMessage().getFrom().getFirstName();

					StringBuilder formAnswer = new StringBuilder(
							"Oon muuten tosi huonon nettiyhteyden päässä ja 24/7 robottina univajetta on päässyt pikkasen kertymään, joten sori jo etukäteen siitä, että mun vastaukset saattaa paikoitellen vaikuttaa vähän epäjohdonmukaisilta (olenhan vasta beta-versio botista ja jatkuvassa kehityksessä).\r\n\r\n"
									+ "Nyt voisit " + firstName
									+ " kuitenkin kuvata minulle yhdellä sanalla sitä, mikä kuvastaa tätä kuluvaa hetkeä mielestäsi osuvimmin juuri nyt 🕉");

					replieForString = formAnswer.toString();

				} else {

					// The service which will be called should handle request and reply with
					// appropriate response for TelegramServerAPI
					replieForString = ReplyForString.replyForString(userStatus, update);
				}

				message.setChatId(update.getMessage().getChatId()).setText(replieForString);

				Boolean bot = true; // Message is from Bot
				writeConversationLog(message.toString(), privateConversationPath, bot);

				try {
					execute(message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}

	}



	public void writeConversationLog(String message, String path, Boolean bothOrNot) {
		try {
			// 'Append' = , true
			FileWriter uusi = new FileWriter(path, true);
			BufferedWriter conversation = new BufferedWriter(uusi);

			// If Bot = true
			if (bothOrNot) {
				conversation.write(new GregorianCalendar().getTime().toString() + "\r\nBot reply" + "\r\n" + message
						+ "\r\n\r\n\r\n");
			} else {
				// If human user = true (bot = false)
				conversation.write(new GregorianCalendar().getTime().toString() + "\r\n" + message + "\r\n\r\n");
			}
			conversation.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	// t.me/ShalaRelaxBot
	@Override
	public String getBotUsername() {
		return "@ShalaRelaxPrivateChatBot";
	}

	@Override
	public String getBotToken() {
		return System.getenv().get("PRIVATE_CHAT_BOT_TOKEN");
	}

}
//FileOutputStream ulosmeno = new FileOutputStream(serializePath.toString());
//ObjectOutputStream paketointi = new ObjectOutputStream(ulosmeno);
//paketointi.writeObject(userStatus.get(userId).toString());
//paketointi.close();
//ulosmeno.close();

//// DATALLE
//try {
//	FileInputStream sisaantulo = new FileInputStream("responsesForString.txt"); // Lukee 8 bittiä heksadesimaalina eli vaikka valokuvia eli ongelmia ääkkösten kanssa
//	// new FileInputStream("responsesForString.txt", Charset.forName("UTF-8"));
//    DataInputStream kuorrutus = new DataInputStream(sisaantulo);
//
//    int luettuTavu;
//	
//    while ((luettuTavu = kuorrutus.read()) != -1) {
//		strBuilderForString.append((char) luettuTavu);
//	}
//	
//    kuorrutus.close();
//	sisaantulo.close();
//} catch (IOException e) {
//	e.printStackTrace();
//}
