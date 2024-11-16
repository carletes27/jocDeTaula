/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author Joel Monné Mesalles
 */
@Local
public interface UserLocal {
    @NotNull @Size(min=1, max=10, message = "Codi d'usuari no valid. Ha de ser menor de 10 caracters.")
    public String getUser();
    public String getName();
    public String getLastname();
    public void setUser(String user);
    public void setName(String name);
    public void setLastname(String lastname);
}
