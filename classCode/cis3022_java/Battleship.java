import java.util.Scanner;

class BattleshipGame {
  public static void main(String args[]) {
    GameBoard gameBoard;
    Scanner scanner = new Scanner(System.in);
    char row = 'A', play, temp;
    int column = 0, number;
    boolean game = true, loop;
    String data;

    Player player1, player2;
    char p;
    Ship p1Ships[] = new Ship[5];
    Ship p2Ships[] = new Ship[5];
    Screen p1Screen, p2Screen;

    System.out.print("Player #1 - (C) Computer or (H) Human:  ");
    p = Character.toUpperCase(scanner.next().charAt(0));
    if (p == 'C') {
      player1 = new ComputerPlayer("Player 1: Computer");
    }
    else {
      player1 = new HumanPlayer("Player 1: Human");
    }
    player1.positionShips(p1Ships);
    p1Screen = new Screen(p1Ships);
    player1.setScreen(p1Screen);

    System.out.print("Player #2 - (C) Computer or (H) Human:  ");
    p = Character.toUpperCase(scanner.next().charAt(0));
    if (p == 'C') {
      player2 = new ComputerPlayer("Player 2: Computer");
    }
    else {
      player2 = new HumanPlayer("Player 2: Human");
    }
    player2.positionShips(p2Ships);
    p2Screen = new Screen(p2Ships);
    player2.setScreen(p2Screen);

    gameBoard = new GameBoard(p1Screen, p2Screen);

    while (game) {
      gameBoard.printP1ScreenGuessTable();

      player1.makeGuess();
      row = player1.getRowGuess();
      column = player1.getColumnGuess();

      if (gameBoard.guessAtAP2Position(row, column)) {
        System.out.println(player1.getName() + "'s guess at " + row + column + " is correct!");
      }
      else {
        System.out.println(player1.getName() + "'s guess at " + row + column + " is incorrect.");
      }

      if (gameBoard.checkForP1Win()) {
        System.out.println("Congratulations!  " + player1.getName() + " has won the game!");
        System.out.println("Thank you for playing Battleship.");
        break;
      }

      gameBoard.printP1ScreenGuessTable();

      System.out.print("Continue or quit (C/Q):\t");
      play = scanner.next().charAt(0);
      if (Character.toUpperCase(play) == 'Q') {
        System.out.println("Thank you for playing Battleship.");
        break;
      }

      gameBoard.printP2ScreenGuessTable();

      player2.makeGuess();
      row = player2.getRowGuess();
      column = player2.getColumnGuess();

      if (gameBoard.guessAtAP1Position(row, column)) {
        System.out.println(player2.getName() + "'s guess at " + row + column + " is correct!");
      }
      else {
        System.out.println(player2.getName() + "'s guess at " + row + column + " is incorrect.");
      }

      if (gameBoard.checkForP2Win()) {
        System.out.println("Congratulations!  " + player2.getName() + " has won the game!");
        System.out.println("Thank you for playing Battleship.");
        break;
      }

      gameBoard.printP2ScreenGuessTable();

      System.out.print("Continue or quit (C/Q):\t");
      play = scanner.next().charAt(0);
      if (Character.toUpperCase(play) == 'Q') {
        System.out.println("Thank you for playing Battleship.");
        break;
      }
    }
  }
}


class GameBoard {
  private Screen p1Screen;
  private Screen p2Screen;

