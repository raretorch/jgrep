package comp.src;

import java.lang.*;

class Main {

  static String inputText;
  static String[] searchable;
  static String[] param;
  static char paramCharacter = '-';

  public static void main(String[] args) {
    translateArgs(args);
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

    paramCount = 0;
    argsCount = 0;
    int y = 0;
    // paste args[] to these arrays
    while (y < args.length - 1) {
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

    i = 0;
    y = 0;
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
    return args;
  }
}
