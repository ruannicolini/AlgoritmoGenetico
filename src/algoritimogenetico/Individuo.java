/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritimogenetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Ruan
 */
public class Individuo implements Comparable<Individuo>{
    //Cromossomo
    private ArrayList<Integer> onibus;
    private ArrayList<Integer> sequencia;
    
    //Avaliação
    private double fo;
    
    private double km;
    private int qtdFuncAtendidos;
    
    public Individuo() {
        onibus = new ArrayList<Integer>();
        sequencia = new ArrayList<Integer>();
    }
    
    public Individuo(double fo) {
        this.fo = fo;
        onibus = new ArrayList<Integer>();
        sequencia = new ArrayList<Integer>();
    }

    public ArrayList<Integer> getOnibus() {
        return onibus;
    }

    public void setOnibus(ArrayList<Integer> onibus) {
        this.onibus = onibus;
    }

    public ArrayList<Integer> getSequencia() {
        return sequencia;
    }

    public void setSequencia(ArrayList<Integer> sequencia) {
        this.sequencia = sequencia;
    }
    
    

    public double getFo() {
        return fo;
    }

    public void setFo(double fo) {
        this.fo = fo;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public int getQtdFuncAtendidos() {
        return qtdFuncAtendidos;
    }

    public void setQtdFuncAtendidos(int qtdFuncAtendidos) {
        this.qtdFuncAtendidos = qtdFuncAtendidos;
    }
    
    public void inicializaArray_Onibus( int tam) {
        //ArrayList<Integer> array_onibus = new ArrayList();
        Random gerador = new Random();
        for(int i =0; i< tam; i++){
            this.onibus.add( gerador.nextInt(4) ); //gera número >= 0 e < 4
        }
    }

    public void inicializaArray_Sequencia(int tam) {
       
        Random gerador = new Random();
        for(int i=0; i< tam;i++){
            this.sequencia.add(gerador.nextInt(500));
        }
    }

    public int compareTo(Individuo indiv) {
        if(this.getFo() > indiv.getFo()){
            
            return 1;
        }
        if(this.getFo() < indiv.getFo()){
                return -1;
        }//else{
//                System.out.println("FO IGUAL!!!");
//                System.out.println( "Indiv");
//                System.out.println( "Fo = " + indiv.getFo() + " Func = " +indiv.getQtdFuncAtendidos());
//                System.out.println("Onibus = " + indiv.getOnibus());
//                System.out.println("Sequencia = " + indiv.getSequencia());
//                System.out.println("");
//                System.out.println( "this");
//                System.out.println( "Fo = " + this.getFo() + " Func = " +this.getQtdFuncAtendidos());
//                System.out.println("Onibus = " + this.getOnibus());
//                System.out.println("Sequencia = " + this.getSequencia());
//                System.out.println("");
        //}
        return 0;
        
    }
    
    
}
