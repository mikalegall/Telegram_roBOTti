/**
 * 
 */
package yoga.kulatantra;
//import org.apache.log4j.Logger;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import yoga.kulatantra.Services.ReplyForPhoto;

/**
 * @author Lare
 *
 */
public class ControllerShalaRelaxPublicGroupBot extends TelegramLongPollingBot {
//	private static final Logger log = Logger.getLogger(ControllerShalaRelaxPublicGroupBot.class);
	
	String welcomeText = "Tässä ryhmässä on Shala Relax puolesta mukana"
			+ "\r\n"
			+ "Avustaja (chat robotti),"
			+ "\r\n"
			+ "Lare (joogahieroja ja asanaohjaaja) sekä"
			+ "\r\n"
			+ "Opas (chat robotti) eli minä."
			+ "\r\n\r\n"
			+ "Vaikka Avustajalla on admin-oikeudet, niin työnjaollisesti on sovittu, että vain minä osallistun täällä keskusteluun, ja silloinkin vain satunnaisesti kommentoin, mutta en vastaa yksityisviesteihin."
			+ "\r\n"
			+ "Ja Avustaja taasen vastaa vain yksityisviesteihin."
			+ "\r\n"
			+ "Ton ihmisen rooli (Lare) on meille kaikille Boteille vielä hieman epäselvä 🤔";
	
	String goodbye = "No ny se TELEGRAM_FirstName sit läks, teinx mä jotain väärää!?!";
	
	String publicGroupConversationPath = "logs/public_group_conversation.log";

	
	String serializePath = "exported_users/listOfUsers.json";

//	HashMap<Integer, UserIdAndIndex> userStatus = ControllerShalaRelaxPrivateChatBot.getUserStatus();
	
	@Override
	public void onUpdateReceived(Update update) {
//		log.debug("Public group chat update-message = " + update.toString());
		String replieForPhoto;
		
		// Users display name on smartphone
		String firstName = update.getMessage().getFrom().getFirstName();

		SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields

		List<User> temVariableforNewbies = update.getMessage().getNewChatMembers();
		User temVariableforRunaways = update.getMessage().getLeftChatMember();

		
		// This is public group conversation
		if (temVariableforNewbies.size() != 0) {
			// Someone new has joined this group
			message.setChatId(update.getMessage().getChatId()).setText(welcomeText);

			try {
				execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		
		
		// This is public group conversation
		if (temVariableforRunaways != null) {
			// Someone has left this group
			message.setChatId(update.getMessage().getChatId())
					.setText(goodbye.replace("TELEGRAM_FirstName", firstName));

			Boolean bot = true; // Message is from Bot
			writeConversationLog(message.toString(), publicGroupConversationPath, bot);

			try {
				execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}		



		// In this case want to reply publicly on Telegram group chat
		// 'title='null' means that it would be a private chat so this is public group chat
		if (update.getMessage().getChat().getTitle() != null) {
			

				// We check if the update has a message and the message has PHOTO
				if (update.hasMessage() && update.getMessage().hasPhoto()) {
		
					// Photo is just a file so we need ID for that file to get it's location path
					// and after that we can send it to Azure AI
					String fileId = update.getMessage().getPhoto().get(0).getFileId();
					String uploadedFilePathURL = "";
					try {
						GetFile getUploadedFile = new GetFile();
						getUploadedFile.setFileId(fileId);
						uploadedFilePathURL = execute(getUploadedFile).getFilePath();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					// The service which will be called should handle request and reply with
					// appropriate response for TelegramServerAPI
					replieForPhoto = ReplyForPhoto.replyForPhoto(ControllerShalaRelaxPrivateChatBot.getUserStatus(), update, uploadedFilePathURL);
		
					message.setChatId(update.getMessage().getChatId()).setText(replieForPhoto);
		
					Boolean bot = true; // Message is from Bot
					writeConversationLog(message.toString(), publicGroupConversationPath, bot);
		
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
	
	// t.me/ShalaRelaxGroupBot
	@Override
	public String getBotUsername() {
		return "@ShalaRelaxGroupChatBot";
	}

	@Override
	public String getBotToken() {
		return System.getenv().get("GROUP_CHAT_BOT_TOKEN");
	}

}
