import java.awt.Color;

/**
 * Creates a Tetrad of blocks for the tetrid game and
 * manages information about it. 
 * 
 * @author Julia Biswas
 * @version January 2, 2018
 */
public class Tetrad
{
    private Block [] blocks;
    private MyBoundedGrid<Block> grid;
    private String shape;
    private Color color;

    /**
     * Constructor for objects of class Tetrad.
     * 
     * @param gr    the grid that the Tetrad will be placed in
     */
    public Tetrad(MyBoundedGrid<Block> gr)
    {
        grid = gr;
        blocks = new Block[4];
        String [] shapes = {"I", "T", "O", "L", "J", "S", "Z"};
        Color [] colors = {new Color(255, 220, 210), new Color(250, 250, 250), 
                           new Color(130, 255, 230), new Color(255, 218, 185), 
                           new Color(238, 130, 238), new Color(135, 206, 235),
                           new Color(200, 250, 100)};
        int index = (int)(Math.random()*shapes.length);
        color = colors[index];
        for (int i = 0; i < blocks.length; i++)
        {
            blocks[i] = new Block();
            blocks[i].setColor(color);
        }
        shape = shapes[index];
        Location [] locs = new Location[4];
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        
        //places the blocks in their locations in the grid
        if (shape.equals("I"))
        {
            locs[0] = new Location (1,(cols/2)-1);
            locs[1] = new Location (0,(cols/2)-1);
            locs[2] = new Location (2,(cols/2)-1);
            locs[3] = new Location (3,(cols/2)-1);
        }
        else if (shape.equals("T"))
        {
            locs[0] = new Location(0, ((cols/2) - 2 + 1));
            locs[1] = new Location(0, ((cols/2) - 2 + 0));
            locs[2] = new Location(0, ((cols/2) - 2 + 2));
            locs[3] = new Location(1, (cols/2)-1);
        }
        else if (shape.equals("O"))
        {
            locs[0] = new Location(1, (cols/2));
            locs[1] = new Location(0, (cols/2));
            locs[2] = new Location(0, (cols/2)-1);
            locs[3] = new Location(1, (cols/2)-1);
        }
        else if (shape.equals("L"))
        {
            locs[0] = new Location(1, ((cols/2) - 1));
            locs[1] = new Location(0, ((cols/2) - 1));
            locs[2] = new Location(2, ((cols/2) - 1));
            locs[3] = new Location(2, (cols/2));
        }
        else if (shape.equals("J"))
        {
            locs[0] = new Location(1, ((cols/2)));
            locs[1] = new Location(0, ((cols/2)));
            locs[2] = new Location(2, ((cols/2)));
            locs[3] = new Location(2, ((cols/2)-1));
        }
        else if (shape.equals("S"))
        {
            locs[0] = new Location(0, (cols/2)-1);
            locs[1] = new Location(1, (cols/2)-1);
            locs[2] = new Location(0, (cols/2));
            locs[3] = new Location(1, (cols/2)-2);
        }
        else if (shape.equals("Z"))
        {
            locs[0] = new Location(0, (cols/2)-1);
            locs[1] = new Location(0, (cols/2)-2);
            locs[2] = new Location(1, (cols/2)-1);
            locs[3] = new Location(1, (cols/2));
        }
        addToLocations(gr, locs);
    }

    /**
     * Puts the tetrad blocks at the locations given in the locs array.
     * 
     * @param gr    the grid in which the blocks are to be located in
     * @param locs  the locations on which the blocks are to be located
     * 
     * @precondition the tetrad blocks are not in any grid.
     */
    private void addToLocations(MyBoundedGrid<Block> gr, Location[] locs)
    {
        for (int loc = 0; loc < locs.length; loc++)
        {
            blocks[loc].putSelfInGrid(gr, locs[loc]);
        }
    }
    
    /**
     * Retrieves the array of blocks for the tetrad.
     * 
     * @return the array of blocks that make up the tetrad
     */
    public Block[] getBlocks()
    {
        return blocks;
    }
    
    /**
     * Removes the tetrad blocks from the grid while returning 
     * the locations where the blocks were.
     * 
     * @precondition the tetrad blocks are in the grid
     * 
     * @return the locations where the blocks were
     */ 
    public Location[] removeBlocks()
    {
        Location[] locations = new Location[blocks.length];
        for (int i = 0; i < blocks.length; i++)
        {
            Block block = blocks[i];
            locations[i] = block.getLocation();
            block.removeSelfFromGrid();
        }
        return locations;
    }

