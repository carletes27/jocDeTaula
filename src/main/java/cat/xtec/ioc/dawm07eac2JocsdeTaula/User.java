
package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import javax.ejb.Stateful;

/**
 *
 * @author Joel Monné Mesalles
 */
@Stateful
public class User implements UserLocal {

    private String user;
    private String name;
    private String lastname;

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
}
