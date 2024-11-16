package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import javax.ejb.Local;

/**
 *
 * @author Joel Monné Mesalles
 */
@Local
public interface ValidateJocBeanLocal {
    public Boolean isValidFileImageName(String jocName, String fileImageName);
}
