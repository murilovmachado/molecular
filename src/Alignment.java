import java.io.IOException;

/**
 * Created by murilovmachado on 9/1/15.
 */
public class Alignment {
    public static void main(String[] args) {
        try {
            System.out.println("[C]ustos das operações");
            System.out.println("M:+5 S:-1 I:-8 R: -6");
            System.out.println();

            System.out.println("[P]rioridade das operações");
            System.out.println("M > S > R > I");
            System.out.println();

            System.out.println("[E]ntradas");
            System.out.println("s: ATAAG");
            System.out.println("t: GTAAATTA");
            System.out.println();

            System.out.println("[G]erar alinhamento");

            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
