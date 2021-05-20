package codestats;

import java.util.ArrayList;
import java.util.HashMap;

public class ModeLibrary {
  final private HashMap<String, Mode> knownModes;

  public ModeLibrary() {
    knownModes = new HashMap<String, Mode>();

    // define full mode
    ArrayList<Statistics> fullList = new ArrayList<Statistics>();
    fullList.add(new CollectCommentLines());
    fullList.add(new CollectConstants());
    fullList.add(new CollectPrimitives());

    // define base mode
    ArrayList<Statistics> baseList = new ArrayList<Statistics>();
    baseList.add(new CollectCommentLines());
    baseList.add(new CollectConstants());
    baseList.add(new CollectPrimitives());

    addMode(new Mode("base", baseList, false));
    addMode(new Mode("full", fullList, true));
  }

  public void addMode(Mode mode) {
    knownModes.put(mode.getTag(), mode);
  }

  public Mode getMode(String tag) {
    return knownModes.get(tag);
  }
}
