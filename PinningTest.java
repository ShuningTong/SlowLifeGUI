
import org.junit.Test;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;


public class PinningTest {
	MainPanel panel;
	
	@Before
	public void setUp(){
		panel = new MainPanel(5);
	}
	
	/*
	 * The convertToInt method should return 0 when passed in 0
	 */
	@Test
	public void convertToIntTestZero(){
		Method method;
		try {
			// parameterTypes, an array of Class objects that identify the methods' formal parameter types in declared order
			Class[] argTypes = new Class[]{int.class};
			method = MainPanel.class.getDeclaredMethod("convertToInt", argTypes);
			method.setAccessible(true);
			Object returnValue = method.invoke(panel, 0);
			int result = ((Integer)returnValue).intValue();
			assertEquals(0, result);	
		} catch (NoSuchMethodException e) {
			fail(e.getMessage());
		} catch (SecurityException e) {
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			fail(e.getMessage());
		}
	}
	
	/*
	 * The convertToInt method should return 10 when passed in 10
	 */
	@Test
	public void convertToIntTestPositive(){
		Method method;
		try {
			Class[] argTypes = new Class[]{int.class};
			method = MainPanel.class.getDeclaredMethod("convertToInt", argTypes);
			method.setAccessible(true);
			Object returnValue = method.invoke(panel, 10);
			int result = ((Integer)returnValue).intValue();
			assertEquals(10, result);	
		} catch (NoSuchMethodException e) {
			fail(e.getMessage());
		} catch (SecurityException e) {
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			fail(e.getMessage());
		}
	}
	
	/*
	 * The convertToInt method should throw NumberFormatException when passed in -10
	 * The invoke method in this test will throw InvocationTargetException if the underlying method throws an exception
	 */
	@Test
	public void convertToIntTestNegative(){
		Method method;
			try {
				Class[] argTypes = new Class[]{int.class};
				method = MainPanel.class.getDeclaredMethod("convertToInt", argTypes);
				method.setAccessible(true);
				Object returnValue = method.invoke(panel, -10);
				int result = ((Integer)returnValue).intValue();
				fail("No NumberFormatException occurred!");
			} catch (NoSuchMethodException e) {
				fail(e.getMessage());
			} catch (SecurityException e) {
				fail(e.getMessage());
			} catch (IllegalAccessException e) {
				fail(e.getMessage());
			} catch (IllegalArgumentException e) {
				fail(e.getMessage());
			} catch (InvocationTargetException e) {
				// pass
				return;
			}
	}
	
	/*
	 * The convertToInt method should return Integer.MAX_VALUE when passed in Integer.MAX_VALUE
	 */
	@Test
	public void convertToIntTestMaxInt(){
		Method method;
		try {
			Class[] argTypes = new Class[]{int.class};
			method = MainPanel.class.getDeclaredMethod("convertToInt", argTypes);
			method.setAccessible(true);
			Object returnValue = method.invoke(panel, Integer.MAX_VALUE);
			int result = ((Integer)returnValue).intValue();
			assertEquals(Integer.MAX_VALUE, result);	
		} catch (NoSuchMethodException e) {
			fail(e.getMessage());
		} catch (SecurityException e) {
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			fail(e.getMessage());
		}
	}
	
	/*
	 * When start with all cells dead in the grid, runContinuous for 2 seconds,
	 * the final state of the grid should be all dead.
	 * Use a seconc thread to call stop after 2 seconds to avoid infinite loop.
	 */
	@Test
	public void runContinuousTestAllFalse(){
		Cell[][] cells = new Cell[5][5];
		/*
		 Pattern when start:
		 .....
		 .....
		 .....
		 .....
		 .....
		 */
		for (int i = 0; i < cells.length; i++){
			for (int j = 0; j < cells[i].length; j++){
				cells[i][j] = new Cell(false);
			}
		}
		panel.setCells(cells);
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		executor.schedule(new Runnable(){
			@Override
			public void run(){
				panel.stop();
			}
		}, 2, TimeUnit.SECONDS);
		panel.runContinuous();
		boolean[][] finalState = panel.convertToBoolean(panel.getCells());
		/*
		 Pattern when end:
		 .....
		 .....
		 .....
		 .....
		 .....
		 */
		for (int i = 0; i < finalState.length; i++){
			for (int j = 0; j < finalState[i].length; j++){
				if (finalState[i][j]){
					fail("Final state is not all dead, grid [" + i + "," + j + "] is alive");
				}
			}
		}
	}
	
