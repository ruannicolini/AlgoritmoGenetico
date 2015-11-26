/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Ruan
 */
public class Bairro_Sequencia {
    private Bairro bairro;
    private int sequencia;

    public Bairro_Sequencia() {
        this.bairro = new Bairro();
    }

    public Bairro_Sequencia(Bairro bairro, int sequencia) {
        this.bairro = bairro;
        this.sequencia = sequencia;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }
    
    
}
