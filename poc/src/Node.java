import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {

    private List<Node> nodes;

    private String nome;

    private String port;

    private boolean isMain;

    private IService service;

    private boolean isOnline = true;

    private Node main = null;

    private int battery;
    private int bandwith;
    private int cpuPower;

    public Node(List<Node> nodes, String nome, String port, boolean isMain, IService service, int battery, int cpuPower, int bandwith) {
        this.nodes = nodes;
        this.nome = nome;
        this.port = port;
        this.isMain = isMain;
        this.service = service;
        this.battery = battery;
        this.bandwith = bandwith;
        this.cpuPower = cpuPower;

        if(isMain)
            this.setMain(this);
    }

    public void receiveKnownNodes(List<Node> nodes){

        System.out.println("Starting receiveKnownNodes for Node " + this.getNome());

        for (Node node : nodes){

            System.out.println("Testing node " + node.getNome());

            if (this.nodes.contains(node) || node == this) {
                System.out.println(this.getNome() + " Already knows " + node.getNome() + " returning");
                continue;
            }

            System.out.println("Adding " + node.getNome() + " to the known nodes of " + this.getNome());
            this.nodes.add(node);

            System.out.println(node.getNome() + " Is a new node for Node " + node.getNome() +"! sending connection to them!");
            node.receiveConnection(this);

            if(node.isMain()){
                System.out.println("Received main node, adding it as main: " + node.getNome());
                this.setMain(node);
            }
        }

    }

    //1 vai receber isso de 3
    //1.receiveConnection(3)

    //1 diz pra 3 se conectar com os knownNodes de 1
    //3.receiveKnownNodes(this.nodes)

    //1 aceita conexaao com 3
    //1.nodes.add(node)
    public void receiveConnection(Node node){

        System.out.println("Node: " + this.getNome() + " is receiving a conection from Node " + node.getNome());

        if (this.nodes.contains(node)) {
            System.out.println("Node " + this.getNome() + " already knows Node " + node.getNome() + " - Returning");
            return;
        }

        System.out.println("Node " + this.getNome() + " is adding Node " + node.getNome() + " to its known nodes");
        this.nodes.add(node);

        System.out.println("Node " + this.getNome() + " sending known nodes to Node " + node.getNome());
        node.receiveKnownNodes(this.nodes);

        System.out.println("Finished receiveConnection for Node " + this.getNome() + " from Node " + node.getNome());

        if(this.isMain() && node.isMain()){
            //Impedindo que tenha dois principais, caso ja tenha um ativo

            System.out.println("Foi identificado que " + node.getNome() + " tambem era principal, marcando principal como falso");
            node.setMain(false);

        }
    }

    // 3 vai enviar isso pra 1
    // 3.connectTo(1)
    // 3 vai se conectar com 1
    // 3.nodes.add(node)
    public void connectTo(Node node){

        if(!this.isOnline()){
            this.setOnline(true);
        }

        System.out.println("Starting connectTo from node " + this.getNome() + " to node " + node.getNome());
        System.out.println(this.getNome() + " wants to connect to " + node.getNome());

        System.out.println("Connection OK, adding " + node.getNome() + " to " + this.getNome() + "'s known nodes");
        this.nodes.add(node);

        System.out.println(this.getNome() + " sending receiveConnection to node " + node.getNome());
        node.receiveConnection(this);
    }

    public void removeSelf() {

        this.setOnline(false);

    }

    public void removeNode(Node nodeToRemove) {

        System.out.println("Starting proccess to remove node " + nodeToRemove.getNome() + " from " + getNome());

        if(nodeToRemove == this) {
            System.out.println("O no a ser removido e voce mesmo, executar removeSelf");
            removeSelf();
            return;
        }

        if(!nodes.contains(nodeToRemove)) {
            System.out.println(this.getNome() + " does not contain " + nodeToRemove.getNome());
            return;
        }

        System.out.println("Removing " + nodeToRemove.getNome() + " from " + this.getNome());
        nodes.remove(nodeToRemove);


        for (Node n : nodes){
            System.out.println("Broadcasting removal of " + nodeToRemove.getNome());
            n.removeNode(nodeToRemove);
        }


        System.out.println("Finished proccess of removing " + nodeToRemove.getNome());
    }

    public void forget(){
        this.setNodes(new ArrayList<>());
    }

    public void checkConnections(){
        System.out.println("Checking connections from " + this.getNome());
        for (Node n : nodes){

            System.out.println("Checking " + n.getNome());

            if (n.isOnline()) {
                System.out.println(n.getNome() + " is online");
                continue;
            }

            System.out.println(n.getNome() + " is offline, removing");
            removeNode(n);

            System.out.println("Telling " + n.getNome() + " to forget the network");
            n.forget();

            System.out.println("Check for " + n.getNome() + " completed");

            if(n.isMain()){
                System.out.println("Deleted node " + n.getNome() + " was main node");
                System.out.println("Starting proccess to select main node");
                Node newMain = electMain();

                System.out.println("Saving main node");
                this.setMain(newMain);

                System.out.println("Broadcasting new main node to nodes");
                for (Node node : this.nodes){
                    System.out.println("Setting main for: " + node.getNome());
                    node.setMain(newMain);
                }
                System.out.println("Election and broadcast finished");
                checkConnections();
                return;

            }
        }

        System.out.println("Connection Check finished");
    }

    public Node electMain(){
        System.out.println();
        System.out.println("Election started");
        System.out.println("To elect a new node, we will check the node that has the bigger election value");
        System.out.println("The election value is a weighted average of three main features: battery, cpu power and bandwith");
        System.out.println("Each node should calculate its own average and return it");

        Node biggestAverage = null;

        for (Node n : nodes){
            System.out.println("Asking " + n.getNome() + " for its average");
            int average = n.getAverage();
            if(biggestAverage == null){
                biggestAverage = n;
            }

            else if(biggestAverage.getAverage() < average)
                biggestAverage = n;

            System.out.println("The average of " + n.getNome() + " is " + average);
        }

        if(biggestAverage == null){
            System.out.println("Error in electing, the biggest node is null");
            return null;
        }

        System.out.println("The node with the biggest average is " + biggestAverage.getNome());

        biggestAverage.setMain(true);

        return biggestAverage;
    }

    public int getAverage(){
        return ((2*battery) + (5*cpuPower) + (3*bandwith))/10;
    }

    public Node getMain(){
        for (Node n : nodes){
            if (n.isMain())
                return n;
        }
        if(this.isMain)
            return this;
        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public IService getService() {
        return service;
    }

    public void setService(IService service) {
        this.service = service;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getBandwith() {
        return bandwith;
    }

    public void setBandwith(int bandwith) {
        this.bandwith = bandwith;
    }

    public int getCpuPower() {
        return cpuPower;
    }

    public void setCpuPower(int cpuPower) {
        this.cpuPower = cpuPower;
    }

    public void setMain(Node main) {
        this.main = main;
    }
}
