import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p on 12/7/2016.
 */


public class  AStar extends JFrame{

    /*
    * https://en.wikipedia.org/wiki/A*_search_algorithm
    * https://www.youtube.com/watch?v=-L-WgKMFuhE
    * */

    private JPanel jPanel;

    public static boolean startSet = false;
    public static boolean goalSet = false;

    private static Node start, goal;

    private List<Node> nodesList;
    private List<Node> reachableNodes;

    public AStar(int x, int y){
        super("AStar");  //Here I gave it a title ;)
        jPanel = new JPanel();

        nodesList = new ArrayList<Node>();
        reachableNodes = new ArrayList<Node>();

        createMenuBar();


        setSize(Constants.WIDTH, Constants.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jPanel.setLayout(new GridLayout(Constants.X, Constants.Y));

        //Here is where I initializate the array list with the needed elements
        for (int i = 0; i < Constants.X; i++){
            for (int j = 0; j < Constants.Y; j++){
                Node node = new Node(i,j);
                nodesList.add(node);
                jPanel.add(node);
            }
        }
        add(jPanel);  //And here is where the points really start to appear ;)
    }

    private void createMenuBar(){

        JMenuBar menuBar = new JMenuBar();

        JMenu pathFinding = new JMenu("Find Path");
        JMenu help = new JMenu("Help");

        JMenuItem aStarItem = new JMenuItem("A*");
        JMenuItem resetMenuItem = new JMenuItem("Restart");
        JMenuItem informationItem = new JMenuItem("About");

        aStarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startSet && goalSet){
                    aStar();
                }else if(startSet){
                    JOptionPane.showMessageDialog(null, "You got to set a goal!",
                            Constants.STRINGS_TO_BE_PRINTED.ERROR, JOptionPane.PLAIN_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "First of all, you got to set a starting point!",
                            Constants.STRINGS_TO_BE_PRINTED.ERROR, JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        resetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSet = false;
                goalSet = false;

                for (Node n: nodesList){
                    n.reset();
                }
            }
        });

        informationItem.addActionListener(new ActionListener() {

            final String instructions = "First of all set the start node with left mouse button,\n"
                    + "then the goal node with the same mouse button.\n"
                    + "After you have done all that just create the obstacles by pressing CTRL button"
                    + "and moving the mouse around\n";

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, instructions, "A* implementation",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        pathFinding.add(aStarItem);
        pathFinding.add(resetMenuItem);
        help.add(informationItem);
        menuBar.add(pathFinding);
        menuBar.add(help);
        setJMenuBar(menuBar);
    }

    private void aStar() {
        int nodesExplored = 0;
        Node u = null;

        if (!init())
            return;

        do {
            nodesExplored++;
            u = extract_min_with_heuristics(reachableNodes);
            if (u != null && !u.isGoal() && !u.isStart() && !u.isBlock())
                u.setBackground(Color.GRAY);

            for (Node v : getNeighbors(u))
                relax(u, v);

        } while (u != null && !u.isGoal());

        endMessage(nodesExplored);
    }


    //I got it from someone
    private void relax(Node u, Node v) {
        // horizontal or vertical move
        if (u.x == v.x || u.y == v.y) {
            if (u.distance + 10 < v.distance) {
                v.distance = u.distance + 10;
                v.predecessor = u;
            }
        } else { // diagonal move
            if (u.distance + 14 < v.distance) {
                v.distance = u.distance + 14;
                v.predecessor = u;
            }
        }
    }


    private Node extract_min_with_heuristics(List<Node> unvisitedNodes) {
        Node min = unvisitedNodes.get(0);
        double minDist = min.distance + getHeuristic(min);

        for (Node node : unvisitedNodes) {
            if (node.distance + getHeuristic(node) < minDist) {
                min = node;
                minDist = min.distance + getHeuristic(min);
            }
        }
        unvisitedNodes.remove(min);
        return min;
    }


    ///This method I got from someone else
    private double getHeuristic(Node node) {
        int rows_away = Math.abs(goal.x - node.x);
        int cols_away = Math.abs(goal.y - node.y);

        // admissible heuristic - don't overestimate the cost of reaching the goal
        return Math.min(rows_away, cols_away) * 10 + Math.max(rows_away, cols_away) * 6;
    }

    private void endMessage(int nodesExplored) {


        colorPath(Color.CYAN);
        JOptionPane.showMessageDialog(null,
                Constants.STRINGS_TO_BE_PRINTED.P_MESSAGE
                        + " The distance is: "
                        + goal.distance
                        + "\nThere have been "
                        + nodesExplored
                        + " nodes explored \n",

                "WELL DONE", JOptionPane.PLAIN_MESSAGE);
    }

    private void colorPath(Color color) {
        Node currentNode = goal.predecessor;

        while (currentNode != null && currentNode != start) {
            currentNode.setBackground(color);
            currentNode = currentNode.predecessor;
        }
    }

    private boolean init() {
        start = getStartNode();
        goal = getGoalNode();
        start.distance = 0;
        getReachableNodes(start);

        if (!reachableNodes.contains(goal)) {
            JOptionPane.showMessageDialog(null, Constants.STRINGS_TO_BE_PRINTED.NO_ROUTE,
                    Constants.STRINGS_TO_BE_PRINTED.ERROR, JOptionPane.PLAIN_MESSAGE);
            return false;
        } else
            return true;
    }

    private Node getStartNode() {
        if (AStar.startSet)
            for (Node node : nodesList)
                if (node.isStart())
                    return node;
        return null;
    }

    private Node getGoalNode() {
        if (AStar.goalSet)
            for (Node node : nodesList)
                if (node.isGoal())
                    return node;
        return null;
    }

    private void getReachableNodes(Node start){
        for (Node node: getNeighbors(start)){
            if (!node.isTraversed() && !node.isBlock()){
                node.setTraversed(true);
                reachableNodes.add(node);
                getReachableNodes(node);
            }
        }
    }

    private List<Node> getNeighbors (Node node){

        List<Node> neighbors = new ArrayList<Node>();

        for (Node currentNode: nodesList){
            if (!node.isBlock()){
                if (currentNode.x == node.x){
                    if (currentNode.y == node.y - 1 || currentNode.y == node.y + 1){
                        neighbors.add(currentNode);
                    }
                }else if (currentNode.x == node.x - 1){
                    if(currentNode.y == node.y || currentNode.y == node.y - 1 || currentNode.y == node.y + 1){
                        neighbors.add(currentNode);
                    }
                }else if (currentNode.x == node.x + 1){
                    if(currentNode.y == node.y || currentNode.y == node.y - 1 || currentNode.y == node.y + 1){
                        neighbors.add(currentNode);
                    }
                }
            }
        }
        return neighbors;
    }
}