	/*
	 * When start with corner square alive in the grid, runContinuous for 2 seconds,
	 * the final state of the grid should be the same with the start.
	 * Use a seconc thread to call stop after 2 seconds to avoid infinite loop.
	 */
	@Test
	public void runContinuousTestSquareStable(){
		Cell[][] cells = new Cell[5][5];
		/*
		 Pattern when start:
		 XX...
		 XX...
		 .....
		 .....
		 .....
		 */
		for (int i = 0; i < cells.length; i++){
			for (int j = 0; j < cells[i].length; j++){
				if ((i == 0 && (j == 0 || j == 1)) || (i == 1 && (j == 0 || j == 1))){
					cells[i][j] = new Cell(true);
				} else {
					cells[i][j] = new Cell(false);
				}
			}
		}
		panel.setCells(cells);
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		executor.schedule(new Runnable(){
			@Override
			public void run(){
				panel.stop();
			}
		}, 2, TimeUnit.SECONDS);
		panel.runContinuous();
		boolean[][] finalState = panel.convertToBoolean(panel.getCells());
		/*
		 Pattern when end:
		 XX...
		 XX...
		 .....
		 .....
		 .....
		 */
		for (int i = 0; i < finalState.length; i++){
			for (int j = 0; j < finalState[i].length; j++){
				if ((i == 0 && (j == 0 || j == 1)) || (i == 1 && (j == 0 || j == 1))){
					if (!finalState[i][j]){
						fail("Square is not all alive, grid [" + i + "," + j + "] is dead");
					}
				} else {
					if (finalState[i][j]){
						fail("Other cells are not all dead, grid [" + i + "," + j + "] is alive");
					}
				}
			}
		}
	}
	/*
	 * When start with three diagonal cells alive in the grid, runContinuous for 2 seconds,
	 * the final state of the grid should be all dead.
	 * Use a seconc thread to call stop after 2 seconds to avoid infinite loop.
	 */
	@Test
	public void runContinuousTestDisappearPattern(){
		Cell[][] cells = new Cell[5][5];
		/*
		 Pattern when start:
		 ..X..
		 .X...
		 X....
		 .....
		 .....
		 */
		for (int i = 0; i < cells.length; i++){
			for (int j = 0; j < cells[i].length; j++){
				if ((i == 0 && j == 2) || (i == 1 && j == 1) || (i == 2 && j == 0)){
					cells[i][j] = new Cell(true);
				} else {
					cells[i][j] = new Cell(false);
				}
			}
		}
		panel.setCells(cells);
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		executor.schedule(new Runnable(){
			@Override
			public void run(){
				panel.stop();
			}
		}, 2, TimeUnit.SECONDS);
		panel.runContinuous();
		boolean[][] finalState = panel.convertToBoolean(panel.getCells());
		/*
		 Pattern when end:
		 .....
		 .....
		 .....
		 .....
		 .....
		 */
		for (int i = 0; i < finalState.length; i++){
			for (int j = 0; j < finalState[i].length; j++){
				if (finalState[i][j]){
					fail("Final state is not all dead, grid [" + i + "," + j + "] is alive");
				}
			}
		}
	}
	
	
	/*
     * The toString method of Cell class should return "X" when a cell is alive
     */
    @Test
    public void cellToStringTestAlive(){
        Cell c = new Cell(true);
        assertEquals("X", c.toString());
    }


    /*
     * The toString method of Cell class should return "." when a cell is dead
     */
    @Test
    public void cellToStringTestDead(){
        Cell c = new Cell(false);
        assertEquals(".", c.toString());
    }


    /*
     * The toString method of Cell class should return "X" when a cell is first set dead then changed to alive
     */
    @Test
    public void cellToStringTestDeadToAlive(){
        Cell c = new Cell(false);
        c.setAlive(true);
        assertEquals("X", c.toString());
    }


    /*
     * The toString method of Cell class should return "." when a cell is first set alive then changed to dead
     */
    @Test
    public void cellToStringTestAliveToDead(){
        Cell c = new Cell(true);
        c.setAlive(false);
        assertEquals(".", c.toString());
    }
	
}
