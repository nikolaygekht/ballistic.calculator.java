package gehtsoft.ballisticcalculator.Drag;

public final class StandardDragTableFactory {
    private AdvancedDragTable gG1;

    /** Gets G1 ballistic table */
    public IDragTable G1() {
        if (gG1 == null) {
            gG1 = new G1Table();
        }
        return gG1;
    }

    private AdvancedDragTable gG2;

    /** Gets G5 ballistic table */
    public IDragTable G2() {
        if (gG2 == null) {
            gG2 = new G2Table();
        }
        return gG2;
    }

    private AdvancedDragTable gG5;

    /** Gets G5 ballistic table */
    public IDragTable G5() {
        if (gG5 == null) {
            gG5 = new G5Table();
        }
        return gG5;
    }

    private AdvancedDragTable gG6;

    /** Gets G1 ballistic table */
    public IDragTable G6() {
        if (gG6 == null) {
            gG6 = new G6Table();
        }
        return gG6;
    }

    private AdvancedDragTable gG7;

    /** Gets G1 ballistic table */
    public IDragTable G7() {
        if (gG7 == null) {
            gG7 = new G7Table();
        }
        return gG7;
    }

    private AdvancedDragTable gG8;

    /** Gets G8 ballistic table */
    public IDragTable G8() {
        if (gG8 == null) {
            gG8 = new G8Table();
        }
        return gG8;
    }

    private AdvancedDragTable gGI;

    /** Gets G1 ballistic table */
    public IDragTable GI() {
        if (gGI == null) {
            gGI = new GITable();
        }
        return gGI;
    }

    private AdvancedDragTable gGS;

    /** Gets GS ballistic table */
    public IDragTable GS() {
        if (gGS == null) {
            gGS = new GSTable();
        }
        return gGS;
    }

    private static StandardDragTableFactory gInst;

    /** Gets the instance of the factory */
    public static StandardDragTableFactory getInstance() {
        if (gInst == null) {
            gInst = new StandardDragTableFactory();
        }
        return gInst;
    }


    public IDragTable getTable(DragTableId id) 
        throws IllegalArgumentException {
        switch (id) {
            case G1:
                return G1();
            case G2:
                return G2();
            case G5:
                return G5();
            case G6:
                return G6();
            case G7:
                return G7();
            case G8:
                return G8();
            case GI:
                return GI();
            case GS:
                return GS();
            default:
                throw new IllegalArgumentException("Unknown table id");
        }
    }
}

