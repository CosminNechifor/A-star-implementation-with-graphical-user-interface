import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by p on 12/7/2016.
 */

public class Node extends JButton implements MouseListener {

    public Node predecessor;
    private boolean start, goal, block, traversed;
    public int distance, x, y;

    public Node(int x, int y ){
        this.x = x;
        this.y = y;
        distance = Integer.MAX_VALUE;
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !isBlock() && !isGoal() && !AStar.startSet) {
            AStar.startSet = true;
            setStart(true);
            setBackground(Color.YELLOW);

        } else if(e.getButton() == MouseEvent.BUTTON1 && !isBlock() && !isStart() && AStar.startSet && !AStar.goalSet) {
            AStar.goalSet = true;
            setGoal(true);
            setBackground(Color.BLUE);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.isControlDown() && !isBlock() && !isStart() && !isGoal()) {
            setBlock(true);
            setBackground(Color.BLACK);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean isStart() {
        return start;
    }

    public boolean isGoal() {
        return goal;
    }

    public boolean isBlock() {
        return block;
    }

    public boolean isTraversed() {
        return traversed;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void setTraversed(boolean traversed) {
        this.traversed = traversed;
    }

    public void reset(){
        ///start, goal, block, traversed;
        setStart(false);
        setGoal(false);
        setBlock(false);
        setTraversed(false);
        setBackground(null);
        distance = Integer.MAX_VALUE;
    }
}
