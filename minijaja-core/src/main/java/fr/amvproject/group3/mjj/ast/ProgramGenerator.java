package fr.amvproject.group3.mjj.ast;

import java.util.HashMap;
import java.util.Map;

import fr.amvproject.group3.utils.IndentManager;
import fr.amvproject.group3.utils.StringSaver;

public class ProgramGenerator {

  class ProgramSettings {
    int classCount = 2;
    // Used to prevent infinite recursion, if 0 then inil
    int instrRecursionLevel = 1;
    boolean exhaustiveDeclsBlocks = false;
    boolean exhaustiveVarsBlocks = false;
    boolean exhaustiveRefBlocks = false;
    int declBlocksCount = 1;
    int varBlocksCount = 1;
    int refBlocksCount = 1;
  }

  // Literal definitions (Sonar ce plaint des repetitions)
  private static final String BLOCK_OPEN = " {\n";
  private static final String BLOCK_CLOSE = "}\n";
  private static final String BLOCK_OPEN_EXP = ") {\n";
  private static final String BLOCK_CLOSE_EXP = "};\n";
  private static final String PARAM_SEPARATOR = ", ";

  private enum Type {
    BOOLEAN,
    INT;

    public String getType() {
        return name().toLowerCase();
    }
  }

  private enum TypeMeth {
    BOOLEAN,
    INT,
    VOID;

    public String getType() {
        return name().toLowerCase();
    }
  }

  private enum ObjType {
    CLASS,
    METHODE,
    ENTETE,
    VAR,
    TAB,
    FINAL
  }

  private enum ExpTypes {
    ARITHM_EXP,
    LOGIC_EXP,
    ARITHM_LOGIC_EXP,
    INT_EXP,
    BOOL_EXP,
    METHODCALL_EXP_SINGLE_INT,
    METHODCALL_EXP_MULTY_INT,
    METHODCALL_EXP_SINGLE_BOOL,
    METHODCALL_EXP_MULTY_BOOL,
    LENGTH_EXP,
    SUP_LENGTH_EXP,
    AMBIGUOUS_EXP,
    AMBIGUOUS_LENGTH_EXP
  }

  private enum InstrTypes {
    ASSIGN,
    ADD_ASSIGN,
    INC,
    METHODCALL_INSTR,
    RETURN,
    WRITE,
    IF,
    WHILE
  }

  private enum DeclType {
    VAR,
    METHODE
  }

  private enum ParamType {
    SINGLE,
    MULTY
  }

  private final Map<String, Integer> identCounter = new HashMap<>();

  private StringBuilder sb = new StringBuilder();
  private ProgramSettings programSettings = new ProgramSettings();
  IndentManager indentManager = new IndentManager();

  private void lnAppender(String... str) {
    for (String part : str) sb.append(part);
  }

  private String getIdent(ObjType objType, Type type) {
    int identCount = identCounter.getOrDefault(objType.name() + type.name(), 0);
    identCounter.put(objType.name() + type.name(), identCount + 1);
    if (objType == ObjType.ENTETE)
      return "e" + identCount;
    else if (objType == ObjType.VAR) {
      if (type == Type.INT)
        return "intv" + identCount;
      return "boolv" + identCount;
    } else if (objType == ObjType.TAB) {
      if (type == Type.INT)
        return "intt" + identCount;
      return "boolt" + identCount;
    } else {
      if (type == Type.INT)
        return "intf" + identCount;
      return "boolf" + identCount;
    }
  }

  private String getIdent(ObjType objType, TypeMeth typeMeth) {
    int identCount = identCounter.getOrDefault(objType.name() + typeMeth.name(), 0);
    identCounter.put(objType.name() + typeMeth.name(), identCount + 1);
    if (objType == ObjType.CLASS) return "C" + identCount;
    else if (objType == ObjType.METHODE) {
      if (typeMeth == TypeMeth.VOID)
        return "voidm" + identCount;
      else if (typeMeth == TypeMeth.INT)
        return "intm" + identCount;
      else
        return "boolm" + identCount;
    } else if (objType == ObjType.VAR) {
      return "voidv" + identCount;
    } else {
      return "voidt" + identCount;
    }
  }

  private String getIdentRef(ObjType objType, Type type) {
    if (objType == ObjType.CLASS)
      return "C" + 0;
    else if (objType == ObjType.ENTETE)
      return "e" + 0;
    else if (objType == ObjType.VAR) {
      if (type == Type.INT)
        return "intv" + 0;
      return "boolv" + 0;
    } else if (objType == ObjType.TAB) {
      if (type == Type.INT)
        return "intt" + 0;
      return "boolt" + 0;
    } else {
      if (type == Type.INT)
        return "intf" + 0;
      return "boolf" + 0;
    }
  }

