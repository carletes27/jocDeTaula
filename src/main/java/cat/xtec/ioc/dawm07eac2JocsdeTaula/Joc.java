package cat.xtec.ioc.dawm07eac2JocsdeTaula;

import java.util.ArrayList;

/**
 *
 * @author Joel Monné Mesalles
 */
public class Joc {
    private String name;
    private ArrayList<Integer> valoracions; 
    private Double mitjana;

    public Double getMitjana() {
        return mitjana;
    }

    public String getName() {
        return name;
    }

    public Integer getValoracio() {
        return (int) valoracions.get(valoracions.size()-1);
    }
    
    public ArrayList<Integer>  getTotesValoracions() {
        return  valoracions;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setValoracio(Integer valoracio) {
        this.valoracions.add(valoracio);
        this.mitjana = recalculateMedian();
    }
    
    public void eliminaUltimaValoracio(){
        this.valoracions.remove(this.valoracions.size() - 1);
    }
    
    public Joc(String name, Integer valoracio) {
        this.valoracions = new ArrayList();
        this.name = name;
        this.valoracions.add(valoracio);
        this.mitjana = (double) valoracio;
    }
    
    private Double recalculateMedian (){
        int sum = 0;
        if (valoracions.size() <=1){
            return -1.0;
        }
        for (int valoracio: valoracions) {
              sum += valoracio;
        }
        return (double) sum/valoracions.size();
    }
    
}
