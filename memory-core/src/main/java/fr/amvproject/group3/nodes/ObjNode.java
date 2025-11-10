package fr.amvproject.group3.nodes;

public class ObjNode {
  public enum ObjName {
    VAR("var"),
    CST("cst"),
    VCST("vcst"),
    TAB("tab"),
    METH("meth");

    private final String name;

    ObjName(String name) {
      this.name = name;
    }

    public static ObjName fromString(String name) {
      for (ObjName obj : values()) {
        if (obj.name.equals(name)) {
          return obj;
        }
      }
      throw new IllegalArgumentException("Obj non valide : " + name);
    }
  }

  private final ObjName obj;

  public ObjNode(String name) {
    this.obj = ObjName.fromString(name);
  }

  public String getName() {
    return obj.name;
  }

  public ObjName getObj() {
    return obj;
  }

  public boolean isVar() {
    return obj == ObjName.VAR;
  }

  public boolean isCst() {
    return obj == ObjName.CST;
  }

  public boolean isVcst() {
    return obj == ObjName.VCST;
  }

  public boolean isTab() {
    return obj == ObjName.TAB;
  }

  public boolean isMeth() {
    return obj == ObjName.METH;
  }
}
