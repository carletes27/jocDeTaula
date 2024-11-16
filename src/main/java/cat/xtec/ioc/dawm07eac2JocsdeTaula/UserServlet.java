package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.json.JSONObject;

/**
 *
 * @author CARLOS ORDÓÑEZ ORTIZ M07 EAC2
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    @Resource
    Validator validator;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {

        } else {
            switch (action) {
                case "formUser":
                    formUser(request, response);
                    break;
                case "newUser":
                    newUser(request, response);
                    break;
            }
        }
    }

    /**
     * Recull i manté les dades de l'usuari introduïdes durant la sessió. Aquest
     * mètode permet també l'inici d'una sessió, sense introduir dades d'usuari,
     * manejant tots els camps com a cadenes buides en lloc de nuls.
     *
     * @param request servlet request
     * @param response servlet response
     */
    private void formUser(HttpServletRequest request, HttpServletResponse response) {
// EXERCICI 3
// Agafa les dades de l'usuari introduides i ens les manté al llarg d ela sessió
// També poden introduir-se tots els camps en blanc (fem servir "" no nulls) i seria una mena de sessió anònima
// Com no desem les dades relacionades amb l'usuari, no importa no tenir usuari
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()){
            // Obtenim els pàrametres d'usuari i creem objecte
            UserLocal usuari = (UserLocal) request.getSession().getAttribute("userLocal");
            if (usuari == null) {
                usuari = (UserLocal) new InitialContext().lookup("java:global/dawm07eac2JocsdeTaulaEnunciat/User");
                request.getSession().setAttribute("userLocal", usuari);
            }
            // Establim codificació de caracters i creem objectes i establim valors
            JSONObject json = new JSONObject();
            if (usuari != null) {
                json.put("user", usuari.getUser());
                json.put("name", usuari.getName());
                json.put("lastname", usuari.getLastname());
            } else {
                json.put("user", "");
                json.put("name", "");
                json.put("lastname", "");
            }
            response.getWriter().print(json.toString());
            // Tanquem i llançem l'exempció
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Crea un nou usuari i guarda les dades introduïdes a la sessió. Aquest
     * mètode recopila les dades del formulari proporcionades pel client, crea o
     * actualitza un objecte d'usuari a la sessió amb aquestes dades i realitza
     * una validació bàsica de les mateixes. Si les dades no compleixen les
     * restriccions de validació, s'agreguen missatges d'error a la resposta.
     *
     * @param request servlet request
     * @param response servlet response
     */
    private void newUser(HttpServletRequest request, HttpServletResponse response) {
// EXERCICI 3
// Crea un nou usuari desant les dades introduides
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()){
            // Obtenim els pàrametres d'usuari i establim valors
            UserLocal usuari = (UserLocal) request.getSession().getAttribute("userLocal");
            usuari.setUser(request.getParameter("user"));
            usuari.setName(request.getParameter("name"));
            usuari.setLastname(request.getParameter("lastname"));
            Set<ConstraintViolation<UserLocal>> violations = validator.validate(usuari);
            // Construim la resposta JSON
            JSONObject jsonResponse = new JSONObject();
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("OK");
            for (ConstraintViolation<UserLocal> violation : violations) {
                messageBuilder.append(" - ").append(violation.getMessage());
            }
            jsonResponse.put("resposta", messageBuilder.toString());
            // Imprimim la resposta JSON en el PrintWriter
            out.print(jsonResponse.toString());
            out.flush();
            // Tanquem i llançem l'exempció
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    //
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
