package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Joel Monné Mesalles
 */
@Local
public interface ValoracioLocal {
    public List<Joc> getJocsValorats();
    public void setJocsValorats(List<Joc> jocsValorats);
}