  private String getIdentRef(ObjType objType, TypeMeth typeMeth) {
    if (objType == ObjType.METHODE) {
      if (typeMeth == TypeMeth.VOID)
        return "voidm" + 0;
      else if (typeMeth == TypeMeth.INT)
        return "intm" + 0;
      else
        return "boolm" + 0;
    } else if (objType == ObjType.VAR) {
      return "voidv" + 0;
    } else {
      return "voidt" + 0;
    }
  }

  // --------- Nodes ---------
  public void generateProgram() {
    for (int i = 0; i < programSettings.classCount; i++)
      generateClass();
  }

  public void generateClass() {
    lnAppender("class ", getIdent(ObjType.CLASS, TypeMeth.VOID), BLOCK_OPEN);
    indentManager.increaseIndent();
    generateDecls();
    generateMethMain();
    indentManager.decreaseIndent();
    lnAppender(indentManager.indent(), BLOCK_CLOSE);
  }

  public void generateDecls() {
    if (!programSettings.exhaustiveDeclsBlocks && (programSettings.declBlocksCount-- <= 0)) return;
    generateDecl(DeclType.VAR);
    generateDecl(DeclType.METHODE);
  }

  public void generateDecl(DeclType declType) {
    if (declType == DeclType.VAR) {
      for (Type type : Type.values()) {
        generateVar(type);
      }
    } else {
      for (TypeMeth typeMeth : TypeMeth.values()) {
        generateMethode(typeMeth);
      }
    }
  }

  public void generateVars() {
    if (!programSettings.exhaustiveVarsBlocks && (programSettings.varBlocksCount-- <= 0)) return;
    for (Type type : Type.values()) {
      generateVar(type);
    }
  }

