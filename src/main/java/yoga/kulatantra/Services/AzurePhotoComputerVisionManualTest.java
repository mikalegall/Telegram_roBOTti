package yoga.kulatantra.Services;

import java.io.File;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageTag;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;

public class AzurePhotoComputerVisionManualTest {
	private static final Logger log = Logger.getLogger(AzurePhotoComputerVisionManualTest.class);
	static String subscriptionKey = System.getenv().get("COMPUTER_VISION_SUBSCRIPTION_KEY");
    static String endpoint = System.getenv().get("COMPUTER_VISION_ENDPOINT");
	
    
    public static List<String> sendPhotoManualyToAzure() {

		ComputerVisionClient compVisClient = ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);

		// Analyze local and remote images
		return AnalyzeImage(compVisClient);

	}

	public static List<String> AnalyzeImage(ComputerVisionClient compVisClient) {

		List<VisualFeatureTypes> featuresToExtractFromImage = new ArrayList<>();
		featuresToExtractFromImage.add(VisualFeatureTypes.TAGS);
		featuresToExtractFromImage.add(VisualFeatureTypes.COLOR);

//	    
//*********************************  IF LOCAL IMAGE THEN USE THIS *********************************
//	    // Need a byte array for analyzing a local image.
	    File rawImage = new File("import_init/test_image_for_debugging.jpg");
	    byte[] imageByteArray = null;
		try {
			imageByteArray = Files.readAllBytes(rawImage.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // Call the Computer Vision service and tell it to analyze the loaded image.
	    List<String> tagNoun = new ArrayList<>();
		ImageAnalysis analysis = null;
	    try {
	    analysis = compVisClient.computerVision().analyzeImageInStream().withImage(imageByteArray)
	            .withVisualFeatures(featuresToExtractFromImage).execute();
	    } catch (Exception e) {
			tagNoun.add("\r\nAzurePhotoComputerVision exception:\r\n" + e.toString());
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
		List<String> tagNounTranslatedAndAccentColor = new ArrayList<>();
		if (analysis != null) {
				try {
					tagNounTranslatedAndAccentColor = GoogleTranslateText.googleTranslateText(tagNoun);
				} catch (IOException e) {
					log.debug("\r\n\r\n\r\n************************************************************************************************** IOException e = "
							+ "\r\n\r\n"
							+ e
							+ "\r\n\r\n\r\n"
							+ "**************************************************************************************************\r\n\r\n\r\n");
					e.printStackTrace();
				}
				if (analysis != null && analysis.color().accentColor() != null) {
					tagNounTranslatedAndAccentColor.add(analysis.color().accentColor());
				}
		}

		log.debug("VIIDES AzurePhotoComputerVision AnalyzeImage() AZURE ja GOOGLE PALAUTTAA NIITÄ KUTSUNEELLE METODILLE tagNounTranslatedAndAccentColor.toString() = "
				+ "\r\n"
				+ tagNounTranslatedAndAccentColor.toString()
				+ "\r\n\r\n\r\n"
				+ "**************************************************************************************************\r\n\r\n\r\n");

		return tagNounTranslatedAndAccentColor;
		
	}
}
