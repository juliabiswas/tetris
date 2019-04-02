import java.awt.Color;

/**
 * Displays a grid with 20 rows and 10 columns
 * and manages the Tetris game.
 * 
 * @author Julia Biswas
 * @version January 2, 2018
 */
public class Tetris implements ArrowListener
{
    private MyBoundedGrid<Block> grid;
    private BlockDisplay display;
    private Tetrad activeTetrad;

    /**
     * Constructor for objects of class Tetris.
     */
    public Tetris()
    {
        grid = new MyBoundedGrid<Block>(20,10);
        display = new BlockDisplay(grid);
        display.setArrowListener(this);
        display.setTitle("Tetris");
        display.showBlocks();
        activeTetrad = new Tetrad(grid);
        display.showBlocks();
    }

    /**
     * Moves the activeTetrad up.
     */
    public void upPressed()
    {
        activeTetrad.rotate();
        display.showBlocks();
    }

    /**
     * Moves the activeTetrad down.
     */
    public void downPressed()
    {
        activeTetrad.translate(1,0);
        display.showBlocks();
    }

    /**
     * Moves the activeTetrad to the left.
     */
    public void leftPressed()
    {
        activeTetrad.translate(0, -1);
        display.showBlocks();
    }

    /**
     * Moves the activeTetrad to the right.
     */
    public void rightPressed()
    {
        activeTetrad.translate(0, 1);
        display.showBlocks();
    }
    
    /**
     * Retrieves the display.
     * 
     * @return  the tetris game display
     */
    public BlockDisplay getDisplay()
    {
        return display;
    }

    /**
     * Tests if a specified row is completely filled.
     * 
     * @param row   the row to check if it is filled
     * 
     * @return true if the row is filled; otherwise,
     *         false
     */
    private boolean isCompletedRow(int row)
    {
        for (int c = 0; c < grid.getNumCols(); c++)
        {
            if (grid.get(new Location (row, c)) == null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears a specified row.
     * 
     * @param row   the row to clear
     */
    private void clearRow(int row)
    {
        int cols = grid.getNumCols();
        for (int c = 0; c < cols; c++)
        {
            grid.remove(new Location (row, c));
        }
        for (int r = row-1; r >= 0; r--)
        {
            for (int c = 0; c < cols; c++)
            {
                if (grid.get(new Location(r, c)) != null)
                {
                    grid.get(new Location(r, c)).moveTo(new Location(r+1, c));
                }
            }
        }
    }

    /**
     * Clears all the completed rows.
     */
    private int clearCompletedRows()
    {
        int count = 0;
        for (int r = 0; r < grid.getNumRows(); r++)
        {
            if (isCompletedRow(r))
            {
                clearRow(r);
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if the game is over or not.
     * 
     * @return true if the game is over; otherwise, 
     *         false
     */
    public boolean isGameOver()
    {
        if (!activeTetrad.canMoveLeft() && 
                !activeTetrad.canMoveRight() &&
                !activeTetrad.canMoveDown())
        {
            Block [] blocks = activeTetrad.getBlocks();
            for (int i = 0; i < blocks.length; i++)
            {
                if (blocks[i].getLocation().getRow() == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Plays the tetris game.
     * 
     * @param sleep how many milliseconds the game should "sleep" for 
     *              each time this method is called
     * 
     * @return  the number of rows cleared in this play
     */
    public int play(int sleep)
    {
        int cleared = 0;
        try
        {
            //Pause for 1000 milliseconds.
            Thread.sleep(sleep);
        }
        catch(InterruptedException e)
        {
            //ignore
        }
        if (!activeTetrad.translate(1,0))
        {
            cleared = clearCompletedRows();
            activeTetrad = new Tetrad(grid);
        }
        display.showBlocks();
        return cleared;
    }

    /**
     * Clears the screen to end the game.
     */
    public void clearScreen()
    {
        for (int r = 0; r < grid.getNumRows(); r++)
        {
            for (int c = 0; c < grid.getNumCols(); c++)
            {
                grid.remove(new Location (r, c));
            }
        }
        Block [] blocks = activeTetrad.getBlocks();
        for (int i = 0; i < blocks.length; i++)
        {
            blocks[i].setColor(Color.BLACK);
        }
    }

    /** 
     * Draws a sad face at the end of the game.
     */
    public void drawSadFace()
    {
        Block [] sadFace = new Block[15];
        for (int i = 0; i < sadFace.length; i++)
        {
            sadFace[i] = new Block();
            sadFace[i].setColor(new Color (224,255,255));
        }
        int index = 0;
        int midRow = grid.getNumRows()/2 -1;
        int midCol = grid.getNumCols()/2 -1;
        sadFace[0].putSelfInGrid(grid, new Location (midRow-2, midCol-2));
        sadFace[1].putSelfInGrid(grid, new Location (midRow-2, midCol-1));
        sadFace[2].putSelfInGrid(grid, new Location (midRow-2, midCol+1));
        sadFace[3].putSelfInGrid(grid, new Location (midRow-2, midCol+2));
        sadFace[4].putSelfInGrid(grid, new Location (midRow-1, midCol-2));
        sadFace[5].putSelfInGrid(grid, new Location (midRow-1, midCol-1));
        sadFace[6].putSelfInGrid(grid, new Location (midRow-1, midCol+1));
        sadFace[7].putSelfInGrid(grid, new Location (midRow-1, midCol+2));
        sadFace[8].putSelfInGrid(grid, new Location (midRow+1, midCol+2));
        sadFace[9].putSelfInGrid(grid, new Location (midRow+1, midCol+1));
        sadFace[10].putSelfInGrid(grid, new Location (midRow+1, midCol));
        sadFace[11].putSelfInGrid(grid, new Location (midRow+1, midCol-1));
        sadFace[12].putSelfInGrid(grid, new Location (midRow+1, midCol-2));
        sadFace[13].putSelfInGrid(grid, new Location (midRow+2, midCol-2));
        sadFace[14].putSelfInGrid(grid, new Location (midRow+2, midCol+2));
        display.showBlocks();
    }
    
    /**
     * Oversees the tetris game.
     * 
     * @param args  arguments from the command line
     */
    public static void main(String [] args)
    {
        Tetris tetris = new Tetris();
        boolean done = false;
        
        //variables to increase level, points, and speed
        int cleared = 0;
        int level = 1;
        int points = 0;
        int sleep = 1000;
        
        //sets the title to keep track of the points and level
        tetris.getDisplay().setTitle("Level: 1" + "Points: 0");
        while (!done)
        {
            //gets the cleared number of rows
            int rowsCleared = tetris.play(sleep);
            cleared += rowsCleared;
            
            //adds points based on rows cleared
            if (rowsCleared == 1)
            {
                points += 40*level;
            }
            else if (rowsCleared == 2)
            {
                points += 100*level;
            }
            else if (rowsCleared == 3)
            {
                points += 300*level;
            }
            else if (rowsCleared > 3)
            {
                points += 1200*level;
            }
            if (cleared >= 10)
            {
                level++;
                sleep -= 100;
                cleared -= 10;
            }
            tetris.getDisplay().setTitle("Level: " + level + "\t"
                                            + "Points: " + points);
            done = tetris.isGameOver();
        }
        tetris.clearScreen();
        tetris.drawSadFace();
        tetris.getDisplay().setTitle("Game Over");
    }
}
