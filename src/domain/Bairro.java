package domain;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruan
 */


public class Bairro {
    private int cod;
    private String nome;
    private double distanciaPW;
    private int qtdFuncionarios;

    public Bairro() {
    }

    public Bairro(String nome, double distanciaPW, int qtdFuncionarios) {
        this.nome = nome;
        this.distanciaPW = distanciaPW;
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getDistanciaPW() {
        return distanciaPW;
    }

    public void setDistanciaPW(double distanciaPW) {
        this.distanciaPW = distanciaPW;
    }

    public int getQtdFuncionarios() {
        return qtdFuncionarios;
    }

    public void setQtdFuncionarios(int qtdFuncionarios) {
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    
    
}
