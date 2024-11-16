package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author CARLOS ORDÓÑEZ ORTIZ M07 EAC2
 */
@MultipartConfig(location = "C:\\Users\\Usuario\\Desktop\\dawm07eac2JocsdeTaulaEnunciat\\src\\main\\java\\cat\\xtec\\ioc\\dawm07eac2JocsdeTaula\\tmp")
public class LlistaJocsServlet extends HttpServlet {

    @EJB
    private ValidateJocBeanLocal validation;

    private List<Joc> jocs = new ArrayList<Joc>();

    //Directori on es guarden les imatges
    private static final String UPLOAD_DIR = "img";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Enumeration initValorations = getServletConfig().getInitParameterNames();
        while (initValorations.hasMoreElements()) {
            String name = (String) initValorations.nextElement();
            String values = getServletConfig().getInitParameter(name);
            Integer valoracio = Integer.parseInt(values);
            jocs.add(new Joc(name, valoracio));

        }
    }

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
        switch (action) {
            case "llistaJocs":
                llistaJocs(request, response);
                break;
            case "afegirValoracioJoc":
                afegirValoracioJoc(request, response);
                break;
            case "afegirNouJoc":
                afegirNouJoc(request, response);
                break;
        }
    }

    /**
     * Genera una llista de jocs en format JSON i l'envia com a resposta HTTP.
     * Aquest mètode llegeix la col·lecció de jocs disponibles i els retorna en
     * un format JSON, incloent la comprovació de si el joc ja ha estat valorat
     * a la sessió actual.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void llistaJocs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
// EXERCICI 1            
// Heu de llegir la col·lecció de jocs que tenim i retornar-les en un JSON.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            for (Joc joc : jocs) {
                //Indiquem les dades que porta la resposta i ordenem els atributs de cada joc amb LinkedHashMap
                response.setContentType("application/json");
                LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<String, String>();
                jsonOrderedMap.put("name", joc.getName());
                jsonOrderedMap.put("valoracions", joc.getValoracio().toString());
                jsonOrderedMap.put("mitjana", joc.getMitjana().toString());
// EXERCICI 3
// COMPROVACIÓ SI EL JOC JA HA ESTAT VALORAT
                jsonOrderedMap.put("valorat", comprovarJocValoratSession(request, joc) ? "SI" : "NO");
                // Creem objecte
                JSONObject member = new JSONObject(jsonOrderedMap);
                array.put(member);
            }
            // Agregem array
            json.put("jsonArray", array);
            out.print(json.toString());
            // Tanquem i llançem l'exempció
            out.close();
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Processa una sol·licitud HTTP per afegir una valoració a un joc específic
     * i actualitza la sessió de l'usuari amb aquesta informació.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void afegirValoracioJoc(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// EXERCICI 1
// Quan l'usuari prem una valoració (1-5), ens fa la petició des d'eac.js
// Hem d'afegir la valoració a la pel·lícula
        // Obtenim els pàrametres joc i valoració
        String valoracioJoc = request.getParameter("joc");
        int valoracio = Integer.parseInt(request.getParameter("valoracio"));
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Creem objecte json i la variable mitjana
            JSONObject json = new JSONObject();
            Double mitjana = 0.0;
            // Busquem joc
            for (Joc jocValorat : this.jocs) {
                if (jocValorat.getName().equals(valoracioJoc)) {
                    jocValorat.setValoracio(valoracio);
                    mitjana = jocValorat.getMitjana();
// EXERCICI 3
// Afegir al valoració de la pel·lícula a la sessió
                    afegirJocValoratToSession(request, jocValorat);
                    break;
                }
            }
            // Agreguem valors
            json.put("valoracioJoc", valoracioJoc);
            json.put("mitjanaJoc", mitjana);
            out.print(json.toString());
            // Tanquem i llançem l'exempció
            out.close();
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Afegeix un nou joc a la col·lecció i redirigeix el client a la pàgina
     * principal. Aquest mètode recull les dades d'un nou joc a través d'una
     * petició HTTP, incloent-hi una imatge associada al joc. Guarda la imatge
     * en un directori específic i afegeix el joc a la col·lecció de jocs
     * disponibles.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void afegirNouJoc(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// EXERCICI 2
// Aquest mètode ens afegirà un nou joc agafant les dades que introduim.
// Heu de pujar el fitxer que ens envien a la carpeta img/ del mateix projecte
// Això no es pot fer directament. Heu de pujar el fitxer a la carpeta temporal que
// defineix l'anotació @MultipartConfig(location ... i després copiar-lo a la carpeta img/
// En acabar de desar el Joc, heu de retornar a index.html per veure la col·lecció amb el nou joc
        // Obtenim ruta del directori i indiquem on guardarem les dades
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        try {
            File fileSaveDir = new File(uploadFilePath);
            System.out.println("Upload File Directory=" + fileSaveDir.getAbsolutePath());
            Part imatgeJoc = request.getPart("fileImageName");
            if (isValidFileName(request.getParameter("name"), imatgeJoc.getSubmittedFileName())) {
                File joc = new File(uploadFilePath, imatgeJoc.getSubmittedFileName());
                InputStream input = imatgeJoc.getInputStream();
                Files.copy(input, joc.toPath(), REPLACE_EXISTING);
                Joc nouJoc = new Joc(request.getParameter("name"), Integer.valueOf(request.getParameter("valor")));
                this.jocs.add(nouJoc);
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("index.html");
                response.setHeader("error", "Error");
            }
            // Tanquem i llançem l'exempció
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Comprova si el nom del fitxer proporcionat és vàlid per a un joc. Aquest
     * mètode primer verifica si el nom del fitxer compleix certs criteris de
     * validació i després assegura que el nom no estigui ja utilitzat per un
     * altre joc a la col·lecció.
     *
     * @param paramName El nom proposat per al joc, utilitzat per comprovar la
     * unicitat.
     * @param fileName El nom del fitxer a validar.
     * @return true si el nom del fitxer és vàlid i únic; false en cas contrari.
     */
    private boolean isValidFileName(String paramName, String fileName) {
        boolean valid = false;
// EXERCICI 2      
// Comprova si el nom del Joc és valid
        valid = validation.isValidFileImageName(paramName, fileName);
        for (Joc joc : jocs) {
            if (joc.getName().equals(paramName)) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Afegeix un joc valorat a la sessió de l'usuari. Aquest mètode guarda un
     * joc que ha estat valorat per l'usuari a la sessió actual.
     *
     * @param request servlet request
     * @param afegirValor El joc que s'afegirà a la llista de jocs valorats a la
     * sessió.
     */
    private void afegirJocValoratToSession(HttpServletRequest request, Joc afegirValor) {
// EXERCICI 3
// AFEGIM UN JOC VALORAT A LA SESSIÓ
        try {
            ValoracioLocal valor = (ValoracioLocal) request.getSession().getAttribute("valor");
            if (valor == null) {
                valor = (ValoracioLocal) new InitialContext().lookup("java:global/dawm07eac2JocsdeTaulaEnunciat/Valoracio");
                valor.setJocsValorats(new ArrayList<Joc>());
                request.getSession().setAttribute("valor", valor);
            }
            valor.getJocsValorats().add(afegirValor);
            // Tanquem i llançem l'exempció
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Comprova si un joc ja ha estat valorat a la sessió actual. Aquest mètode
     * revisa la llista de jocs valorats emmagatzemada a la sessió de l'usuari
     * per determinar si el joc especificat ja ha estat valorat prèviament.
     *
     * @param request servlet request
     * @param valorJoc El joc que es comprovarà si ja ha estat valorat.
     * @return true si el joc ja ha estat valorat a la sessió actual; false en
     * cas contrari.
     */
    private Boolean comprovarJocValoratSession(HttpServletRequest request, Joc valorJoc) {
        Boolean toReturn = false;
// EXERCICI 3
// Comprovem en la nostra sessió si el joc ja ha estat valorat.
        try {
            ValoracioLocal valor = (ValoracioLocal) request.getSession().getAttribute("valor");
            if (valor == null) {
                valor = (ValoracioLocal) new InitialContext().lookup("java:global/dawm07eac2JocsdeTaulaEnunciat/Valoracio");
                valor.setJocsValorats(new ArrayList<Joc>());
                request.getSession().setAttribute("valor", valor);
            }
            for (Joc joc : valor.getJocsValorats()) {
                if (joc.getName().equals(valorJoc.getName())) {
                    toReturn = true;
                    break;
                }
            }
            // Tanquem i llançem l'exempció
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return toReturn;
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
