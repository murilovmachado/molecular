
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by murilovmachado on 9/1/15.
 */
public class Alignment {

    private MatrixStep[][] costMatrix;

    public static void calculateCostMatrix(char[] entryS, char[] entryT, Operation match, Operation substitution,
                                                 Operation insertion, Operation removal) {

        MatrixStep[][] operationsMatrix = new MatrixStep[entryS.length + 1][ entryT.length + 1];

        operationsMatrix[0][0] = new MatrixStep(0, null, null);
        fillFirstLine(operationsMatrix, removal);
        fillFirstColumn(operationsMatrix, insertion);
        fillRemainingSteps(entryS, entryT, operationsMatrix, match, substitution, insertion, removal);

        printCostMatrix(entryS, entryT, operationsMatrix);
    }

    private static void fillRemainingSteps(char[] entryS, char[] entryT, MatrixStep[][] operationsMatrix,
                                           Operation match, Operation substitution, Operation insertion,
                                           Operation removal) {
        for(int i = 1; i < operationsMatrix.length; i++) {
            for(int j = 1; j < operationsMatrix[i].length; j++) {
                //insertion
                MatrixStep aboveStep = operationsMatrix[i - 1][j];
                int insertionCost = aboveStep.getCost() + insertion.getCost();
                MatrixStep insertionStep = new MatrixStep(insertionCost, aboveStep, insertion);

                //match or substitution
                boolean isMatchBetter = isMatchBetterThanSubstitution(entryS[i - 1], entryT[j - 1], match, substitution);
                MatrixStep diagonalStep = operationsMatrix[i -1][j - 1];
                MatrixStep matchOrSubstitutionStep;

                if(isMatchBetter) {
                    int matchCost = diagonalStep.getCost() + match.getCost();
                    matchOrSubstitutionStep = new MatrixStep(matchCost, diagonalStep, match);
                } else {
                    int substitutionCost = diagonalStep.getCost() + substitution.getCost();
                    matchOrSubstitutionStep = new MatrixStep(substitutionCost, diagonalStep, substitution);
                }

                //removal
                MatrixStep leftStep = operationsMatrix[i][j - 1];
                int removalCost = leftStep.getCost() + removal.getCost();
                MatrixStep removalStep = new MatrixStep(removalCost, leftStep, removal);

                //best
                MatrixStep betterCostAndPriorityStep = Arrays.asList(insertionStep, matchOrSubstitutionStep, removalStep)
                        .stream()
                        .sorted((s1, s2) -> s2.getCost() - s1.getCost())
                        .sorted((s1, s2) -> s2.getPreviousOperation().getPriority() - s1.getPreviousOperation().getPriority())
                        .findFirst().get();

                operationsMatrix[i][j] = betterCostAndPriorityStep;
            }
        }

    }

    private static boolean isMatchBetterThanSubstitution(char a, char b, Operation match, Operation substitution) {
        return a == b && match.getCost() > substitution.getCost()
                || a == b && match.getCost() == substitution.getCost() && match.getPriority() >= substitution.getPriority();
    }

    private static void fillFirstLine(MatrixStep[][] operationsMatrix, Operation removal) {
        for(int j = 1; j < operationsMatrix[0].length; j++) {
            int cost = operationsMatrix[0][j - 1].getCost() + removal.getCost();
            operationsMatrix[0][j] = new MatrixStep(cost, operationsMatrix[0][j - 1], removal);
        }
    }

    private static void fillFirstColumn(MatrixStep[][] operationsMatrix, Operation insertion) {
        for(int i = 1; i < operationsMatrix.length; i++) {
            int cost = operationsMatrix[i - 1][0].getCost() + insertion.getCost();
            operationsMatrix[i][0] = new MatrixStep(cost, operationsMatrix[i - 1][0], insertion);
        }
    }

    private static void printCostMatrix(char[] entryS, char[] entryT, MatrixStep[][] operationsMatrix) {
        StringBuilder costMatrixString = new StringBuilder();

        for(int i = 0; i < operationsMatrix.length; i++) {
            for(int j = 0; j < operationsMatrix[0].length; j++) {
                costMatrixString.append((operationsMatrix[i][j] == null) ? "-" : operationsMatrix[i][j].getCost());
                costMatrixString.append("\t");
            }
            costMatrixString.append("\n");
        }

        System.out.println(costMatrixString.toString());
    }

    public static void main(String[] args) {
        AlignmentInputs inputs = new AlignmentInputs();
        inputs.displayMainMenu();
    }
}

class MatrixStep {
    private int cost;
    private MatrixStep previousStep;
    private Operation previousOperation;

    public MatrixStep(int cost, MatrixStep previousStep, Operation previousOperation) {
        this.cost = cost;
        this.previousStep = previousStep;
        this.previousOperation = previousOperation;
    }

    public int getCost() {
        return cost;
    }

    public MatrixStep getPreviousStep() {
        return previousStep;
    }

    public Operation getPreviousOperation() {
        return previousOperation;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPreviousStep(MatrixStep previousStep) {
        this.previousStep = previousStep;
    }
}

class AlignmentInputs {
    private final int EDIT_OPERATIONS_COST = 1;
    private final int EDIT_OPERATIONS_PRIORITY = 2;
    private final int EDIT_ENTRIES = 3;
    private final int GENERATE_ALIGNED_SEQUENCE = 4;
    private final int EXIT = 5;

    private Scanner scanner;

