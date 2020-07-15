package yoga.kulatantra.Services;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageTag;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;

// https://docs.microsoft.com/fi-fi/azure/cognitive-services/computer-vision/quickstarts-sdk/client-library?pivots=programming-language-java
public class AzurePhotoComputerVision {
	private static final Logger log = Logger.getLogger(AzurePhotoComputerVision.class);

	public static List<String> sendPhotoToAzure(String subscriptionKey, String endpoint, String pathToImage) {

		ComputerVisionClient compVisClient = ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);

		// Recognize printed text with OCR for a local and remote (URL) image
		// RecognizeTextOCRLocal(compVisClient);

		// Analyze local and remote images
		return AnalyzeImage(compVisClient, pathToImage);

	}

	public static List<String> AnalyzeImage(ComputerVisionClient compVisClient, String pathToImage) {

		// This list defines the features to be extracted from the image.
		List<VisualFeatureTypes> featuresToExtractFromImage = new ArrayList<>();
//	    featuresToExtractFromImage.add(VisualFeatureTypes.DESCRIPTION);
//	    featuresToExtractFromImage.add(VisualFeatureTypes.CATEGORIES);
		featuresToExtractFromImage.add(VisualFeatureTypes.TAGS);
//	    featuresToExtractFromImage.add(VisualFeatureTypes.FACES);
//	    featuresToExtractFromImage.add(VisualFeatureTypes.ADULT);
		featuresToExtractFromImage.add(VisualFeatureTypes.COLOR);
//	    featuresToExtractFromImage.add(VisualFeatureTypes.IMAGE_TYPE);

//	    
//*********************************  IF LOCAL IMAGE THEN USE THIS *********************************
//	    // Need a byte array for analyzing a local image.
//	    File rawImage = new File(pathToImage);
//	    byte[] imageByteArray = null;
//		try {
//			imageByteArray = Files.readAllBytes(rawImage.toPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	    // Call the Computer Vision service and tell it to analyze the loaded image.
//	    ImageAnalysis analysis = null;
//	    try {
//	    analysis = compVisClient.computerVision().analyzeImageInStream().withImage(imageByteArray)
//	            .withVisualFeatures(featuresToExtractFromImage).execute();
//	    } catch (Exception e) {
//	    	// FIXME Catch exception for too large image and let the enduser know that
//	    	e.printStackTrace();
//	    }
//*********************************  *********************************
//

		// Call the Computer Vision service and tell it to analyze the loaded image.
		List<String> tagNoun = new ArrayList<>();
		ImageAnalysis analysis = null;
		boolean code400flag = false;
		try {
			analysis = compVisClient.computerVision().analyzeImage().withUrl(pathToImage)
					.withVisualFeatures(featuresToExtractFromImage).execute();
		} catch (Exception e) {
			tagNoun.add("\r\nAzurePhotoComputerVision exception:\r\n" + e.toString());
			code400flag = true;
		}

		// If Telegram's URLpath to fetch a file is broken again don't send anything to
		// Google translator
		if (code400flag) {
			return tagNoun;
		}

		if (analysis != null) {
			int i = 0;
			// Add image tags 2-4 to list which will be send to Google translator
			for (ImageTag tag : analysis.tags()) {
				if (i > 0 && i < 4) {
					tagNoun.add(tag.name());
				}
				i++;
			}
		}
		log.debug("\r\n\r\n\r\n************************************************************************************************** EKA AzurePhotoComputerVision AnalyzeImage() tagNoun = "
				+ "\r\n\r\n"
				+ tagNoun
				+ "\r\n\r\n\r\n"
				+ "**************************************************************************************************\r\n\r\n\r\n");


		// Substantiivin vois ehkä käännättää suomeksi ilmaiseksikin ennen kuin lähettää
		// sen takaisin ihmiskäyttäjälle
		// https://rapidapi.com/googlecloud/api/google-translate1/pricing
//	    OkHttpClient client = new OkHttpClient();
//	    
////	    https://translate.google.fi/?hl=fi&op=translate&sl=en&tl=fi&text=
////	    	https://www.labnol.org/code/19909-google-translate-api
//	    String apumuuttuja = "source=en&q="
//	    		+ tag.name().toString()
//	    		+ "&target=fi";
//
//	    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//	    RequestBody body = RequestBody.create(mediaType, apumuuttuja);
//	    Request request = new Request.Builder()
//	    		.url("https://google-translate1.p.rapidapi.com/language/translate/v2")
//	    		.post(body)
//	    		.addHeader("x-rapidapi-host", "google-translate1.p.rapidapi.com")
//	    		.addHeader("x-rapidapi-key", System.getenv("RAPID_API_KEY"))
//	    		.addHeader("accept-encoding", "application/gzip")
//	    		.addHeader("content-type", "application/x-www-form-urlencoded")
//	    		.build();
//
//	    Response response = null;
//		try {
//			response = client.newCall(request).execute();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		List<String> tagNounTranslatedAndAccentColor = new ArrayList<>();
		try {
			tagNounTranslatedAndAccentColor = GoogleTranslateText.googleTranslateText(tagNoun);
		} catch (IOException e) {
			log.debug("\r\n\r\n\r\n************************************************************************************************** IOException e = "
					+ "\r\n\r\n"
					+ e
					+ "\r\n\r\n\r\n"
					+ "**************************************************************************************************\r\n\r\n\r\n");

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (analysis != null && analysis.color().accentColor() != null) {
			tagNounTranslatedAndAccentColor.add(analysis.color().accentColor());
		}
		
		log.debug("VIIDES AzurePhotoComputerVision AnalyzeImage() AZURE ja GOOGLE PALAUTTAA NIITÄ KUTSUNEELLE METODILLE tagNounTranslatedAndAccentColor.toString() = "
				+ "\r\n"
				+ tagNounTranslatedAndAccentColor.toString()
				+ "\r\n\r\n\r\n"
				+ "**************************************************************************************************\r\n\r\n\r\n");

		return tagNounTranslatedAndAccentColor;

	}
}
