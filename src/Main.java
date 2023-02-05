package comp.src;

import java.nio.file.*;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {

  static String inputText;
  static List<String> Text;
  static ArrayList<int[]> finds = new ArrayList<>();
  static int findsCount;
  static String[] searchable;
  static String[] param;
  static char paramCharacter = '-';
  static final String ANSI_RED = "\u001B[31m";//"\u001B[31m";
  static final char[] ANSI_RED_CHARS = ANSI_RED.toCharArray();
  static final String ANSI_RESET = "\u001B[0m";//"\u001B[0m";
  static final char[] ANSI_RESET_CHARS = ANSI_RESET.toCharArray();
  static boolean verbose = false;

  public static void main(String[] args) {
    translateArgs(args);
    if (inputText.equalsIgnoreCase("null parameter")) {
      return;
    }
    if (paramFinder("-v")) { // setting verbose mode
      verbose = true;
    }
    if (verbose){setParametersLog();}
    if (paramFinder("-f")) { //read text from file
      if (verbose) {System.out.println("Enter file mode");}
      try { //read the file
        Text = Files.readAllLines(Paths.get(inputText));
      } 
      catch(IOException exception) {
        System.out.println(exception.getMessage());
      }
    } else {
      Text = Arrays.asList(inputText);
    }
    if (verbose) { //print full file text
      System.out.println("=========================");
      for (String str : Text) {
        System.out.println(str);
      }
      System.out.println("=========================");
    }
    System.out.println("||||||||||||||||||||||||||||");
    for (int f = 0; f < searchable.length; f++) { //get finds
      int[] buffer = new int[3];
      while ((buffer = findSequence(searchable[f], buffer[0], buffer[1]+buffer[2]))[0] < Text.size()) {
        finds.add(buffer);
      }
    }
    for (int y = 0; y < Text.size(); y++){ //compare highlighted text
      String tx = Text.get(y);
      char[] bufferedString = tx.toCharArray();
      char[] comparedSting = new char[0];
      boolean[] highlightedChars = new boolean[bufferedString.length];
      boolean buff = false;
      for (int x = 0; x < tx.length(); x++) {
        highlightedChars[x] = findHighlight(x, y);
        if (highlightedChars[x] == true) {
          if (x > 0) {
            if (highlightedChars[x-1] != buff) {
              comparedSting = concatChars(comparedSting, ANSI_RED_CHARS);
              comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
              buff = true;
            } else {
              comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
            }
          } 
          else {
            comparedSting = ANSI_RED_CHARS; 
            comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
            buff = true;
          }
        }
        if (highlightedChars[x] == false) {
          if (x > 0) {
            if (highlightedChars[x-1] != buff) {
              comparedSting = concatChars(comparedSting, ANSI_RESET_CHARS);
              comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
              buff = false;
            } else {
              comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
            }
          }
          else {
            comparedSting = ANSI_RESET_CHARS; 
            comparedSting = concatChars(comparedSting, new char[] { bufferedString[x] });
            buff = false;
          }
        }
      }
      String str = String.valueOf(comparedSting);
      System.out.println(str);
    }
  }

  static String[] translateArgs(String[] args) { //setting parameters into variables
    int paramCount = 0;
    int argsCount = 0;
    int i = 0;
    // recording arrays length for initializing
    while (i < args.length - 1) {
      if ((args[i].toCharArray())[0] == paramCharacter) {
        paramCount++;
        i++;
        continue;
      }
      argsCount++;
      i++;
    }
    // init arrays
    searchable = new String[argsCount];
    param = new String[paramCount];
    if (searchable.length == 0) {
      System.out.println("jgrep | usage: jgrep [SEARCH] -PARAMETERS[-v | -f] (TEXT)");
      inputText = "null parameter";
      return null;
    }
    paramCount = 0;
    argsCount = 0;
    int y = 0;
    while (y < args.length - 1) {  // paste args[] to these arrays
      if ((args[y].toCharArray())[0] == paramCharacter) {
        param[paramCount] = args[y];
        paramCount++;
        y++;
        continue;
      }
      searchable[argsCount] = args[y];
      argsCount++;
      y++;
    }
    inputText = args[args.length - 1];
    return args;
  }

  static void setParametersLog() { //logging parameters
    int i = 0;
    int y = 0;
    // log arrays
    while (i < searchable.length) {
      System.out.println("searchable[" + i + "] = " + searchable[i]);
      i++;
    }
    while (y < param.length) {
      System.out.println("parameter[" + y + "] = " + param[y]);
      y++;
    }
    System.out.println("text = " + inputText);
    return;
  }

  static boolean paramFinder(String parameter) { //find parameter in param[]
    if (param.length == 0) {
      return false;
    }
    for (int i = 0; i < param.length; i++) {
      if(param[i].equalsIgnoreCase(parameter)) {
        if (verbose) {System.out.println("Find " + parameter + " parameter");}
        return true;
      } 
    }
    return false;
  }

  static int[] findSequence (String search, int start, int indexFrom) { //find one word in a line
    int[] index = new int[3];
    int ch;
    if ((ch = Text.get(start).indexOf(search, indexFrom)) != -1) {
      index[1] = ch;
      index[0] = start;
      index[2] = search.length();
      if (verbose) {System.out.println("String: " + index[0] + "; Index: " + index[1] + "; Length:" + index[2]);}
      return index;
    } 
    else {
      index[0] = start +1;
      index[1] = 0;
      index[2] = 0;
      return index;
    }
  }

  static boolean findHighlight (int charIndex, int stringIndex) { //return true when char is highlighted
    boolean itHighlight = false;
    for (int i = 0; i < finds.size(); i++) {
      int[] sequence = finds.get(i);
      if (sequence[0] == stringIndex) {
        int end = sequence[1] + sequence[2] - 1;
        if (charIndex < end && charIndex >= (sequence[1]-1)) {
          itHighlight = true;
          break;
        }
      }
    }
    return itHighlight;
  }

  public static char[] concatChars(char[] first, char[] second) { //merge arrays
    char[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
}