  private GameBoard() {
    Ship ships[] = new Ship[5];

    ships[0] = new AircraftCarrier(3, true, 'A', 5, 'F');
    ships[1] = new Battleship(6, true, 'B', 4, 'A');
    ships[2] = new Destroyer(2, false, 'D', 3, 'E');
    ships[3] = new Submarine(1, true, 'S', 3, 'J');
    ships[4] = new PatrolBoat(9, false, 'P', 2, 'C');

    p1Screen = new Screen(ships);
    p2Screen = new Screen(ships);
  }
  public GameBoard(Screen p1Screen, Screen p2Screen) {
    this.p1Screen = p1Screen;
    this.p2Screen = p2Screen;
  }
  public Screen getP1Screen() {
    return p1Screen;
  }
  public Screen getp2Screen() {
    return p1Screen;
  }
  public void setP1Screen(Screen p1Screen) {
    this.p1Screen = p1Screen;
  }
  public void setP2Screen(Screen p2Screen) {
    this.p2Screen = p2Screen;
  }
  public String toString() {
    return "P1 Screen\n" + p1Screen.toString() + "\n" +
           "P2 Screen\n" + p2Screen.toString() + "\n";
  }
  public boolean checkForP1Win() {
    return p1Screen.searchForWin();
  }
  public boolean checkForP2Win() {
    return p2Screen.searchForWin();
  }
  public boolean guessAtAP1Position(char row, int column) {
    boolean correct;

    if (p1Screen.getShipTablePosition(row, column) == ' ') {
      correct = false;
      p1Screen.markOnHitTable(row, column, 'X');
      p2Screen.markOnGuessTable(row, column, 'X');
    }
    else {
      correct = true;
      p1Screen.markOnHitTable(row, column, 'H');
      p2Screen.markOnGuessTable(row, column, 'H');
    }

    return correct;
  }
  public boolean guessAtAP2Position(char row, int column) {
    boolean correct;

    if (p2Screen.getShipTablePosition(row, column) == ' ') {
      correct = false;
      p1Screen.markOnGuessTable(row, column, 'X');
      p2Screen.markOnHitTable(row, column, 'X');
    }
    else {
      correct = true;
      p1Screen.markOnGuessTable(row, column, 'H');
      p2Screen.markOnHitTable(row, column, 'H');
    }

    return correct;
  }
  public void printP1ScreenGuessTable() {
    int row, col, i;

    System.out.println();
    System.out.println("         The Player 1 Screen's Guess Table");
    System.out.println();
    System.out.print("  ");
    for (col = 0; col < 10; col++) {
      System.out.print("   " + col);
    }
    System.out.println();

    System.out.print("   +");
    for (col = 0; col < 10; col++) {
      for (i = 0; i < 3; i++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    for (row = 0; row < 10; row++) {
      System.out.print(" " + ((char) (row + 65)) + " |");
      for (col = 0; col < 10; col++) {
        System.out.print(" ");
        System.out.print(p1Screen.getGuessTablePosition(((char) (row + 65)), col));
        System.out.print(" |");
      }
      System.out.println();

      System.out.print("   +");
      for (col = 0; col < 10; col++) {
        for (i = 0; i < 3; i++) {
          System.out.print("-");
        }
        System.out.print("+");
      }
      System.out.println();
    }
    System.out.println();
  }
  public void printP1ScreenShipTable() {
    int row, col, i;

    System.out.println();
    System.out.println("         The Player 1 Screen's Ship Table");
    System.out.println();
    System.out.print("  ");
    for (col = 0; col < 10; col++) {
      System.out.print("   " + col);
    }
    System.out.println();

    System.out.print("   +");
    for (col = 0; col < 10; col++) {
      for (i = 0; i < 3; i++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    for (row = 0; row < 10; row++) {
      System.out.print(" " + ((char) (row + 65)) + " |");
      for (col = 0; col < 10; col++) {
        System.out.print(p1Screen.getHitTablePosition(((char) (row + 65)), col));
        System.out.print(p1Screen.getShipTablePosition(((char) (row + 65)), col));
        System.out.print(p1Screen.getHitTablePosition(((char) (row + 65)), col));
        System.out.print("|");
      }
      System.out.println();

      System.out.print("   +");
      for (col = 0; col < 10; col++) {
        for (i = 0; i < 3; i++) {
          System.out.print("-");
        }
        System.out.print("+");
      }
      System.out.println();
    }
    System.out.println();
  }
  public void printP2ScreenGuessTable() {
    int row, col, i;

    System.out.println();
    System.out.println("         The Player 2 Screen's Guess Table");
    System.out.println();
    System.out.print("  ");
    for (col = 0; col < 10; col++) {
      System.out.print("   " + col);
    }
    System.out.println();

    System.out.print("   +");
    for (col = 0; col < 10; col++) {
      for (i = 0; i < 3; i++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    for (row = 0; row < 10; row++) {
      System.out.print(" " + ((char) (row + 65)) + " |");
      for (col = 0; col < 10; col++) {
        System.out.print(" ");
        System.out.print(p2Screen.getGuessTablePosition(((char) (row + 65)), col));
        System.out.print(" |");
      }
      System.out.println();

      System.out.print("   +");
      for (col = 0; col < 10; col++) {
        for (i = 0; i < 3; i++) {
          System.out.print("-");
        }
        System.out.print("+");
      }
      System.out.println();
    }
    System.out.println();
  }
  public void printP2ScreenShipTable() {
    int row, col, i;

    System.out.println();
    System.out.println("         The Player 2 Screen's Ship Table");
    System.out.println();
    System.out.print("  ");
    for (col = 0; col < 10; col++) {
      System.out.print("   " + col);
    }
    System.out.println();

    System.out.print("   +");
    for (col = 0; col < 10; col++) {
      for (i = 0; i < 3; i++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    for (row = 0; row < 10; row++) {
      System.out.print(" " + ((char) (row + 65)) + " |");
      for (col = 0; col < 10; col++) {
        System.out.print(p2Screen.getHitTablePosition(((char) (row + 65)), col));
        System.out.print(p2Screen.getShipTablePosition(((char) (row + 65)), col));
        System.out.print(p2Screen.getHitTablePosition(((char) (row + 65)), col));
        System.out.print("|");
      }
      System.out.println();

      System.out.print("   +");
      for (col = 0; col < 10; col++) {
        for (i = 0; i < 3; i++) {
          System.out.print("-");
        }
        System.out.print("+");
      }
      System.out.println();
    }
    System.out.println();
  }
}


class Player {
  private int columnGuess;
  private String name;
  private char rowGuess;

/*****************************************************************************************
 *                                                                                       *
 *  NOTE:  The property screen is provided so that if you decide to enhance the          *
 *         player logic, previous guessed locations will be available.                   *
 *                                                                                       *
 *****************************************************************************************/

  protected Screen screen;

  public Player() {
    name = new String();
  }
  public Player(String name) {
    this.name = new String(name);
  }
  public int getColumnGuess() {
    return columnGuess;
  }
  public String getName() {
    return name;
  }
  public Screen getScreen() {
    return screen;
  }
  public char getRowGuess() {
    return rowGuess;
  }

/*****************************************************************************************
 *                                                                                       *
 *  NOTE:  The method makeGuess will be overridden in the subclass ComputerPlayer        *
 *         and HumanPlayer.                                                              *
 *                                                                                       *
 *****************************************************************************************/

  public void makeGuess() {
    columnGuess = ((int) (Math.random() * 100000)) % 10;

    int number = ((int) (Math.random() * 100000)) % 10;
    rowGuess = (char) (number + 65);
  }

/*****************************************************************************************
 *                                                                                       *
 *  NOTE:  The method positionShips will be overridden in the subclass ComputerPlayer    *
 *         and HumanPlayer.                                                              *
 *                                                                                       *
 *****************************************************************************************/

  public void positionShips(Ship ships[]) {
    ships[0] = new AircraftCarrier(3, true, 'A', 5, 'F');
    ships[1] = new Battleship(6, true, 'B', 4, 'A');
    ships[2] = new Destroyer(2, false, 'D', 3, 'E');
    ships[3] = new Submarine(1, true, 'S', 3, 'J');
    ships[4] = new PatrolBoat(9, false, 'P', 2, 'C');
  }
  public void setColumnGuess(int columnGuess) {
    this.columnGuess = columnGuess;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setScreen(Screen screen) {
    this.screen = screen;
  }
  public void setRowGuess(char rowGuess) {
    this.rowGuess = rowGuess;
  }
  public String toString() {
    return "Player Name:  " + name + "\n";
  }
}

class HumanPlayer extends Player{

    public HumanPlayer(){
        super("Human");
    }

    public HumanPlayer(String s)
    {
        super(s);
    }
}


class ComputerPlayer extends Player{

    public ComputerPlayer(){
        super("Computer");
    }

    public ComputerPlayer(String s)
    {
        super(s);
    }
}
class Screen
{

    public char guessTable[][];
    public char hitTable[][];
    public char shipTable[][];

    public Screen()
    {
        this(10, 10);
        shipTable[5][3] = 'A';
        shipTable[5][4] = 'A';
        shipTable[5][5] = 'A';
        shipTable[5][6] = 'A';
        shipTable[5][7] = 'A';
        shipTable[0][6] = 'B';
        shipTable[0][7] = 'B';
        shipTable[0][8] = 'B';
        shipTable[0][9] = 'B';
        shipTable[4][2] = 'D';
        shipTable[5][2] = 'D';
        shipTable[6][2] = 'D';
        shipTable[9][1] = 'S';
        shipTable[9][2] = 'S';
        shipTable[9][3] = 'S';
        shipTable[2][9] = 'P';
        shipTable[3][9] = 'P';
    }

    public Screen(int i, int j)
    {
        guessTable = new char[i][j];
        hitTable = new char[i][j];
        shipTable = new char[i][j];
        for(int k = 0; k < guessTable.length; k++)
        {
            for(int j1 = 0; j1 < guessTable[k].length; j1++)
            {
                guessTable[k][j1] = ' ';
            }

        }

        for(int l = 0; l < shipTable.length; l++)
        {
            for(int k1 = 0; k1 < shipTable[l].length; k1++)
            {
                hitTable[l][k1] = ' ';
            }

        }

        for(int i1 = 0; i1 < shipTable.length; i1++)
        {
            for(int l1 = 0; l1 < shipTable[i1].length; l1++)
            {
                shipTable[i1][l1] = ' ';
            }

        }

    }

    public char getGuessTablePosition(char c, int i)
    {
        c = Character.toUpperCase(c);
        int j = c - 65;
        return guessTable[j][i];
    }

    public char getHitTablePosition(char c, int i)
    {
        c = Character.toUpperCase(c);
        int j = c - 65;
        return hitTable[j][i];
    }

    public char getShipTablePosition(char c, int i)
    {
        c = Character.toUpperCase(c);
        int j = c - 65;
        return shipTable[j][i];
    }

    public void markGuess(char c, int i, char c1)
    {
        c = Character.toUpperCase(c);
        int j = c - 65;
        guessTable[j][i] = c1;
        hitTable[j][i] = c1;
    }

    public boolean searchForWin()
    {
        int k = 0;
        for(int i = 0; i < guessTable.length; i++)
        {
            for(int j = 0; j < guessTable.length; j++)
            {
                if(guessTable[i][j] != ' ' && guessTable[i][j] != 'X' && guessTable[i][j] == shipTable[i][j])
                {
                    k++;
                }
            }

        }

        return k == 17;
    }
}

class Ship
{
    private int column;
    private boolean horizontal;
    private char letter;
    private int positions;
    private char row;

    public Ship(){
        column = 0;
        horizontal = true;
        letter = 'A';
        positions = 0;
        row = 'A';
    }

    public Ship(int c, boolean h, char l, int p, char r){
        column = c;
        horizontal = h;
        letter = l;
        positions = p;
        row = r;
    }

    public int getColumn(){
        return column;
    }

    public boolean getHorizontal(){
        return horizontal;
    }

    public char getLetter(){
        return letter;
    }

    public int getPositions(){
        return positions;
    }

    public char getRow(){
        return row;
    }

    public void setColumn(){
        this.column = column;
    }

    public void setHorizontal(){
        this.horizontal = horizontal;
    }

    public void setLetter(){
        this.letter = letter;
    }

    public void setPositions(){
        this.positions = positions;
    }

    public void setRow(){
        this.row = row;
    }

    public String toString(){
		String data = "Column:      " + column + "\n" + "Horizontal:  " + horizontal + "\n" + "Letter:      " + letter + "\n" + "Positions:   " + positions + "\n" + "Row:         " + row + "\n";
        return data;
    }
}

class AircraftCarrier extends Ship{

    public AircraftCarrier(){
    }

    public AircraftCarrier(int c, boolean h, char l, int p, char r){
        super(c, h, l, p, r);
    }
}

class Submarine extends Ship{

    public Submarine(){
    }

    public Submarine(int c, boolean h, char l, int p, char r){
        super(c, h, l, p, r);
    }
}

class Battleship extends Ship{

    public Battleship(){
    }

    public Battleship(int c, boolean h, char l, int p, char r){
        super(c, h, l, p, r);
    }
}

class PatrolBoat extends Ship{

    public PatrolBoat(){
    }

    public PatrolBoat(int c, boolean h, char l, int p, char r){
        super(c, h, l, p, r);
    }
}


class Destroyer extends Ship{

    public Destroyer(){
    }

    public Destroyer(int c, boolean h, char l, int p, char r){
        super(c, h, l, p, r);
    }
}