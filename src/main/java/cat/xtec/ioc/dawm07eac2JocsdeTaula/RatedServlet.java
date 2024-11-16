package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author CARLOS ORDÓÑEZ ORTIZ M07 EAC2
 */
@WebServlet(name = "Valorat", urlPatterns = {"/valorat"})
public class RatedServlet extends HttpServlet {

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
                case "llistaJocsValorats":
                    llistaJocsValorats(request, response);
                    break;
                case "eliminarValoracioJoc":
                    eliminarValoracioJoc(request, response);
                    break;
            }
        }
    }

    /**
     * Recupera i mostra la llista de jocs valorats emmagatzemats a la sessió de
     * l'usuari. Aquest mètode genera una resposta JSON que conté la llista de
     * jocs valorats, incloent detalls com el nom del joc, la valoració, la
     * valoració mitjana i la llista completa de valoracions. Si no hi ha una
     * llista de jocs valorats a la sessió, s'inicialitza una nova.
     *
     * @param request servlet request
     * @param response servlet response
     */
    private void llistaJocsValorats(HttpServletRequest request, HttpServletResponse response) {
// EXERCICI 3
// Agafa la llista de jocs valorats en la sessió i ens els mostra
        // Establim codificació de caracters i creem objectes
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            // Obtenir la llista de jocs valorats de la sessió de l'usuari
            ValoracioLocal valor = (ValoracioLocal) request.getSession().getAttribute("valor");
            if (valor == null) {
                valor = (ValoracioLocal) new InitialContext().lookup("java:global/dawm07eac2JocsdeTaulaEnunciat/Valoracio");
                valor.setJocsValorats(new ArrayList<Joc>());
                request.getSession().setAttribute("valor", valor);
            }
            // Creem el objecte JSON per la resposta
            response.setContentType("application/json");
            // Construim el JSON amb la llista
            for (Joc joc : valor.getJocsValorats()) {
                LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<String, String>();
                jsonOrderedMap.put("name", joc.getName());
                jsonOrderedMap.put("valoracio", joc.getValoracio().toString());
                jsonOrderedMap.put("mitjana", joc.getMitjana().toString());
                jsonOrderedMap.put("valoracions", (new JSONArray(joc.getTotesValoracions())).toString());
                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }
            json.put("jsonArray", array);
            out.print(json.toString());
            // Tanquem i llançem l'exempció
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Elimina la darrera valoració realitzada per l'usuari per a un joc
     * específic a la sessió. El mètode busca el joc basant-se en un paràmetre
     * enviat a la sol·licitud, elimina l'última valoració associada a aquest
     * joc i després actualitza la llista de jocs valorats a la sessió. Es torna
     * una resposta JSON indicant l'èxit de l'operació.
     *
     * @param request servlet request
     * @param response servlet response
     */
    private void eliminarValoracioJoc(HttpServletRequest request, HttpServletResponse response) {
// EXERCICI 3
// Elimina una valoració feta en la sessió
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Obtenim els pàrametres joc i valor
            ValoracioLocal valor = (ValoracioLocal) new InitialContext().lookup("java:global/dawm07eac2JocsdeTaulaEnunciat/Valoracio");
            String eliminarValoracio = request.getParameter("joc");            
            valor = (ValoracioLocal) request.getSession().getAttribute("valor");
            // Busquem el joc a la llista
            for (Joc joc : valor.getJocsValorats()) {
                if (joc.getName().equals(eliminarValoracio)) {
                    // Eliminem l'última valoració associada
                    joc.eliminaUltimaValoracio();
                    valor.getJocsValorats().remove(joc);
                    break;
                }
            }
            // Responem amb exit
            String toReturn = "OK";
            JSONObject json = new JSONObject();
            json.put("resposta", toReturn);
            out.print(json.toString());
            // Tanquem i llançem l'exempció
            out.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

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
