import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * BlockDisplay is used to display the contents of the
 * game board.
 * 
 * @author  Dave Feinberg
 * @author  Richard Page
 * @author  Susan King     Added documentation
 * @version May 13, 2015
 */
public class BlockDisplay implements KeyListener
{
    private static final Color BACKGROUND = Color.BLACK;

    private MyBoundedGrid<Block> board;
    private JPanel[][] grid;
    private JFrame frame;
    private ArrowListener listener;

    /**
     * Constructs a new display for displaying the given board.
     * 
     * @param brd   the grid on which the game is to be played
     */
    public BlockDisplay(MyBoundedGrid<Block> brd)
    {
        this.board = brd;
        grid = new JPanel[board.getNumRows()][board.getNumCols()];

        /* 
         * Schedules a job for the event-dispatching thread, which
         * creates and shows this application's GUI.
         */
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });

        /*
         * Waits until display has been drawn.
         */
        try
        {
            while (frame == null || !frame.isVisible())
                Thread.sleep(1);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the GUI and shows it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI()
    {
        // Creates and sets up the window.
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(board.getNumRows(), board.getNumCols()));
        frame.addKeyListener(this);

        // Creates each square component.
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
            {
                grid[row][col] = new JPanel();
                grid[row][col].setBackground(BACKGROUND);
                grid[row][col].setPreferredSize(new Dimension(20, 20));
                //JLabel textLabel = new JLabel("" + 123);
                //frame.getContentPane().add(textLabel,BorderLayout.CENTER);
                //JButton button = new JButton(""+123);
                //grid[row][col].add(button);
                frame.getContentPane().add(grid[row][col]);
            }

        // Shows the board.
        showBlocks();

        // Displays the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Redraws the board to include the pieces and border colors.
     */
    public void showBlocks()
    {
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
            {
                Location loc = new Location(row, col);

                Block square = board.get(loc);

                if (square == null)
                {
                    //JButton button = new JButton("");
                    //button.setBackground(BACKGROUND);
                    //button.setBorder(null);
                    //grid[row][col].add(button);
                    while (!grid[row][col].getBackground().equals(BACKGROUND))
                    {
                        grid[row][col].setBackground(BACKGROUND);
                        grid[row][col].setBorder(null);
                    }
                }
                else
                {
                    //JButton button = new JButton(""+123);
                    //grid[row][col].add(button);
                    while (!grid[row][col].getBackground().equals(square.getColor()))
                    {
                        grid[row][col].setBackground(square.getColor());
                        grid[row][col].setBorder(BorderFactory.createLineBorder(BACKGROUND));
                    }
                }
            }
    }

    /**
     * Sets the title of the window.
     * 
     * @param title  the information to be placed at the
     *               top of the window
     */
    public void setTitle(String title)
    {
        frame.setTitle(title);
    }

    /**
     * Creates a skeleton method to respond when a key is typed in.
     * This event occurs when a key press is followed by a key release.
     * 
     * @param e an event which indicates that a keystroke occurred 
     *          in a component (such as the grid)
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * Creates a skeleton method to respond when a
     * keyboard key is released.
     * 
     * @param e an event which indicates that a keystroke occurred 
     *          in a component (such as the grid)
     */
    public void keyReleased(KeyEvent e)
    {
    }

    /**
     * Sets up the action when a key is pressed
     * on the keyboard.
     * 
     * @param e an event which indicates that a keystroke occurred 
     *          in a component (such as the grid)
     */
    public void keyPressed(KeyEvent e)
    {
        if (listener == null)
            return;
            
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT)
            listener.leftPressed();
        else if (code == KeyEvent.VK_RIGHT)
            listener.rightPressed();
        else if (code == KeyEvent.VK_DOWN)
            listener.downPressed();
        else if (code == KeyEvent.VK_UP)
            listener.upPressed();
    }

    /**
     * Establishes the class that is the ArrowListener.
     * 
     * @param lstener   the class that is assigned the task of
     *                  being the ArrowListener
     */
    public void setArrowListener(ArrowListener lstener)
    {
        this.listener = lstener;
    }
}
