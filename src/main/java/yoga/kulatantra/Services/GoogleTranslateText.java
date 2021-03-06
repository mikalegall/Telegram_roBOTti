package yoga.kulatantra.Services;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;

// https://cloud.google.com/translate/docs/advanced/translating-text-v3#translating_text
public class GoogleTranslateText {
	private static final Logger log = Logger.getLogger(GoogleTranslateText.class);

	public static List<String> googleTranslateText(List<String> tagNoun) throws IOException {

		
		String projectId = System.getenv("GOOGLE_PROJECT_ID");
		// Supported Languages: https://cloud.google.com/translate/docs/languages
		String targetLanguage = "fi";

		// We have picked up only image tags 2-4 from Azure AI
		List<String> tagNounTranslatedPassingThrue = translateText(projectId, targetLanguage, tagNoun);
		
		return tagNounTranslatedPassingThrue;
		
	}


	// Translating Text
	public static List<String> translateText(String projectId, String targetLanguage, List<String> tagNoun)
			throws IOException {

		List<String> tagNounTranslated = new ArrayList<>();

		for (String textToTranslate : tagNoun) {

			// Initialize client that will be used to send requests. This client only needs
			// to be created
			// once, and can be reused for multiple requests. After completing all of your
			// requests, call
			// the "close" method on the client to safely clean up any remaining background
			// resources.
			try (TranslationServiceClient client = TranslationServiceClient.create()) {
				// Supported Locations: `global`, [glossary location], or [model location]
				// Glossaries must be hosted in `us-central1`
				// Custom Models must use the same location as your model. (us-central1)
				LocationName parent = LocationName.of(projectId, "global");

				// Supported Mime Types:
				// https://cloud.google.com/translate/docs/supported-formats
				TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString())
						.setMimeType("text/plain").setTargetLanguageCode(targetLanguage).addContents(textToTranslate).build();

				TranslateTextResponse response = client.translateText(request);
				log.debug("KOLMAS GoogleTranslateText googleTranslateText() SUOMENNETUN YKSITTÄISEN KÄÄNNÖKSEN SISÄLTÖ TranslateTextResponse response.getTranslationsList() = "
						+ "\r\n"
						+ response.getTranslationsList()
						+ "\r\n\r\n\r\n"
						+ "**************************************************************************************************\r\n\r\n\r\n");
				// Display the translation for each input text provided
				for (Translation translation : response.getTranslationsList()) {
					tagNounTranslated.add(translation.getTranslatedText());
				}
				client.close();
			}
		}
		log.debug("NELJÄS GoogleTranslateText googleTranslateText() SUOMENNETUN KOKO LISTAN SISÄLTÖ tagNounTranslated.toString() = "
				+ "\r\n"
				+ tagNounTranslated.toString()
				+ "\r\n\r\n\r\n"
				+ "**************************************************************************************************\r\n\r\n\r\n");

		return tagNounTranslated;
	}
}