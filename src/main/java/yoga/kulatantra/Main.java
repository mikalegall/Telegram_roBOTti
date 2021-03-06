package yoga.kulatantra;

// import yoga.kulatantra.Services.AzurePhotoComputerVisionManualTest;
import org.apache.log4j.BasicConfigurator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// Travis https://docs.travis-ci.com/user/deployment/heroku/

// Heroku Procfile
// --> worker: java $JAVA_OPTS -jar target/*.jar
// https://stackoverflow.com/a/47997801/8639325

// heroku logs --app stagingshalarelax --source app --tail
// heroku run bash --app stagingshalarelax

//https://devcenter.heroku.com/articles/dynos#cli-commands-for-dyno-management
//heroku ps --app stagingshalarelax
public class Main {

	public static void main(String[] args) {

//		https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
		BasicConfigurator.configure();

		// Example taken from https://github.com/rubenlagus/TelegramBotsExample
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new ControllerShalaRelaxPrivateChatBot());
			telegramBotsApi.registerBot(new ControllerShalaRelaxPublicGroupBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

//		AzurePhotoComputerVisionManualTest.sendPhotoManualyToAzure();
	}

}