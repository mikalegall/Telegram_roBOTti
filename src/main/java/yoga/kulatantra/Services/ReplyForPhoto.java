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
public class ReplyForPhoto {

	static List<String> responseForPhoto = InitResponse.createRepliesForPhoto();


	public static String replyForPhoto(HashMap<Integer, UserIdAndIndex> userStatus, Update update) {

		// Users display name on smartphone
		String firstName = update.getMessage().getFrom().getFirstName();
		int userId = update.getMessage().getFrom().getId();

		String serializePath = "exported_users/listOfUsers.json";
		
		UserIdAndIndex currentUser = userStatus.get(userId);

		int indexForPhoto = currentUser.getPhotoIndex();
		String replieForPhoto;

		if (indexForPhoto < responseForPhoto.size()) {

			replieForPhoto = responseForPhoto.get(indexForPhoto).replace("TELEGRAM_FirstName", firstName);

			indexForPhoto++;
			currentUser.setPhotoIndex(indexForPhoto);

		} else {

			indexForPhoto = 0;

			replieForPhoto = responseForPhoto.get(indexForPhoto).replace("TELEGRAM_FirstName", firstName);

			indexForPhoto++;
			currentUser.setPhotoIndex(indexForPhoto);

		}

		// Serialize and continue from the backup snapshot where user was last time visited us (Shala Relax)
		try {
			// https://fasterxml.github.io/jackson-databind/javadoc/2.7/com/fasterxml/jackson/databind/ObjectMapper.html#writeValue(java.io.File,%20java.lang.Object)
			ObjectMapper objectMapperToJSON = new ObjectMapper();
			objectMapperToJSON.writeValue(new File(serializePath), userStatus.values());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return replieForPhoto;

	}

}
