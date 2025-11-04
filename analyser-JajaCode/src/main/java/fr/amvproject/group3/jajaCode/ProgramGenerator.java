package fr.amvproject.group3.jajaCode;

import java.util.HashMap;
import java.util.Map;

import fr.amvproject.group3.utils.StringSaver;

public class ProgramGenerator {
  private enum Type {
    BOOLEAN,
    STRING,
    ENTIER;

    public String getType() {
        return name().toLowerCase();
    }
  }

  private enum ObjType {
    VAR,
    TAB;

    public String getType() {
        return name().toLowerCase();
    }
  }

  private final Map<String, Integer> identCounter = new HashMap<>();
  private static final int INSTR_COUNT = 21;

  private StringBuilder sb = new StringBuilder();

  private void lnAppender(String... str) {
    sb.append(nextAddress()).append(" ");
    for (String part : str) sb.append(part);
    sb.append("\n");
  }

  private String getIdent(ObjType objType, Type type) {
    int identCount = identCounter.getOrDefault(objType.name() + type.name(), 0);
    identCounter.put(objType.name() + type.name(), identCount + 1);
    if (objType == ObjType.VAR) {
      if (type == Type.ENTIER) return "intv" + identCount;
      if (type == Type.STRING) return "stringv" + identCount;
      return "boolv" + identCount;
    } else if (objType == ObjType.TAB) {
      if (type == Type.ENTIER) return "intt" + identCount;
      if (type == Type.STRING) return "stringt" + identCount;
      return "boolt" + identCount;
    }
    return null;
  }

  private String getIdentRef(ObjType objType, Type type) {
    if (objType == ObjType.VAR) {
      if (type == Type.ENTIER) return "intv" + 0;
      if (type == Type.STRING) return "stringv" + 0;
      return "boolv" + 0;
    } else if (objType == ObjType.TAB) {
      if (type == Type.ENTIER) return "intt" + 0;
      if (type == Type.STRING) return "stringt" + 0;
      return "boolt" + 0;
    }
    return null;
  }

  // --------- Nodes ---------
  private int address = 1;
  public String nextAddress() {
    return String.valueOf(address++);
  }

  public void generateProgram() {
    for (int i = 0; i < INSTR_COUNT; i++) {
      generateClass(i);
    }
  }

  public void generateClass(int i) {
    generateInstr(i);
  }

  public void generateInstr(int i) {
    switch (i) {
        case 0 -> lnAppender("init");
        case 1 -> lnAppender("swap");
        case 2 -> generateNew();
        case 3 -> generateNewArray();
        case 4 -> generateInvoke();
        case 5 -> generateLength();
        case 6 -> lnAppender("return");
        case 7 -> lnAppender("write");
        case 8 -> lnAppender("writeln");
        case 9 -> generatePush();
        case 10 -> lnAppender("pop");
        case 11 -> generateLoad();
        case 13 -> generateStore();
        case 15 -> generateIf();
        case 16 -> generateGoto();
        case 17 -> generateInc();
        case 18 -> generateOper();
        case 19 -> lnAppender("nop");
        case 20 -> lnAppender("jcstop");
        default -> {break;}
    }
  }

  private static final String NEW = "new("; 
  public void generateNew() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      if (type == Type.STRING || objType == ObjType.TAB) continue;
      lnAppender(NEW, getIdent(objType, type), ", ", type.getType(), ", ", objType.getType(), ", ", "0)");
      lnAppender(NEW, getIdent(objType, type), ", ", type.getType(), ", ", objType.getType(), ", ", "true)");
      lnAppender(NEW, getIdent(objType, type), ", ", type.getType(), ", ", objType.getType(), ", ", "false)");
      lnAppender(NEW, getIdent(objType, type), ", ", type.getType(), ", ", objType.getType(), ", ", "\"String\")");
    }    
  }

  private static final String NEWARRAY = "newarray("; 
  public void generateNewArray() {
    for (Type type : Type.values()) {
      if (type == Type.STRING) continue;
      lnAppender(NEWARRAY, getIdent(ObjType.TAB, type), ", ", type.getType(), ")");
      lnAppender(NEWARRAY, getIdent(ObjType.TAB, type), ", ", type.getType(), ")");
      lnAppender(NEWARRAY, getIdent(ObjType.TAB, type), ", ", type.getType(), ")");
      lnAppender(NEWARRAY, getIdent(ObjType.TAB, type), ", ", type.getType(), ")");
    }
  }

  public void generateInvoke() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      lnAppender("invoke(", getIdentRef(objType, type), ")");
    }
  }

  public void generateLength() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      lnAppender("length(", getIdentRef(objType, type), ")");
    }
  }

  private static final String PUSH = "push("; 
  public void generatePush() {
    lnAppender(PUSH, "0", ")");
    lnAppender(PUSH, "true", ")");
    lnAppender(PUSH, "false", ")");
    lnAppender(PUSH, "\"String\"", ")");
  }

  public void generateLoad() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      lnAppender("load(", getIdentRef(objType, type), ")");
      lnAppender("aload(", getIdentRef(objType, type), ")");
    }
  }

  public void generateStore() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      lnAppender("store(", getIdentRef(objType, type), ")");
      lnAppender("astore(", getIdentRef(objType, type), ")");
    }
  }

  public void generateIf() {
    lnAppender("if(", "0", ")");
  }

  public void generateGoto() {
    lnAppender("goto(", "0", ")");
  }

  public void generateInc() {
    for (Type type : Type.values()) for (ObjType objType : ObjType.values()) {
      lnAppender("inc(", getIdentRef(objType, type), ")");
      lnAppender("ainc(", getIdentRef(objType, type), ")");
    }
  }

  public void generateOper() {
    lnAppender("neg");
    lnAppender("not");
    lnAppender("add");
    lnAppender("sub");
    lnAppender("mul");
    lnAppender("div");
    lnAppender("cmp");
    lnAppender("sup");
    lnAppender("or");
    lnAppender("and");
  }


  public void print(String preset) {
      StringSaver.save(sb.toString(), "generatedProgram.jjc", preset);
  }

  public String getString() {
    return """
      1 init
      2 push(0)
      3 new(x,entier,var,0)
      4 push(4)
      5 newarray(t,entier)
      6 push(9)
      7 new(fct,entier,meth,0)
      8 goto(29)
      9 new(max,entier,var,1)
      10 push(5)
      11 new(y,entier,var,0)
      12 load(max)
      13 push(0)
      14 sup
      15 not
      16 if(24)
      17 load(max)
      18 inc(y)
      19 load(max)
      20 push(1)
      21 sub
      22 store(max)
      23 goto(12)
      24 load(y)
      25 swap
      26 pop
      27 swap 
      28 return
      29 push(4)
      30 load(x)
      31 sup
      32 not
      33 if(44)
      34 load(x)
      35 push(1)
      36 invoke(fct)
      37 writeln
      38 swap
      39 pop
      40 astore(t)
      41 push(1)
      42 inc(x)
      43 goto(29)
      44 push(0)
      45 swap
      46 pop
      47 swap
      48 pop
      49 swap
      50 pop
      51 pop
      52 jcstop
      """;
  }

}
