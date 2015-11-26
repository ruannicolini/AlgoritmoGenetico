/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import algoritimogenetico.Individuo;
import domain.Bairro;
import domain.Bairro_Sequencia;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextArea;

/**
 *
 * @author Ruan
 */
public class Controlador {

    private ArrayList<Bairro> bairros = new ArrayList();

    public Controlador() {
    }

    public double[][] montaMatriz(int tam) {
        String linha = null;
        double[][] m = new double[tam][tam];
        int lin = 0;

        try {
            RandomAccessFile arquivo = new RandomAccessFile("Arquivos/Matriz.txt", "rw");

            while ((linha = arquivo.readLine()) != null) {
                //System.out.println(linha);
                StringTokenizer parser = new StringTokenizer(linha);
                while (parser.hasMoreTokens()) {
                    ArrayList<String> tokens = new ArrayList<String>();
                    // pega cada token
                    while (parser.hasMoreTokens()) {
                        tokens.add(parser.nextToken());
                    }

                    //matriz atribui valor aqui
                    for (int col = 0; col < tokens.size(); col++) {
                        //System.out.println(tokens.get(col));
                        m[lin][col] = Double.parseDouble(tokens.get(col));
                    }
                }
                lin++;
            }
            arquivo.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return m;
    }

    public ArrayList montaArrayBairros() {
        String linha = null;
        int cod = 0;
        try {
            RandomAccessFile arquivo = new RandomAccessFile("Arquivos/Bairros.txt", "rw");
            while ((linha = arquivo.readLine()) != null) {
                //System.out.println(linha);
                StringTokenizer parser = new StringTokenizer(linha);
                while (parser.hasMoreTokens()) {
                    Bairro bairro = new Bairro();
                    ArrayList<String> tokens = new ArrayList<String>();
                    // pega cada token
                    while (parser.hasMoreTokens()) {
                        tokens.add(parser.nextToken());
                    }
                    bairro.setCod(cod);
                    bairro.setNome(tokens.get(0));
                    //System.out.println(tokens.get(0));
                    bairro.setQtdFuncionarios(Integer.parseInt(tokens.get(1)));
                    //System.out.println(tokens.get(1));
                    bairro.setDistanciaPW(Double.parseDouble(tokens.get(2)));
                    //System.out.println(tokens.get(2));

                    bairros.add(bairro);
                    cod++;
                }
            }
            arquivo.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        //JOptionPane.showMessageDialog(null, bairros.get(5).getNome());
        return bairros;
    }

    public void iniciaPopulacao(ArrayList<Individuo> populacao, int qtdADD_Populacao, int qtdBairros) {
        Individuo indiv;

        for (int i = 0; i < qtdADD_Populacao; i++) {
            indiv = new Individuo();
            indiv.inicializaArray_Onibus(qtdBairros);
            indiv.inicializaArray_Sequencia(qtdBairros);
            populacao.add(indiv);
        }
    }

    private int retornaQtdFuncionarios(ArrayList<Bairro_Sequencia> onibus) {
        int qtdFuncionarios = 0;
        //System.out.println("___QUANTIDADE FUNCIONARIOS___");
        for (int i = 0; i < onibus.size(); i++) {
            //if(){
            qtdFuncionarios = qtdFuncionarios + onibus.get(i).getBairro().getQtdFuncionarios();
            //System.out.println(onibus.get(i).getBairro().getNome() + "= " + onibus.get(i).getBairro().getQtdFuncionarios());
            //}
        }
        return qtdFuncionarios;
    }

    public double retornaDistancia(ArrayList<Bairro_Sequencia> onibus, double[][] m) {
        double distancia = 0;
        double r = 0.0;

        try {
            if (onibus.size() > 0) { //Se siz e== 0; o onibus não sai da PW. 
                distancia = distancia + onibus.get(0).getBairro().getDistanciaPW();
                distancia = distancia + onibus.get(onibus.size() - 1).getBairro().getDistanciaPW();
                //System.out.println("\n\n\n");
                if (onibus.size() > 1) { //Se size < 2 não tem distancia entre bairros 
                    for (int i = 0; i < (onibus.size() - 1); i++) {
                        distancia = distancia + m[onibus.get(i).getBairro().getCod()][onibus.get(i + 1).getBairro().getCod()];
                    }
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("ERRO  ========= " + e.getMessage());
        }
        return distancia;
    }

    private void ordenaBairro_Sequencia(ArrayList<Bairro_Sequencia> onibus) {
        Collections.sort(onibus, new Comparator() {
            public int compare(Object o1, Object o2) {
                Bairro_Sequencia p1 = (Bairro_Sequencia) o1;
                Bairro_Sequencia p2 = (Bairro_Sequencia) o2;
                return p1.getSequencia() < p2.getSequencia() ? -1 : (p1.getSequencia() > p2.getSequencia() ? +1 : 0);
            }
        });
    }

    public void avaliaIndividuo(Individuo indiv, double[][] m) {
        double distancia = 0, distancia_01 = 0, distancia_02 = 0, distancia_03 = 0;
        int qtdFuncAtendidos = 0, qtdFuncAtendidos_01 = 0, qtdFuncAtendidos_02 = 0, qtdFuncAtendidos_03 = 0;

        ArrayList<Bairro_Sequencia> onibus1 = new ArrayList();
        ArrayList<Bairro_Sequencia> onibus2 = new ArrayList();
        ArrayList<Bairro_Sequencia> onibus3 = new ArrayList();

        //Separa o caminho dos onibus
        for (int i = 0; i < indiv.getSequencia().size(); i++) {
            Bairro_Sequencia bs = null;
            switch (indiv.getOnibus().get(i)) {
                case 0:
                    break;
                case 1:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus1.add(bs);
                    break;
                case 2:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus2.add(bs);
                    break;
                case 3:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus3.add(bs);
                    break;
            }
        }

        ordenaBairro_Sequencia(onibus1);
        distancia_01 = retornaDistancia(onibus1, m);
        qtdFuncAtendidos_01 = retornaQtdFuncionarios(onibus1);

        ordenaBairro_Sequencia(onibus2);
        distancia_02 = retornaDistancia(onibus2, m);
        qtdFuncAtendidos_02 = retornaQtdFuncionarios(onibus2);

        ordenaBairro_Sequencia(onibus3);
        distancia_03 = retornaDistancia(onibus3, m);
        qtdFuncAtendidos_03 = retornaQtdFuncionarios(onibus3);

        distancia = distancia_01 + distancia_02 + distancia_03;
        indiv.setKm(distancia);
        qtdFuncAtendidos = qtdFuncAtendidos_01 + qtdFuncAtendidos_02 + qtdFuncAtendidos_03;
        indiv.setQtdFuncAtendidos(qtdFuncAtendidos);

        double result = 0, numerador = 0, denominador = 0;

        //Restrições de Número de Funcionario
        numerador = qtdFuncAtendidos;
        if (qtdFuncAtendidos <= 135) {
            if ((qtdFuncAtendidos_01 <= 45) && (qtdFuncAtendidos_02 <= 45) && (qtdFuncAtendidos_03 <= 45)) {
                //Quanto menor a diferença entre qtd_func e o total de lugares dos onibus, melhor
                numerador = numerador - (135 - qtdFuncAtendidos);
                numerador = Math.pow(numerador, 2);
            } else {
                //Quanto melhor distribuido, menor o valor sa subtação;
                //numerador = (numerador/2) + Math.pow (((qtdFuncAtendidos_01/45) + (qtdFuncAtendidos_02/45) + (distancia_03/45)),2);
                numerador = numerador - Math.pow(((qtdFuncAtendidos_01 / 45) + (qtdFuncAtendidos_02 / 45) + (qtdFuncAtendidos_03 / 45)), 2);

            }
        } else {
            //Penaliza pois mantem o msm Num_Funcionarios
        }

        //Restrição de distância
        denominador = distancia;
        if (distancia > 48) {
            //denominador = denominador*10000;
            denominador = Math.pow(denominador, 2);
        } else {
            denominador = denominador + ((distancia_01 / 48) + (distancia_02 / 48) + (distancia_03 / 48));
        }

        result = numerador / denominador;

        indiv.setFo(result);
        //System.out.println(result);
    }

    public void avaliaPopulacao(ArrayList<Individuo> populacao, double[][] m, int indInicial) {

        for (int i = indInicial; i < populacao.size(); i++) {
            avaliaIndividuo(populacao.get(i), m);
        }

    }

    private int roleta(ArrayList<Individuo> populacao) {
        int indiceEscolhido = 0;
        double somaFO = 0;
        Random gerador = new Random();

        //Soma a Função objetiva de todos od individuos da população;
        for (int i = 0; i < 1000; i++) {
            somaFO = somaFO + populacao.get(i).getFo();
        }

        // Gera numero aleatorio entre o menor numero FO da População e a soma de todas
        double num = gerador.nextDouble() * somaFO;

        double intervaloInicio = 0;
        double intervaloFim = 0;
        for (int i = 0; i < 1000; i++) {
            intervaloFim = intervaloFim + populacao.get(i).getFo();
            if ((comparaRoleta(intervaloInicio, intervaloFim, num)) == true) {
                //indiv = populacao.get(i);
                indiceEscolhido = i;
                break;
            }
            intervaloInicio = intervaloFim;
        }
        return indiceEscolhido;
    }

    //@SuppressWarnings("empty-statement")
    private void cruzamento(int ind1, int ind2, ArrayList<Individuo> populacao) {
        Individuo filho = new Individuo();
        Random gerador = new Random();
        int indice = 0;

        do {
            indice = gerador.nextInt(53);
        } while ((indice == 0) || (indice == 52));

        for (int i = 0; i < 53; i++) {
            if (i < 26) {
                int num = populacao.get(ind1).getOnibus().get(i);
                filho.getOnibus().add(num);

                int numSeq = populacao.get(ind1).getSequencia().get(i);
                filho.getSequencia().add(numSeq);
            } else {
                int num = populacao.get(ind2).getOnibus().get(i);
                filho.getOnibus().add(num);

                int numSeq = populacao.get(ind2).getSequencia().get(i);
                filho.getSequencia().add(numSeq);
            }
        }
        populacao.add(filho);
    }

    private void mutacao_individual(Individuo indiv, int tam) {
        ArrayList<Integer> IndicesAlterados = new ArrayList();
        Random gerador = new Random();
        int indice = 0, indOnibus = 0;

        for (int i = 0; i < 1; i++) {
            //Escolhe o quantidade indices que sofrerão mutação
            do {
                indice = gerador.nextInt(tam);
            } while (IndicesAlterados.contains(indice) == true);
            IndicesAlterados.add(indice);

            do {
                indOnibus = gerador.nextInt(4);
            } while (indOnibus != indiv.getOnibus().get(indice));
            indiv.getOnibus().set(indice, indOnibus);
            indiv.getSequencia().set(indice, gerador.nextInt(500));
        }

    }

    private void mutacao(ArrayList<Individuo> populacao, int poncentagemMutacao, int tam) {
        Random gerador = new Random();
        double qtdIndividuos_Mutacao = ((double) tam) * (((double) (poncentagemMutacao)) / 100.0);// seleciona quantidade de individuos que sofrerão motacao
        ArrayList<Integer> IndicesIndividuosAlterados = new ArrayList();
        int indice = 0;

        for (int i = 0; i < qtdIndividuos_Mutacao; i++) {
            //Escolhe o individuo pra sofrer mutação
            do {
                indice = gerador.nextInt(500) + (1000);
            } while (IndicesIndividuosAlterados.contains(indice) == true);
            IndicesIndividuosAlterados.add(indice);

            //Faz Mutacao
            System.out.println("\n\n indice mutacao individual = " + indice + "TAM VETOR = " + populacao.size());
            mutacao_individual(populacao.get(indice), tam);

        }
    }

    private void selecionaSobreviventes(ArrayList<Individuo> populacao) {
        int tam = populacao.size();

//        System.out.println(" \n\n\n ANTES DA SELEÇÃO");
//        for (int i = 0; i < populacao.size(); i++) {
//            System.out.println("i: " + i + "     FO = " + populacao.get(i).getFo() + "   FUNC = " + populacao.get(i).getQtdFuncAtendidos() + "   KM = " + populacao.get(i).getKm());
//        }
        //Deixa apenas os 1000 melhores indivíduos
        for (int i = 1000; i < tam; i++) {
            populacao.remove(1000);

        }

//        System.out.println(" \n\n\n DEPOIS DA SELEÇÃO");
//        for (int i = 0; i < populacao.size(); i++) {
//            System.out.println("i: " + i + "     FO = " + populacao.get(i).getFo() + "   FUNC = " + populacao.get(i).getQtdFuncAtendidos() + "   KM = " + populacao.get(i).getKm());
//        }
    }

    private boolean comparaRoleta(double intevaloInicio, double intevaloFim, double num) {
        if ((num >= intevaloInicio) && (num < intevaloFim)) {
            return true;
        }
        return false;
    }

    public void heuristica(ArrayList<Bairro> bairros, double[][] m, JTextArea jtaPrincipal, int nGeracoes, int PoncentagemMutacao) {
        int indiv1 = 0, indiv2 = 0, cont = 0;
        ArrayList<Individuo> populacao = new ArrayList();
        Random gerador = new Random();

        //Inicia a população pela primeira vez
        iniciaPopulacao(populacao, 1000, bairros.size()); // Cria 1000 indivíduos
        avaliaPopulacao(populacao, m, 0);

        //Ordena População Pela F.O
        Collections.sort((List) populacao, Collections.reverseOrder());

        while (cont < nGeracoes) {  // Condição de parada  

            // Gera 500 indivíduos
            for (int i = 0; i < 500; i++) {
                //Escolhe 2 indivíduos     
                indiv1 = 0;
                indiv2 = 0;
                do {
                    indiv1 = roleta(populacao);
                    indiv2 = roleta(populacao);
                } while (comparaIndividuos(populacao.get(indiv1), populacao.get(indiv2)) == true);

                Individuo filho = new Individuo();
                int indice = 0;

                do {
                    indice = gerador.nextInt(53);
                } while ((indice <= 0) || (indice >= 52));

                for (int j = 0; j < 53; j++) {
                    if (j < indice) {
                        int num = populacao.get(indiv1).getOnibus().get(j);
                        filho.getOnibus().add(num);

                        int numSeq = populacao.get(indiv1).getSequencia().get(j);
                        filho.getSequencia().add(numSeq);
                    } else {
                        int num = populacao.get(indiv2).getOnibus().get(j);
                        filho.getOnibus().add(num);

                        int numSeq = populacao.get(indiv2).getSequencia().get(j);
                        filho.getSequencia().add(numSeq);
                    }
                }
                populacao.add(filho);

                //Mutacao
                int ind = gerador.nextInt(53);
                int novoNum = 0;
                do {
                    novoNum = gerador.nextInt(4);
                } while (novoNum == populacao.get(populacao.size() - 1).getOnibus().get(ind));
                populacao.get(populacao.size() - 1).getOnibus().set(ind, novoNum);
                populacao.get(populacao.size() - 1).getSequencia().set(ind, gerador.nextInt(1000));

            }

            avaliaPopulacao(populacao, m, 1000);

            //Ordena População Pela F.O
            try {
                Collections.sort((List) populacao, Collections.reverseOrder());
            } catch (Exception e) {
                System.out.println("Erro === " + e.getMessage());
            }

            //Seleciona Sobreviventes (1.000 indivíduos com maio F.O )
            selecionaSobreviventes(populacao);

            System.out.println("Cont = " + cont + "     M.I =   FO = " + populacao.get(0).getFo() + " Funk = " + populacao.get(0).getQtdFuncAtendidos() + " KM = " + populacao.get(0).getKm());
            cont++;
        }
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>        
        System.out.println("Teste");
        jtaPrincipal.setText("MELHOR RESUTADO! \n \n");
        jtaPrincipal.setText(jtaPrincipal.getText() + "Onibus = " + populacao.get(0).getOnibus());
        jtaPrincipal.setText(jtaPrincipal.getText() + "\n\n Sequencia = " + populacao.get(0).getSequencia());
        jtaPrincipal.setText(jtaPrincipal.getText() + " \n\n F.O = " + populacao.get(0).getFo());
        jtaPrincipal.setText(jtaPrincipal.getText() + "\n N_Fun = " + populacao.get(0).getQtdFuncAtendidos() + "\n KM = " + populacao.get(0).getKm());

        lerResultado(jtaPrincipal, populacao.get(0), m);
    }

    private void lerOnibus(ArrayList<Bairro_Sequencia> onibus, JTextArea jtaPrincipal, double[][] m) {
        double distancia = 0.0;
        jtaPrincipal.setText(jtaPrincipal.getText() + "\n\n\n ONIBUS:   " + retornaQtdFuncionarios(onibus) + " Funcinários \n");
        ordenaBairro_Sequencia(onibus);

        for (int i = 0; i < onibus.size(); i++) {
            if (i == 0) {

                jtaPrincipal.setText(jtaPrincipal.getText() + "PW --- " + onibus.get(0).getBairro().getNome() + ": " + (onibus.get(0).getBairro().getDistanciaPW()) + " -   FUNC: " + (onibus.get(0).getBairro().getQtdFuncionarios()) + "\n");
                distancia = distancia + (onibus.get(0).getBairro().getDistanciaPW());
                if (onibus.size() > 1) {
                    jtaPrincipal.setText(jtaPrincipal.getText() + onibus.get(i).getBairro().getNome() + " --- " + onibus.get(i + 1).getBairro().getNome() + " = " + m[onibus.get(i).getBairro().getCod()][onibus.get(i + 1).getBairro().getCod()] + "   -   FUNC: " + (onibus.get(i + 1).getBairro().getQtdFuncionarios()) + "\n");
                    distancia = distancia + m[onibus.get(i).getBairro().getCod()][onibus.get(i + 1).getBairro().getCod()];
                }

            } else {
                if (i == (onibus.size() - 1)) {
                    distancia = distancia + (onibus.get(i).getBairro().getDistanciaPW());
                    jtaPrincipal.setText(jtaPrincipal.getText() + onibus.get(i).getBairro().getNome() + " --- PW: " + (onibus.get(i).getBairro().getDistanciaPW()) + " \n");

                } else {
                    distancia = distancia + m[onibus.get(i).getBairro().getCod()][onibus.get(i + 1).getBairro().getCod()];
                    jtaPrincipal.setText(jtaPrincipal.getText() + onibus.get(i).getBairro().getNome() + " --- " + onibus.get(i + 1).getBairro().getNome() + " = " + m[onibus.get(i).getBairro().getCod()][onibus.get(i + 1).getBairro().getCod()] + "   -   FUNC: " + (onibus.get(i + 1).getBairro().getQtdFuncionarios()) + "\n");
                }
            }
        }
        jtaPrincipal.setText(jtaPrincipal.getText() + " Distância = " + distancia + "\n");

    }

    private void lerResultado(JTextArea jtaPrincipal, Individuo indiv, double[][] m) {
        double distancia = 0;
        int qtdFuncAtendidos_01 = 0, qtdFuncAtendidos_02 = 0, qtdFuncAtendidos_03 = 0;
        ArrayList<Bairro_Sequencia> onibus1 = new ArrayList();
        ArrayList<Bairro_Sequencia> onibus2 = new ArrayList();
        ArrayList<Bairro_Sequencia> onibus3 = new ArrayList();

        //Separa o caminho dos onibus
        for (int i = 0; i < indiv.getSequencia().size(); i++) {
            Bairro_Sequencia bs = null;
            switch (indiv.getOnibus().get(i)) {
                case 0:
                    break;
                case 1:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus1.add(bs);
                    break;
                case 2:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus2.add(bs);
                    break;
                case 3:
                    bs = new Bairro_Sequencia();
                    bs.setBairro(bairros.get(i));
                    bs.setSequencia(indiv.getSequencia().get(i));
                    onibus3.add(bs);
                    break;
            }
        }

        //Onibus 1
        lerOnibus(onibus1, jtaPrincipal, m);
        //Onibus 2
        lerOnibus(onibus2, jtaPrincipal, m);
        //Onibus 3
        lerOnibus(onibus3, jtaPrincipal, m);
    }

    private boolean comparaIndividuos(Individuo indiv1, Individuo indiv2) {
        boolean status = false;

        if (indiv1.getFo() == indiv2.getFo()) {
            if (indiv1.getQtdFuncAtendidos() == indiv2.getQtdFuncAtendidos()) {
                if (indiv1.getKm() == indiv2.getKm()) {
                    for (int i = 0; i < 53; i++) {
                        if (indiv1.getOnibus().get(i).equals(indiv2.getOnibus().get(i)) == true) {
                            if (indiv1.getSequencia().get(i).equals(indiv2.getSequencia().get(i)) == true) {
                                status = true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return status;
    }

    private boolean verificaIndividuosIguais(ArrayList<Individuo> populacao) {
        //Verifica se tem Indivíduos iguars
        for (int r = 0; r < populacao.size(); r++) {
            for (int s = r + 1; s < populacao.size(); s++) {
                if (s == populacao.size()) {

                } else {
                    if (comparaIndividuos(populacao.get(r), populacao.get(s)) == true) {
                        System.out.println("******************************************");
                        System.out.println("Individuos Iguais na Populacao");
                        System.out.println("individuo produtoCruzamento: " + r + " + " + s + ";");
                        System.out.println(r + " Onibus = " + populacao.get(r).getOnibus());
                        System.out.println(r + " Sequencia = " + populacao.get(r).getSequencia());
                        System.out.println(" ");

                        System.out.println(s + " Onibus = " + populacao.get(s).getOnibus());
                        System.out.println(s + " Sequencia = " + populacao.get(s).getSequencia());
                        System.out.println("******************************************");
                        return true;
//                            
                        //Random gerador = new Random();
                        //mutacao_individual(populacao.get( populacao.size() -1) , 53);

                    }
                }
            }
        }

        return false;
    }

}//Fim Controlador
