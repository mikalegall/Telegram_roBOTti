package yoga.kulatantra.Classes;

import lombok.Data;

/**
 * @author Lare
 *
 */
// https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/
@Data
public class UserIdAndIndex {
	private int userId;
	private String firstName;
	private int stringIndex;
	private int photoIndex;
	private int groupPhotoIndexViaAzureAndGoogle;
	private int videoIndex;
	private int documentIndex;
	private int stickerIndex;

	@Override
    public String toString() {
        return "\r\n" +
        		userId + ", (userId)"
        		+ "\r\n" + firstName + ", (firstName)"
        		+ "\r\n" + stringIndex + ", (stringIndex)"
        		+ "\r\n" + groupPhotoIndexViaAzureAndGoogle + ", (groupPhotoIndexViaAzureAndGoogle)"
        		+ "\r\n" + photoIndex + ", (photoIndex)"
        		+ "\r\n" + videoIndex + ", (videoIndex)"
        		+ "\r\n" + documentIndex +", (documentIndex)"
        		+ "\r\n" + stickerIndex + ", (stickerIndex)";
        }

	// Equals vertailee olioiden tallentamaa sisältöä eli onko sama käyttäjä kyseessä (sama ID)
		// https://www.ibm.com/developerworks/library/j-jtp05273/
	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject)
		    return true;
		
		if (!(otherObject instanceof UserIdAndIndex))
		    return false;
		
		if (otherObject == null || getClass() != otherObject.getClass())
			return false;
		
		UserIdAndIndex otherObjectUserIdAndIndex = (UserIdAndIndex) otherObject;

//		// Stringille toimisi equals
//		if (!userId.equals(otherObjectUserIdAndIndex.userId))
//			return false;

		if (userId != otherObjectUserIdAndIndex.userId)
			return false;
				
		return
				(userId == otherObjectUserIdAndIndex.userId)
			      && ((firstName == null) 
			          ? otherObjectUserIdAndIndex.firstName == null 
			          : firstName.equals(otherObjectUserIdAndIndex.firstName));
		}

	// HashCode laskee parametrille numerollisen tunnisteen joka on aina sama esim. "Lare" nimelle
	@Override
	public int hashCode() { 
		  int hash = 1;
		  hash = hash * 31 + firstName.hashCode();
		  hash = hash * 31 
		              + (firstName == null ? 0 : firstName.hashCode());
		  return hash;
		}
}