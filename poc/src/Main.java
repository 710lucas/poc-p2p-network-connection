import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Node node1 = new Node(
                new ArrayList<>(),
                "N1",
                "123",
                true,
                new Service(),
                10,
                20,
                30
        );

        Node node2 = new Node(
            new ArrayList<>(),
            "N2",
            "1234",
            false,
            new Service(),
            5,
            10,
            15
        );

        Node node3 = new Node(
            new ArrayList<>(),
            "N3",
            "12345",
            false,
            new Service(),
            5,
            2,
            20
        );

        Node node4 = new Node(
                new ArrayList<>(),
                "N4",
                "12345",
                false,
                new Service(),
                10,
                50,
                1
        );

        node2.connectTo(node1);

        System.out.println();
        System.out.println();
        System.out.println();

        node3.connectTo(node2);

        System.out.println();
        System.out.println();
        System.out.println();


        node4.connectTo(node3);

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("Printando conexoes com node 1");
        printConnections(node1);

        System.out.println();

        System.out.println("Printando conexoes com node 2");
        printConnections(node2);

        System.out.println();

        System.out.println("Printando conexoes com node 3");
        printConnections(node3);

        System.out.println();

        System.out.println("Printando conexoes com node 4");
        printConnections(node4);

        System.out.println();
        System.out.println("Printando node principal");
        System.out.println(node4.getMain().getNome());

        System.out.println("Removendo node 3");
        node3.setOnline(false);

        node1.checkConnections();
        System.out.println();
        System.out.println();

        System.out.println("Printando novamente a conexao de cada node");

        printConnections(node1);

        System.out.println();

        printConnections(node2);

        System.out.println();

        printConnections(node3);

        System.out.println();

        printConnections(node4);

        System.out.println();


        System.out.println("Conectando 3 novamente");
        node3.connectTo(node4);

        System.out.println();

        System.out.println("Verificando se todos os nos tem o mesmo no principal");
        for (Node n : node1.getNodes()){
            printMainNode(n);
        }

        System.out.println();

        System.out.println("Removendo no principal");
        Node principal = node1.getMain();
        principal.removeNode(principal);

        System.out.println();

        System.out.println("Checando conexao novamente a partir de outro no");
        node2.checkConnections();

        System.out.println();

        System.out.println("Verificando conexoes na rede");
        for (Node n : node2.getNodes()){
            printConnections(n);
        }

        System.out.println();

        System.out.println("Verificando se todos os nos tem o mesmo no principal");
        for (Node n : node4.getNodes()){
            printMainNode(n);
        }

    }

    public static void printConnections(Node n) {
        System.out.println("Printando conexoes de node " + n.getNome());

        for (Node node : n.getNodes()) {
            System.out.println(node.getNome());
        }
    }

    public static void printMainNode(Node n){
        System.out.println("O no principal de " + n.getNome() + " e " + n.getMain().getNome());
    }
}