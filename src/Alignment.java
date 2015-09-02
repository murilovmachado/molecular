import java.io.IOException;

/**
 * Created by murilovmachado on 9/1/15.
 */
public class Alignment {
    private Operation match;
    private Operation substitution;
    private Operation insertion;
    private Operation removal;

    private char[] entryS;
    private char[] entryT;

    public Alignment() {
        match = new Operation(5, 4);
        substitution = new Operation(-1, 3);
        insertion = new Operation(-8, 1);
        removal = new Operation(-6, 2);

        entryS = new char[] {'A', 'A', 'T', 'T'};
        entryT = new char[] {'T', 'A', 'A', 'G', 'C'};
    }

    public void startConsole() {
        printOperationCosts();
        printOperationPriorities();
        printEntries();
        printGenerateAlignment();
        printQuit();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printOperationCosts() {
        System.out.println("[C]ustos das operações");
        System.out.println("M:+5 S:-1 I:-8 R: -6");
        System.out.println();
    }

    public void printOperationPriorities() {
        System.out.println("[P]rioridade das operações");
        System.out.println("M > S > R > I");
        System.out.println();
    }

    public void printEntries() {
        System.out.println("[E]ntradas");
        System.out.println("s: ATAAG");
        System.out.println("t: GTAAATTA");
        System.out.println();
    }

    public void printGenerateAlignment() {
        System.out.println("[G]erar alinhamento");
        System.out.println();
    }

    public void printQuit() {
        System.out.println("[S]air");
        System.out.println();
    }

    public static void main(String[] args) {
        Alignment alignment = new Alignment();
        alignment.startConsole();
    }
}

class Operation {
    private int cost;
    private int priority;

    public Operation(int cost, int priority) {
        this.cost = cost;
        this.priority = priority;
    }

    public int getCost() {
        return cost;
    }

    public int getPriority() {
        return priority;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