  public void generateVar(Type type) {
    lnAppender(indentManager.indent(), "void ", getIdent(ObjType.VAR, TypeMeth.VOID), ";\n");
    lnAppender(indentManager.indent(), type.getType(), " ", getIdent(ObjType.VAR, type), ";\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), type.getType(), " ", getIdent(ObjType.VAR, type), " = ", getExp(expType), ";\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), "void ", getIdent(ObjType.TAB, TypeMeth.VOID), "[", getExp(expType), "];\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), type.getType(), " ", getIdent(ObjType.TAB, type), "[", getExp(expType), "];\n");
    lnAppender(indentManager.indent(), "final ", type.getType(), " ", getIdent(ObjType.FINAL, type), ";\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), "final ", type.getType(), " ", getIdent(ObjType.FINAL, type), " = ", getExp(expType), ";\n");
  }

  public void generateMethode(TypeMeth typeMeth) {
    lnAppender(indentManager.indent(), typeMeth.getType(), " ", getIdent(ObjType.METHODE, typeMeth), "(");
    generateEntetes(ParamType.SINGLE);
    sb.append(BLOCK_OPEN_EXP);
    indentManager.increaseIndent();
    generateVars();
    generateInstrs(programSettings.instrRecursionLevel);
    indentManager.decreaseIndent();
    lnAppender(indentManager.indent(), BLOCK_CLOSE_EXP);

    lnAppender(indentManager.indent(), typeMeth.getType(), " ", getIdent(ObjType.METHODE, typeMeth), "(");
    generateEntetes(ParamType.MULTY);
    sb.append(BLOCK_OPEN_EXP);
    indentManager.increaseIndent();
    generateVars();
    generateInstrs(programSettings.instrRecursionLevel);
    indentManager.decreaseIndent();
    lnAppender(indentManager.indent(), BLOCK_CLOSE_EXP);
  }

  public void generateMethMain() {
    lnAppender(indentManager.indent(), "main {\n");
    indentManager.increaseIndent();
    generateVars();
    generateInstrs(programSettings.instrRecursionLevel);
    indentManager.decreaseIndent();
    lnAppender(indentManager.indent(), "}\n");
  }

  public void generateEntetes(ParamType entetesType) {
    if (entetesType == ParamType.SINGLE) {
      generateEntete(Type.INT);
    } else if (entetesType == ParamType.MULTY) {
      generateEntete(Type.BOOLEAN);
      sb.append(PARAM_SEPARATOR);
      generateEntete(Type.INT);
      sb.append(PARAM_SEPARATOR);
      generateEntete(Type.BOOLEAN);
    }
  }

  public void generateEntete(Type type) {
    lnAppender(type.getType(), " ", getIdent(ObjType.ENTETE, type));
  }

  public void generateInstrs(int recursionLevel) {
    generateInstr(InstrTypes.ASSIGN, recursionLevel - 1);
    generateInstr(InstrTypes.ADD_ASSIGN, recursionLevel - 1);
    generateInstr(InstrTypes.INC, recursionLevel - 1);
    generateInstr(InstrTypes.METHODCALL_INSTR, recursionLevel - 1);
    generateInstr(InstrTypes.RETURN, recursionLevel - 1);
    generateInstr(InstrTypes.WRITE, recursionLevel - 1);
    generateInstr(InstrTypes.IF, recursionLevel - 1);
    generateInstr(InstrTypes.WHILE, recursionLevel - 1);
  }

  public void generateInstr(InstrTypes instrType, int recursionLevel) {
    if (instrType.equals(InstrTypes.ASSIGN)) {
      generateAssign(recursionLevel);
    } else if (instrType.equals(InstrTypes.ADD_ASSIGN)) {
      generateAddAssign(recursionLevel);
    } else if (instrType.equals(InstrTypes.INC)) {
      generateInc(recursionLevel);
    } else if (instrType.equals(InstrTypes.METHODCALL_INSTR)) {
      generateMethodCallInstr(recursionLevel);
    } else if (instrType.equals(InstrTypes.RETURN)) {
      generateRetour(recursionLevel);
    } else if (instrType.equals(InstrTypes.WRITE)) {
      generateWrite(recursionLevel);
    } else if (instrType.equals(InstrTypes.IF)) {
      generateIf(recursionLevel);
    } else if (instrType.equals(InstrTypes.WHILE)) {
      generateWhile(recursionLevel);
    }
  }

  public String getListExp(ParamType listExpType) {
    if (listExpType == ParamType.SINGLE) {
      return getExp(ExpTypes.ARITHM_EXP);
    } else {
      return getExp(ExpTypes.ARITHM_EXP) + PARAM_SEPARATOR + getExp(ExpTypes.LOGIC_EXP) + PARAM_SEPARATOR + getExp(ExpTypes.ARITHM_LOGIC_EXP);
    }
  }

  public String getExp(ExpTypes expType) {
    if (expType == ExpTypes.ARITHM_EXP) {
      return ("(1 + 2 * 3 / 4) + " + getIdentRef(ObjType.VAR, Type.INT) + " / " + getIdentRef(ObjType.VAR, Type.INT) + " * "
          + getIdentRef(ObjType.VAR, Type.INT));
    } else if (expType == ExpTypes.LOGIC_EXP) {
      return ("(true || false && !false == true) || " + getIdentRef(ObjType.VAR, Type.BOOLEAN) + " && "
          + getIdentRef(ObjType.VAR, Type.BOOLEAN) + " && " + getIdentRef(ObjType.VAR, Type.BOOLEAN));
    } else if (expType == ExpTypes.ARITHM_LOGIC_EXP) {
      return ("1 > 2 == false && " + getIdentRef(ObjType.VAR, Type.BOOLEAN) + " == " + getIdentRef(ObjType.VAR, Type.BOOLEAN) + " > "
          + getIdentRef(ObjType.VAR, Type.BOOLEAN));
    } else if (expType == ExpTypes.INT_EXP) {
      return ("5");
    } else if (expType == ExpTypes.BOOL_EXP) {
      return ("true");
    } else if (expType == ExpTypes.METHODCALL_EXP_SINGLE_INT) {
      return (getIdentRef(ObjType.METHODE, Type.INT) + "(" + getListExp(ParamType.SINGLE) + ")");
    } else if (expType == ExpTypes.METHODCALL_EXP_MULTY_INT) {
      return (getIdentRef(ObjType.METHODE, Type.INT) + "(" + getListExp(ParamType.MULTY) + ")");
    } else if (expType == ExpTypes.METHODCALL_EXP_SINGLE_BOOL) {
      return (getIdentRef(ObjType.METHODE, Type.BOOLEAN) + "(" + getListExp(ParamType.SINGLE) + ")");
    } else if (expType == ExpTypes.METHODCALL_EXP_MULTY_BOOL) {
      return (getIdentRef(ObjType.METHODE, Type.BOOLEAN) + "(" + getListExp(ParamType.MULTY) + ")");
    } else if (expType == ExpTypes.LENGTH_EXP) {
      return ("length(" + getIdentRef(ObjType.VAR, Type.INT)) + ")";
    } else if (expType == ExpTypes.SUP_LENGTH_EXP) {
      return (getIdentRef(ObjType.VAR, Type.INT) + " > length(" + getIdentRef(ObjType.VAR, Type.INT)) + ")";
    } else if (expType == ExpTypes.AMBIGUOUS_EXP) {
      return ("(1 > 2 == (false > " + getIdentRef(ObjType.VAR, Type.BOOLEAN) + " == " + getIdentRef(ObjType.VAR, Type.BOOLEAN) + ") > "
          + getIdentRef(ObjType.VAR, Type.BOOLEAN)) + ")";
    } else if (expType == ExpTypes.AMBIGUOUS_LENGTH_EXP) {
      return (getIdentRef(ObjType.VAR, Type.INT) + " > length(" + getIdentRef(ObjType.VAR, Type.BOOLEAN)) + ")";
    }
    return "edgeCase";
  }

  public void generateIf(int recursionLevel) {
    if (recursionLevel < 0) return;
    for (ExpTypes expType : ExpTypes.values()) {
      lnAppender(indentManager.indent(), "if (", getExp(expType), BLOCK_OPEN_EXP);
      indentManager.increaseIndent();
      generateInstrs(recursionLevel);
      indentManager.decreaseIndent();
      lnAppender(indentManager.indent(), BLOCK_CLOSE_EXP);

      lnAppender(indentManager.indent(), "if (", getExp(expType), BLOCK_OPEN_EXP);
      indentManager.increaseIndent();
      generateInstrs(recursionLevel);
      indentManager.decreaseIndent();
      lnAppender(indentManager.indent(), "} else {\n");
      indentManager.increaseIndent();
      generateInstrs(recursionLevel);
      indentManager.decreaseIndent();
      lnAppender(indentManager.indent(), BLOCK_CLOSE_EXP);
    }
  }

  public void generateWhile(int recursionLevel) {
    if (recursionLevel < 0) return;
    for (ExpTypes expType : ExpTypes.values()) {
      lnAppender(indentManager.indent(), "while (", getExp(expType), BLOCK_OPEN_EXP);
      indentManager.increaseIndent();
      generateInstrs(recursionLevel);
      indentManager.decreaseIndent();
      lnAppender(indentManager.indent(), BLOCK_CLOSE_EXP);
    }
  }

  public void generateWrite(int recursionLevel) {
    if (recursionLevel < 0) return;
    lnAppender(indentManager.indent(), "write(\"String\");\n");
    lnAppender(indentManager.indent(), "writeln(\"String\");\n");
    lnAppender(indentManager.indent(), "write(", getIdentRef(ObjType.VAR, Type.INT), ");\n");
    lnAppender(indentManager.indent(), "write(", getIdentRef(ObjType.TAB, Type.INT), ");\n");
    lnAppender(indentManager.indent(), "writeln(", getIdentRef(ObjType.VAR, Type.INT), ");\n");
    lnAppender(indentManager.indent(), "writeln(", getIdentRef(ObjType.TAB, Type.INT), ");\n");
  }

  public void generateRetour(int recursionLevel) {
    if (recursionLevel < 0) return;
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), "return ", getExp(expType), ";\n");
  }

  public void generateAssign(int recursionLevel) {
    if (recursionLevel < 0) return;
    if (!programSettings.exhaustiveRefBlocks && (programSettings.refBlocksCount-- <= 0)) return;
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), getIdentRef(ObjType.VAR, Type.INT), " = ", getExp(expType), ";\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), getIdentRef(ObjType.FINAL, Type.INT), " = ", getExp(expType), ";\n");


    for (ExpTypes expType1 : ExpTypes.values())
      for (ExpTypes expType2 : ExpTypes.values())
        lnAppender(indentManager.indent(), getIdentRef(ObjType.TAB, Type.INT), "[", getExp(expType1), "] = ", getExp(expType2), ";\n");
  }

  public void generateAddAssign(int recursionLevel) {
    if (recursionLevel < 0) return;
    if (!programSettings.exhaustiveRefBlocks && (programSettings.refBlocksCount-- <= 0)) return;
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), getIdentRef(ObjType.VAR, Type.INT), " += ", getExp(expType), ";\n");
    for (ExpTypes expType : ExpTypes.values())
      lnAppender(indentManager.indent(), getIdentRef(ObjType.TAB, Type.INT), " += ", getExp(expType), ";\n");

    for (ExpTypes expType1 : ExpTypes.values())
      for (ExpTypes expType2 : ExpTypes.values())
        lnAppender(indentManager.indent(), getIdentRef(ObjType.TAB, Type.INT), "[", getExp(expType1), "] += ", getExp(expType2), ";\n");
  }

  public void generateInc(int recursionLevel) {
    if (recursionLevel < 0) return;
    lnAppender(indentManager.indent(), getIdentRef(ObjType.VAR, Type.INT), "++;\n");
    lnAppender(indentManager.indent(), getIdentRef(ObjType.TAB, Type.INT), "++;\n");
  }

  public void generateMethodCallInstr(int recursionLevel) {
    if (recursionLevel < 0) return;
    lnAppender(indentManager.indent(), getIdentRef(ObjType.METHODE, TypeMeth.VOID), "(", getListExp(ParamType.SINGLE), ");\n");
    lnAppender(indentManager.indent(), getIdentRef(ObjType.METHODE, TypeMeth.VOID), "(", getListExp(ParamType.MULTY), ");\n");
  }

  public void print(String preset) {
      StringSaver.save(sb.toString(), "generatedProgram.mjj", preset);
  }

  public String getString() {
    return sb.toString();
  }

}
