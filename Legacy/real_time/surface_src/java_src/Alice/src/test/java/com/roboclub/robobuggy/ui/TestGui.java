package com.roboclub.robobuggy.ui;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * tests the gui - won't run on travis
 */
public class TestGui {

    /**
     * tries to open multiple robobuggyjframes and sees if they succeed
     */
    @Test
    public void test() {
        if (!System.getProperty("os.name").equals("Mac OS X")) {
            System.out.println("skipping this test because you are not running mac OS X ");
            //this test fails on travis so we only want to run it locally
        } else {

            if (Gui.getInstance().getWindow(-1) != null) {
                fail("returned a window when passed an invalid id");
            }

            if (Gui.getInstance().getWindow(0) != null) {
                fail("returned a window when passed an invalid id");
            }

            //add a single frame
            RobobuggyJFrame testFrame1 = new RobobuggyJFrame("test Frame 1", 1, 1);
            int id = Gui.getInstance().addWindow(testFrame1);
            if (Gui.getInstance().getWindow(id) != testFrame1) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            if (Gui.getInstance().getWindow(1) != null) {
                fail("returned a window when passed an invalid id");
            }

            if (Gui.getInstance().getWindow(-1) != null) {
                fail("returned a window when passed an invalid id");
            }

            //add a second frame
            RobobuggyJFrame testFrame2 = new RobobuggyJFrame("test Frame 2", 1, 1);
            int id2 = Gui.getInstance().addWindow(testFrame2);
            //add a third frame
            RobobuggyJFrame testFrame3 = new RobobuggyJFrame("test Frame 3", 1, 1);
            int id3 = Gui.getInstance().addWindow(testFrame3);

            if (Gui.getInstance().getWindow(id3) != testFrame3) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            if (Gui.getInstance().getWindow(id2) != testFrame2) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            if (Gui.getInstance().getWindow(id) != testFrame1) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            //now delete a frame
            Gui.getInstance().deleteWindow(id2);
            if (Gui.getInstance().getWindow(-1) != null) {
                fail("returned a window when passed an invalid id");
            }

            if (Gui.getInstance().getWindow(id3) != testFrame3) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            if (Gui.getInstance().getWindow(id2) != null) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }

            if (Gui.getInstance().getWindow(id) != testFrame1) {
                fail("evaluated to a diffrent window then the one for the id passed");
            }


            // testing to make sure that data about the window is updated  and maintained
            RobobuggyJFrame window1 = new RobobuggyJFrame("title1", 1, 1);
            if (!window1.getTitle().equals("title1")) {
                fail("window name does not agree");
            }
            int ref = Gui.getInstance().addWindow(window1);
            window1.setTitle("title2");
            if (!window1.getTitle().equals("title2")) {
                fail("window name does not agree");
            }
            RobobuggyJFrame window2 = Gui.getInstance().getWindow(ref);
            if (!window2.getTitle().equals("title2")) {
                fail("window name does not agree");
            }
            window2.setTitle("title3");
            if (!window1.getTitle().equals("title3")) {
                fail("window name does not agree");
            }
            RobobuggyJFrame window3 = Gui.getInstance().getWindow(ref);
            if (!window1.getTitle().equals("title3")) {
                fail("window name does not agree");
            }
            if (!window3.getTitle().equals("title3")) {
                fail("window name does not agree");
            }
        }

    }

}
