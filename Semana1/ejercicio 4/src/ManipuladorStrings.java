import java.util.Locale;

public class ManipuladorStrings {
    public static String invertir(String s) {
        // TODO: usar StringBuilder.reverse()
        StringBuilder sb = new StringBuilder(s);
        sb.reverse();
        return sb.toString();

    }

    public static boolean esPalindromo(String s) {
        // TODO: limpiar (toLowerCase, replaceAll espacios)
        s=s.toLowerCase();
        s=s.replaceAll(" ", "");
        // TODO: comparar con su version invertida
        StringBuilder sb = new StringBuilder(s);
        sb.reverse();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != sb.charAt(i)) {
                return  false;
            }
        }
            return  true;
    }

    public static int contarVocales(String s) {
        int count = 0;
        String vocales = "aeiouAEIOU";
        // TODO: recorrer cada caracter, verificar si es vocal
      for (int i = 0; i < s.length(); i++) {
          if (vocales.indexOf(s.charAt(i)) != -1) {
              count++;
          }
      }
        return count;
    }

    public static String construirPiramide(int niveles) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= niveles; i++) {
            // TODO: agregar espacios (niveles - i)
            for (int j = 1; j <= niveles-i; j++) {
            sb.append(" ");}
            // TODO: agregar asteriscos (2*i - 1)
            for (int j = 1; j <= 2*i-1; j++) {
            sb.append("*");}
            // TODO: agregar salto de linea
            sb.append("\n");

        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Invertir 'Hola Mundo': "
                + invertir("Hola Mundo"));
        System.out.println("'Anita lava la tina' es palindromo: "
                + esPalindromo("Anita lava la tina"));
        System.out.println("Vocales en 'Murcielago': "
                + contarVocales("Murcielago"));
        System.out.println("Piramide de 5 niveles:");
        System.out.println(construirPiramide(5));
    }
}
