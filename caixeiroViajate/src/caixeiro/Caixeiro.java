package caixeiro;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Caixeiro {

    static IloCplex modelo;
    static IloIntVar[][] s;

    public static void main(String[] args){
        try{
            Data data = new Data();
            data.leitura();
            modelo = new IloCplex();
            modelo.setParam(IloCplex.DoubleParam.TiLim, 300);

            //Instaciando a matriz solução
            s = new IloIntVar[data.n][data.n];

            //matriz solucao
            for(int i = 0; i < data.n; i++){
                for(int j = 0; j < data.n; j++){
                    s[i][j] = modelo.boolVar();
                }
            }

            //função objetivo
            IloLinearNumExpr fo = modelo.linearNumExpr();
            for(int i = 0; i < data.n; i++){
                for(int j = 0 ; j < data.n; j++){
                    fo.addTerm(s[i][j], data.matriz[i][j]);
                }
            }
            modelo.addMinimize(fo);

            //cada cidade so pode ser visitada uma vez
            for(int i = 0; i < data.n; i++){
                IloLinearNumExpr r1 = modelo.linearNumExpr();
                for(int j = 0; j < data.n; j++){
                    if(i != j) {
                        r1.addTerm(1, s[i][j]);
                    }
                }
                modelo.addEq(r1, 1);
            }

            //apenas uma origem e um destino
            for(int j = 0; j < data.n; j++){
                IloLinearNumExpr r2 = modelo.linearNumExpr();
                for(int i = 0; i < data.n; i++){
                    if(i != j) {
                        r2.addTerm(1, s[i][j]);
                    }
                }
                modelo.addEq(r2, 1);
            }

            /*
            //restricao de ciclos
            for(int i = 0; i < data.n; i++){
                for(int j = 0; j < data.n; j++) {
                    if (i != j) {
                        IloLinearNumExpr ciclo = modelo.linearNumExpr();
                        ciclo.addTerm(1.0, u[i]);
                        ciclo.addTerm(-1.0, u[j]);
                        ciclo.addTerm(data.n - 1.0, s[i][j]);
                        modelo.addLe(ciclo, data.n-2.0);
                    }
                }
            }
            */

            //Resolvendo o problema
            if(modelo.solve()){
                System.out.println("------------------------------");
                System.out.println(modelo.getStatus());
                System.out.println(modelo.getObjValue());
                System.out.println("------------------------------");

                print();

            }else{
                System.out.println("Deu Merda!");
            }
        }catch (IloException ex) {
            System.err.println("Erro no solver Cplex: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Caixeiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void print() throws IloException{
        System.out.println("------------------------------------------");

        for (int i = 0; i < 58; i++) {
            for (int j = 0; j < 58; j++) {
                if (modelo.getValue(s[i][j]) != 0) {
                    System.out.println("origem " + i + " destino " + j);
                }
            }
        }

        System.out.println("------------------------------------------");
        System.out.println();
    }

}
