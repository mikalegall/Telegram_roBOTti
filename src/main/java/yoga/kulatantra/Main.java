package yoga.kulatantra;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// Heroku Procfile
// --> worker: java $JAVA_OPTS -jar target/myTargetJar-SNAPSHOT.jar
// https://stackoverflow.com/a/47997801/8639325

// heroku logs --tail --app stagingshalarelax
// heroku run bash --app stagingshalarelax
public class Main {

	public static void main(String[] args) {
		
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