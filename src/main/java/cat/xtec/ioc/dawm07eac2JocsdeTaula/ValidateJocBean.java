package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.util.regex.Pattern;
import javax.ejb.Stateless;

/**
 *
 * @author Joel Monné Mesalles
 */
@Stateless
public class ValidateJocBean implements ValidateJocBeanLocal {

    @Override
    public Boolean isValidFileImageName(String jocName, String fileImageName) {
        Boolean toReturn = false;
        String[] fileImageNameArray = fileImageName.split(Pattern.quote("."));
        if(fileImageNameArray[0].equals(jocName)){
            toReturn = true;
        }
        return toReturn;
    }

}
