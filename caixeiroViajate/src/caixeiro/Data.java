package caixeiro;

import java.io.*;

public class Data {

    static int[][] matriz = new int[58][58];
    static int n = 58;

    public static void leitura() throws IOException {
        int k = 0;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/caixeiro/brazil58.txt"));

            String line = reader.readLine();
            String quebra[] = null;

            int i = 0;
            int j = 1;
            while (line != null) {
                quebra = line.trim().split(" ");
                for (int v = 0; v < quebra.length; v++, j++) {
                    matriz[i][j] = Integer.valueOf(quebra[v]);
                }
                i++;
                j = i + 1;
                line = reader.readLine();

            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //espelha a triangular superior
        for (int i = 0; i < 58; i++) {
            for (int j = 0; j < 58; j++) {
                if (i != j) {
                    matriz[j][i] = matriz[i][j];
                }
            }
        }

    }

}
