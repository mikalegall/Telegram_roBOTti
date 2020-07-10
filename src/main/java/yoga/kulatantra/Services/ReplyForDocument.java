package yoga.kulatantra.Services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.ObjectMapper;

import yoga.kulatantra.InitResponse;
import yoga.kulatantra.Classes.UserIdAndIndex;

/**
 * @author Lare
 *
 */
public class ReplyForDocument {

	static List<String> responseForDocument = InitResponse.createRepliesForDocument();


	public static String replyForDocument(HashMap<Integer, UserIdAndIndex> userStatus, Update update) {

		// Users display name on smartphone
		String firstName = update.getMessage().getFrom().getFirstName();
		int userId = update.getMessage().getFrom().getId();

		String serializePath = "exported_users/listOfUsers.json";
		
		UserIdAndIndex currentUser = userStatus.get(userId);

		int indexForDocument = currentUser.getDocumentIndex();
		String replieForDocument;

		if (indexForDocument < responseForDocument.size()) {

			replieForDocument = responseForDocument.get(indexForDocument).replace("TELEGRAM_FirstName", firstName);

			indexForDocument++;
			currentUser.setDocumentIndex(indexForDocument);

		} else {

			indexForDocument = 0;

			replieForDocument = responseForDocument.get(indexForDocument).replace("TELEGRAM_FirstName", firstName);

			indexForDocument++;
			currentUser.setDocumentIndex(indexForDocument);

		}

		// Serialize and continue from the backup snapshot where user was last time visited us (Shala Relax)
		try {
			// https://fasterxml.github.io/jackson-databind/javadoc/2.7/com/fasterxml/jackson/databind/ObjectMapper.html#writeValue(java.io.File,%20java.lang.Object)
			ObjectMapper objectMapperToJSON = new ObjectMapper();
			objectMapperToJSON.writeValue(new File(serializePath), userStatus.values());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return replieForDocument;

	}

}
