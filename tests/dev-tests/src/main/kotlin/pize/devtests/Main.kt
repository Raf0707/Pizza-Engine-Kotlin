package pize.devtests

import pize.Pize.create
import pize.Pize.run

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        // new NeuralTest();
        create("Dev-Test", 1300, 1300)
        run(QuadFromNormalTest())
        // Pize.run(new AtlasTest());
    }
}
