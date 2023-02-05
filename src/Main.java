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
    for (int f = 0; f < searchable.length; f++) {
      int[] buffer = new int[3];
      while ((buffer = findSequence(searchable[f], buffer[0], buffer[1]+buffer[2]))[0] < Text.size()) {
        finds.add(buffer);
      }
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
}