    private Operation match;
    private Operation substitution;
    private Operation insertion;
    private Operation removal;

    private char[] entryS;
    private char[] entryT;

    public AlignmentInputs() {
        scanner = new Scanner(System.in);

        match = new Operation('M', 5, 4);
        substitution = new Operation('S', -1, 3);
        insertion = new Operation('I', -8, 1);
        removal = new Operation('R', -6, 2);

        entryS = new char[] {'A', 'A', 'T', 'T'};
        entryT = new char[] {'T', 'A', 'A', 'G', 'C'};
    }

    public void displayMainMenu() {
        System.out.println("\n>>>");
        printCurrentConfiguration();
        int action = chooseAction();

        switch (action) {
            case EDIT_OPERATIONS_COST:
                displayEditOperationsCost();
                break;
            case EDIT_OPERATIONS_PRIORITY:
                displayEditOperationsPriority();
                break;
            case EDIT_ENTRIES:
                displayEditEntries();
                break;
            case GENERATE_ALIGNED_SEQUENCE:
                generateAlignedSequence();
                break;
            case EXIT:
                exit();
                break;
            default:
                invalidAction();
                break;
        }
    }

    private void displayEditOperationsCost() {
        System.out.println("\n>>>");
        System.out.println("Digite o custo para as seguintes operações:");
        System.out.print("Match(M): ");
        match.setCost(scanner.nextInt());
        System.out.print("Substitution(S): ");
        substitution.setCost(scanner.nextInt());
        System.out.print("Insertion(I): ");
        insertion.setCost(scanner.nextInt());
        System.out.print("Removal(R): ");
        removal.setCost(scanner.nextInt());
        System.out.println();

        displayMainMenu();
    }

    private void displayEditOperationsPriority() {
        System.out.println("\n>>>");
        System.out.println("Ordene as operações por prioridade (ex: M > S > I > R): ");
        scanner.useDelimiter("\n");
        String orderedPriority = scanner.next();
        buildPriorities(orderedPriority);
        System.out.println();

        displayMainMenu();
    }

    private void displayEditEntries() {
        System.out.println("\n>>>");
        System.out.println("Digite uma cadeia de caracteres: ");
        System.out.print("t: ");
        scanner.useDelimiter("\n");
        entryT = scanner.next().toCharArray();
        System.out.print("s: ");
        scanner.useDelimiter("\n");
        entryS = scanner.next().toCharArray();
        System.out.println();

        displayMainMenu();
    }

    private void invalidAction() {
        System.out.println("\n>>>");
        System.out.println("Ação inválida");
        System.out.println();

        displayMainMenu();
    }

    private void exit(){
        System.out.println("\n>>>");
        System.out.println("Saindo...");
    }

    private void generateAlignedSequence() {
        System.out.println("\n>>>");
        System.out.println("Alinhamento gerado: ");
        Alignment.calculateCostMatrix(entryS, entryT, match, substitution, insertion, removal);
        System.out.println();

        displayMainMenu();
    }

    public void printCurrentConfiguration() {
        System.out.println("Configuração atual:");
        System.out.println(String.format("M:%+d S:%+d I:%+d R:%+d", match.getCost(), substitution.getCost(),
                insertion.getCost(), removal.getCost()));

        printPrioritiesConfiguration();

        System.out.println(String.format("t: %s", String.valueOf(entryT)));
        System.out.println(String.format("s: %s", String.valueOf(entryS)));
        System.out.println();
    }

    private void printPrioritiesConfiguration() {
        List<Operation> operations = Arrays.asList(match, substitution, insertion, removal)
                .stream()
                .sorted((op1, op2) -> op2.getPriority() - op1.getPriority())
                .collect(Collectors.toList());

        System.out.println(String.format("%c > %c > %c > %c",
                operations.get(0).getSymbol(), operations.get(1).getSymbol(), operations.get(2).getSymbol(),
                operations.get(3).getSymbol()));
    }

    private int chooseAction() {
        System.out.println("[1] Editar custos das operações");
        System.out.println("[2] Editar prioridade das operações");
        System.out.println("[3] Editar cadeias de entrada");
        System.out.println("[4] Gerar alinhamento");
        System.out.println("[5] Sair");
        System.out.print("Digite a ação desejada: ");

        return scanner.nextInt();
    }

    private void buildPriorities(String orderedPriority) {
        final List<Character> permittedOperations = Arrays.asList('M', 'S', 'I', 'R');

        List<Character> priorities = Arrays.asList(orderedPriority.replace(" ", "").split(">"))
                .stream()
                .map(op -> op.charAt(0))
                .collect(Collectors.toList());

        if(priorities.size() != 4 || priorities.stream().anyMatch(op -> !permittedOperations.contains(op))) {
            throw new RuntimeException("Prioridades inválidas");
        }

        int priority = 4;
        for(Character op : priorities) {
            switch (op) {
                case 'M':
                    match.setPriority(priority);
                    break;
                case 'S':
                    substitution.setPriority(priority);
                    break;
                case 'I':
                    insertion.setPriority(priority);
                    break;
                case 'R':
                    removal.setPriority(priority);
                    break;
                default:
                    throw new RuntimeException("Operação inválida: " + op);
            }

            priority--;
        }
    }
}

class Operation {
    private char symbol;
    private int cost;
    private int priority;

    public Operation(char symbol, int cost, int priority) {
        this.symbol = symbol;
        this.cost = cost;
        this.priority = priority;
    }

    public char getSymbol() {
        return symbol;
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
