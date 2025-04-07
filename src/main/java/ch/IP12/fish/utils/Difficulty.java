package ch.IP12.fish.utils;

public enum Difficulty {
    Easy("Baumwolle – Natürlich, aber ressourcenintensiv\n" +
            "Baumwolle ist eine beliebte Naturfaser, die weich und biologisch abbaubar ist.\n" +
            "Doch ihre Produktion hat Schattenseiten: Der Anbau benötigt enorme Wassermengen (bis zu 2.700 Liter pro Shirt) und große Mengen Chemikalien,\n" +
            "die Böden und Gewässer belasten.\n" +
            "Alternativen wie Bio-Baumwolle setzen auf umweltfreundlichere Methoden. Durch bewusstes Konsumieren und Recycling kann jeder helfen,\n" +
            "den ökologischen Fußabdruck von Baumwollprodukten zu verringern.",
            7624841656535L),
    Medium("Mischfasern - Beim Waschen besonders problematisch\n" +
            "Kleidungsstücke aus Mischfasern, wie Baumwolle-Polyester- Mixe, setzen beim Waschen sowohl Mikroplastik als auch natürliche Fasern frei. Polyester, das in solchen Kombinationen oft vorkommt, gibt Mikroplastik in das Wasser ab, während Baumwolle schneller abgebaut wird.\n" +
            "Diese Kombination macht die Reinigung von Mischfasern umwelttechnisch problematisch, da beide Materialien unterschiedliche Auswirkungen auf die Umwelt haben.\n" +
            "Um die Belastung zu verringern, hilft es, Mischfaser-Kleidung seltener zu waschen und nachhaltigere Alternativen zu wählen.",
            6211734858498L),
    Hard("Polyester - Praktisch, aber problematisch\n" +
            "Polyester, eine synthetische Faser aus Plastik, ist langlebig, günstig und knitterarm. Doch die Herstellung basiert auf Erdöl, verursacht hohe CO2-Emissionen und setzt beim Waschen Mikroplastik frei, das unsere Meere belastet.\n" +
            "Recycling-Polyester und ein bewusster Umgang mit Kleidung können helfen, die Umweltauswirkungen zu reduzieren.",
            7751064387955L);

    public final String text;
    public final long barcode;

    Difficulty(String text, long barcode) {
        this.text = text;
        this.barcode = barcode;
    }


    public static Difficulty getDifficulty(long barcode) throws RuntimeException {
        if (Easy.barcode == barcode) {
            return Easy;
        } else if (Medium.barcode == barcode) {
            return Medium;
        } else if (Hard.barcode == barcode) {
            return Hard;
        }

        //if provided barcode is invalid throw error
        throw new RuntimeException("bad barcode: " + barcode);
    }

}

