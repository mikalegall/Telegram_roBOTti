package yoga.kulatantra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import yoga.kulatantra.Classes.UserIdAndIndex;

public class InitResponse {

	static String importPathForStringResponses = "import_init/responsesForString.txt";
	static String importPathForPhotoResponses = "import_init/responsesForPhoto.txt";
	static String importPathForVideoResponses = "import_init/responsesForVideo.txt";
	static String importPathForDocumentResponses = "import_init/responsesForDocument.txt";
	static String importPathForStickerResponses = "import_init/responsesForSticker.txt";

	static HashMap<Integer, UserIdAndIndex> importUserStatus(String serializePath) {

		// People who had use this Telegram Bot (Shala Relax) earlier will be listed
		// here with ongoing personal indices per category from pool of answers
		HashMap<Integer, UserIdAndIndex> statusForUser = new HashMap<>();

		ObjectMapper objectMapperToJSON = new ObjectMapper();
		List<UserIdAndIndex> listExistingUser = null;

		// Read from hard drive users who had play with this BOT (Shala Relax) before
		try {
			// https://www.baeldung.com/jackson-object-mapper-tutorial
			listExistingUser = objectMapperToJSON.readValue(new File(serializePath),
					new TypeReference<List<UserIdAndIndex>>() {
					});
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Known users will be placed into HashMap where userID is the key and
		// userObject is related value
		for (UserIdAndIndex user : listExistingUser) {

			statusForUser.put(user.getUserId(), user);
		}
		return statusForUser;

	}


	public static List<String> createRepliesForString() {

		return createReplies(importPathForStringResponses);
		
	}


	public static List<String> createRepliesForPhoto() {

		return createReplies(importPathForPhotoResponses);

	}


	public static List<String> createRepliesForVideo() {

		return createReplies(importPathForVideoResponses);
		
	}


	public static List<String> createRepliesForDocument() {

		return createReplies(importPathForDocumentResponses);

	}

	
	public static List<String> createRepliesForSticker() {

		return createReplies(importPathForStickerResponses);

	}

	
	
	
	public static List<String> createReplies(String importPath) {
		
		List<String> answers = new ArrayList<>();

		StringBuilder strb = new StringBuilder();

		try {
			FileReader readInitImport = new FileReader(importPath); // Lukee ääkköset
			BufferedReader buffer = new BufferedReader(readInitImport); // Lukee tavuittain char-merkkejä eli kaikki
																		// numerot tulkitaan stringiksi

			int readByte;

			while ((readByte = buffer.read()) != -1) {
				strb.append((char) readByte);
			}

			buffer.close();
			readInitImport.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] linesOnArray = strb.toString().split("VASTAUSVAIHTUU");
		for (String line : linesOnArray) {
			answers.add(line);
		}
		
		return answers;
	}
	
}

//try {
//FileInputStream readInitImport = new FileInputStream("responsesForString.txt"); // Lukee 8 bittiä heksadesimaalina eli vaikka valokuvia eli ongelmia ääkkösten kanssa
//// new FileInputStream("responsesForString.txt", Charset.forName("UTF-8"));
//DataInputStream kuorrutus = new DataInputStream(readInitImport);
//
//int readByte;
//
//while ((readByte = kuorrutus.read()) != -1) {
//	strBuilderForString.append((char) readByte);
//}
//
//kuorrutus.close();
//readInitImport.close();
//} catch (IOException e) {
//e.printStackTrace();
//}
