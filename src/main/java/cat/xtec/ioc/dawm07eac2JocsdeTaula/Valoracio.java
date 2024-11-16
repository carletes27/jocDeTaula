package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author Joel Monné Mesalles
 */
@Stateful
public class Valoracio implements ValoracioLocal {
    private List<Joc> jocsValorats;

    @Override
    public List<Joc> getJocsValorats() {
        return jocsValorats;
    }

    @Override
    public void setJocsValorats(List<Joc> jocsValorats) {
        this.jocsValorats = jocsValorats;
    }
}