    /**
     * Tests whether or not all the locations in locs 
     * are valid and empty in the grid.
     * 
     * @param gr    the given grid of locations (locs)
     * @param locs  the locations that are tested to be valid and empty
     * 
     * @return true if all of the locations are valid and empty; otherwise,
     *         false
     */
    public boolean areEmpty(MyBoundedGrid<Block> gr, Location[] locs)
    {
        for (int i = 0; i < locs.length; i++)
        {
            if (!gr.isValid(locs[i]) || gr.get(locs[i]) != null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves the tetrad deltaRow down and deltaCol columns to the right,
     * as long as the new positions are valid and empty.
     * 
     * @param deltaRow  the number of rows to move it down
     * @param deltaCol  the number of columns to move it to the right
     * 
     * @return true if the translate is successful; otherwise,
     *         false
     */
    public boolean translate(int deltaRow, int deltaCol)
    {
        Location [] locs = removeBlocks();
        Location [] newLocs = new Location [blocks.length];
        for (int i = 0; i < locs.length; i++)
        {
            newLocs[i] = new Location(locs[i].getRow() + deltaRow, 
                locs[i].getCol() + deltaCol);
        }
        if (areEmpty(grid, newLocs))
        {
            addToLocations(grid, newLocs);
            return true;
        }
        addToLocations(grid, locs);
        return false;
    }
    
    /**
     * Rotates the tetrad 90 degrees clockwise
     * 
     * @return true if the rotate is successful; otherwise,
     *         false
     */
    public boolean rotate()
    {
        int rowZero = blocks[0].getLocation().getRow();
        int colZero = blocks[0].getLocation().getCol();
        Location [] locs = removeBlocks();
        Location [] newLocs = new Location [blocks.length];
        for (int i = 0; i < locs.length; i++)
        {
            newLocs[i] = new Location(rowZero - colZero + locs[i].getCol(),
                                      rowZero + colZero - locs[i].getRow());
        }
        if (shape.equals("O"))
        {
            addToLocations(grid,locs);
            return true;
        }
        if (areEmpty(grid, newLocs))
        {
            addToLocations(grid, newLocs);
            return true;
        }
        addToLocations(grid, locs);
        return false;
    }
    
    /**
     * Finds all the blocks of the tetrad in the given row.
     * 
     * @param row    the row to look for the tetrad blocks
     * 
     * @return an array of the blocks in the tetrad in the given row
     */
    public Block[] getBlocksByRow(int row)
    {
        int count = 0;
        for (int i = 0; i < blocks.length; i++)
        {
            if(blocks[i].getLocation().getRow() == row)
            {
                count++;
            }
        }
        Block [] result = new Block[count];
        int index = 0;
        for (int i = 0; i < blocks.length; i++)
        {
            if(blocks[i].getLocation().getRow() == row)
            {
                result[index] = blocks[i];
                index++;
            }
        }
        return result;
    }
    
    /**
     * Finds all the blocks of the tetrad in the given column.
     * 
     * @param col   the column to look for the tetrad blocks
     * 
     * @return an array of the blocks in the tetrad in the given column
     */
    public Block[] getBlocksByCol(int col)
    {
        int count = 0;
        for (int i = 0; i < blocks.length; i++)
        {
            if(blocks[i].getLocation().getCol() == col)
            {
                count++;
            }
        }
        Block [] result = new Block[count];
        int index = 0;
        for (int i = 0; i < blocks.length; i++)
        {
            if(blocks[i].getLocation().getCol() == col)
            {
                result[index] = blocks[i];
                index++;
            }
        }
        return result;
    }
    
    /**
     * Retrieves the leftmost block in the tetrad.
     * 
     * @return the leftmost block in the tetrad
     */
    public Block getLeftmostBlock()
    {
        int leftmost = Integer.MAX_VALUE;
        Block result = blocks[0];
        for (int i = 0; i < blocks.length; i++)
        {
            int col = blocks[i].getLocation().getCol();
            if (col < leftmost)
            {
                leftmost = col;
                result = blocks[i];
            }
        }
        return result;
    }
    
    /**
     * Retrieves the rightmost block in the tetrad.
     * 
     * @return the rightmost block in the tetrad
     */
    public Block getRightmostBlock()
    {
        int rightmost = Integer.MIN_VALUE;
        Block result = blocks[0];
        for (int i = 0; i < blocks.length; i++)
        {
            int col = blocks[i].getLocation().getCol();
            if (col > rightmost)
            {
                rightmost = col;
                result = blocks[i];
            }
        }
        return result;
    }
    
    /**
     * Retrieves the lowest block in the tetrad.
     * 
     * @return the lowest block in the tetrad
     */
    public Block getLowestBlock()
    {
        int lowest = Integer.MIN_VALUE;
        Block result = blocks[0];
        for (int i = 0; i < blocks.length; i++)
        {
            int row = blocks[i].getLocation().getRow();
            if (row > lowest)
            {
                lowest = row;
                result = blocks[i];
            }
        }
        return result;
    }
    
    /**
     * Checks if the tetrad can move to the left.
     * 
     * @return true if the tetrad can move to the left; otherwise,
     *         false
     */
    public boolean canMoveLeft()
    {
        Location leftmost = getLeftmostBlock().getLocation();
        Block [] lftmst = getBlocksByCol(leftmost.getCol());
        for (int i = 0; i < lftmst.length; i++)
        {
            if (leftmost.getCol() == 0 ||
                    grid.get(new Location(lftmst[i].getLocation().getRow(), 
                        leftmost.getCol() - 1)) != null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the tetrad can move to the right.
     * 
     * @return true if the tetrad can move to the right; otherwise,
     *         false
     */
    public boolean canMoveRight()
    {
        Location rightmost = getRightmostBlock().getLocation();
        Block [] rghtmst = getBlocksByCol(rightmost.getCol());
        for (int i = 0; i < rghtmst.length; i++)
        {
            if (rightmost.getCol() == grid.getNumCols()-1 ||
                    grid.get(new Location(rghtmst[i].getLocation().getRow(), 
                        rightmost.getCol() + 1)) != null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the tetrad can move down.
     * 
     * @return true if the tetrad can move down; otherwise,
     *         false
     */
    public boolean canMoveDown()
    {
        Location lowest = getLowestBlock().getLocation();
        Block [] lwst = getBlocksByRow(lowest.getRow());
        for (int i = 0; i < lwst.length; i++)
        {
            if (lowest.getRow() == grid.getNumRows()-1 ||
                    grid.get(new Location(lowest.getRow() - 1,
                        lwst[i].getLocation().getCol())) != null)
            {
                return false;
            }
        }
        return true;
    }
}
