package yoga.kulatantra;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

	public static void main(String[] args) {
			
		// Heroku: Procfile
		// web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar
		// System.getenv().get("PORT");

		
		// Example taken from https://github.com/rubenlagus/TelegramBotsExample
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new ControllerShalaRelaxPrivateChatBot());
			
			telegramBotsApi.registerBot(new ControllerShalaRelaxPublicGroupBot());
			// newChatMembers=null, 
			// newChatMembers=User{id=123, firstName='Lare',

			// leftChatMember=null, 
			//leftChatMember=User{id=123, firstName='Lare',

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}