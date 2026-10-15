package mx.unison;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.List;

public class Calculadora {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No se proporcionaron argumentos requeridos");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }

        String archivoXML = args[0];
        String var1 = args[1];
        String var2 = null;

        if (args.length >= 3) {
            var2 = args[2];
        }

        try {
            File xmlFile = new File(archivoXML);
            if (!xmlFile.exists()) {
                System.out.println("Error: No se puede abrir el documento XML (Archivo no encontrado).");
                System.exit(3);
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            VariablesHandler handler = new VariablesHandler(var1, var2);

            saxParser.parse(xmlFile, handler);

            List<Double> listaVar1 = handler.getListaVar1();

            if (listaVar1.isEmpty()) {
                System.out.println("Error: El usuario se equivoca en el nombre de la variable o la variable '" + var1 + "' no existe en el documento.");
                System.exit(4);
            }

            imprimirEstadisticas(var1, listaVar1);

            if (var2 != null) {
                List<Double> listaVar2 = handler.getListaVar2();
                if (listaVar2.isEmpty()) {
                    System.out.println("Error: La segunda variable '" + var2 + "' no existe.");
                    System.exit(5);
                }
                imprimirEstadisticas(var2, listaVar2);

                double correlacion = calcularCorrelacion(listaVar1, listaVar2);
                System.out.printf("Coeficiente de correlación entre %s y %s: %.4f%n", var1, var2, correlacion);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: La variable qué seleccionó el usuario no es una variable numérica.");
            System.exit(6);
        } catch (Exception e) {
            System.out.println("Error al procesar el archivo XML: " + e.getMessage());
            System.exit(7);
        }
    }

    private static void imprimirEstadisticas(String nombre, List<Double> datos) {
        double suma = 0;
        for (double num : datos) {
            suma += num;
        }
        double media = suma / datos.size();

        double sumaDiferencias = 0;
        for (double num : datos) {
            sumaDiferencias += Math.pow(num - media, 2);
        }
        double varianza = sumaDiferencias / (datos.size() - 1);
        double desviacionEstandar = Math.sqrt(varianza);

        double maximo = java.util.Collections.max(datos);
        double minimo = java.util.Collections.min(datos);

        System.out.println("\n=== Estadísticas para: " + nombre + " ===");
        System.out.printf("- Media: %.4f%n", media);
        System.out.printf("- Varianza: %.4f%n", varianza);
        System.out.printf("- Desviación estándar: %.4f%n", desviacionEstandar);
        System.out.printf("- Valor máximo: %.4f%n", maximo);
        System.out.printf("- Valor mínimo: %.4f%n", minimo);
    }

    // Método para correlación de Pearson
    private static double calcularCorrelacion(List<Double> x, List<Double> y) {
        int n = Math.min(x.size(), y.size());
        double sumaX = 0, sumaY = 0, sumaXY = 0;
        double sumaX2 = 0, sumaY2 = 0;

        for (int i = 0; i < n; i++) {
            double valX = x.get(i);
            double valY = y.get(i);

            sumaX += valX;
            sumaY += valY;
            sumaXY += (valX * valY);
            sumaX2 += Math.pow(valX, 2);
            sumaY2 += Math.pow(valY, 2);
        }

        double numerador = (n * sumaXY) - (sumaX * sumaY);
        double denominador = Math.sqrt((n * sumaX2 - Math.pow(sumaX, 2)) * (n * sumaY2 - Math.pow(sumaY, 2)));

        if (denominador == 0) return 0;
        return numerador / denominador;
    }
}